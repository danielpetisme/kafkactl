package kafkactl.model;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class KafkaConfigTest {

    @Test
    public void should_return_null_when_current_config_not_set() {
        var config = new KafkaConfig();
        config.setCurrentContext(null);
        var currentConfig = config.getCurrentContextConfig();
        assertThat(currentConfig).isNull();
    }

    @Test
    public void should_return_null_when_current_config_not_found() {
        var config = new KafkaConfig();
        config.setCurrentContext("unknown");
        config.setContexts(Collections.emptyMap());
        var currentConfig = config.getCurrentContextConfig();
        assertThat(currentConfig).isNull();
    }


    @Test
    public void should_not_change_context_when_merge() {
        var first = new KafkaConfig();
        first.setCurrentContext("first");
        var second = new KafkaConfig();
        second.setCurrentContext("second");
        var merged = first.merge(second);
        assertThat(merged.getCurrentContext()).isEqualTo("first");
    }

    @Test
    public void should_keep_first_property_if_duplicates_when_merge() {
        var first = new KafkaConfig();
        first.setClusters(Map.of(
                "sample", Map.of(
                        "A", "1"
                )
        ));

        var second = new KafkaConfig();
        second.setClusters(Map.of(
                "sample", Map.of(
                        "A", "2",
                        "B", "3"
                )
        ));
        var merged = first.merge(second);
        assertThat(merged.getClusterProperties("sample").get("A")).isEqualTo("1");
        assertThat(merged.getClusterProperties("sample").get("B")).isNull();
    }

}
