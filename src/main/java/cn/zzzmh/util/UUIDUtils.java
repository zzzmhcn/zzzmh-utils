package cn.zzzmh.util;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * UUID工具类
 * 支持生成UUID v7和ULID等多种格式的唯一标识符
 * 
 * @author zzzmh
 * @since 1.0.0
 */
public class UUIDUtils {
    
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    // ULID编码字符集（Crockford Base32）
    private static final char[] ULID_CHARS = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();
    
    // ULID字符集字符串，用于验证
    private static final String ULID_CHAR_SET = "0123456789ABCDEFGHJKMNPQRSTVWXYZ";
    
    /**
     * 私有构造函数，防止实例化
     */
    private UUIDUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 生成UUID v7 (时间有序UUID)
     * 格式：xxxxxxxx-xxxx-7xxx-xxxx-xxxxxxxxxxxx
     * 前48位是时间戳（毫秒），后续位是随机数
     * 
     * @return UUID v7字符串
     */
    public static String generateUuidV7() {
        long timestamp = System.currentTimeMillis();
        
        // 构建UUID v7
        // 时间戳部分 (48位)
        long timestampHigh = (timestamp >> 16) & 0xFFFFFFFFL;
        long timestampLow = (timestamp & 0xFFFF) << 16;
        
        // 版本号设置为7
        timestampLow |= 0x7000;
        
        // 随机数部分
        long randomHigh = ThreadLocalRandom.current().nextLong() & 0x3FFFFFFFFFFFFFFFL;
        randomHigh |= 0x8000000000000000L; // 设置variant位
        
        long randomLow = ThreadLocalRandom.current().nextLong();
        
        return new UUID(
            (timestampHigh << 32) | timestampLow,
            randomHigh | (randomLow >>> 32)
        ).toString();
    }
    
    /**
     * 生成ULID (Universally Unique Lexicographically Sortable Identifier)
     * 格式：26个字符的Base32编码字符串
     * 前10个字符是时间戳，后16个字符是随机数
     * 
     * @return ULID字符串
     */
    public static String generateUlid() {
        long timestamp = System.currentTimeMillis();
        byte[] randomBytes = new byte[10];
        SECURE_RANDOM.nextBytes(randomBytes);
        
        return encodeUlid(timestamp, randomBytes);
    }
    
    /**
     * 生成ULID (指定时间戳)
     * 
     * @param timestamp 时间戳（毫秒）
     * @return ULID字符串
     */
    public static String generateUlid(long timestamp) {
        if (timestamp < 0) {
            throw new IllegalArgumentException("Timestamp cannot be negative");
        }
        
        byte[] randomBytes = new byte[10];
        SECURE_RANDOM.nextBytes(randomBytes);
        
        return encodeUlid(timestamp, randomBytes);
    }
    
    /**
     * 编码ULID
     * 
     * @param timestamp 时间戳
     * @param randomBytes 随机字节
     * @return ULID字符串
     */
    private static String encodeUlid(long timestamp, byte[] randomBytes) {
        char[] ulid = new char[26];
        
        // 编码时间戳部分（10个字符）
        for (int i = 9; i >= 0; i--) {
            ulid[i] = ULID_CHARS[(int)(timestamp & 31)];
            timestamp >>>= 5;
        }
        
        // 编码随机数部分（16个字符）
        long random1 = ((long)(randomBytes[0] & 0xFF) << 32) |
                      ((long)(randomBytes[1] & 0xFF) << 24) |
                      ((long)(randomBytes[2] & 0xFF) << 16) |
                      ((long)(randomBytes[3] & 0xFF) << 8) |
                      (long)(randomBytes[4] & 0xFF);
        
        long random2 = ((long)(randomBytes[5] & 0xFF) << 32) |
                      ((long)(randomBytes[6] & 0xFF) << 24) |
                      ((long)(randomBytes[7] & 0xFF) << 16) |
                      ((long)(randomBytes[8] & 0xFF) << 8) |
                      (long)(randomBytes[9] & 0xFF);
        
        for (int i = 15; i >= 10; i--) {
            ulid[i] = ULID_CHARS[(int)(random1 & 31)];
            random1 >>>= 5;
        }
        
        for (int i = 25; i >= 16; i--) {
            ulid[i] = ULID_CHARS[(int)(random2 & 31)];
            random2 >>>= 5;
        }
        
        return new String(ulid);
    }
    
