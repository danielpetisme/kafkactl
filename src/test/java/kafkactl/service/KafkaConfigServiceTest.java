package kafkactl.service;

import kafkactl.TestUtil;
import org.junit.jupiter.api.Test;

import java.io.File;

import static kafkactl.TestUtil.fromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class KafkaConfigServiceTest {


    ConfigService configService = new ConfigService();

//    @Test
//    public void should_throw_a_runtime_exception_when_serialization_problem() {
//        assertThrows(RuntimeException.class, () -> {
//            configService.load(new File(fromResources("configs/badconfig")));
//        });
//    }

    @Test
    public void should_return_an_empty_kafka_config_when_file_not_found() {
        var config = configService.load(new File("UNKNOWN"));
        assertThat(config).isNotNull();
        assertThat(config.clusters.isEmpty());
    }
}
