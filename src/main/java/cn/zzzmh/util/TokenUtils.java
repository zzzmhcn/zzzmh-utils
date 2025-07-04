package cn.zzzmh.util;

import com.alibaba.fastjson2.JSONObject;

import java.io.InputStream;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Token工具类
 * 支持生成和解析无状态token，使用AES+Base36加密
 * 
 * @author zzzmh
 * @since 1.0.0
 */
public class TokenUtils {
    
    // 默认配置
    private static final String DEFAULT_SECRET_KEY = "g2h5j8k3m9n2p6q4r7s1t5u8v2w9x3y7z4a6b9c2d5e8f1";
    private static final String DEFAULT_IV = "h5j8k3m9n2p6q4r7s1t5u8v2w9x3y7z4";
    private static final long DEFAULT_EXPIRE_TIME = 24 * 60 * 60 * 1000; // 24小时
    
    // 配置变量
    private static String secretKey = DEFAULT_SECRET_KEY;
    private static String iv = DEFAULT_IV;
    private static long expireTime = DEFAULT_EXPIRE_TIME;
    
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    /**
     * 私有构造函数，防止实例化
     */
    private TokenUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    /**
     * 从配置文件初始化Token配置
     * 配置文件：resources/config.properties
     * 
     * Configuration file example:
     * <pre>
     * # Token configuration
     * token.secret.key=g2h5j8k3m9n2p6q4r7s1t5u8v2w9x3y7z4a6b9c2d5e8f1
     * token.iv=h5j8k3m9n2p6q4r7s1t5u8v2w9x3y7z4
     * token.expire.time=86400000
     * </pre>
     */
    public static void init() {
        try {
            Properties props = new Properties();
            InputStream inputStream = TokenUtils.class.getClassLoader().getResourceAsStream("config.properties");
            
            if (inputStream == null) {
                throw new RuntimeException(
                    "Configuration file not found: Please create 'config.properties' file in your project's 'src/main/resources/' directory.\n" +
                    "Example Token configuration:\n" +
                    "token.secret.key=g2h5j8k3m9n2p6q4r7s1t5u8v2w9x3y7z4a6b9c2d5e8f1\n" +
                    "token.iv=h5j8k3m9n2p6q4r7s1t5u8v2w9x3y7z4\n" +
                    "token.expire.time=86400000"
                );
            }
            
            props.load(inputStream);
            inputStream.close();
            
            String configSecretKey = props.getProperty("token.secret.key");
            String configIv = props.getProperty("token.iv");
            String configExpireTime = props.getProperty("token.expire.time");
            
            if (configSecretKey != null && !configSecretKey.trim().isEmpty()) {
                secretKey = configSecretKey.trim();
            }
            
            if (configIv != null && !configIv.trim().isEmpty()) {
                iv = configIv.trim();
            }
            
            if (configExpireTime != null && !configExpireTime.trim().isEmpty()) {
                try {
                    expireTime = Long.parseLong(configExpireTime.trim());
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid token.expire.time format in config.properties: " + configExpireTime);
                }
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize token configuration from config.properties: " + e.getMessage(), e);
        }
    }
    
    /**
     * 初始化Token配置（手动设置）
     * 
     * @param secretKey 密钥（Base36编码）
     * @param iv 初始化向量（Base36编码）
     * @param expireTime 过期时间（毫秒）
     */
    public static void init(String secretKey, String iv, long expireTime) {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Secret key cannot be null or empty");
        }
        if (iv == null || iv.trim().isEmpty()) {
            throw new IllegalArgumentException("IV cannot be null or empty");
        }
        if (expireTime <= 0) {
            throw new IllegalArgumentException("Expire time must be positive");
        }
        
        TokenUtils.secretKey = secretKey.trim();
        TokenUtils.iv = iv.trim();
        TokenUtils.expireTime = expireTime;
    }
    
    /**
     * 生成随机密钥（Base36编码）
     * 
     * @return 随机密钥字符串
     */
    public static String generateSecretKey() {
        byte[] keyBytes = new byte[32]; // 256位密钥
        SECURE_RANDOM.nextBytes(keyBytes);
        return EncoderUtils.base36Encode(keyBytes);
    }
    
