package pl.sansoft.scoreboard;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import lombok.extern.slf4j.Slf4j;

/**
 * To listen data from scoreboard PLC
 * 
 * @author bserwatko
 *
 */
@Component
@Configuration
@Slf4j
@Profile("!dev")
public class ScoreboardListenerService implements SerialPortEventListener {
  
  private String serialPortName;
  private SerialPort serialPort;
  
  @Autowired
  private IDataProcessor dataProcessor;


  public ScoreboardListenerService(@Value("${application.scoreboard.comport}") String serialPortName) {
    this.serialPortName = serialPortName;    
  }
  @PostConstruct
  private void init() {
    if(!StringUtils.hasLength(this.serialPortName))
    {  
      log.warn("No com port name provided");
      return;    
    }
    this.serialPort = new SerialPort(this.serialPortName);

    try {
      this.serialPort.openPort();
      this.serialPort.setParams(SerialPort.BAUDRATE_19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
          SerialPort.PARITY_NONE);
      //this.serialPort.setParams(SerialPort.BAUDRATE_19200, SerialPort.DATABITS_7, SerialPort.STOPBITS_1,
      //    SerialPort.PARITY_NONE);
      this.serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
      log.info("Com port initialized {}", this.serialPortName);
    } catch (SerialPortException e) {
      log.error("COM port initialize error", e);
    }
  }

  @Override
  public void serialEvent(SerialPortEvent event) {
    if (event.isRXCHAR() && event.getEventValue() > 0) {
      try {
        byte[] receivedData = serialPort.readBytes();
        dataProcessor.processData(receivedData);
        //System.out.println("Received response: " + receivedData.length);
      } catch (SerialPortException ex) {
        log.error("Error in receiving bytes from COM-port: ", ex);
      }
    }
  }
}
