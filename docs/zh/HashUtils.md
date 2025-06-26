# 🔒 HashUtils 使用文档

## 概述

HashUtils是一个哈希计算工具类，提供MD5、SHA1、SHA256等多种哈希算法的字符串和文件哈希计算功能，适用于数据完整性验证和安全存储。

## 🚀 快速开始

### 基础哈希计算

```java
// MD5哈希
String text = "Hello World";
String md5Hash = HashUtils.md5(text);
System.out.println("MD5: " + md5Hash);

// SHA1哈希
String sha1Hash = HashUtils.sha1(text);
System.out.println("SHA1: " + sha1Hash);

// SHA256哈希
String sha256Hash = HashUtils.sha256(text);
System.out.println("SHA256: " + sha256Hash);
```

### 文件哈希计算

```java
// 计算文件MD5
String fileMd5 = HashUtils.md5File("path/to/your/file.txt");

// 计算文件SHA256
String fileSha256 = HashUtils.sha256File("path/to/your/file.txt");

// 计算文件SHA1
String fileSha1 = HashUtils.sha1File("path/to/your/file.txt");
```

## 📋 哈希算法详解

### MD5哈希

```java
// 字符串MD5
String password = "myPassword123";
String md5Hash = HashUtils.md5(password);

// 中文字符串MD5
String chineseText = "你好世界";
String chineseMd5 = HashUtils.md5(chineseText);

// 文件MD5
String filePath = "C:\\Users\\Documents\\file.pdf";
String fileMd5 = HashUtils.md5File(filePath);

// 验证MD5
boolean isValid = HashUtils.md5(password).equals(md5Hash);
```

### SHA1哈希

```java
// 字符串SHA1
String data = "sensitive data";
String sha1Hash = HashUtils.sha1(data);

// 文件SHA1
String fileSha1 = HashUtils.sha1File("path/to/file.txt");

// 批量SHA1计算
List<String> texts = Arrays.asList("text1", "text2", "text3");
List<String> sha1Hashes = texts.stream()
    .map(HashUtils::sha1)
    .collect(Collectors.toList());
```

### SHA256哈希

```java
// 字符串SHA256
String data = "important data";
String sha256Hash = HashUtils.sha256(data);

// 文件SHA256
String fileSha256 = HashUtils.sha256File("path/to/file.txt");

// 安全密码哈希
String userPassword = "userPassword123";
String hashedPassword = HashUtils.sha256(userPassword + "salt");
```

## 🔧 实际应用场景

### 密码存储

```java
public class PasswordManager {
    
    private static final String SALT = "myAppSalt2023";
    
    /**
     * 加密存储密码
     */
    public static String hashPassword(String plainPassword) {
        // 使用盐值增强安全性
        String saltedPassword = plainPassword + SALT;
        return HashUtils.sha256(saltedPassword);
    }
    
    /**
     * 验证密码
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        String computedHash = hashPassword(plainPassword);
        return computedHash.equals(hashedPassword);
    }
    
    /**
     * 用户注册
     */
    public static JSONObject registerUser(String username, String password) {
        JSONObject user = new JSONObject();
        user.put("username", username);
        user.put("password", hashPassword(password));
        user.put("salt", SALT);
        user.put("createTime", System.currentTimeMillis());
        
        return user;
    }
}
```

### 文件完整性验证

```java
public class FileIntegrityChecker {
    
    /**
     * 生成文件校验信息
     */
    public static JSONObject generateChecksum(String filePath) {
        JSONObject checksum = new JSONObject();
        
        try {
            checksum.put("filePath", filePath);
            checksum.put("md5", HashUtils.md5File(filePath));
            checksum.put("sha1", HashUtils.sha1File(filePath));
            checksum.put("sha256", HashUtils.sha256File(filePath));
            checksum.put("timestamp", System.currentTimeMillis());
            
            // 文件大小
            File file = new File(filePath);
            checksum.put("size", file.length());
            
        } catch (RuntimeException e) {
            throw new RuntimeException("文件校验信息生成失败: " + e.getMessage());
        }
        
        return checksum;
    }
    
    /**
     * 验证文件完整性
     */
    public static boolean verifyFileIntegrity(String filePath, JSONObject expectedChecksum) {
        try {
            JSONObject currentChecksum = generateChecksum(filePath);
            
            // 比较MD5
            String expectedMd5 = expectedChecksum.getString("md5");
            String currentMd5 = currentChecksum.getString("md5");
            
            return expectedMd5.equals(currentMd5);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 批量验证文件
     */
    public static Map<String, Boolean> batchVerifyFiles(Map<String, JSONObject> fileChecksums) {
        Map<String, Boolean> results = new HashMap<>();
        
        for (String filePath : fileChecksums.keySet()) {
            JSONObject expectedChecksum = fileChecksums.get(filePath);
            boolean isValid = verifyFileIntegrity(filePath, expectedChecksum);
            results.put(filePath, isValid);
        }
        
        return results;
    }
}
```

