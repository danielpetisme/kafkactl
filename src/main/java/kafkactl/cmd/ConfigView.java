package kafkactl.cmd;

import kafkactl.model.KafkaConfig;
import kafkactl.service.PrinterService;
import picocli.CommandLine;

import javax.inject.Inject;
import java.io.IOException;

@CommandLine.Command(name = "view", description = "")
public class ConfigView implements Runnable {

    @CommandLine.Option(names = {"--minify"}, defaultValue = "false")
    boolean minify;

    @CommandLine.Option(names = {"--merge"}, defaultValue = "false")
    boolean merge;

    @CommandLine.Mixin
    OutputOptions outputOptions;

    @Inject
    KafkaConfig kafkaConfig;

    @Inject
    PrinterService printerService;

    @Override
    public void run() {
        var config = kafkaConfig;
        if (minify) {
            config = kafkaConfig.getCurrentContextConfig();
        }
        try {
            printerService.print(config, outputOptions.format);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
