package me.tisana.miniblog.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Card getCardSample1() {
        return new Card().id(1L).name("name1").content("content1");
    }

    public static Card getCardSample2() {
        return new Card().id(2L).name("name2").content("content2");
    }

    public static Card getCardRandomSampleGenerator() {
        return new Card().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).content(UUID.randomUUID().toString());
    }
}
