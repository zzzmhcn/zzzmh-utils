# 🔤 EncoderUtils 使用文档

## 概述

EncoderUtils是一个编码转换工具类，提供Base36和Base64编码的相互转换功能，支持字符串和字节数组的编码操作。

## 🚀 快速开始

### Base36编码

```java
// 字符串编码
String text = "Hello World 你好世界";
String encoded = EncoderUtils.base36EncodeString(text);
System.out.println("Base36编码: " + encoded);

// 字符串解码
String decoded = EncoderUtils.base36DecodeString(encoded);
System.out.println("解码结果: " + decoded);

// 字节数组编码
byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
String encodedBytes = EncoderUtils.base36Encode(bytes);

// 字节数组解码
byte[] decodedBytes = EncoderUtils.base36Decode(encodedBytes);
```

### Base64编码

```java
// 字符串编码
String text = "Hello World 你好世界";
String encoded = EncoderUtils.base64EncodeString(text);
System.out.println("Base64编码: " + encoded);

// 字符串解码
String decoded = EncoderUtils.base64DecodeString(encoded);
System.out.println("解码结果: " + decoded);

// 字节数组编码
byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
String encodedBytes = EncoderUtils.base64Encode(bytes);

// 字节数组解码
byte[] decodedBytes = EncoderUtils.base64Decode(encodedBytes);
```

## 📋 编码方法详解

### Base36操作

```java
// === 字符串操作 ===

// 基础编码
String originalText = "测试数据123";
String base36Text = EncoderUtils.base36EncodeString(originalText);

// 基础解码
String decodedText = EncoderUtils.base36DecodeString(base36Text);

// === 字节数组操作 ===

// 字节数组编码
byte[] originalBytes = "binary data".getBytes();
String base36String = EncoderUtils.base36Encode(originalBytes);

// 字节数组解码
byte[] decodedBytes = EncoderUtils.base36Decode(base36String);

// === 安全操作（带异常处理）===
try {
    String encoded = EncoderUtils.base36EncodeString("数据");
    String decoded = EncoderUtils.base36DecodeString(encoded);
} catch (RuntimeException e) {
    System.err.println("Base36编码错误: " + e.getMessage());
}
```

### Base64操作

```java
// === 字符串操作 ===

// 基础编码
String originalText = "测试数据123";
String base64Text = EncoderUtils.base64EncodeString(originalText);

// 基础解码
String decodedText = EncoderUtils.base64DecodeString(base64Text);

// === 字节数组操作 ===

// 字节数组编码
byte[] originalBytes = "binary data".getBytes();
String base64String = EncoderUtils.base64Encode(originalBytes);

// 字节数组解码
byte[] decodedBytes = EncoderUtils.base64Decode(base64String);

// === 安全操作（带异常处理）===
try {
    String encoded = EncoderUtils.base64EncodeString("数据");
    String decoded = EncoderUtils.base64DecodeString(encoded);
} catch (RuntimeException e) {
    System.err.println("Base64编码错误: " + e.getMessage());
}
```

## 🔄 编码转换

### Base36与Base64互转

```java
// 原始数据
String originalData = "需要编码的数据内容";

// 先转Base36
String base36Encoded = EncoderUtils.base36EncodeString(originalData);
System.out.println("Base36: " + base36Encoded);

// 再转Base64
String base64Encoded = EncoderUtils.base64EncodeString(originalData);
System.out.println("Base64: " + base64Encoded);

// Base36 -> Base64转换
byte[] base36Bytes = EncoderUtils.base36Decode(base36Encoded);
String base64FromBase36 = EncoderUtils.base64Encode(base36Bytes);

// Base64 -> Base36转换
byte[] base64Bytes = EncoderUtils.base64Decode(base64Encoded);
String base36FromBase64 = EncoderUtils.base36Encode(base64Bytes);
```

### 批量编码转换

```java
public class BatchEncoder {
    
    /**
     * 批量Base36编码
     */
    public static List<String> batchBase36Encode(List<String> texts) {
        return texts.stream()
            .map(EncoderUtils::base36EncodeString)
            .collect(Collectors.toList());
    }
    
    /**
     * 批量Base36解码
     */
    public static List<String> batchBase36Decode(List<String> encodedTexts) {
        return encodedTexts.stream()
            .map(EncoderUtils::base36DecodeString)
            .collect(Collectors.toList());
    }
    
    /**
     * 批量Base64编码
     */
    public static List<String> batchBase64Encode(List<String> texts) {
        return texts.stream()
            .map(EncoderUtils::base64EncodeString)
            .collect(Collectors.toList());
    }
    
    /**
     * 批量Base64解码
     */
    public static List<String> batchBase64Decode(List<String> encodedTexts) {
        return encodedTexts.stream()
            .map(EncoderUtils::base64DecodeString)
            .collect(Collectors.toList());
    }
}
```

## ⚡ 实际应用场景

### URL安全编码

