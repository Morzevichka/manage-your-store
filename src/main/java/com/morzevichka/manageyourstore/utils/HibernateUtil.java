package com.morzevichka.manageyourstore.utils;

import com.morzevichka.manageyourstore.entity.*;

import com.morzevichka.manageyourstore.repository.WorkerRepository;
import com.morzevichka.manageyourstore.services.WorkerServiceImpl;
import com.morzevichka.manageyourstore.services.impl.WorkerService;
import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.source.spi.PluralAttributeElementSourceAssociation;
import org.hibernate.cfg.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

public final class HibernateUtil {
    private final static SessionFactory sessionFactory;

    static {
        try {
            String dbProfile = System.getProperty("db", "h2");

            Configuration configuration = new Configuration();

            if (dbProfile.equals("oracle")) {
                Dotenv dotenv = Dotenv.configure()
                        .directory("src/main/assets")
                        .filename("env")
                        .load();

                configuration.configure("hibernate-oracle.cfg.xml");
                configuration.setProperty("hibernate.connection.url", dotenv.get("DB_URL"));
                configuration.setProperty("hibernate.connection.username", dotenv.get("DB_USERNAME"));
                configuration.setProperty("hibernate.connection.password", dotenv.get("DB_PASSWORD"));
            } else {
                configuration.configure("hibernate-h2.cfg.xml");
            }

            // Create L2 Cache configuration
            CachingProvider provider = Caching.getCachingProvider();
            CacheManager cacheManager = provider.getCacheManager();

            MutableConfiguration<Object, Object> config = new MutableConfiguration<>()
                    .setStoreByValue(false)
                    .setStatisticsEnabled(true)
                    .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.TEN_MINUTES));

            cacheManager.createCache("com.morzevichka.manageyourstore.entity.Card", config);
            cacheManager.createCache("com.morzevichka.manageyourstore.entity.Card.clients", config);
            cacheManager.createCache("com.morzevichka.manageyourstore.entity.Client", config);
            cacheManager.createCache("com.morzevichka.manageyourstore.entity.Client.orders", config);
            cacheManager.createCache("com.morzevichka.manageyourstore.entity.Order", config);
            cacheManager.createCache("com.morzevichka.manageyourstore.entity.Category", config);
            cacheManager.createCache("com.morzevichka.manageyourstore.entity.Category.products", config);
            cacheManager.createCache("com.morzevichka.manageyourstore.entity.Product", config);
            cacheManager.createCache("com.morzevichka.manageyourstore.entity.Product.orderItems", config);
            cacheManager.createCache("com.morzevichka.manageyourstore.entity.Worker", config);
            cacheManager.createCache("com.morzevichka.manageyourstore.entity.Worker.orders", config);
            cacheManager.createCache("default-query-results-region", config);
            cacheManager.createCache("default-update-timestamps-region", config);

            configuration.getProperties().put("hibernate.javax.cache.manager", cacheManager);

            configuration.addAnnotatedClass(Card.class);
            configuration.addAnnotatedClass(Category.class);
            configuration.addAnnotatedClass(Client.class);
            configuration.addAnnotatedClass(Order.class);
            configuration.addAnnotatedClass(OrderItem.class);
            configuration.addAnnotatedClass(Product.class);
            configuration.addAnnotatedClass(Worker.class);

            sessionFactory = configuration.buildSessionFactory();

        } catch (Exception e) {
            System.err.println("Initial SessionFactory creation failed: "+ e);
            throw new ExceptionInInitializerError(e);
        }
    }

    private HibernateUtil() {}

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void close() {
        sessionFactory.close();
    }

    public static void setUpTestUserForH2DB() {
        if (System.getProperty("db").equals("h2")) {
            WorkerService workerService = new WorkerServiceImpl();
            Worker worker = new Worker();
            worker.setFirstName("test");
            worker.setSecondName("test");
            worker.setUsername("test");
            worker.setPasswordHash(PasswordUtils.encrypt("test"));
            worker.setRole(Role.ADMIN);
            workerService.registerWorker(worker);
        } else {
            throw new IllegalStateException("Database should be H2");
        }
    }
}