### 数据去重

```java
public class DataDeduplicator {
    
    /**
     * 基于内容哈希去重
     */
    public static List<String> deduplicateByContent(List<String> dataList) {
        Set<String> hashSet = new HashSet<>();
        List<String> uniqueData = new ArrayList<>();
        
        for (String data : dataList) {
            String hash = HashUtils.sha256(data);
            if (hashSet.add(hash)) {
                uniqueData.add(data);
            }
        }
        
        return uniqueData;
    }
    
    /**
     * 文件去重
     */
    public static List<String> deduplicateFiles(List<String> filePaths) {
        Map<String, String> hashToPath = new HashMap<>();
        List<String> uniqueFiles = new ArrayList<>();
        
        for (String filePath : filePaths) {
            try {
                String fileHash = HashUtils.md5File(filePath);
                
                if (!hashToPath.containsKey(fileHash)) {
                    hashToPath.put(fileHash, filePath);
                    uniqueFiles.add(filePath);
                }
                
            } catch (RuntimeException e) {
                // 文件读取失败，跳过
                System.err.println("文件读取失败: " + filePath);
            }
        }
        
        return uniqueFiles;
    }
    
    /**
     * JSON数据去重
     */
    public static JSONArray deduplicateJsonArray(JSONArray jsonArray) {
        Set<String> hashSet = new HashSet<>();
        JSONArray uniqueArray = new JSONArray();
        
        for (int i = 0; i < jsonArray.size(); i++) {
            Object item = jsonArray.get(i);
            String itemJson = JSON.toJSONString(item);
            String hash = HashUtils.sha256(itemJson);
            
            if (hashSet.add(hash)) {
                uniqueArray.add(item);
            }
        }
        
        return uniqueArray;
    }
}
```

### 缓存键生成

```java
public class CacheKeyGenerator {
    
    /**
     * 生成缓存键
     */
    public static String generateCacheKey(String prefix, Object... params) {
        StringBuilder keyBuilder = new StringBuilder();
        
        for (Object param : params) {
            if (param != null) {
                keyBuilder.append(param.toString()).append(":");
            }
        }
        
        String paramString = keyBuilder.toString();
        String hash = HashUtils.md5(paramString);
        
        return prefix + ":" + hash;
    }
    
    /**
     * 用户相关缓存键
     */
    public static String userCacheKey(Long userId, String action) {
        return generateCacheKey("user", userId, action);
    }
    
    /**
     * 查询结果缓存键
     */
    public static String queryCacheKey(String sql, Object... params) {
        return generateCacheKey("query", sql, params);
    }
    
    /**
     * API响应缓存键
     */
    public static String apiCacheKey(String endpoint, JSONObject params) {
        String paramString = JSON.toJSONString(params);
        return generateCacheKey("api", endpoint, paramString);
    }
}
```

### 数据签名验证

```java
public class DataSignature {
    
    private static final String SECRET_KEY = "your-secret-key";
    
    /**
     * 生成数据签名
     */
    public static String generateSignature(String data) {
        String signData = data + SECRET_KEY;
        return HashUtils.sha256(signData);
    }
    
    /**
     * 验证数据签名
     */
    public static boolean verifySignature(String data, String signature) {
        String expectedSignature = generateSignature(data);
        return expectedSignature.equals(signature);
    }
    
    /**
     * 生成JSON数据签名
     */
    public static JSONObject signJsonData(JSONObject data) {
        String dataString = JSON.toJSONString(data);
        String signature = generateSignature(dataString);
        
        JSONObject signedData = new JSONObject();
        signedData.put("data", data);
        signedData.put("signature", signature);
        signedData.put("timestamp", System.currentTimeMillis());
        
        return signedData;
    }
    
    /**
     * 验证JSON数据签名
     */
    public static boolean verifyJsonSignature(JSONObject signedData) {
        try {
            JSONObject data = signedData.getJSONObject("data");
            String signature = signedData.getString("signature");
            
            String dataString = JSON.toJSONString(data);
            return verifySignature(dataString, signature);
            
        } catch (Exception e) {
            return false;
        }
    }
}
```

## 🔍 哈希算法对比

### 算法特性对比

| 算法 | 输出长度 | 安全性 | 性能 | 适用场景 |
|------|----------|--------|------|----------|
| MD5 | 32字符 | 较低 | 最快 | 文件校验、简单去重 |
| SHA1 | 40字符 | 一般 | 较快 | 版本控制、完整性验证 |
| SHA256 | 64字符 | 高 | 较慢 | 密码存储、数字签名 |

