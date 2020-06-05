package kafkactl.config;

import io.quarkus.picocli.runtime.PicocliCommandLineFactory;
import picocli.CommandLine;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class KafkaCtlConfiguration {

    @Inject
    Event<CommandLine.ParseResult> event;

    int onCommandLineParsed(CommandLine.ParseResult parseResult) {
        event.fire(parseResult);
        return new CommandLine.RunLast().execute(parseResult);
    }

    @Produces
    CommandLine customCommandLine(PicocliCommandLineFactory factory) {
        return factory.create().setExecutionStrategy(this::onCommandLineParsed);
    }
}