```java
public class UrlSafeEncoder {
    
    /**
     * URL参数编码（Base36更适合URL）
     */
    public static String encodeUrlParam(String param) {
        // Base36编码结果只包含0-9和a-z，URL安全
        return EncoderUtils.base36EncodeString(param);
    }
    
    /**
     * URL参数解码
     */
    public static String decodeUrlParam(String encodedParam) {
        return EncoderUtils.base36DecodeString(encodedParam);
    }
    
    /**
     * 构建安全的URL
     */
    public static String buildSafeUrl(String baseUrl, String paramName, String paramValue) {
        String encodedValue = encodeUrlParam(paramValue);
        return baseUrl + "?" + paramName + "=" + encodedValue;
    }
}

// 使用示例
String url = UrlSafeEncoder.buildSafeUrl(
    "https://api.example.com/data", 
    "query", 
    "用户搜索关键词"
);
```

### 数据存储编码

```java
public class DataStorageEncoder {
    
    /**
     * 存储前编码（避免特殊字符问题）
     */
    public static JSONObject encodeForStorage(JSONObject data) {
        JSONObject encoded = new JSONObject();
        
        for (String key : data.keySet()) {
            Object value = data.get(key);
            if (value instanceof String) {
                // 字符串值进行Base64编码
                String encodedValue = EncoderUtils.base64EncodeString((String) value);
                encoded.put(key, encodedValue);
            } else {
                encoded.put(key, value);
            }
        }
        
        return encoded;
    }
    
    /**
     * 读取后解码
     */
    public static JSONObject decodeFromStorage(JSONObject encodedData) {
        JSONObject decoded = new JSONObject();
        
        for (String key : encodedData.keySet()) {
            Object value = encodedData.get(key);
            if (value instanceof String) {
                try {
                    // 尝试Base64解码
                    String decodedValue = EncoderUtils.base64DecodeString((String) value);
                    decoded.put(key, decodedValue);
                } catch (RuntimeException e) {
                    // 解码失败，可能不是编码数据
                    decoded.put(key, value);
                }
            } else {
                decoded.put(key, value);
            }
        }
        
        return decoded;
    }
}
```

### 密钥编码存储

```java
public class KeyEncoder {
    
    /**
     * 编码密钥用于安全存储
     */
    public static String encodeKey(byte[] keyBytes) {
        // Base36编码，结果更简洁
        return EncoderUtils.base36Encode(keyBytes);
    }
    
    /**
     * 解码密钥用于使用
     */
    public static byte[] decodeKey(String encodedKey) {
        return EncoderUtils.base36Decode(encodedKey);
    }
    
    /**
     * 编码配置文件中的密钥
     */
    public static Properties encodeKeys(Properties config) {
        Properties encodedConfig = new Properties();
        
        for (String key : config.stringPropertyNames()) {
            String value = config.getProperty(key);
            
            if (key.toLowerCase().contains("key") || 
                key.toLowerCase().contains("secret") ||
                key.toLowerCase().contains("password")) {
                // 敏感信息进行编码
                String encodedValue = EncoderUtils.base64EncodeString(value);
                encodedConfig.setProperty(key, encodedValue);
            } else {
                encodedConfig.setProperty(key, value);
            }
        }
        
        return encodedConfig;
    }
}
```

### 文件名编码

```java
public class FileNameEncoder {
    
    /**
     * 编码文件名（避免特殊字符）
     */
    public static String encodeFileName(String originalName) {
        // 提取文件扩展名
        String extension = "";
        int lastDot = originalName.lastIndexOf('.');
        if (lastDot > 0) {
            extension = originalName.substring(lastDot);
            originalName = originalName.substring(0, lastDot);
        }
        
        // Base36编码文件名主体
        String encodedName = EncoderUtils.base36EncodeString(originalName);
        
        return encodedName + extension;
    }
    
    /**
     * 解码文件名
     */
    public static String decodeFileName(String encodedName) {
        // 提取文件扩展名
        String extension = "";
        int lastDot = encodedName.lastIndexOf('.');
        if (lastDot > 0) {
            extension = encodedName.substring(lastDot);
            encodedName = encodedName.substring(0, lastDot);
        }
        
        // Base36解码文件名主体
        String decodedName = EncoderUtils.base36DecodeString(encodedName);
        
        return decodedName + extension;
    }
    
    /**
     * 批量编码目录中的文件名
     */
    public static Map<String, String> encodeFileNames(List<String> fileNames) {
        Map<String, String> mapping = new HashMap<>();
        
        for (String fileName : fileNames) {
            String encodedName = encodeFileName(fileName);
            mapping.put(fileName, encodedName);
        }
        
        return mapping;
    }
}
```

## 🔧 高级功能

### 自定义编码工具

