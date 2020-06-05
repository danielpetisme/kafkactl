package kafkactl.config;

import java.util.List;

public enum ConfigFormat {
    JSON, YAML;

    private List<String> values;

    public static ConfigFormat to(String value) {
        if (value.equalsIgnoreCase("yaml") || value.equalsIgnoreCase("yml")) {
            return YAML;
        } else return JSON;
    }
}
