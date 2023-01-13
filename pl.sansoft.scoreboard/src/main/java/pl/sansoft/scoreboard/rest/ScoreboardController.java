package pl.sansoft.scoreboard.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import pl.sansoft.scoreboard.ScoreboardData;
import pl.sansoft.scoreboard.ScoreboardDataService;

@RestController
@AllArgsConstructor
@RequestMapping
public class ScoreboardController {

  private ScoreboardDataService scoreboardDataService;
  
  @GetMapping(path = "scoreboardData", produces = "application/json")
  @ResponseStatus(value = HttpStatus.OK)
  public ScoreboardData getScoreboardData() {
    return scoreboardDataService.getScoreboardData();
  }
  
  @GetMapping(path = "test", produces = "text/plain")
  @ResponseStatus(value = HttpStatus.OK)
  public String test() {
    return "test";
  }
  
}
