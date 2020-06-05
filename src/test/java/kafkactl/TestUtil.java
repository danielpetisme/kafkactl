package kafkactl;

import kafkactl.config.KafkaConfigResolver;

import java.io.File;

public class TestUtil {

    public static final String emptyString = "";

    static final String SRC_TEST_RESOURCES = "src/test/resources";

    public static String fromResources(String filename) {
        return new File(SRC_TEST_RESOURCES, filename).getAbsolutePath();
    }

    public static String kafkaConfigEnvVar(String... files) {
        return String.join(KafkaConfigResolver.ENV_KAFKACONFIG_SEPARATOR, files);
    }
}
