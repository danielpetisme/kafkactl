package kafkactl.model;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

public class KafkaConfigTest {

    @Test
    public void should_return_empty_string_when_current_config_not_set() {
        var config = new KafkaConfig(null, emptyMap(), emptyMap(), emptyMap());
        var currentConfig = config.currentContext;
        assertThat(currentConfig).isBlank();
    }

    @Test
    public void should_return_default_config_when_current_config_not_found() {
        var config = KafkaConfig.empty();
        var currentConfig = config.minify("unknown");
        assertThat(currentConfig.currentContext).isBlank();
    }


    @Test
    public void should_not_change_context_when_merge() {
        var first = new KafkaConfig("first", emptyMap(), emptyMap(), emptyMap());
        var second = new KafkaConfig("second", emptyMap(), emptyMap(), emptyMap());
        var merged = first.merge(second);
        assertThat(merged.currentContext).isEqualTo("first");
    }

    @Test
    public void should_keep_first_property_if_duplicates_when_merge() {
        var first = new KafkaConfig(
                "",
                Map.of(
                        "sample", Map.of(
                                "A", "1"
                        )
                ),
                emptyMap(),
                emptyMap()
        );

        var second = new KafkaConfig(
                "",
                Map.of(
                        "sample", Map.of(
                                "A", "2",
                                "B", "3"
                        )
                ),
                emptyMap(),
                emptyMap()
        );
        var merged = first.merge(second);
        assertThat(merged.clusters.get("sample").get("A")).isEqualTo("1");
        assertThat(merged.clusters.get("sample").get("B")).isNull();
    }

}
