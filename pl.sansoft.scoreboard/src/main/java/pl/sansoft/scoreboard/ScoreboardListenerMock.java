package pl.sansoft.scoreboard;

import java.io.File;
import java.io.FileInputStream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
@Profile("dev")
public class ScoreboardListenerMock {

  private String dataFileName;

  @Autowired
  private IDataProcessor dataProcessor;


  public ScoreboardListenerMock(@Value("${application.scoreboard.filename}") String filename) {
    this.dataFileName = filename;
  }

  @PostConstruct
  private void init() {
    if (!StringUtils.hasLength(this.dataFileName)) {
      log.warn("No data file name provided");
      return;
    }

    Thread t = new Thread("file-processor") {
      @Override
      public void run() {
        File file = new File(dataFileName);

        while(true) try (FileInputStream fileInputStream = new FileInputStream(file)) {
          int singleCharInt;
          byte singleByte;

          while ((singleCharInt = fileInputStream.read()) != -1) {
            singleByte = (byte) singleCharInt;
            dataProcessor.processData(new byte[] {singleByte});
            if(singleCharInt==0x5d)
              ApplicationUtils.sleep(1);
          }
        } catch (Exception e) {
          log.error("Reading file error", e);
        }
      }
    };

    t.start();
  }



}
