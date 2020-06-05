package kafkactl.utils;

import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.internals.KafkaFutureImpl;

import java.util.concurrent.CompletableFuture;

public final class KafkaFutures {

    private KafkaFutures() {
    }

    /**
     * Returns a {@link KafkaFuture} that is completed exceptionally with the given {@code
     * exception}.
     */
    public static <T> KafkaFuture<T> failedFuture(Throwable exception) {
        KafkaFutureImpl<T> future = new KafkaFutureImpl<>();
        future.completeExceptionally(exception);
        return future;
    }

    /**
     * Converts the given {@link KafkaFuture} to a {@link CompletableFuture}.
     */
    public static <T> CompletableFuture<T> toCompletableFuture(KafkaFuture<T> kafkaFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        kafkaFuture.whenComplete(
                (value, exception) -> {
                    if (exception == null) {
                        completableFuture.complete(value);
                    } else {
                        completableFuture.completeExceptionally(exception);
                    }
                });
        return completableFuture;
    }
}