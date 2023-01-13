package pl.sansoft.scoreboard.updaters;

import java.util.Arrays;
import pl.sansoft.scoreboard.ScoreboardData;

public abstract class ScoreboardUpdater {

  protected final byte[] data;
  
  public ScoreboardUpdater(byte[] data) {
    this.data = Arrays.copyOf(data, data.length);
  }
  
  public abstract void update(ScoreboardData updater);
  
  
}
