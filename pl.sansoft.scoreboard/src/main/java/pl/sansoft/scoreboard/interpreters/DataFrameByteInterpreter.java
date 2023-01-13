package pl.sansoft.scoreboard.interpreters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import pl.sansoft.scoreboard.ApplicationUtils;
import pl.sansoft.scoreboard.ScoreboardData;
import pl.sansoft.scoreboard.ScoreboardDataService;
import pl.sansoft.scoreboard.updaters.PenaltyUpdater;
import pl.sansoft.scoreboard.updaters.ScorePeriodTimeoutUpdater;
import pl.sansoft.scoreboard.updaters.ScoreboardUpdater;
import pl.sansoft.scoreboard.updaters.TimeUpdater;

@Slf4j
public class DataFrameByteInterpreter implements IFrameByteInterpreter {

  private static final byte ETX = 0x5d;

  private FrameType frameType;
  private int byteToProcess = -1;
  private List<Byte> frameData = Collections.synchronizedList(new ArrayList<>());

  @Autowired
  private ScoreboardDataService scoreboardDataService;
  
  @Override
  public boolean isFrameCollecting() {
    return byteToProcess != 0;
  }

  @Override
  public void processByte(byte b) {
    if (b == ETX && byteToProcess == 1) { // frame end detection
      log.trace("Frame received: {}", ApplicationUtils.toHexString(frameData));
      byteToProcess--;
      updateScoreboard();
      return;
    } else if (byteToProcess == 0) {
      log.error("End of frame reached without ETX ", String.format("%02x", ETX));
    }

    if (frameType == null) {
      switch (b) {
        case 0x30:
        //case 0x5c:
          frameType = FrameType.Time;
          break;
        case 0x31:
        //case 0x4c:  
          frameType = FrameType.ScorePeriodTimeout;
          break;
        case 0x36:
        //case 0x13:
          frameType = FrameType.Penalty;
          break;

        default:
          frameType = FrameType.Unknown;
          log.warn(String.format("Unknown frame type detected: %02x", b));
          break;
      }
      byteToProcess = frameType.getByteToProcess();
      log.trace(String.format("%s [%02x] frame detected, byte to process [%d]", frameType, b, byteToProcess));
    } else {
      log.trace("Adding [{}] to frame: {}", String.format("%02x", b), ApplicationUtils.toHexString(frameData));
      frameData.add(b);
      byteToProcess--;
    }
  }

  private void updateScoreboard() {
    ScoreboardUpdater updater;
    byte[] updateData = ApplicationUtils.toByteArray(frameData);
    
    switch (frameType) {
      case Time: // time
        updater = new TimeUpdater(updateData);
        break;
      case ScorePeriodTimeout: // score,period,timeout
        updater =  new ScorePeriodTimeoutUpdater(updateData);
        break;
      case Penalty: //penalties
        updater =  new PenaltyUpdater(updateData);
        break;
      default:
        updater = new ScoreboardUpdater(updateData){
          @Override
          public void update(ScoreboardData scoreboardData) {
            log.info("Nothing to update");
          };
        };
        log.warn("Unhandled frame type: {}, IdleUpdater cerated", frameType.getText());
    }
    scoreboardDataService.update(updater);
  }

  public enum FrameType {
    Unknown("Unknown", 0), Time("", 7), ScorePeriodTimeout("", 14), Penalty("", 26);

    private final String text;
    private final int byteToProcess;

    private FrameType(String text, int byteToProcess) {
      this.text = text;
      this.byteToProcess = byteToProcess;
    }

    public String getText() {
      return this.text;
    }

    public int getByteToProcess() {
      return byteToProcess;
    }
  };
}
