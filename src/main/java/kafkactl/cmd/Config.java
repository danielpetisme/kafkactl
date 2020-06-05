package kafkactl.cmd;

import picocli.CommandLine;

@CommandLine.Command(name = "config", description = "", subcommands = {ConfigView.class})
public class Config {

}
