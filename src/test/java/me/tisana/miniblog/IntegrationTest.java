package me.tisana.miniblog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.tisana.miniblog.config.AsyncSyncConfiguration;
import me.tisana.miniblog.config.DatabaseTestcontainer;
import me.tisana.miniblog.config.JacksonConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        MiniBlogApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        me.tisana.miniblog.config.JacksonHibernateConfiguration.class,
    }
)
@ImportTestcontainers(DatabaseTestcontainer.class)
public @interface IntegrationTest {}
