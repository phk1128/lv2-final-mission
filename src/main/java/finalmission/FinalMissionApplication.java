package finalmission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinalMissionApplication {

  public static void main(String[] args) {
    SpringApplication.run(FinalMissionApplication.class, args);
  }

}
