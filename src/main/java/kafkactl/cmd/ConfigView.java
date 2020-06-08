package kafkactl.cmd;

import kafkactl.config.ExplicitContext;
import kafkactl.model.KafkaConfig;
import kafkactl.service.PrinterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import javax.inject.Inject;
import java.io.IOException;

@CommandLine.Command(name = "view", description = "")
public class ConfigView implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ConfigView.class.getName());

    @CommandLine.Option(names = {"--minify"}, defaultValue = "false")
    boolean minify;

    @CommandLine.Mixin
    OutputOptions outputOptions;

    @Inject
    @ExplicitContext
    String explicitContext;

    @Inject
    KafkaConfig kafkaConfig;

    @Inject
    PrinterService printerService;

    @Override
    public void run() {
        var config = kafkaConfig;
        if (minify) {
            var context = (explicitContext != null) ? explicitContext : kafkaConfig.currentContext;
            config = kafkaConfig.minify(context);
        }
        try {
            printerService.print(config, outputOptions.format);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
