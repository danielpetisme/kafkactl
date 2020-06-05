package kafkactl.config;

import kafkactl.cmd.KafkaCtl;
import kafkactl.model.KafkaConfig;
import kafkactl.service.ConfigService;
import picocli.CommandLine;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class KafkaConfigProvider {

    @Inject
    KafkaConfigResolver resolver;

    void onCommandParsed(@Observes CommandLine.ParseResult parseResult) {
        var globalOptions = ((KafkaCtl) parseResult.commandSpec().root().userObject()).globalOptions;
        resolver.setKafkaConfig(globalOptions.kafkaconfig);
        resolver.setContext(globalOptions.context);
        resolver.setCluster(globalOptions.cluster);
        resolver.setUser(globalOptions.user);
    }


    @Produces
    @ApplicationScoped
    public KafkaConfig getKafkaConfig() {
        return resolver.getKafkaConfig();
    }

}
