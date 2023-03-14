package org.aery.blackjack;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;
import java.util.stream.IntStream;

public class Force {

    private static class Style {
        private final static String BLACK_JACK = "黑傑克";

        private final static String FIVE_CARDS = "五小龍";

        private final static String EXPLODE = "爆牌";

        private final static Map<Integer, String> POINT = Collections.unmodifiableMap(
                IntStream.rangeClosed(4, 21).collect(
                        HashMap::new,
                        (map, point) -> map.put(point, String.format("%02d", point) + "點"),
                        Map::putAll
                )
        );
    }

    public static void go(int eachCardNumberTestTimes, int eachCardNumberTestThreads, IntSupplier launchCard) {
        int cardNumber2345 = 4;
        long totalTestTimes = eachCardNumberTestTimes * cardNumber2345;
        int totalThreads = cardNumber2345 * eachCardNumberTestThreads;
        int eachThreadTestTimes = eachCardNumberTestTimes / eachCardNumberTestThreads;

        Map<String, AtomicLong> styleCount = initStyleCount();

        List<Future<?>> futures = new ArrayList<>(totalThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);
        Consumer<Runnable> loop = runnable -> futures.add(executorService.submit(() -> {
            for (int i = 0; i < eachThreadTestTimes; i++) {
                runnable.run();
            }
        }));

        for (int i = 0; i < eachCardNumberTestThreads; i++) {
            loop.accept(() -> { // C(104,2)
                int card1 = launchCard.getAsInt();
                int card2 = launchCard.getAsInt();
                int point = BlackJackPoint.sum(card1, card2);

                String trem = point == 21 ? Style.BLACK_JACK : Style.POINT.get(point);
                styleCount.get(trem).incrementAndGet();
            });

            loop.accept(() -> { // C(104,3)
                int card1 = launchCard.getAsInt();
                int card2 = launchCard.getAsInt();
                int card3 = launchCard.getAsInt();
                int point = BlackJackPoint.sum(card1, card2, card3);

                String trem = point > 21 ? Style.EXPLODE : Style.POINT.get(point);
                styleCount.get(trem).incrementAndGet();
            });

            loop.accept(() -> { // C(104,4)
                int card1 = launchCard.getAsInt();
                int card2 = launchCard.getAsInt();
                int card3 = launchCard.getAsInt();
                int card4 = launchCard.getAsInt();
                int point = BlackJackPoint.sum(card1, card2, card3, card4);

                String trem = point > 21 ? Style.EXPLODE : Style.POINT.get(point);
                styleCount.get(trem).incrementAndGet();
            });

            loop.accept(() -> { // C(104,5)
                int card1 = launchCard.getAsInt();
                int card2 = launchCard.getAsInt();
                int card3 = launchCard.getAsInt();
                int card4 = launchCard.getAsInt();
                int card5 = launchCard.getAsInt();
                int point = BlackJackPoint.sum(card1, card2, card3, card4, card5);

                String trem = point > 21 ? Style.EXPLODE : Style.FIVE_CARDS;
                styleCount.get(trem).incrementAndGet();
            });
        }

        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        LongFunction<String> format1 = number -> String.format("%,15d", number);
        BiFunction<Long, Long, String> format2 = (n1, n2) -> String.format("%7.4f", 100d * n1 / n2);
        BiConsumer<String, String> print = (s1, s2) -> System.out.println(s1 + "\t" + s2);

        print.accept("總共", format1.apply(totalTestTimes) + " 次");
        styleCount.forEach((term, countAtomicLong) -> {
            long count = countAtomicLong.get();
            print.accept(term, format1.apply(count) + " 次, " + format2.apply(count, totalTestTimes) + "%");
        });
    }

    private static Map<String, AtomicLong> initStyleCount() {
        Map<String, AtomicLong> styleCount = new TreeMap<>();
        styleCount.put(Style.BLACK_JACK, new AtomicLong());
        styleCount.put(Style.FIVE_CARDS, new AtomicLong());
        styleCount.put(Style.EXPLODE, new AtomicLong());
        Style.POINT.forEach((point, term) -> styleCount.put(term, new AtomicLong()));
        return styleCount;
    }

}
