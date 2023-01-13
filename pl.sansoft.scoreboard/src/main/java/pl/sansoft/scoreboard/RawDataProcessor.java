package pl.sansoft.scoreboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import pl.sansoft.scoreboard.interpreters.DataFrameByteInterpreter;
import pl.sansoft.scoreboard.interpreters.IFrameByteInterpreter;
import pl.sansoft.scoreboard.interpreters.TextFrameByteInterpreter;

@Component
@Slf4j
public class RawDataProcessor implements IDataProcessor { 
  
  @Autowired
  private AutowireCapableBeanFactory beanFactory;
  
  private final IFrameByteInterpreter VOID_FRAME_BYTE_INTERPRETER = new IFrameByteInterpreter (){
    @Override
    public boolean isFrameCollecting() {
      return false;
    }
    @Override
    public void processByte(byte b) {
      throw new RuntimeException(String.format("[%x]: out of frame byte [%02x]", byteCounter, b));
      
    }
  };

  private volatile long byteCounter = 0;  

  private IFrameByteInterpreter interpreter = VOID_FRAME_BYTE_INTERPRETER;

  public RawDataProcessor() {
    log.info("NewRawDataProcessor created");
  }
  
  @Override
  public void processData(byte[] data) {
    log.trace("Bytes to process: {}", data.length);
    for (byte b : data) {
      processByte(b);
    }

  }

  private void processByte(byte b) {
    byteCounter++;
    log.trace(String.format("Processing [%x]. byte value [%02x]", byteCounter, b));
    if (!this.interpreter.isFrameCollecting())
      detectFrameType(b);
    else
      processFrameByte(b);
  }

  private void detectFrameType(byte b) {
    log.info("Detecting frame type [{}]", String.format("%02x", b));
    switch (b) {
      case 0x5b:
      case 0x2b:
          this.interpreter = new DataFrameByteInterpreter();
        break;
      case 0x7b:
          this.interpreter = new TextFrameByteInterpreter();
        break;

      default:
        this.interpreter = VOID_FRAME_BYTE_INTERPRETER;
        log.warn(String.format("[%x]: Unknown STX byte [%02x]", byteCounter, b));
        break;
    }
    beanFactory.autowireBean(interpreter);
  }

  private void processFrameByte(byte b) {
    this.interpreter.processByte(b);
  }
  
  

}
