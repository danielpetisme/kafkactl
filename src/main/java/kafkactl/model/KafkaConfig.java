package kafkactl.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;

@RegisterForReflection
public class KafkaConfig {

    public static KafkaConfig empty() {
        return new KafkaConfig("", emptyMap(), emptyMap(), emptyMap());
    }

    public static KafkaConfig fromMap(Map<String, Object> map) {
        Map<String, Map<String, Object>> clusters = (Map<String, Map<String, Object>>) map.getOrDefault("clusters", Collections.emptyMap());
        Map<String, Map<String, Object>> users = (Map<String, Map<String, Object>>) map.getOrDefault("users", Collections.emptyMap());
        Map<String, Map<String, Object>> contexts = (Map<String, Map<String, Object>>) map.getOrDefault("contexts", Collections.emptyMap());
        String currentContext = (String) map.getOrDefault("current-context", "");

        var config = new KafkaConfig(currentContext, clusters, users, contexts);
        return config;
    }

    public static Map<String, Object> toMap(KafkaConfig kafkaConfig) {
        Map<String, Object> map = new HashMap<>();
        map.put("current-context", kafkaConfig.currentContext);
        map.put("clusters", kafkaConfig.clusters);
        map.put("users", kafkaConfig.clusters);
        map.put("contexts", kafkaConfig.clusters);
        return map;
    }

    public final String currentContext;
    public final Map<String, Map<String, Object>> clusters;
    public final Map<String, Map<String, Object>> users;
    public final Map<String, Map<String, Object>> contexts;

    public KafkaConfig(String currentContext, Map<String, Map<String, Object>> clusters, Map<String, Map<String, Object>> users, Map<String, Map<String, Object>> contexts) {
        this.currentContext = currentContext;
        this.clusters = clusters;
        this.users = users;
        this.contexts = contexts;
    }

    public KafkaConfig minify(String context) {
        if (!contexts.containsKey(context)) {
            return empty();
        }
        String clusterName = (String) contexts.get(context).getOrDefault("cluster", "");
        String userName = (String) contexts.get(context).getOrDefault("user", "");
        Map<String, Object> cluster = clusters.getOrDefault(clusterName, new HashMap<>());
        Map<String, Object> user = users.getOrDefault(userName, new HashMap<>());
        return new KafkaConfig(
                context,
                Map.of(clusterName, cluster),
                Map.of(userName, user),
                Map.of(
                        context,
                        Map.of(
                                "cluster", clusterName,
                                "user", userName
                        )

                )
        );
    }

    public KafkaConfig merge(KafkaConfig loaded) {
        return new KafkaConfig(
                (currentContext != null && !currentContext.trim().isBlank()) ? currentContext : loaded.currentContext,
                shallowMerge(clusters, loaded.clusters),
                shallowMerge(users, loaded.contexts),
                shallowMerge(users, loaded.contexts)
        );
    }

    private Map shallowMerge(Map first, Map second) {
        var merged = new HashMap<>(first);
        second.forEach((key, value) -> merged.merge(key, value, (firstValue, secondValue) -> firstValue));
        return merged;
    }

    public Map<String, Object> toMap() {
        return toMap(this);
    }

}
