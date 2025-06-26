# 🔐 AESUtils 使用文档

## 概述

AESUtils是基于AES/CBC/PKCS7Padding算法的加密工具类，使用Bouncy Castle提供者实现，支持Base36和Base64编码的字符串加密。

## 🚀 快速开始

### 基础字节数组加密

```java
// 准备数据
String plainText = "需要加密的敏感数据";
byte[] plainData = plainText.getBytes(StandardCharsets.UTF_8);

// 准备密钥和IV（实际应用中应该安全生成）
byte[] secretKey = "your-16-byte-key".getBytes(); // 16字节密钥
byte[] iv = "your-16-byte-iv!".getBytes();        // 16字节IV

// 加密
byte[] encryptedData = AESUtils.encrypt(plainData, secretKey, iv);

// 解密
byte[] decryptedData = AESUtils.decrypt(encryptedData, secretKey, iv);
String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);
```

### Base36字符串加密（推荐）

```java
// 明文数据
String plainText = "用户密码或敏感信息";

// Base36编码的密钥和IV
String secretKey = "j8k2l9m3n4p5q6r7s8t9u1v2w3x4y5z6"; // Base36格式
String iv = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6";        // Base36格式

// 加密
String encryptedText = AESUtils.encryptBase36(plainText, secretKey, iv);

// 解密
String decryptedText = AESUtils.decryptBase36(encryptedText, secretKey, iv);
```

### Base64字符串加密

```java
// 明文数据
String plainText = "需要加密的数据";

// Base64编码的密钥和IV
String secretKey = "your-base64-encoded-key==";
String iv = "your-base64-encoded-iv==";

// 加密
String encryptedText = AESUtils.encryptBase64(plainText, secretKey, iv);

// 解密
String decryptedText = AESUtils.decryptBase64(encryptedText, secretKey, iv);
```

## 🔑 密钥和IV管理

### 密钥长度支持

```java
// AES支持三种密钥长度
byte[] key128 = new byte[16]; // 128位密钥
byte[] key192 = new byte[24]; // 192位密钥  
byte[] key256 = new byte[32]; // 256位密钥

// IV固定为128位（16字节）
byte[] iv = new byte[16];

// 填充随机数据（实际应用中使用安全随机数生成器）
new SecureRandom().nextBytes(key256);
new SecureRandom().nextBytes(iv);
```

### 安全的密钥生成

```java
import java.security.SecureRandom;

public class KeyGenerator {
    
    /**
     * 生成安全的AES密钥
     */
    public static byte[] generateSecretKey() {
        byte[] key = new byte[32]; // 256位密钥
        new SecureRandom().nextBytes(key);
        return key;
    }
    
    /**
     * 生成安全的IV
     */
    public static byte[] generateIV() {
        byte[] iv = new byte[16]; // 128位IV
        new SecureRandom().nextBytes(iv);
        return iv;
    }
    
    /**
     * 生成Base36格式的密钥
     */
    public static String generateBase36Key() {
        byte[] key = generateSecretKey();
        return EncoderUtils.base36Encode(key);
    }
    
    /**
     * 生成Base36格式的IV
     */
    public static String generateBase36IV() {
        byte[] iv = generateIV();
        return EncoderUtils.base36Encode(iv);
    }
}
```

## 🛡️ 实际应用场景

### 用户密码加密

```java
public class UserPasswordService {
    
    // 应用级密钥（实际应用中应该从配置文件或环境变量读取）
    private static final String APP_SECRET_KEY = "your-app-secret-key-base36";
    private static final String APP_IV = "your-app-iv-base36";
    
    /**
     * 加密用户密码
     */
    public static String encryptPassword(String plainPassword) {
        return AESUtils.encryptBase36(plainPassword, APP_SECRET_KEY, APP_IV);
    }
    
    /**
     * 验证用户密码
     */
    public static boolean verifyPassword(String plainPassword, String encryptedPassword) {
        try {
            String decrypted = AESUtils.decryptBase36(encryptedPassword, APP_SECRET_KEY, APP_IV);
            return plainPassword.equals(decrypted);
        } catch (Exception e) {
            return false;
        }
    }
}
```

### 敏感数据存储

```java
public class SensitiveDataManager {
    
    /**
     * 加密用户敏感信息
     */
    public static JSONObject encryptUserData(JSONObject userData, String userKey, String userIV) {
        JSONObject encryptedData = new JSONObject();
        
        // 加密敏感字段
        if (userData.containsKey("idCard")) {
            String encrypted = AESUtils.encryptBase64(userData.getString("idCard"), userKey, userIV);
            encryptedData.put("idCard", encrypted);
        }
        
        if (userData.containsKey("phone")) {
            String encrypted = AESUtils.encryptBase64(userData.getString("phone"), userKey, userIV);
            encryptedData.put("phone", encrypted);
        }
        
        // 非敏感字段直接复制
        encryptedData.put("username", userData.getString("username"));
        encryptedData.put("email", userData.getString("email"));
        
        return encryptedData;
    }
    
    /**
     * 解密用户敏感信息
     */
    public static JSONObject decryptUserData(JSONObject encryptedData, String userKey, String userIV) {
        JSONObject userData = new JSONObject();
        
        // 解密敏感字段
        if (encryptedData.containsKey("idCard")) {
            String decrypted = AESUtils.decryptBase64(encryptedData.getString("idCard"), userKey, userIV);
            userData.put("idCard", decrypted);
        }
        
        if (encryptedData.containsKey("phone")) {
            String decrypted = AESUtils.decryptBase64(encryptedData.getString("phone"), userKey, userIV);
            userData.put("phone", decrypted);
        }
        
        // 非敏感字段直接复制
        userData.put("username", encryptedData.getString("username"));
        userData.put("email", encryptedData.getString("email"));
        
        return userData;
    }
}
```

