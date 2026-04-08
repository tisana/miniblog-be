package me.tisana.miniblog.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AuthorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Author getAuthorSample1() {
        return new Author().id(1L).username("username1").password("password1");
    }

    public static Author getAuthorSample2() {
        return new Author().id(2L).username("username2").password("password2");
    }

    public static Author getAuthorRandomSampleGenerator() {
        return new Author().id(longCount.incrementAndGet()).username(UUID.randomUUID().toString()).password(UUID.randomUUID().toString());
    }
}