    /**
     * 生成随机IV（Base36编码）
     * 
     * @return 随机IV字符串
     */
    public static String generateIV() {
        byte[] ivBytes = new byte[16]; // 128位IV
        SECURE_RANDOM.nextBytes(ivBytes);
        return EncoderUtils.base36Encode(ivBytes);
    }
    
    /**
     * 生成Token（使用JSONObject）
     * 
     * @param payload 载荷数据
     * @return 加密后的Token字符串
     */
    public static String generateToken(JSONObject payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }
        
        try {
            // 创建完整的token数据
            JSONObject tokenData = new JSONObject();
            tokenData.put("payload", payload);
            tokenData.put("iat", System.currentTimeMillis()); // issued at time
            tokenData.put("exp", System.currentTimeMillis() + expireTime); // expire time
            tokenData.put("jti", UUIDUtils.generateUuid()); // JWT ID
            
            // 转换为JSON字符串
            String tokenJson = JsonUtils.toJsonString(tokenData);
            
            // 使用AES加密
            return AESUtils.encryptBase36(tokenJson, secretKey, iv);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token: " + e.getMessage(), e);
        }
    }
    
    /**
     * 生成Token（使用固定的id和创建时间）
     * 
     * @param id 用户ID
     * @param createTime 创建时间
     * @return 加密后的Token字符串
     */
    public static String generateToken(String id, LocalDateTime createTime) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (createTime == null) {
            throw new IllegalArgumentException("Create time cannot be null");
        }
        
        try {
            // 创建标准载荷
            JSONObject payload = new JSONObject();
            payload.put("id", id.trim());
            payload.put("createTime", createTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            
            return generateToken(payload);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token with id and createTime: " + e.getMessage(), e);
        }
    }
    
    /**
     * 生成Token（使用固定的id，创建时间为当前时间）
     * 
     * @param id 用户ID
     * @return 加密后的Token字符串
     */
    public static String generateToken(String id) {
        return generateToken(id, LocalDateTime.now());
    }
    
    /**
     * 验证Token是否有效
     * 
     * @param token 待验证的Token
     * @return 是否有效
     */
    public static boolean isValidToken(String token) {
        try {
            TokenInfo tokenInfo = parseToken(token);
            return tokenInfo != null && !tokenInfo.isExpired();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 解析Token获取TokenInfo
     * 
     * @param token 待解析的Token
     * @return TokenInfo对象
     */
    public static TokenInfo parseToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        
        try {
            // 解密token
            String decryptedJson = AESUtils.decryptBase36(token.trim(), secretKey, iv);
            
            // 解析JSON
            JSONObject tokenData = JsonUtils.parseObject(decryptedJson);
            
            // 提取数据
            JSONObject payload = tokenData.getJSONObject("payload");
            Long iat = tokenData.getLong("iat");
            Long exp = tokenData.getLong("exp");
            String jti = tokenData.getString("jti");
            
            return new TokenInfo(payload, iat, exp, jti);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token: " + e.getMessage(), e);
        }
    }
    
    /**
     * 解析Token获取用户ID和创建时间
     * 
     * @param token 待解析的Token
     * @return 包含id和createTime的JSONObject
     */
    public static JSONObject parseTokenToIdAndTime(String token) {
        TokenInfo tokenInfo = parseToken(token);
        
        if (tokenInfo.isExpired()) {
            throw new RuntimeException("Token has expired");
        }
        
        JSONObject payload = tokenInfo.getPayload();
        JSONObject result = new JSONObject();
        
        // 提取id和createTime
        String id = payload.getString("id");
        String createTime = payload.getString("createTime");
        
        result.put("id", id);
        result.put("createTime", createTime);
        
        return result;
    }
    
    /**
     * 刷新Token（重新生成过期时间）
     * 
     * @param token 原Token
     * @return 新的Token
     */
    public static String refreshToken(String token) {
        TokenInfo tokenInfo = parseToken(token);
        
        if (tokenInfo.isExpired()) {
            throw new RuntimeException("Cannot refresh expired token");
        }
        
        // 使用原payload重新生成token
        return generateToken(tokenInfo.getPayload());
    }
    
    /**
     * 获取Token剩余有效时间（毫秒）
     * 
     * @param token Token字符串
     * @return 剩余有效时间（毫秒），如果已过期返回0
     */
    public static long getTokenRemainingTime(String token) {
        TokenInfo tokenInfo = parseToken(token);
        long remaining = tokenInfo.getExpireTime() - System.currentTimeMillis();
        return Math.max(0, remaining);
    }
    
    /**
     * Token信息类
     */
    public static class TokenInfo {
        private final JSONObject payload;
        private final Long issuedAt;
        private final Long expireTime;
        private final String jwtId;
        
        public TokenInfo(JSONObject payload, Long issuedAt, Long expireTime, String jwtId) {
            this.payload = payload;
            this.issuedAt = issuedAt;
            this.expireTime = expireTime;
            this.jwtId = jwtId;
        }
        
        public JSONObject getPayload() {
            return payload;
        }
        
        public Long getIssuedAt() {
            return issuedAt;
        }
        
        public Long getExpireTime() {
            return expireTime;
        }
        
        public String getJwtId() {
            return jwtId;
        }
        
        /**
         * 检查Token是否已过期
         * 
         * @return 是否已过期
         */
        public boolean isExpired() {
            return expireTime != null && System.currentTimeMillis() > expireTime;
        }
        
        /**
         * 获取Token剩余有效时间（毫秒）
         * 
         * @return 剩余有效时间（毫秒），如果已过期返回0
         */
        public long getRemainingTime() {
            if (expireTime == null) {
                return Long.MAX_VALUE;
            }
            long remaining = expireTime - System.currentTimeMillis();
            return Math.max(0, remaining);
        }
        
        /**
         * 获取用户ID（如果存在）
         * 
         * @return 用户ID
         */
        public String getId() {
            return payload != null ? payload.getString("id") : null;
        }
        
        /**
         * 获取创建时间（如果存在）
         * 
         * @return 创建时间字符串
         */
        public String getCreateTime() {
            return payload != null ? payload.getString("createTime") : null;
        }
        
        /**
         * 获取创建时间（LocalDateTime对象）
         * 
         * @return 创建时间LocalDateTime对象
         */
        public LocalDateTime getCreateTimeAsLocalDateTime() {
            String createTime = getCreateTime();
            if (createTime != null) {
                return LocalDateTime.parse(createTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
            return null;
        }
        
        /**
         * 获取签发时间（LocalDateTime对象）
         * 
         * @return 签发时间LocalDateTime对象
         */
        public LocalDateTime getIssuedAtAsLocalDateTime() {
            if (issuedAt != null) {
                return LocalDateTime.ofEpochSecond(issuedAt / 1000, (int) (issuedAt % 1000) * 1000000, ZoneOffset.UTC);
            }
            return null;
        }
        
        /**
         * 获取过期时间（LocalDateTime对象）
         * 
         * @return 过期时间LocalDateTime对象
         */
        public LocalDateTime getExpireTimeAsLocalDateTime() {
            if (expireTime != null) {
                return LocalDateTime.ofEpochSecond(expireTime / 1000, (int) (expireTime % 1000) * 1000000, ZoneOffset.UTC);
            }
            return null;
        }
        
        @Override
        public String toString() {
            return "TokenInfo{" +
                    "payload=" + payload +
                    ", issuedAt=" + issuedAt +
                    ", expireTime=" + expireTime +
                    ", jwtId='" + jwtId + '\'' +
                    ", expired=" + isExpired() +
                    '}';
        }
    }
    
    /**
     * 自动初始化（用于第一次使用时）
     */
    private static void autoInit() {
        try {
            // 尝试从配置文件初始化
            init();
        } catch (Exception e) {
            // 配置文件不存在或参数缺失时，使用默认配置
            System.out.println("Token configuration not found, using default configuration. " +
                    "For production use, please create 'config.properties' file in 'src/main/resources/' with:\n" +
                    "token.secret.key=your_secret_key\n" +
                    "token.iv=your_iv\n" +
                    "token.expire.time=86400000");
        }
    }
    
    // 静态初始化块
    static {
        autoInit();
    }
} 