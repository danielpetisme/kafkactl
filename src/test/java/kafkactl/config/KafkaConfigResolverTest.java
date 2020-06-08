package kafkactl.config;

import kafkactl.service.ConfigService;
import org.junit.jupiter.api.Test;

import java.io.File;

import static kafkactl.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class KafkaConfigResolverTest {

    @Test
    public void should_load_explicit_arg_without_merge_when_defined() {
        var resolver = new KafkaConfigResolver(new ConfigService());
        resolver.setKafkaConfig(new File(fromResources("configs/config1")));
        resolver.setEnvVarSupplier(() -> kafkaConfigEnvVar(fromResources("configs/config2")));
        resolver.setGlobalFileSupplier(() -> fromResources("configs/config3"));
        var config = resolver.getKafkaConfig();
        assertThat(config.currentContext).isEqualToIgnoringCase("context1");
        assertThat(config.clusters.size()).isEqualTo(1);
    }

    @Test
    public void should_load_env_var_files_with_merge_when_no_explicit_arg_defined() {
        var resolver = new KafkaConfigResolver(new ConfigService());
        resolver.setEnvVarSupplier(() -> kafkaConfigEnvVar(fromResources("configs/config1"), fromResources("configs/config2")));
        resolver.setGlobalFileSupplier(() -> fromResources("configs/config3"));
        var config = resolver.getKafkaConfig();
        assertThat(config.currentContext).isEqualToIgnoringCase("context1");
        assertThat(config.clusters.size()).isEqualTo(2);
        assertThat(config.clusters.containsKey("cluster3")).isFalse();
    }

    @Test
    public void should_load_global_file_without_merge_when_no_explicit_arg_and_no_env_var_defined() {
        var resolver = new KafkaConfigResolver(new ConfigService());
        resolver.setEnvVarSupplier(() -> emptyString);
        resolver.setGlobalFileSupplier(() -> fromResources("configs/config1"));
        var config = resolver.getKafkaConfig();
        assertThat(config.currentContext).isEqualToIgnoringCase("context1");
        assertThat(config.clusters.size()).isEqualTo(1);
    }

    @Test
    public void should_return_empty_config_when_no_explicit_arg_and_no_env_var_defined_and_no_gobal_file_exists() {
        var resolver = new KafkaConfigResolver(new ConfigService());
        resolver.setEnvVarSupplier(() -> emptyString);
        resolver.setGlobalFileSupplier(() -> "UNKNOWN");
        var config = resolver.getKafkaConfig();
        assertThat(config.currentContext).isBlank();
        assertThat(config.clusters.isEmpty()).isTrue();
    }

//    @Test
//    public void should_use_explicit_context_arg_when_defined() {
//        var resolver = new KafkaConfigResolver(new ConfigService());
//        resolver.setEnvVarSupplier(() -> kafkaConfigEnvVar(fromResources("configs/config1"), " ", fromResources("configs/config2"), emptyString));
//        resolver.setContext("context2");
//        var config = resolver.getKafkaConfig();
//        assertThat(config.currentContext).isEqualToIgnoringCase("context2");
//    }

    @Test
    public void should_use_current_context_from_merged_config_when_no_explicit_context_defined() {
        var resolver = new KafkaConfigResolver(new ConfigService());
        resolver.setEnvVarSupplier(() -> kafkaConfigEnvVar(fromResources("configs/config1"), fromResources("configs/config2")));
        var config = resolver.getKafkaConfig();
        assertThat(config.currentContext).isEqualToIgnoringCase("context1");
    }

    @Test
    public void should_remove_duplicate_from_env_var() {
        var resolver = new KafkaConfigResolver(new ConfigService());
        resolver.setEnvVarSupplier(() -> kafkaConfigEnvVar(fromResources("configs/config1"), fromResources("configs/config1")));
        var files = resolver.getEnvVarKafkaConfigFiles();
        assertThat(files.size()).isEqualTo(1);
    }

    @Test
    public void should_remove_empty_values_from_env_var() {
        var resolver = new KafkaConfigResolver(new ConfigService());
        resolver.setEnvVarSupplier(() -> kafkaConfigEnvVar(fromResources("configs/config1"), " ", fromResources("configs/config2"), emptyString));
        var files = resolver.getEnvVarKafkaConfigFiles();
        assertThat(files.size()).isEqualTo(2);
    }
}
