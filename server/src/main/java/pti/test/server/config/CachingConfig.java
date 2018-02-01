package pti.test.server.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableCaching
class CachingConfig {

    @Bean
    CacheManager cacheManager() {

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ConcurrentMapCache[] caches = new ConcurrentMapCache[]{new ConcurrentMapCache("product1"),
                new ConcurrentMapCache("product2"), new ConcurrentMapCache("product3"),
                new ConcurrentMapCache("product4"), new ConcurrentMapCache("product5"),
                new ConcurrentMapCache("types"), new ConcurrentMapCache("users1"),
                new ConcurrentMapCache("users2")};
        cacheManager.setCaches(Arrays.asList(caches));

        return cacheManager;

    }

}