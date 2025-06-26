# 🔐 AESUtils Documentation

## Overview

AESUtils is an encryption utility class based on AES/CBC/PKCS7Padding algorithm using Bouncy Castle provider, supporting string encryption with Base36 and Base64 encoding.

## 🚀 Quick Start

### Basic Byte Array Encryption

```java
// Prepare data
String plainText = "Sensitive data to encrypt";
byte[] plainData = plainText.getBytes(StandardCharsets.UTF_8);

// Prepare key and IV (should be securely generated in production)
byte[] secretKey = "your-16-byte-key".getBytes(); // 16-byte key
byte[] iv = "your-16-byte-iv!".getBytes();        // 16-byte IV

// Encrypt
byte[] encryptedData = AESUtils.encrypt(plainData, secretKey, iv);

// Decrypt
byte[] decryptedData = AESUtils.decrypt(encryptedData, secretKey, iv);
String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);
```

### Base36 String Encryption (Recommended)

```java
// Plain text data
String plainText = "User password or sensitive information";

// Base36 encoded key and IV
String secretKey = "j8k2l9m3n4p5q6r7s8t9u1v2w3x4y5z6"; // Base36 format
String iv = "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6";        // Base36 format

// Encrypt
String encryptedText = AESUtils.encryptBase36(plainText, secretKey, iv);

// Decrypt
String decryptedText = AESUtils.decryptBase36(encryptedText, secretKey, iv);
```

### Base64 String Encryption

```java
// Plain text data
String plainText = "Data to encrypt";

// Base64 encoded key and IV
String secretKey = "your-base64-encoded-key==";
String iv = "your-base64-encoded-iv==";

// Encrypt
String encryptedText = AESUtils.encryptBase64(plainText, secretKey, iv);

// Decrypt
String decryptedText = AESUtils.decryptBase64(encryptedText, secretKey, iv);
```

## 🔑 Key and IV Management

### Key Length Support

```java
// AES supports three key lengths
byte[] key128 = new byte[16]; // 128-bit key
byte[] key192 = new byte[24]; // 192-bit key  
byte[] key256 = new byte[32]; // 256-bit key

// IV is fixed at 128 bits (16 bytes)
byte[] iv = new byte[16];

// Fill with random data (use secure random generator in production)
new SecureRandom().nextBytes(key256);
new SecureRandom().nextBytes(iv);
```

## ⚙️ Algorithm Configuration

### Encryption Algorithm Specifications

- **Algorithm**: AES (Advanced Encryption Standard)
- **Mode**: CBC (Cipher Block Chaining)
- **Padding**: PKCS7Padding
- **Provider**: Bouncy Castle
- **IV Length**: 128 bits (16 bytes)
- **Key Length**: 128/192/256 bits (16/24/32 bytes)

### Compatibility Methods

```java
// Compatible with old API (deprecated, use new methods)
@Deprecated
String encrypted = AESUtils.encrypt(plainText, secretKey, iv); // Same as encryptBase36

@Deprecated  
String decrypted = AESUtils.decrypt(encryptedText, secretKey, iv); // Same as decryptBase36
```

## 🔒 Security Best Practices

### Key Management

1. **Key Generation**: Use `SecureRandom` to generate random keys
2. **Key Storage**: Don't hardcode keys in source code
3. **Key Rotation**: Regularly change encryption keys
4. **Access Control**: Limit key access permissions

### IV (Initialization Vector) Management

1. **Randomness**: Use different IV for each encryption
2. **Publicity**: IV can be stored publicly but should not be reused
3. **Storage**: Usually stored together with ciphertext

## 📝 Notes

- Key length must be 16, 24, or 32 bytes
- IV length must be 16 bytes
- Same plaintext, key and IV will produce same ciphertext
- Recommend using different IV for each encryption to enhance security
- Base36 encoding is suitable for URLs, Base64 encoding is more universal
- Ensure Bouncy Castle dependency is added to project before use 