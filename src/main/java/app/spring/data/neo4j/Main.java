package app.spring.data.neo4j;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
@EnableAutoConfiguration
public class Main {
    public static void main(String[] args) throws Exception {

        SpringApplication.run(Main.class, args);
    }
}