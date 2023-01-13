package pl.sansoft.scoreboard.updaters;

import java.time.Duration;
import java.time.LocalTime;
import lombok.extern.slf4j.Slf4j;
import pl.sansoft.scoreboard.ScoreboardData;

@Slf4j
public class TimeUpdater extends ScoreboardUpdater {

  public TimeUpdater(byte[] data) {
    super(data);
  }

  @Override
  public void update(ScoreboardData scoreboardData) {
    LocalTime begin = LocalTime.of(0, 0, 0, 0);
    LocalTime end = LocalTime.of(0, getMinutes(), getSeconds(), getNanos());
    
    Duration time = Duration.between(begin, end);
    log.info("Setting new time: {}", time);
    scoreboardData.setTime(time); 
  }
  
  private int getMinutes() {    
    return this.data[0] * 10 + data[1];
  }
  
  private int getSeconds() {
    return this.data[2] * 10 + data[3];
  }
  
  private int getNanos() {
    return this.data[4] * 100000000;
  }

}
