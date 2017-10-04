package dataflow;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("file:/home/sps/monitoring/dataflowAPI.properties")

public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	
        return application.sources(Application.class);
    }

  public static void main(String[] args) {
	  PropertyConfigurator.configure("/home/sps/monitoring/log4jAPI.properties");

    SpringApplication.run(Application.class, args);  
  }


}