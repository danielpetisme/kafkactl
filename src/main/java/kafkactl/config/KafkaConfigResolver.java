package kafkactl.config;

import kafkactl.model.KafkaConfig;
import kafkactl.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ApplicationScoped
public class KafkaConfigResolver {

    private static final Logger log = LoggerFactory.getLogger(KafkaConfigResolver.class.getName());

    // Defaults for where to find a kafkaconfig file
    public static final String HOME_DIR = System.getProperty("user.home");
    public static final String GLOBAL_KAFKACONFIG_FILE = ".kafka/config";

    public static final String ENV_KAFKACONFIG = "KAFKACONFIG";
    public static final String ENV_KAFKACONFIG_SEPARATOR = ":";

    private ConfigService configService;

    private Supplier<String> envVarSupplier = () -> System.getenv(ENV_KAFKACONFIG);
    private Supplier<String> globalFileSupplier = () -> new File(HOME_DIR, GLOBAL_KAFKACONFIG_FILE).getAbsolutePath();

    private File kafkaConfig;

    private String context;

    private String cluster;

    private String user;


    public KafkaConfigResolver(ConfigService configService) {
        this.configService = configService;
    }

    public void setKafkaConfig(File kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    KafkaConfig getKafkaConfig() {
        var config = loadKafkaConfig();
//        var contextToTuse = context != null ? context : config.getCurrentContext();
        return config;
    }


    public Supplier<String> getEnvVarSupplier() {
        return envVarSupplier;
    }

    public void setEnvVarSupplier(Supplier<String> envVarSupplier) {
        this.envVarSupplier = envVarSupplier;
    }

    public Supplier<String> getGlobalFileSupplier() {
        return globalFileSupplier;
    }

    public void setGlobalFileSupplier(Supplier<String> globalFileSupplier) {
        this.globalFileSupplier = globalFileSupplier;
    }

    /**
     * The precedences works as follow
     * 1- if `kafkaconfig is set explicitly, load it exclusively (no merge)
     * 2- if `KAFKACONFIG` env var set, load all listed files and merge them with a first occurence wins.
     * 3- otherwise use the default kafka config file in `$HOME/.kafka/config, without merging.
     *
     * @return the consolidated Kafka Config
     */
    KafkaConfig loadKafkaConfig() {
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

//    public String getCurrentContext() {
//        if (context != null) {
//            return context;
//        }
//        var config = loadKafkaConfig();
//        return config.getCurrentContext();
//    }
}
