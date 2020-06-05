package kafkactl.cmd;

import picocli.CommandLine;

@CommandLine.Command(name = "get", description = "Get topic information")
public class Get {

//    @Inject
//    AdminClient adminClient;

    @CommandLine.Command(name = "topic")
    public void topic() {
//        KafkaFutures
//                .toCompletableFuture(adminClient.listTopics(new ListTopicsOptions()).names())
//                .thenAccept((topics) -> {
//                    topics.stream().forEach((name) -> System.out.println("Topic " + name));
//                });

    }
}
