package kafkactl.config;

import kafkactl.model.KafkaConfig;
import kafkactl.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ApplicationScoped
public class KafkaConfigProvider {

    private static final Logger log = LoggerFactory.getLogger(KafkaConfigProvider.class.getName());

    // Defaults for where to find a kafkaconfig file
    public static final String HOME_DIR = System.getProperty("user.home");
    public static final String GLOBAL_KAFKACONFIG_FILE = ".kafka/config";

    public static final String ENV_KAFKACONFIG = "KAFKACONFIG";
    public static final String ENV_KAFKACONFIG_SEPARATOR = ":";

    private final ConfigService configService;
    private final File kafkaConfig;

    Supplier<String> envVarSupplier = () -> System.getenv(ENV_KAFKACONFIG);
    Supplier<String> globalFileSupplier = () -> new File(HOME_DIR, GLOBAL_KAFKACONFIG_FILE).getAbsolutePath();

    public KafkaConfigProvider(ConfigService configService, @ExplicitKafkaConfig File kafkaConfig) {
        this.configService = configService;
        this.kafkaConfig = kafkaConfig;
    }

    /**
     * The precedences works as follow
     * 1- if `kafkaconfig is set explicitly, load it exclusively (no merge)
     * 2- if `KAFKACONFIG` env var set, load all listed files and merge them with a first occurence wins.
     * 3- otherwise use the default kafka config file in `$HOME/.kafka/config, without merging.
     *
     * @return the consolidated Kafka Config
     */
    @Produces
    @ApplicationScoped
    public KafkaConfig getKafkaConfig() {
        if (kafkaConfig != null) {
            return configService.load(kafkaConfig);
        }

        var files = getEnvVarKafkaConfigFiles();
        if (!files.isEmpty()) {
            var config = KafkaConfig.empty();
            for (var file : files) {
                var loaded = configService.load(file);
                config = config.merge(loaded);
            }
            return config;
        }

        var globalKafkaConfig = getGlobalKafkaConfigFile();

        if (globalKafkaConfig.exists()) {
            return configService.load(globalKafkaConfig);
        }

        return KafkaConfig.empty();
    }

    public List<File> getEnvVarKafkaConfigFiles() {
        var envVar = envVarSupplier.get();
        if (envVar == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(envVar.split(ENV_KAFKACONFIG_SEPARATOR)).stream()
                .filter(filename -> filename != null && !filename.trim().isBlank())
                .distinct()
                .map(File::new)
                .collect(Collectors.toList());
    }

    public File getGlobalKafkaConfigFile() {
        return new File(globalFileSupplier.get());
    }

}
