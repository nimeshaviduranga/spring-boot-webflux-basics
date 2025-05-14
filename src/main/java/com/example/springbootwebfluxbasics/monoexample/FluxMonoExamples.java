package com.example.springbootwebfluxbasics.monoexample;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class FluxMonoExamples implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("\n--- FLUX CREATION EXAMPLES ---");
        createFluxExamples();

        System.out.println("\n--- MONO CREATION EXAMPLES ---");
        createMonoExamples();

        System.out.println("\n--- TRANSFORMATION EXAMPLES ---");
        transformationExamples();

        System.out.println("\n--- FILTERING EXAMPLES ---");
        filteringExamples();

        System.out.println("\n--- COMBINING EXAMPLES ---");
        combiningExamples();

        System.out.println("\n--- ERROR HANDLING EXAMPLES ---");
        errorHandlingExamples();

        System.out.println("\n--- ADVANCED EXAMPLES ---");
        advancedExamples();
    }

    private void createFluxExamples() {
        // Create Flux from values
        Flux<String> fluxFromValues = Flux.just("Apple", "Orange", "Banana", "Kiwi");
        System.out.println("Flux from values:");
        fluxFromValues.subscribe(
                item -> System.out.println("  Received: " + item),
                error -> System.err.println("  Error: " + error),
                () -> System.out.println("  Completed!")
        );

        // Create Flux from array
        String[] fruitsArray = {"Strawberry", "Blueberry", "Raspberry"};
        Flux<String> fluxFromArray = Flux.fromArray(fruitsArray);
        System.out.println("\nFlux from array:");
        fluxFromArray.subscribe(item -> System.out.println("  Received: " + item));

        // Create Flux from iterable
        List<String> fruitsList = Arrays.asList("Mango", "Pineapple", "Papaya");
        Flux<String> fluxFromIterable = Flux.fromIterable(fruitsList);
        System.out.println("\nFlux from iterable:");
        fluxFromIterable.subscribe(item -> System.out.println("  Received: " + item));

        // Create Flux from range
        Flux<Integer> fluxFromRange = Flux.range(1, 5);
        System.out.println("\nFlux from range:");
        fluxFromRange.subscribe(item -> System.out.println("  Received: " + item));

        // Empty Flux
        Flux<String> emptyFlux = Flux.empty();
        System.out.println("\nEmpty Flux:");
        emptyFlux.subscribe(
                item -> System.out.println("  Received: " + item),
                error -> System.err.println("  Error: " + error),
                () -> System.out.println("  Completed with no values!")
        );

        // Flux from interval (commented out to avoid long-running example)
        /*
        System.out.println("\nFlux from interval (one value per second):");
        Flux<Long> fluxFromInterval = Flux.interval(Duration.ofSeconds(1))
                .take(5); // Limit to 5 values
        fluxFromInterval.subscribe(item -> System.out.println("  Received: " + item));

        // Sleep to allow time for interval to emit
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
    }

    private void createMonoExamples() {
        // Create Mono from value
        Mono<String> monoFromValue = Mono.just("Single Value");
        System.out.println("Mono from value:");
        monoFromValue.subscribe(
                item -> System.out.println("  Received: " + item),
                error -> System.err.println("  Error: " + error),
                () -> System.out.println("  Completed!")
        );

        // Empty Mono
        Mono<String> emptyMono = Mono.empty();
        System.out.println("\nEmpty Mono:");
        emptyMono.subscribe(
                item -> System.out.println("  Received: " + item),
                error -> System.err.println("  Error: " + error),
                () -> System.out.println("  Completed with no value!")
        );

        // Mono from Callable
        Mono<String> monoFromCallable = Mono.fromCallable(() -> {
            System.out.println("  Called in Callable");
            return "Result from Callable";
        });
        System.out.println("\nMono from Callable (lazy):");
        System.out.println("  Before subscribe");
        monoFromCallable.subscribe(item -> System.out.println("  Received: " + item));

        // Mono with delay
        System.out.println("\nMono with delay (not waiting for result in this example):");
        Mono<String> delayedMono = Mono.just("Delayed Value")
                .delayElement(Duration.ofSeconds(2));
        delayedMono.subscribe(item -> System.out.println("  Received: " + item));
    }

    private void transformationExamples() {
        // Map transformation
        Flux<String> namesFlux = Flux.just("John", "Jane", "Bob", "Alice");
        System.out.println("Map transformation (to uppercase):");
        namesFlux.map(String::toUpperCase)
                .subscribe(item -> System.out.println("  Transformed: " + item));

        // FlatMap transformation
        Flux<String> wordsFlux = Flux.just("Hello World", "Reactive Programming");
        System.out.println("\nFlatMap transformation (split into words):");
        wordsFlux.flatMap(phrase -> Flux.fromArray(phrase.split("\\s+")))
                .subscribe(item -> System.out.println("  Word: " + item));

        // FlatMap with delay (simulating async operations)
        System.out.println("\nFlatMap with simulated async operations:");
        Flux.range(1, 3)
                .flatMap(i -> asyncOperation(i)
                        .subscribeOn(Schedulers.boundedElastic()))
                .subscribe(result -> System.out.println("  Result: " + result));

        // Sleep to allow async operations to complete
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Transform to uppercase and concatenate
        System.out.println("\nMultiple transformations:");
        namesFlux.map(String::toUpperCase)
                .map(name -> name + "!")
                .subscribe(item -> System.out.println("  Transformed: " + item));
    }

    private Mono<String> asyncOperation(int i) {
        return Mono.just("Operation " + i + " result")
                .delayElement(Duration.ofMillis(new Random().nextInt(1000)));
    }

    private void filteringExamples() {
        Flux<Integer> numbersFlux = Flux.range(1, 10);

        // Filter
        System.out.println("Filter (even numbers):");
        numbersFlux.filter(n -> n % 2 == 0)
                .subscribe(item -> System.out.println("  Even: " + item));

        // Take
        System.out.println("\nTake (first 3 elements):");
        numbersFlux.take(3)
                .subscribe(item -> System.out.println("  Taken: " + item));

        // Skip
        System.out.println("\nSkip (first 5 elements):");
        numbersFlux.skip(5)
                .subscribe(item -> System.out.println("  After skip: " + item));

        // Distinct
        Flux<Integer> duplicatesFlux = Flux.just(1, 2, 2, 3, 4, 4, 5);
        System.out.println("\nDistinct (remove duplicates):");
        duplicatesFlux.distinct()
                .subscribe(item -> System.out.println("  Distinct: " + item));

        // ElementAt
        System.out.println("\nElementAt (get element at index 3):");
        numbersFlux.elementAt(3)
                .subscribe(item -> System.out.println("  Element at index 3: " + item));
    }

    private void combiningExamples() {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("X", "Y", "Z");

        // Concat (sequential combination)
        System.out.println("Concat example:");
        Flux<String> concatFlux = Flux.concat(flux1, flux2);
        concatFlux.subscribe(item -> System.out.println("  Concat: " + item));

        // Merge (interleaved combination - order not guaranteed)
        System.out.println("\nMerge example:");
        Flux<String> mergeFlux = Flux.merge(
                flux1.delayElements(Duration.ofMillis(50)),
                flux2.delayElements(Duration.ofMillis(30))
        );
        mergeFlux.subscribe(item -> System.out.println("  Merge: " + item));

        // Sleep to allow for delayed elements
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Zip (combining corresponding elements)
        System.out.println("\nZip example:");
        Flux<String> zipFlux = Flux.zip(flux1, flux2, (f1, f2) -> f1 + "-" + f2);
        zipFlux.subscribe(item -> System.out.println("  Zipped: " + item));

        // CombineLatest
        System.out.println("\nCombineLatest example:");
        Flux<String> combineLatestFlux = Flux.combineLatest(
                flux1.delayElements(Duration.ofMillis(50)),
                flux2.delayElements(Duration.ofMillis(30)),
                (f1, f2) -> f1 + ":" + f2
        );
        combineLatestFlux.subscribe(item -> System.out.println("  Combined: " + item));

        // Sleep to allow for delayed elements
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void errorHandlingExamples() {
        Flux<String> flux = Flux.just("A", "B")
                .concatWith(Flux.error(new RuntimeException("Simulated error")))
                .concatWith(Flux.just("C"));

        // Without error handling
        System.out.println("Without error handling:");
        flux.subscribe(
                item -> System.out.println("  Received: " + item),
                error -> System.err.println("  Error: " + error),
                () -> System.out.println("  Completed (won't be called)")
        );

        // OnErrorReturn
        System.out.println("\nWith onErrorReturn:");
        flux.onErrorReturn("Default after error")
                .subscribe(
                        item -> System.out.println("  Received: " + item),
                        error -> System.err.println("  Error: " + error),
                        () -> System.out.println("  Completed")
                );

        // OnErrorResume
        System.out.println("\nWith onErrorResume:");
        flux.onErrorResume(e -> {
                    System.out.println("  Resuming with alternate Flux after: " + e.getMessage());
                    return Flux.just("D", "E");
                })
                .subscribe(
                        item -> System.out.println("  Received: " + item),
                        error -> System.err.println("  Error: " + error),
                        () -> System.out.println("  Completed")
                );

        // Retry
        System.out.println("\nWith retry (2 attempts):");
        Flux<String> fluxWithRetry = Flux.concat(
                Flux.just("Attempt"),
                Flux.error(new RuntimeException("Error to retry"))
        );

        fluxWithRetry.retry(2)
                .subscribe(
                        item -> System.out.println("  Received: " + item),
                        error -> System.err.println("  Error after retries: " + error),
                        () -> System.out.println("  Completed")
                );
    }

    private void advancedExamples() {
        // Using defer for lazy evaluation
        System.out.println("Defer example (lazy evaluation):");
        Mono<Long> currentTime = Mono.defer(() -> Mono.just(System.currentTimeMillis()));

        System.out.println("  Time before subscribe: " + System.currentTimeMillis());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        currentTime.subscribe(time ->
                System.out.println("  Time when subscribed: " + time));

        // Using window to batch elements
        System.out.println("\nWindow example (batching):");
        Flux<Integer> rangeFlux = Flux.range(1, 10);
        rangeFlux.window(3)
                .flatMap(fluxOfInts -> {
                    System.out.println("  New window:");
                    return fluxOfInts;
                })
                .subscribe(item -> System.out.println("    Item: " + item));

        // Using switchMap (only processes latest)
        System.out.println("\nSwitchMap example (switches to processing newer values):");
        Flux.just("A", "B", "C")
                .switchMap(letter -> {
                    System.out.println("  Processing letter: " + letter);
                    return Flux.just(letter + "1", letter + "2")
                            .delayElements(Duration.ofMillis(50));
                })
                .subscribe(result -> System.out.println("    Result: " + result));

        // Sleep to allow for delayed elements
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Using doOnNext, doOnError, doOnComplete
        System.out.println("\nUsing side effect operators:");
        Flux.just("One", "Two", "Three")
                .doOnSubscribe(s -> System.out.println("  Subscription happened"))
                .doOnNext(s -> System.out.println("  About to emit: " + s))
                .doOnComplete(() -> System.out.println("  Completed"))
                .subscribe(item -> System.out.println("    Received: " + item));
    }
}