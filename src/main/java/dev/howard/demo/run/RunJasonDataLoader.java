package dev.howard.demo.run;

import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RunJasonDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RunJasonDataLoader.class);
    private final JdbcClientRunRepository runRepository;
    private final ObjectMapper objectMapper;

    public RunJasonDataLoader(JdbcClientRunRepository runRepository, ObjectMapper objectMapper) {
        this.runRepository = runRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if(runRepository.count() == 0){
            try(InputStream inputStream = TypeReference.class.getResourceAsStream("/data/runs.json")){
                Runs allRuns = objectMapper.readValue(inputStream, Runs.class);
                log.info("Reading {} runs from JSON data and saving to a database.", allRuns.runs().size());
                runRepository.saveAll(allRuns.runs());
            }catch(IOException e){
                throw new RuntimeException("failed to read JSON data", e);
            }
        }else{
            log.info("Runs collection is already populated. Skipping JSON data import.");
        }
    }

}
