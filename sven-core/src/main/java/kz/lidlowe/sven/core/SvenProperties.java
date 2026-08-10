package kz.lidlowe.sven.core;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
@ConfigurationProperties(prefix = "sven")
public class SvenProperties {

    @Data
    public static class Cache {
        private String type = "caffeine";
        private Integer ttlSeconds = 300;
    }

    @Data
    public static class Jwt {
        @NotBlank
        private String secret;
        private Integer expirationSeconds = 3600;
    }

    private String rpc = "https://api.mainnet-beta.solana.com";
    private Cache cache = new Cache();
    private Jwt jwt = new Jwt();
    private List<String> blacklist;

}
