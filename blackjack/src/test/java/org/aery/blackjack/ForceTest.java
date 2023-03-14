package org.aery.blackjack;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class ForceTest {

    @Test
    public void go() {
        int eachCardNumberTestTimes = 100_000_000;
        int eachCardNumberTestThreads = 4;

//        List<Integer> seeds = Stream.of(suitPoker(), suitPoker()).flatMap(List::stream).collect(Collectors.toList());
//        IntSupplier launchCard = () -> random(seeds);
        IntSupplier launchCard = ForceTest::random;

        Force.go(eachCardNumberTestTimes, eachCardNumberTestThreads, launchCard);
    }

    private static List<Integer> suitPoker() {
        return Stream.of(
                IntStream.rangeClosed(1, 13).boxed().collect(Collectors.toList()),
                IntStream.rangeClosed(1, 13).boxed().collect(Collectors.toList()),
                IntStream.rangeClosed(1, 13).boxed().collect(Collectors.toList()),
                IntStream.rangeClosed(1, 13).boxed().collect(Collectors.toList())
        ).flatMap(List::stream).collect(Collectors.toList());
    }

    private static int random(List<Integer> seeds) {
        int index = (int) (Math.random() * seeds.size());
        return seeds.get(index);
    }

    private static int random() {
        return 1 + ((int) (Math.random() * 13));
    }

}
