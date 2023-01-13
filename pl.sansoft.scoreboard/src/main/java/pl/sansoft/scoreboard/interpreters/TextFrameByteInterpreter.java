package pl.sansoft.scoreboard.interpreters;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextFrameByteInterpreter implements IFrameByteInterpreter{
  
  private StringBuffer text = new StringBuffer();
  private boolean frameCollecting = true;
  
  @Override
  public boolean isFrameCollecting() {
    return frameCollecting;
  }
  
  @Override
  public void processByte(byte b) {
    if(b == 0x7d) {
      frameCollecting = false;
      log.info("text frame received: {}", text.toString());
    } else {
      text.append((char)b);
    }
  }
}