### 性能测试

```java
public class HashPerformanceTest {
    
    public static void comparePerformance(String testData, int iterations) {
        // MD5性能测试
        long md5StartTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            HashUtils.md5(testData);
        }
        long md5Time = System.currentTimeMillis() - md5StartTime;
        
        // SHA1性能测试
        long sha1StartTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            HashUtils.sha1(testData);
        }
        long sha1Time = System.currentTimeMillis() - sha1StartTime;
        
        // SHA256性能测试
        long sha256StartTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            HashUtils.sha256(testData);
        }
        long sha256Time = System.currentTimeMillis() - sha256StartTime;
        
        System.out.println("MD5耗时: " + md5Time + "ms");
        System.out.println("SHA1耗时: " + sha1Time + "ms");
        System.out.println("SHA256耗时: " + sha256Time + "ms");
    }
    
    public static void compareFileHashPerformance(String filePath) {
        long startTime, endTime;
        
        // 文件MD5
        startTime = System.currentTimeMillis();
        String md5 = HashUtils.md5File(filePath);
        endTime = System.currentTimeMillis();
        System.out.println("文件MD5耗时: " + (endTime - startTime) + "ms");
        
        // 文件SHA256
        startTime = System.currentTimeMillis();
        String sha256 = HashUtils.sha256File(filePath);
        endTime = System.currentTimeMillis();
        System.out.println("文件SHA256耗时: " + (endTime - startTime) + "ms");
    }
}
```

## 🛡️ 安全最佳实践

### 密码哈希增强

```java
public class SecurePasswordHashing {
    
    /**
     * 使用多重哈希增强安全性
     */
    public static String secureHashPassword(String password, String salt) {
        // 第一次SHA256
        String firstHash = HashUtils.sha256(password + salt);
        
        // 第二次SHA256
        String secondHash = HashUtils.sha256(firstHash + salt);
        
        // 第三次SHA256
        return HashUtils.sha256(secondHash + salt);
    }
    
    /**
     * 时间戳相关的哈希
     */
    public static String timestampHash(String data) {
        long timestamp = System.currentTimeMillis() / 1000; // 秒级时间戳
        return HashUtils.sha256(data + timestamp);
    }
    
    /**
     * 随机盐值生成
     */
    public static String generateSalt() {
        return HashUtils.md5(String.valueOf(System.nanoTime()));
    }
}
```

### 哈希碰撞检测

```java
public class HashCollisionDetector {
    
    /**
     * 检测哈希碰撞
     */
    public static boolean hasCollision(List<String> dataList, String algorithm) {
        Set<String> hashSet = new HashSet<>();
        
        for (String data : dataList) {
            String hash;
            switch (algorithm.toLowerCase()) {
                case "md5":
                    hash = HashUtils.md5(data);
                    break;
                case "sha1":
                    hash = HashUtils.sha1(data);
                    break;
                case "sha256":
                    hash = HashUtils.sha256(data);
                    break;
                default:
                    throw new IllegalArgumentException("不支持的算法: " + algorithm);
            }
            
            if (!hashSet.add(hash)) {
                return true; // 发现碰撞
            }
        }
        
        return false; // 无碰撞
    }
    
    /**
     * 找出碰撞的数据
     */
    public static List<String> findCollisions(List<String> dataList, String algorithm) {
        Map<String, List<String>> hashToData = new HashMap<>();
        
        for (String data : dataList) {
            String hash;
            switch (algorithm.toLowerCase()) {
                case "md5":
                    hash = HashUtils.md5(data);
                    break;
                case "sha1":
                    hash = HashUtils.sha1(data);
                    break;
                case "sha256":
                    hash = HashUtils.sha256(data);
                    break;
                default:
                    throw new IllegalArgumentException("不支持的算法: " + algorithm);
            }
            
            hashToData.computeIfAbsent(hash, k -> new ArrayList<>()).add(data);
        }
        
        // 找出有多个数据的哈希值
        List<String> collisions = new ArrayList<>();
        for (List<String> dataWithSameHash : hashToData.values()) {
            if (dataWithSameHash.size() > 1) {
                collisions.addAll(dataWithSameHash);
            }
        }
        
        return collisions;
    }
}
```

## 📝 注意事项

- MD5已被认为不够安全，推荐使用SHA256用于安全敏感场景
- 密码存储时应使用盐值增强安全性
- 大文件哈希计算可能耗时较长，建议异步处理
- 文件不存在或无法读取时会抛出RuntimeException异常
- 所有字符串哈希都使用UTF-8编码
- 建议定期更新哈希算法以应对安全威胁 