package kafkactl.cmd;

import kafkactl.config.OutputFormat;
import picocli.CommandLine;

public class OutputOptions {

    @CommandLine.Option(names = {"-o", "--output"}, defaultValue = "yaml")
    OutputFormat format;
}
