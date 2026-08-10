package kz.lidlowe.sven.core.nonce.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import kz.lidlowe.sven.core.SvenProperties;
import kz.lidlowe.sven.core.nonce.NonceService;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

import java.util.UUID;

@Service
public class CaffeineNonceService implements NonceService {

    private final SvenProperties properties;
    private final Cache<String, String> caffeineCache;

    public CaffeineNonceService(SvenProperties properties) {
        this.properties = properties;
        this.caffeineCache = Caffeine
                .newBuilder()
                .expireAfterWrite(properties.getCache().getTtlSeconds(), java.util.concurrent.TimeUnit.SECONDS)
                .build();
    }

    @Override
    public String generateNonce(String publicKey) {
        String nonce = UUID.randomUUID().toString();
        caffeineCache.put(publicKey, nonce);

        return nonce;
    }

    @Override
    public boolean validateNonce(String publicKey, String nonce) {
        String cacheValue = caffeineCache.getIfPresent(publicKey);

        if (cacheValue != null && cacheValue.equals(nonce)) {
            caffeineCache.invalidate(publicKey);
            return true;
        }

        return false;
    }
}