```java
public class CustomEncoder {
    
    /**
     * 安全的编码方法（带前缀标识）
     */
    public static String safeEncode(String data, String method) {
        switch (method.toLowerCase()) {
            case "base36":
                return "B36:" + EncoderUtils.base36EncodeString(data);
            case "base64":
                return "B64:" + EncoderUtils.base64EncodeString(data);
            default:
                throw new IllegalArgumentException("不支持的编码方法: " + method);
        }
    }
    
    /**
     * 自动识别解码方法
     */
    public static String safeDecode(String encodedData) {
        if (encodedData.startsWith("B36:")) {
            return EncoderUtils.base36DecodeString(encodedData.substring(4));
        } else if (encodedData.startsWith("B64:")) {
            return EncoderUtils.base64DecodeString(encodedData.substring(4));
        } else {
            throw new IllegalArgumentException("无法识别编码格式");
        }
    }
    
    /**
     * 智能编码选择
     */
    public static String smartEncode(String data) {
        // 根据数据特征选择最优编码方式
        
        // 如果包含大量数字和字母，Base36更紧凑
        long alphanumericCount = data.chars()
            .filter(c -> Character.isLetterOrDigit(c))
            .count();
        
        double alphanumericRatio = (double) alphanumericCount / data.length();
        
        if (alphanumericRatio > 0.8) {
            return safeEncode(data, "base36");
        } else {
            return safeEncode(data, "base64");
        }
    }
}
```

### 流式编码处理

```java
public class StreamEncoder {
    
    /**
     * 大文件Base64编码
     */
    public static void encodeFile(String inputPath, String outputPath) {
        try {
            byte[] fileContent = FileUtils.readBytes(inputPath);
            String encoded = EncoderUtils.base64Encode(fileContent);
            FileUtils.writeString(outputPath, encoded);
        } catch (Exception e) {
            throw new RuntimeException("文件编码失败", e);
        }
    }
    
    /**
     * 大文件Base64解码
     */
    public static void decodeFile(String encodedPath, String outputPath) {
        try {
            String encodedContent = FileUtils.readString(encodedPath);
            byte[] decoded = EncoderUtils.base64Decode(encodedContent);
            FileUtils.writeBytes(outputPath, decoded);
        } catch (Exception e) {
            throw new RuntimeException("文件解码失败", e);
        }
    }
    
    /**
     * JSON数据编码
     */
    public static JSONObject encodeJsonValues(JSONObject json) {
        JSONObject encoded = new JSONObject();
        
        for (String key : json.keySet()) {
            Object value = json.get(key);
            
            if (value instanceof String) {
                encoded.put(key, EncoderUtils.base64EncodeString((String) value));
            } else if (value instanceof JSONObject) {
                encoded.put(key, encodeJsonValues((JSONObject) value));
            } else if (value instanceof JSONArray) {
                encoded.put(key, encodeJsonArray((JSONArray) value));
            } else {
                encoded.put(key, value);
            }
        }
        
        return encoded;
    }
    
    private static JSONArray encodeJsonArray(JSONArray array) {
        JSONArray encoded = new JSONArray();
        
        for (int i = 0; i < array.size(); i++) {
            Object item = array.get(i);
            
            if (item instanceof String) {
                encoded.add(EncoderUtils.base64EncodeString((String) item));
            } else if (item instanceof JSONObject) {
                encoded.add(encodeJsonValues((JSONObject) item));
            } else {
                encoded.add(item);
            }
        }
        
        return encoded;
    }
}
```

## 📊 编码对比

### Base36 vs Base64特性对比

| 特性 | Base36 | Base64 |
|------|--------|--------|
| 字符集 | 0-9, a-z (36个字符) | A-Z, a-z, 0-9, +, / (64个字符) |
| URL安全 | ✅ 完全安全 | ❌ 需要URL编码 |
| 编码效率 | 较低（更长） | 较高（更短） |
| 可读性 | 较好 | 一般 |
| 大小写敏感 | ❌ 不敏感 | ✅ 敏感 |

### 性能测试

```java
public class EncoderPerformanceTest {
    
    public static void comparePerformance(String testData, int iterations) {
        // Base36性能测试
        long base36StartTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            String encoded = EncoderUtils.base36EncodeString(testData);
            EncoderUtils.base36DecodeString(encoded);
        }
        long base36Time = System.currentTimeMillis() - base36StartTime;
        
        // Base64性能测试
        long base64StartTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            String encoded = EncoderUtils.base64EncodeString(testData);
            EncoderUtils.base64DecodeString(encoded);
        }
        long base64Time = System.currentTimeMillis() - base64StartTime;
        
        System.out.println("Base36耗时: " + base36Time + "ms");
        System.out.println("Base64耗时: " + base64Time + "ms");
    }
}
```

## 📝 注意事项

- Base36编码结果只包含数字0-9和小写字母a-z，URL安全
- Base64编码结果包含大小写字母、数字和特殊字符，需要URL编码
- Base36编码后的字符串通常比Base64长20-30%
- 编码操作会增加数据大小，选择合适的编码方式很重要
- 所有方法都使用UTF-8字符集处理字符串
- 异常情况会抛出RuntimeException，建议添加try-catch处理 