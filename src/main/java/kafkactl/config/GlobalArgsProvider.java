package kafkactl.config;

import kafkactl.cmd.KafkaCtl;
import kafkactl.model.KafkaConfig;
import picocli.CommandLine;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.File;

@ApplicationScoped
public class GlobalArgsProvider {


    File kafkaconfig;

    String context;

    String cluster;

    String user;

    void onCommandParsed(@Observes CommandLine.ParseResult parseResult) {
        var globalOptions = ((KafkaCtl) parseResult.commandSpec().root().userObject()).globalOptions;
        this.kafkaconfig = globalOptions.kafkaconfig;
        this.context = globalOptions.context;
        this.cluster = globalOptions.cluster;
        this.user = globalOptions.user;
    }

    @Produces
    @ExplicitKafkaConfig
    public File getKafkaConfigFile() {
        return kafkaconfig;
    }


    @Produces
    @ExplicitContext
    public String getExplicitContext() {
        return context;
    }

    @Produces
    @ExplicitCluster
    public String getExplicitCluster() {
        return cluster;
    }

    @Produces
    @ExplicitUser
    public String getExplicitUser() {
        return user;
    }

}
