package kafkactl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RegisterForReflection
public class KafkaConfig {

    private String currentContext;

    private Map<String, Map<String, Object>> clusters = Collections.emptyMap();

    private Map<String, Map<String, Object>> users = Collections.emptyMap();

    private Map<String, Map<String, Object>> contexts = Collections.emptyMap();

    public String getCurrentContext() {
        return currentContext;
    }

    public void setCurrentContext(String currentContext) {
        this.currentContext = currentContext;
    }

    public Map<String, Map<String, Object>> getClusters() {
        return clusters;
    }

    public void setClusters(Map<String, Map<String, Object>> clusters) {
        this.clusters = clusters;
    }

    public Map<String, Map<String, Object>> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Map<String, Object>> users) {
        this.users = users;
    }

    public Map<String, Map<String, Object>> getContexts() {
        return contexts;
    }

    public void setContexts(Map<String, Map<String, Object>> contexts) {
        this.contexts = contexts;
    }

    Map<String, Object> getClusterProperties(String cluster) {
        return clusters.get(cluster);
    }

    @JsonIgnore
    public KafkaConfig getCurrentContextConfig() {
        return getContextAsConfig(currentContext);
    }

    @JsonIgnore
    public KafkaConfig getContextAsConfig(String context) {
        if (!contexts.containsKey(context)) {
            return null;
        }
        String clusterName = (String) contexts.get(context).get("cluster");
        String userName = (String) contexts.get(context).get("user");
        Map<String, Object> cluster = clusters.get(clusterName);
        Map<String, Object> user = users.get(userName);
        var config = new KafkaConfig();
        config.setCurrentContext(context);
        config.setClusters(Map.of(clusterName, cluster));
        config.setUsers(Map.of(userName, user));
        Map<String, Map<String, Object>> currentContext = Map.of(
                context,
                Map.of(
                        "cluster", clusterName,
                        "user", userName
                )
        );
        config.setContexts(currentContext);
        return config;
    }

    public KafkaConfig merge(KafkaConfig loaded) {
        var mergedConfig = new KafkaConfig();
        mergedConfig.setCurrentContext((currentContext != null) ? currentContext : loaded.getCurrentContext());
        mergedConfig.setClusters(shallowMerge(clusters, loaded.getClusters()));
        mergedConfig.setUsers(shallowMerge(users, loaded.getUsers()));
        mergedConfig.setContexts(shallowMerge(contexts, loaded.getContexts()));
        return mergedConfig;
    }

    private Map shallowMerge(Map first, Map second) {
        var merged = new HashMap<>(first);
        second.forEach((key, value) -> merged.merge(key, value, (firstValue, secondValue) -> firstValue));
        return merged;
    }
}
