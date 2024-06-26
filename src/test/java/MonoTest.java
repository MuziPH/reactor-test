import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class MonoTest {

    // Mono publisher but no subscription
    @Test
    void firstMono() {
        Mono.just("A");
    }

    // Added subscription to the Mono publisher so there will be log outputs
    @Test
    void monoWithConsumer() {
        Mono.just("A")
                .log()
                .doOnSubscribe(str -> System.out.println("On Subscribed: " + str))
                .doOnRequest(i -> System.out.println("On Request: " + i))
                .doOnSuccess(str -> System.out.println("On Complete/Success: " + str))
                .subscribe(System.out::println);
    }

    // Similar to returning void in the traditional sense
    @Test
    void emptyMono() {
        Mono.empty()
                .log()
                .subscribe(System.out::println);
    }

    @Test
    void emptyMonoWithConsumer() {
        Mono.empty()
                .log()
                .subscribe(System.out::println);
    }

    @Test
    void emptyOnCompleteConsumerMono() {
        Mono.empty()
                .log()
                .subscribe(
                        System.out::println,
                        null,
                        () -> System.out.println("Done")
                );
    }

    @Test
    void errorRuntimeException() {
        Mono.error(new RuntimeException())
                .log()
                .subscribe();
    }

    @Test
    void errorCheckedException() {
        Mono.error(new Exception())
                .log()
                .subscribe();
    }

    // Catch the thrown exception for whatever reason
    @Test
    void errorConsumerMono() {
        Mono.error(new Exception())
                .log()
                .subscribe(System.out::println, error -> System.out.println("Exception caught: " + error));
    }

    // same as above using doOnError
    // Simulates catching the exception with doOnError
    @Test
    void errorDoOnErrorMono() {
        Mono.error(new Exception())
                .doOnError(error -> System.out.println("Caught error with doOnError: " + error))
                .log()
                .subscribe();
    }

    // Fail on error gracefully and return a Mono, no stack trace
    // Catch the exception - print a message, return a fallback Mono of B
    @Test
    void errorOnErrorResume() {
        Mono.error(new Exception())
                .onErrorResume(error -> {
                    System.out.println("Exception caught: " + error);
                    return Mono.just("B");
                })
                .log()
                .subscribe();
    }

    // Fail but just return a value that is not a Mono like above
    @Test
    void errorOnErrorReturn() {
        Mono.error(new Exception())
                .onErrorReturn("C")
                .log()
                .subscribe();
    }
}
