package kafkactl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import kafkactl.model.KafkaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;

@ApplicationScoped
public class ConfigService {

    private static final Logger log = LoggerFactory.getLogger(ConfigService.class.getName());

    ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory()).setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);

    public void save(KafkaConfig kafkaConfig) throws IOException {
        var string = new ObjectMapper(new YAMLFactory()).writeValueAsString(kafkaConfig);
        var writer = new BufferedWriter(new FileWriter("/Users/daniel/test.yaml", true));
        writer.append(string);
    }

    public KafkaConfig load(File file) {
        if (!file.exists()) {
            log.warn("Config not found: \"{}\"", file.getAbsolutePath());
            return KafkaConfig.empty();
        }
        try (var reader = Files.newBufferedReader(file.toPath())) {
            return KafkaConfig.fromMap(objectMapper.readValue(reader, Map.class));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error loading config file \"" + file.getAbsolutePath() + "\": " + e.getMessage());
        }
    }
}
