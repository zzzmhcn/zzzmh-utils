# 🔒 HashUtils Documentation

## Overview

HashUtils is a hash calculation utility class that provides multiple hash algorithms including MD5, SHA1, SHA256 for string and file hash calculation, suitable for data integrity verification and secure storage.

## 🚀 Quick Start

### Basic Hash Calculation

```java
// MD5 hash
String text = "Hello World";
String md5Hash = HashUtils.md5(text);
System.out.println("MD5: " + md5Hash);

// SHA1 hash
String sha1Hash = HashUtils.sha1(text);
System.out.println("SHA1: " + sha1Hash);

// SHA256 hash
String sha256Hash = HashUtils.sha256(text);
System.out.println("SHA256: " + sha256Hash);
```

### File Hash Calculation

```java
// Calculate file MD5
String fileMd5 = HashUtils.md5File("path/to/your/file.txt");

// Calculate file SHA256
String fileSha256 = HashUtils.sha256File("path/to/your/file.txt");

// Calculate file SHA1
String fileSha1 = HashUtils.sha1File("path/to/your/file.txt");
```

## 📋 Hash Algorithm Details

### MD5 Hash

```java
// String MD5
String password = "myPassword123";
String md5Hash = HashUtils.md5(password);

// Chinese string MD5
String chineseText = "Hello World";
String chineseMd5 = HashUtils.md5(chineseText);

// File MD5
String filePath = "C:\\Users\\Documents\\file.pdf";
String fileMd5 = HashUtils.md5File(filePath);

// Verify MD5
boolean isValid = HashUtils.md5(password).equals(md5Hash);
```

### SHA1 Hash

```java
// String SHA1
String data = "sensitive data";
String sha1Hash = HashUtils.sha1(data);

// File SHA1
String fileSha1 = HashUtils.sha1File("path/to/file.txt");

// Batch SHA1 calculation
List<String> texts = Arrays.asList("text1", "text2", "text3");
List<String> sha1Hashes = texts.stream()
    .map(HashUtils::sha1)
    .collect(Collectors.toList());
```

### SHA256 Hash

```java
// String SHA256
String data = "important data";
String sha256Hash = HashUtils.sha256(data);

// File SHA256
String fileSha256 = HashUtils.sha256File("path/to/file.txt");

// Secure password hash
String userPassword = "userPassword123";
String hashedPassword = HashUtils.sha256(userPassword + "salt");
```

## 🔍 Hash Algorithm Comparison

### Algorithm Feature Comparison

| Algorithm | Output Length | Security | Performance | Use Cases |
|-----------|---------------|----------|-------------|-----------|
| MD5 | 32 characters | Low | Fastest | File checksum, simple deduplication |
| SHA1 | 40 characters | Medium | Fast | Version control, integrity verification |
| SHA256 | 64 characters | High | Slower | Password storage, digital signature |

## 🛡️ Security Best Practices

### Password Hash Enhancement

```java
public class SecurePasswordHashing {
    
    /**
     * Use multiple hashing for enhanced security
     */
    public static String secureHashPassword(String password, String salt) {
        // First SHA256
        String firstHash = HashUtils.sha256(password + salt);
        
        // Second SHA256
        String secondHash = HashUtils.sha256(firstHash + salt);
        
        // Third SHA256
        return HashUtils.sha256(secondHash + salt);
    }
    
    /**
     * Generate random salt
     */
    public static String generateSalt() {
        return HashUtils.md5(String.valueOf(System.nanoTime()));
    }
}
```

## 📝 Notes

- MD5 is considered insecure, recommend using SHA256 for security-sensitive scenarios
- Use salt values when storing passwords to enhance security
- Large file hash calculation may take considerable time, consider async processing
- RuntimeException will be thrown when file doesn't exist or can't be read
- All string hashes use UTF-8 encoding
- Recommend regular updates of hash algorithms to address security threats 