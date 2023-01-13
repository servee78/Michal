package pl.sansoft.scoreboard;

import java.time.Duration;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class ScoreboardData {
  int period;

  int homeScore;
  int visitorScore;

  Duration time = Duration.ZERO;

  Optional<Duration> timeout = Optional.empty();


  Optional<Penalty>[] homePenalties = new Optional[3];
  Optional<Penalty>[] visitorPenalties = new Optional[3];
  
  public ScoreboardData() {
    for(int i=0; i<3; i++) {
      homePenalties[i] = Optional.empty();
      visitorPenalties[i] = Optional.empty();
    }
  }
  
  public void setHomePenalty(int index, Optional<Penalty> penalty) {
    log.debug("Setting {} home penalty: {}", index, penalty);
    homePenalties[index] = penalty;
  }
  
  public void setVisitorPenalty(int index, Optional<Penalty> penalty) {
    log.debug("Setting {} visitor penalty: {}", index, penalty);
    visitorPenalties[index] = penalty;
  }
  
  @Data
  @Builder
  public static class Penalty {
    int playerNo;
    Duration duration;
  }

}
