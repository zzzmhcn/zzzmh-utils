package cn.zzzmh.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;

/**
 * AES工具类
 * 使用AES/CBC/PKCS7Padding加密算法
 * 
 * @author zzzmh
 * @since 1.0.0
 */
public class AESUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS7Padding";

    // 静态初始化Bouncy Castle提供者
    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * 私有构造函数，防止实例化
     */
    private AESUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * AES加密
     * 
     * @param plainData 明文数据
     * @param secretKey 密钥（16、24或32字节）
     * @param iv 初始化向量（16字节）
     * @return 加密后的数据
     */
    public static byte[] encrypt(byte[] plainData, byte[] secretKey, byte[] iv) {
        if (plainData == null) {
            throw new IllegalArgumentException("Plain data cannot be null");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("Secret key cannot be null");
        }
        if (iv == null) {
            throw new IllegalArgumentException("IV cannot be null");
        }
        if (iv.length != 16) {
            throw new IllegalArgumentException("IV must be 16 bytes");
        }
        if (secretKey.length != 16 && secretKey.length != 24 && secretKey.length != 32) {
            throw new IllegalArgumentException("Secret key must be 16, 24 or 32 bytes");
        }

        try {
            // 创建密钥规范
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGORITHM);
            
            // 创建IV参数规范
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            
            // 创建加密器
            Cipher cipher = Cipher.getInstance(TRANSFORMATION, BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            
            // 执行加密
            return cipher.doFinal(plainData);
            
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败: " + e.getMessage(), e);
        }
    }

    /**
     * AES解密
     * 
     * @param encryptedData 加密数据
     * @param secretKey 密钥（16、24或32字节）
     * @param iv 初始化向量（16字节）
     * @return 解密后的数据
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] secretKey, byte[] iv) {
        if (encryptedData == null) {
            throw new IllegalArgumentException("Encrypted data cannot be null");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("Secret key cannot be null");
        }
        if (iv == null) {
            throw new IllegalArgumentException("IV cannot be null");
        }
        if (iv.length != 16) {
            throw new IllegalArgumentException("IV must be 16 bytes");
        }
        if (secretKey.length != 16 && secretKey.length != 24 && secretKey.length != 32) {
            throw new IllegalArgumentException("Secret key must be 16, 24 or 32 bytes");
        }

        try {
            // 创建密钥规范
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, ALGORITHM);
            
            // 创建IV参数规范
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            
            // 创建解密器
            Cipher cipher = Cipher.getInstance(TRANSFORMATION, BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            
            // 执行解密
            return cipher.doFinal(encryptedData);
            
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败: " + e.getMessage(), e);
        }
    }

    // ==================== Base36字符串加密 ====================

    /**
     * AES字符串加密（使用Base36编码）
     * 
     * @param plainText 明文字符串
     * @param secretKey 密钥字符串（Base36编码）
     * @param iv 初始化向量字符串（Base36编码）
     * @return Base36编码的加密字符串
     */
    public static String encryptBase36(String plainText, String secretKey, String iv) {
        if (plainText == null) {
            throw new IllegalArgumentException("Plain text cannot be null");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("Secret key cannot be null");
        }
        if (iv == null) {
            throw new IllegalArgumentException("IV cannot be null");
        }

        try {
            // 将字符串转换为字节数组
            byte[] plainData = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] keyBytes = EncoderUtils.base36Decode(secretKey);
            byte[] ivBytes = EncoderUtils.base36Decode(iv);
            
            // 执行加密
            byte[] encryptedData = encrypt(plainData, keyBytes, ivBytes);
            
            // 返回Base36编码的结果
            return EncoderUtils.base36Encode(encryptedData);
            
        } catch (Exception e) {
            throw new RuntimeException("AES Base36字符串加密失败: " + e.getMessage(), e);
        }
    }

    /**
     * AES字符串解密（使用Base36编码）
     * 
     * @param encryptedText 加密字符串（Base36编码）
     * @param secretKey 密钥字符串（Base36编码）
     * @param iv 初始化向量字符串（Base36编码）
     * @return 解密后的明文字符串
     */
    public static String decryptBase36(String encryptedText, String secretKey, String iv) {
        if (encryptedText == null) {
            throw new IllegalArgumentException("Encrypted text cannot be null");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("Secret key cannot be null");
        }
        if (iv == null) {
            throw new IllegalArgumentException("IV cannot be null");
        }

        try {
            // 将Base36字符串转换为字节数组
            byte[] encryptedData = EncoderUtils.base36Decode(encryptedText);
            byte[] keyBytes = EncoderUtils.base36Decode(secretKey);
            byte[] ivBytes = EncoderUtils.base36Decode(iv);
            
            // 执行解密
            byte[] plainData = decrypt(encryptedData, keyBytes, ivBytes);
            
            // 返回UTF-8字符串
            return new String(plainData, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            throw new RuntimeException("AES Base36字符串解密失败: " + e.getMessage(), e);
        }
    }

    // ==================== Base64字符串加密 ====================

    /**
     * AES字符串加密（使用Base64编码）
     * 
     * @param plainText 明文字符串
     * @param secretKey 密钥字符串（Base64编码）
     * @param iv 初始化向量字符串（Base64编码）
     * @return Base64编码的加密字符串
     */
    public static String encryptBase64(String plainText, String secretKey, String iv) {
        if (plainText == null) {
            throw new IllegalArgumentException("Plain text cannot be null");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("Secret key cannot be null");
        }
        if (iv == null) {
            throw new IllegalArgumentException("IV cannot be null");
        }

        try {
            // 将字符串转换为字节数组
            byte[] plainData = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] keyBytes = EncoderUtils.base64Decode(secretKey);
            byte[] ivBytes = EncoderUtils.base64Decode(iv);
            
            // 执行加密
            byte[] encryptedData = encrypt(plainData, keyBytes, ivBytes);
            
            // 返回Base64编码的结果
            return EncoderUtils.base64Encode(encryptedData);
            
        } catch (Exception e) {
            throw new RuntimeException("AES Base64字符串加密失败: " + e.getMessage(), e);
        }
    }

    /**
     * AES字符串解密（使用Base64编码）
     * 
     * @param encryptedText 加密字符串（Base64编码）
     * @param secretKey 密钥字符串（Base64编码）
     * @param iv 初始化向量字符串（Base64编码）
     * @return 解密后的明文字符串
     */
    public static String decryptBase64(String encryptedText, String secretKey, String iv) {
        if (encryptedText == null) {
            throw new IllegalArgumentException("Encrypted text cannot be null");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("Secret key cannot be null");
        }
        if (iv == null) {
            throw new IllegalArgumentException("IV cannot be null");
        }

        try {
            // 将Base64字符串转换为字节数组
            byte[] encryptedData = EncoderUtils.base64Decode(encryptedText);
            byte[] keyBytes = EncoderUtils.base64Decode(secretKey);
            byte[] ivBytes = EncoderUtils.base64Decode(iv);
            
            // 执行解密
            byte[] plainData = decrypt(encryptedData, keyBytes, ivBytes);
            
            // 返回UTF-8字符串
            return new String(plainData, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            throw new RuntimeException("AES Base64字符串解密失败: " + e.getMessage(), e);
        }
    }

    // ==================== 兼容性方法 ====================

    /**
     * AES字符串加密（使用Base36编码）
     * @deprecated 使用 {@link #encryptBase36(String, String, String)} 替代
     */
    @Deprecated
    public static String encrypt(String plainText, String secretKey, String iv) {
        return encryptBase36(plainText, secretKey, iv);
    }

    /**
     * AES字符串解密（使用Base36编码）
     * @deprecated 使用 {@link #decryptBase36(String, String, String)} 替代
     */
    @Deprecated
    public static String decrypt(String encryptedText, String secretKey, String iv) {
        return decryptBase36(encryptedText, secretKey, iv);
    }
} 