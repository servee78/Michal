package pl.sansoft.scoreboard;

import org.springframework.stereotype.Service;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pl.sansoft.scoreboard.updaters.ScoreboardUpdater;

@Service
@Slf4j
public class ScoreboardDataService {

  @Getter
  private ScoreboardData scoreboardData = new ScoreboardData();

  public void update(ScoreboardUpdater updater) {
    log.debug("Updatig scoreboard data: {}", updater);
    updater.update(scoreboardData);   
  }
}
