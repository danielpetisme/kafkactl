package kafkactl.cmd;

import kafkactl.SystemOutExtension;
import kafkactl.model.KafkaConfig;
import kafkactl.service.PrinterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;


public class ConfigViewTest {

    @RegisterExtension
    SystemOutExtension out = new SystemOutExtension();

    ConfigView command;

    @BeforeEach
    public void beforeEach() {
        this.command = new ConfigView();
        command.printerService = new PrinterService();
        command.outputOptions = new OutputOptions();
    }

    @Test
    public void minify_should_use_explicit_context_when_defined() {
        command.kafkaConfig = new KafkaConfig(
                "context1",
                emptyMap(),
                emptyMap(),
                Map.of(
                        "context1", emptyMap(),
                        "context2", emptyMap()
                )
        );
        command.explicitContext = "context2";
        command.minify = true;
        command.run();
        assertThat(out.toString()).containsIgnoringCase("current-context: \"context2\"");

    }

    @Test
    public void minify_should_return_empty_config_when_no_current_context_and_no_explicit_arg() {
        command.kafkaConfig = new KafkaConfig(
                "",
                emptyMap(),
                emptyMap(),
                Map.of(
                        "context1", emptyMap(),
                        "context2", emptyMap()
                )
        );
        command.minify = true;
        command.run();
        assertThat(out.toString()).containsIgnoringCase("current-context: \"\"");

    }

}
