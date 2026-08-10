package kz.lidlowe.sven.core.verification.l1;

import lombok.extern.slf4j.Slf4j;
import org.p2p.solanaj.utils.Base58;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

@Component
@Slf4j
public class Ed25519Verifier {

    public boolean verify(String publicKey, String nonce, String signature) {
        byte[] decodedKey = Base58.decode(publicKey);
        byte[] decodedSignature = Base58.decode(signature);
        boolean verified = false;

        try {
            KeySpec spec = new X509EncodedKeySpec(decodedKey);
            KeyFactory factory = KeyFactory.getInstance("Ed25519");
            PublicKey key = factory.generatePublic(spec);

            Signature sig = Signature.getInstance("Ed25519");
            sig.initVerify(key);
            sig.update(nonce.getBytes(StandardCharsets.UTF_8));

            verified = sig.verify(decodedSignature);
        } catch (NoSuchAlgorithmException e) {
            log.error("[sven-core] Ed25519 not available in JDK", e);
            return false;
        } catch (InvalidKeySpecException e) {
            log.warn("[sven-core] Invalid public key format: {}", publicKey);
            return false;
        } catch (InvalidKeyException e) {
            log.warn("[sven-core] Invalid public key: {}", publicKey);
            return false;
        } catch (SignatureException e) {
            log.warn("[sven-core] Signature verification failed for key: {}", publicKey);
            return false;
        }

        return verified;
    }

}
