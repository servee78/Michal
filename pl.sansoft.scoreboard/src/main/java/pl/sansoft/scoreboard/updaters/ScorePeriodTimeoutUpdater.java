package pl.sansoft.scoreboard.updaters;

import java.time.Duration;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import pl.sansoft.scoreboard.ApplicationUtils;
import pl.sansoft.scoreboard.ScoreboardData;

@Slf4j
public class ScorePeriodTimeoutUpdater extends ScoreboardUpdater {

  public ScorePeriodTimeoutUpdater(byte[] data) {
    super(data);
    log.trace("ScorePeriodTimeoutUpdater created: {}", ApplicationUtils.toHexString(data));
  }

  @Override
  public void update(ScoreboardData scoreboardData) {

    scoreboardData.setHomeScore(getHomeScore());
    scoreboardData.setVisitorScore(getVisitorScore());
    scoreboardData.setPeriod(getPeriod());

    final int timeout = getTimeout();
    scoreboardData.getTimeout().ifPresentOrElse(t -> {
      Optional<Duration> newValue = Optional.empty();
      if (Duration.ZERO.compareTo(t) != 0 && (data[10] & 0xff) != 0x81) {
        newValue = Optional.of(Duration.ofSeconds(timeout));
      }
      scoreboardData.setTimeout(newValue);
    }, () -> {
      if (timeout > 0) {
        scoreboardData.setTimeout(Optional.of(Duration.ofSeconds(timeout)));
      }
    });
  }

  private int getHomeScore() {
    return this.data[0];
  }

  private int getVisitorScore() {
    return this.data[2];
  }

  private int getPeriod() {
    return this.data[4];
  }

  private int getTimeout() {
    try {
      return this.data[11] * 10 + data[12];
    } catch (Exception e) {
      log.error("Get timeout error", e);
      return 0;
    }
  }


}