    /**
     * 生成普通UUID (UUID v4)
     * 
     * @return UUID字符串
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 去除UUID中的连字符
     * 
     * @param uuid 带连字符的UUID
     * @return 不带连字符的UUID
     */
    public static String removeUuidHyphens(String uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        return uuid.replace("-", "");
    }
    
    /**
     * 为UUID字符串添加连字符
     * 
     * @param uuid 不带连字符的UUID字符串（32个字符）
     * @return 带连字符的UUID字符串
     */
    public static String addUuidHyphens(String uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        if (uuid.length() != 32) {
            throw new IllegalArgumentException("UUID must be 32 characters long");
        }
        
        return uuid.substring(0, 8) + "-" + 
               uuid.substring(8, 12) + "-" + 
               uuid.substring(12, 16) + "-" + 
               uuid.substring(16, 20) + "-" + 
               uuid.substring(20);
    }
    
    /**
     * 验证是否为有效的UUID格式
     * 
     * @param uuid UUID字符串
     * @return 是否有效
     */
    public static boolean isValidUuid(String uuid) {
        if (uuid == null) {
            return false;
        }
        
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * 验证是否为有效的ULID格式
     * 
     * @param ulid ULID字符串
     * @return 是否有效
     */
    public static boolean isValidUlid(String ulid) {
        if (ulid == null || ulid.length() != 26) {
            return false;
        }
        
        for (char c : ulid.toCharArray()) {
            if (ULID_CHAR_SET.indexOf(c) == -1) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 从ULID中提取时间戳
     * 
     * @param ulid ULID字符串
     * @return 时间戳（毫秒）
     */
    public static long extractTimestampFromUlid(String ulid) {
        if (!isValidUlid(ulid)) {
            throw new IllegalArgumentException("Invalid ULID format");
        }
        
        long timestamp = 0;
        for (int i = 0; i < 10; i++) {
            timestamp = timestamp * 32 + ULID_CHAR_SET.indexOf(ulid.charAt(i));
        }
        
        return timestamp;
    }
    
    /**
     * 从UUID v7中提取时间戳
     * 
     * @param uuidV7 UUID v7字符串
     * @return 时间戳（毫秒）
     */
    public static long extractTimestampFromUuidV7(String uuidV7) {
        if (!isValidUuid(uuidV7)) {
            throw new IllegalArgumentException("Invalid UUID format");
        }
        
        UUID uuid = UUID.fromString(uuidV7);
        
        // 检查是否为UUID v7
        if (uuid.version() != 7) {
            throw new IllegalArgumentException("Not a UUID v7");
        }
        
        // 提取时间戳
        long mostSignificantBits = uuid.getMostSignificantBits();
        long timestamp = mostSignificantBits >>> 16;
        
        return timestamp;
    }
    
    /**
     * 生成短UUID（Base36编码的UUID）
     * 
     * @return 短UUID字符串
     */
    public static String generateShortUuid() {
        UUID uuid = UUID.randomUUID();
        return Long.toString(uuid.getMostSignificantBits(), 36) + 
               Long.toString(uuid.getLeastSignificantBits(), 36);
    }
    
    /**
     * 生成纯数字UUID（仅包含数字）
     * 
     * @return 纯数字UUID字符串
     */
    public static String generateNumericUuid() {
        UUID uuid = UUID.randomUUID();
        return Math.abs(uuid.getMostSignificantBits()) + "" + 
               Math.abs(uuid.getLeastSignificantBits());
    }
    
    /**
     * 比较两个ULID的时间戳大小
     * 
     * @param ulid1 第一个ULID
     * @param ulid2 第二个ULID
     * @return 负数表示ulid1时间戳小于ulid2，0表示相等，正数表示ulid1时间戳大于ulid2
     */
    public static int compareUlidTimestamp(String ulid1, String ulid2) {
        long timestamp1 = extractTimestampFromUlid(ulid1);
        long timestamp2 = extractTimestampFromUlid(ulid2);
        return Long.compare(timestamp1, timestamp2);
    }
} 