package pl.sansoft.scoreboard.rest;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import pl.sansoft.scoreboard.ScoreboardData;
import pl.sansoft.scoreboard.ScoreboardDataService;
import reactor.core.publisher.Flux;

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

  // Tested only on fake api with same class "ScoreboardData" and random values
  @GetMapping(value = "/scoreboardData-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ScoreboardData> getScoreboardDataSse(){
    return Flux.interval(Duration.ofMillis(50))
            .map(data -> scoreboardDataService.getScoreboardData());
  }
  
  @GetMapping(path = "test", produces = "text/plain")
  @ResponseStatus(value = HttpStatus.OK)
  public String test() {
    return "test";
  }
  
}
