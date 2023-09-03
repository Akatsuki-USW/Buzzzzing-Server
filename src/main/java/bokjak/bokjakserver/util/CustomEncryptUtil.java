package bokjak.bokjakserver.util;

import bokjak.bokjakserver.common.exception.StatusCode;
import bokjak.bokjakserver.domain.user.exeption.AuthException;
import bokjak.bokjakserver.domain.user.exeption.UserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Component
public class CustomEncryptUtil {

    private byte[] key;
    private SecretKeySpec secretKeySpec;

    public CustomEncryptUtil(@Value("${aes.secret-key}") String rawKey) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            key = rawKey.getBytes(StandardCharsets.UTF_8);
            key = sha.digest(key);  //바이트배열로 해쉬 반환
            key = Arrays.copyOf(key, 24);
            secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (Exception ignored) {
        }
    }

    public String hash(String socialEmail) {
        try{
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(socialEmail.getBytes());

            return bytesToHex(sha.digest());
        } catch (Exception ignored) {
            throw new UserException(StatusCode.ENCRYPTION_FAILURE);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public String encrypt(String str) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return encodeBase64(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new AuthException(StatusCode.ENCRYPTION_FAILURE);
        }
    }
    //secretKey의 길이가 32bit면 AES-256, 24bit면 AES-192, 16bit의 경우 AES-128로 암호화

    public String decrypt(String str) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(decodeBase64(str)));
        } catch (Exception e) {
            throw new AuthException(StatusCode.DECRYPTION_FAILURE);
        }
    }

    private String encodeBase64(byte[] source) {
        return Base64.getEncoder().encodeToString(source);
    }

    private byte[] decodeBase64(String encodedString) {
        return Base64.getDecoder().decode(encodedString);
    }
}
