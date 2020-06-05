package kafkactl.cmd;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(name = "kafkactl", mixinStandardHelpOptions = true, subcommands = {Config.class, Get.class})
public class KafkaCtl {

    @CommandLine.Mixin
    public GlobalOptions globalOptions;
}
