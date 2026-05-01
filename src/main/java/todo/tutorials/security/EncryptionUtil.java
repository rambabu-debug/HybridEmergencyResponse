package todo.tutorials.security;

import org.apache.commons.codec.binary.Base64;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class EncryptionUtil {
    private static final String SECRET_KEY = "hybrid-emergency-response-system-secret-key";

    public static String encrypt(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((data + SECRET_KEY).getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(hash);
        } catch (Exception e) {
            return data;
        }
    }

    public static String generateQRCode(String data) {
        return Base64.encodeBase64String(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeQRCode(String encoded) {
        try {
            byte[] decodedBytes = Base64.decodeBase64(encoded);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return encoded;
        }
    }

    public static boolean verifyHash(String original, String hash) {
        String computed = encrypt(original);
        return computed.equals(hash);
    }
}

