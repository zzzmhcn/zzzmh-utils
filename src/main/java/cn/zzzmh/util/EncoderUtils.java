package cn.zzzmh.util;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Base64;

/**
 * 编码器工具类
 * 支持Base36和Base64编码解码
 * 
 * @author zzzmh
 * @since 1.0.0
 */
public class EncoderUtils {

    private static final String BASE36_CHARSET = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int BASE36_BASE = 36;

    /**
     * 私有构造函数，防止实例化
     */
    private EncoderUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ==================== Base36编码 ====================

    /**
     * Base36编码字节数组
     * 
     * @param data 要编码的字节数组
     * @return Base36编码字符串
     */
    public static String base36Encode(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (data.length == 0) {
            return "";
        }

        // 处理前导零字节
        int leadingZeros = 0;
        for (byte b : data) {
            if (b == 0) {
                leadingZeros++;
            } else {
                break;
            }
        }

        // 将字节数组转换为BigInteger（无符号）
        BigInteger bigInt = new BigInteger(1, data);
        
        // 如果是0，直接返回
        if (bigInt.equals(BigInteger.ZERO)) {
            return repeatChar('a', data.length);
        }

        // 转换为Base36
        StringBuilder result = new StringBuilder();
        while (bigInt.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = bigInt.divideAndRemainder(BigInteger.valueOf(BASE36_BASE));
            result.insert(0, BASE36_CHARSET.charAt(divmod[1].intValue()));
            bigInt = divmod[0];
        }

        // 添加前导零对应的字符（用'a'表示）
        for (int i = 0; i < leadingZeros; i++) {
            result.insert(0, 'a');
        }

        return result.toString();
    }

    /**
     * Base36解码字符串
     * 
     * @param encoded Base36编码字符串
     * @return 解码后的字节数组
     */
    public static byte[] base36Decode(String encoded) {
        if (encoded == null) {
            throw new IllegalArgumentException("Encoded string cannot be null");
        }
        if (encoded.isEmpty()) {
            return new byte[0];
        }

        // 验证字符集
        for (char c : encoded.toCharArray()) {
            if (BASE36_CHARSET.indexOf(c) == -1) {
                throw new IllegalArgumentException("Base36编码包含无效字符: " + c);
            }
        }

        // 计算前导零（'a'字符）
        int leadingZeros = 0;
        for (char c : encoded.toCharArray()) {
            if (c == 'a') {
                leadingZeros++;
            } else {
                break;
            }
        }

        // 如果全是'a'，返回对应长度的零字节数组
        if (leadingZeros == encoded.length()) {
            return new byte[leadingZeros];
        }

        // 将Base36字符串转换为BigInteger
        BigInteger bigInt = BigInteger.ZERO;
        BigInteger base = BigInteger.valueOf(BASE36_BASE);
        
        for (int i = leadingZeros; i < encoded.length(); i++) {
            char c = encoded.charAt(i);
            int value = BASE36_CHARSET.indexOf(c);
            bigInt = bigInt.multiply(base).add(BigInteger.valueOf(value));
        }

        // 转换为字节数组
        byte[] result = bigInt.toByteArray();
        
        // 处理BigInteger.toByteArray()可能添加的符号位字节
        if (result.length > 1 && result[0] == 0) {
            result = Arrays.copyOfRange(result, 1, result.length);
        }

        // 添加前导零字节
        if (leadingZeros > 0) {
            byte[] finalResult = new byte[leadingZeros + result.length];
            System.arraycopy(result, 0, finalResult, leadingZeros, result.length);
            return finalResult;
        }

        return result;
    }

    // ==================== Base64编码 ====================

    /**
     * Base64编码字节数组
     * 
     * @param data 要编码的字节数组
     * @return Base64编码字符串
     */
    public static String base64Encode(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Base64解码字符串
     * 
     * @param encoded Base64编码字符串
     * @return 解码后的字节数组
     */
    public static byte[] base64Decode(String encoded) {
        if (encoded == null) {
            throw new IllegalArgumentException("Encoded string cannot be null");
        }
        try {
            return Base64.getDecoder().decode(encoded);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Base64格式无效: " + e.getMessage(), e);
        }
    }

    /**
     * Base64编码字符串（UTF-8）
     * 
     * @param text 要编码的字符串
     * @return Base64编码字符串
     */
    public static String base64EncodeString(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }
        return base64Encode(text.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    /**
     * Base64解码字符串（UTF-8）
     * 
     * @param encoded Base64编码字符串
     * @return 解码后的字符串
     */
    public static String base64DecodeString(String encoded) {
        if (encoded == null) {
            throw new IllegalArgumentException("Encoded string cannot be null");
        }
        byte[] decoded = base64Decode(encoded);
        return new String(decoded, java.nio.charset.StandardCharsets.UTF_8);
    }

    // ==================== Base64 URL Safe编码 ====================

    /**
     * Base64 URL Safe编码字节数组
     * 
     * @param data 要编码的字节数组
     * @return Base64 URL Safe编码字符串
     */
    public static String base64UrlEncode(byte[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    /**
     * Base64 URL Safe解码字符串
     * 
     * @param encoded Base64 URL Safe编码字符串
     * @return 解码后的字节数组
     */
    public static byte[] base64UrlDecode(String encoded) {
        if (encoded == null) {
            throw new IllegalArgumentException("Encoded string cannot be null");
        }
        try {
            return Base64.getUrlDecoder().decode(encoded);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Base64 URL格式无效: " + e.getMessage(), e);
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 重复字符
     * 
     * @param c 字符
     * @param count 重复次数
     * @return 重复字符组成的字符串
     */
    private static String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
} 