### 配置文件加密

```java
public class ConfigEncryption {
    
    private static final String CONFIG_KEY = "config-encryption-key-base36";
    private static final String CONFIG_IV = "config-encryption-iv-base36";
    
    /**
     * 加密配置文件
     */
    public static void encryptConfigFile(String configPath, String encryptedPath) {
        try {
            // 读取原始配置
            String configContent = FileUtils.readString(configPath);
            
            // 加密配置内容
            String encryptedContent = AESUtils.encryptBase36(configContent, CONFIG_KEY, CONFIG_IV);
            
            // 保存加密后的配置
            FileUtils.writeString(encryptedPath, encryptedContent);
            
        } catch (Exception e) {
            throw new RuntimeException("配置文件加密失败", e);
        }
    }
    
    /**
     * 解密配置文件
     */
    public static String decryptConfigFile(String encryptedPath) {
        try {
            // 读取加密配置
            String encryptedContent = FileUtils.readString(encryptedPath);
            
            // 解密配置内容
            return AESUtils.decryptBase36(encryptedContent, CONFIG_KEY, CONFIG_IV);
            
        } catch (Exception e) {
            throw new RuntimeException("配置文件解密失败", e);
        }
    }
}
```

## 🔧 高级功能

### 批量加密处理

```java
public class BatchEncryption {
    
    /**
     * 批量加密字符串列表
     */
    public static List<String> encryptList(List<String> plainTexts, String key, String iv) {
        return plainTexts.stream()
            .map(text -> AESUtils.encryptBase36(text, key, iv))
            .collect(Collectors.toList());
    }
    
    /**
     * 批量解密字符串列表
     */
    public static List<String> decryptList(List<String> encryptedTexts, String key, String iv) {
        return encryptedTexts.stream()
            .map(text -> AESUtils.decryptBase36(text, key, iv))
            .collect(Collectors.toList());
    }
    
    /**
     * 批量加密JSON数组
     */
    public static JSONArray encryptJsonArray(JSONArray jsonArray, String key, String iv) {
        JSONArray encrypted = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            Object item = jsonArray.get(i);
            if (item instanceof String) {
                encrypted.add(AESUtils.encryptBase36((String) item, key, iv));
            } else {
                encrypted.add(item); // 非字符串类型不加密
            }
        }
        return encrypted;
    }
}
```

### 流式加密

```java
public class StreamEncryption {
    
    /**
     * 分块加密大文件
     */
    public static void encryptLargeFile(String inputPath, String outputPath, String key, String iv) {
        try {
            byte[] keyBytes = EncoderUtils.base36Decode(key);
            byte[] ivBytes = EncoderUtils.base36Decode(iv);
            
            // 读取文件内容
            byte[] fileContent = FileUtils.readBytes(inputPath);
            
            // 加密
            byte[] encryptedContent = AESUtils.encrypt(fileContent, keyBytes, ivBytes);
            
            // 写入加密文件
            FileUtils.writeBytes(outputPath, encryptedContent);
            
        } catch (Exception e) {
            throw new RuntimeException("文件加密失败", e);
        }
    }
    
    /**
     * 解密大文件
     */
    public static void decryptLargeFile(String encryptedPath, String outputPath, String key, String iv) {
        try {
            byte[] keyBytes = EncoderUtils.base36Decode(key);
            byte[] ivBytes = EncoderUtils.base36Decode(iv);
            
            // 读取加密文件
            byte[] encryptedContent = FileUtils.readBytes(encryptedPath);
            
            // 解密
            byte[] decryptedContent = AESUtils.decrypt(encryptedContent, keyBytes, ivBytes);
            
            // 写入解密文件
            FileUtils.writeBytes(outputPath, decryptedContent);
            
        } catch (Exception e) {
            throw new RuntimeException("文件解密失败", e);
        }
    }
}
```

## ⚙️ 算法配置

### 加密算法规格

- **算法**: AES (Advanced Encryption Standard)
- **模式**: CBC (Cipher Block Chaining)
- **填充**: PKCS7Padding
- **提供者**: Bouncy Castle
- **IV长度**: 128位 (16字节)
- **密钥长度**: 128/192/256位 (16/24/32字节)

### 兼容性方法

```java
// 兼容旧版本API（已废弃，建议使用新方法）
@Deprecated
String encrypted = AESUtils.encrypt(plainText, secretKey, iv); // 等同于 encryptBase36

@Deprecated  
String decrypted = AESUtils.decrypt(encryptedText, secretKey, iv); // 等同于 decryptBase36
```

## 🔒 安全最佳实践

### 密钥管理

1. **密钥生成**: 使用`SecureRandom`生成随机密钥
2. **密钥存储**: 不要在代码中硬编码密钥
3. **密钥轮换**: 定期更换加密密钥
4. **权限控制**: 限制密钥访问权限

### IV（初始化向量）管理

1. **随机性**: 每次加密使用不同的IV
2. **公开性**: IV可以公开存储，但不能重复使用
3. **存储方式**: 通常与密文一起存储

### 错误处理

```java
try {
    String encrypted = AESUtils.encryptBase36(plainText, key, iv);
    // 处理加密结果
} catch (RuntimeException e) {
    if (e.getMessage().contains("AES加密失败")) {
        // 处理加密错误
        log.error("数据加密失败: " + e.getMessage());
    }
}
```

## 📝 注意事项

- 密钥长度必须为16、24或32字节
- IV长度必须为16字节
- 相同的明文、密钥和IV会产生相同的密文
- 建议每次加密使用不同的IV增强安全性
- Base36编码适合在URL中使用，Base64编码更通用
- 使用前确保Bouncy Castle依赖已添加到项目中 