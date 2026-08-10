package kz.lidlowe.sven.core.nonce;

public interface NonceService {
    String generateNonce(String publicKey);
    boolean validateNonce(String publicKey, String nonce);
}
