import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

public class FluxTest {

    // Flux published the provided values, request of unbounded number of values
    @Test
    void firstFlux(){
        Flux.just("A","B","C")
                .log()
                .subscribe();
    }

    // Values from the list are publish with each onNext(T) call
    // onNext(A)
    // onNext(B)
    // onNext(C)
    @Test
    void fluxFromIterable(){
        Flux.fromIterable(Arrays.asList("A","B","C"))
                .log()
                .subscribe();
    }

    // Range(Start Value, Number of Values)
    // onNext(10)
    // onNext(11)
    @Test
    void fluxFromRange(){
        Flux.range(10,5)
                .log()
                .subscribe();
    }

    @Test
    void fluxFromInterval() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))
                .log()
                .take(2) // only take this amount of values form the Flux then calls cancel not good for backpressure
                .subscribe();
        Thread.sleep(5000); // Runs forever but only finishes because the class exits killing all the threads
    }

    // Same as above but handles backpressure
    @Test
    void fluxRequest(){
        Flux.range(1, 5)
                .log()
                .subscribe(null, null, null,subscription -> subscription.request(3));
    }

    @Test
    void fluxCustomSubscriber(){
        Flux.range(1,10)
                .log()
                .subscribe(new BaseSubscriber<Integer>() {
                    int elementsToProcess = 3;
                    int counter = 0;

                    @Override
                    public void hookOnSubscribe(Subscription subscription) {
                        System.out.println("Subscribed!!");
                        request(elementsToProcess);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        counter++;
                        if(counter == elementsToProcess){
                            counter = 0;

                            Random random = new Random();
                            elementsToProcess = random.ints(1, 4)
                                    .findFirst()
                                    .getAsInt();
                            request(elementsToProcess);
                        }
                    }
                });
    }

    @Test
    void fluxLimitRate(){
        Flux.range(1,5)
                .log()
                .limitRate(3)
                .subscribe();
    }
}
