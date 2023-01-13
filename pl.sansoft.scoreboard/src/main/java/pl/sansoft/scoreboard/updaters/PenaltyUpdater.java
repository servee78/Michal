package pl.sansoft.scoreboard.updaters;

import java.time.Duration;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import pl.sansoft.scoreboard.ApplicationUtils;
import pl.sansoft.scoreboard.ScoreboardData;
import pl.sansoft.scoreboard.ScoreboardData.Penalty;

@Slf4j
public class PenaltyUpdater extends ScoreboardUpdater {

  public PenaltyUpdater(byte[] data) {
    super(data);
  }

  @Override
  public void update(ScoreboardData scoreboardData) {
    log.trace("Updating penalty {}", ApplicationUtils.toHexString(data));
    //int lastByte = data[data.length - 1] & (int) 0x003f;
    //log.info("Last byte {}, {}, {}", String.format("%02x", lastByte),
    //    Integer.toBinaryString(0x100 | lastByte).substring(3), ApplicationUtils.toHexString(data));

    for (int i = 0; i < 3; i++) {

      Optional<Penalty> penalty = Optional.empty();
      if (shouldDisplayHomePenalty(i)) {
        Duration penaltyDuration = getPenaltyHomeDuration(i);
        int penaltyPlayerNo = getPenaltyHomePlayerNo(i);

        log.debug("Home penalty {}, duration: {}, playerNo: {}", i, penaltyDuration, penaltyPlayerNo);

        penalty = Optional.of(Penalty.builder().playerNo(penaltyPlayerNo).duration(penaltyDuration).build());
      }
      scoreboardData.setHomePenalty(i, penalty);

      penalty = Optional.empty();
      if (shouldDisplayVisitorPenalty(i)) {
        Duration penaltyDuration = getPenaltyVisitorDuration(i);
        int penaltyPlayerNo = getPenaltyVisitorPlayerNo(i);

        log.debug("Visitor penalty {}, duration: {}, playerNo: {}", i, penaltyDuration, penaltyPlayerNo);
        penalty = Optional.of(Penalty.builder().playerNo(penaltyPlayerNo).duration(penaltyDuration).build());
      }
      scoreboardData.setVisitorPenalty(i, penalty);

    }
  }

  private boolean shouldDisplayHomePenalty(int i) {
    int lastByte = data[data.length - 1] & (int) 0x003f;
    int mask = 0x01 << i;

    return (lastByte & mask) > 0;
  }
  
  private boolean shouldDisplayVisitorPenalty(int i) {
    int lastByte = data[data.length - 1] & (int) 0x003f;
    int mask = 0x08 << i;

    return (lastByte & mask) > 0;
  }

  private int getPenaltyHomePlayerNo(int i) {
    int base = i + 18;
    log.trace("Reading penalty player {} no: {}", i, base);
    return this.data[base];
  }

  private Duration getPenaltyHomeDuration(int i) {
    int base = i * 3;
    int minutes = this.data[base];
    int seconds = this.data[base + 1] * 10 + this.data[base + 2];
    log.trace("Home penalty {}({}) duration {}:{}", i, base, minutes, seconds);
    return Duration.ofMinutes(minutes).plus(Duration.ofSeconds(seconds));
  }

  private int getPenaltyVisitorPlayerNo(int i) {
    int base = i + 21;
    log.trace("Reading penalty player {} no: {}", i, base);
    return this.data[base];
  }

  private Duration getPenaltyVisitorDuration(int i) {
    int base = i * 3 + 9;
    int minutes = this.data[base];
    int seconds = this.data[base + 1] * 10 + this.data[base + 2];
    log.trace("Visitor penalty {}({}) duration {}:{}", i, base, minutes, seconds);
    return Duration.ofMinutes(minutes).plus(Duration.ofSeconds(seconds));
  }


}
