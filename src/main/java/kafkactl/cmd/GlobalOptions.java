package kafkactl.cmd;

import picocli.CommandLine;

import java.io.File;

public class GlobalOptions {

    @CommandLine.Option(names = {"--kafkaconfig"}, description = "", scope = CommandLine.ScopeType.INHERIT)
    public File kafkaconfig;

    @CommandLine.Option(names = {"--context"}, description = "", scope = CommandLine.ScopeType.INHERIT)
    public String context;

    @CommandLine.Option(names = {"--cluster"}, description = "", scope = CommandLine.ScopeType.INHERIT)
    public String cluster;

    @CommandLine.Option(names = {"--user"}, description = "", scope = CommandLine.ScopeType.INHERIT)
    public String user;
}
