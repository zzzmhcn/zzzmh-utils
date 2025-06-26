# 🔤 EncoderUtils Documentation

## Overview

EncoderUtils is an encoding conversion utility class that provides mutual conversion between Base36 and Base64 encoding, supporting encoding operations for both strings and byte arrays.

## 🚀 Quick Start

### Base36 Encoding

```java
// String encoding
String text = "Hello World";
String encoded = EncoderUtils.base36EncodeString(text);
System.out.println("Base36 encoded: " + encoded);

// String decoding
String decoded = EncoderUtils.base36DecodeString(encoded);
System.out.println("Decoded result: " + decoded);

// Byte array encoding
byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
String encodedBytes = EncoderUtils.base36Encode(bytes);

// Byte array decoding
byte[] decodedBytes = EncoderUtils.base36Decode(encodedBytes);
```

### Base64 Encoding

```java
// String encoding
String text = "Hello World";
String encoded = EncoderUtils.base64EncodeString(text);
System.out.println("Base64 encoded: " + encoded);

// String decoding
String decoded = EncoderUtils.base64DecodeString(encoded);
System.out.println("Decoded result: " + decoded);

// Byte array encoding
byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
String encodedBytes = EncoderUtils.base64Encode(bytes);

// Byte array decoding
byte[] decodedBytes = EncoderUtils.base64Decode(encodedBytes);
```

## 🔄 Encoding Conversion

### Base36 vs Base64 Conversion

```java
// Original data
String originalData = "Data content to encode";

// Convert to Base36
String base36Encoded = EncoderUtils.base36EncodeString(originalData);
System.out.println("Base36: " + base36Encoded);

// Convert to Base64
String base64Encoded = EncoderUtils.base64EncodeString(originalData);
System.out.println("Base64: " + base64Encoded);

// Base36 -> Base64 conversion
byte[] base36Bytes = EncoderUtils.base36Decode(base36Encoded);
String base64FromBase36 = EncoderUtils.base64Encode(base36Bytes);

// Base64 -> Base36 conversion
byte[] base64Bytes = EncoderUtils.base64Decode(base64Encoded);
String base36FromBase64 = EncoderUtils.base36Encode(base64Bytes);
```

## 📊 Encoding Comparison

### Base36 vs Base64 Feature Comparison

| Feature | Base36 | Base64 |
|---------|--------|--------|
| Character Set | 0-9, a-z (36 characters) | A-Z, a-z, 0-9, +, / (64 characters) |
| URL Safe | ✅ Completely safe | ❌ Requires URL encoding |
| Encoding Efficiency | Lower (longer) | Higher (shorter) |
| Readability | Better | Moderate |
| Case Sensitive | ❌ Not sensitive | ✅ Sensitive |

## 📝 Notes

- Base36 encoding result contains only digits 0-9 and lowercase letters a-z, URL safe
- Base64 encoding result contains uppercase/lowercase letters, digits and special characters, requires URL encoding
- Base36 encoded strings are usually 20-30% longer than Base64
- Encoding operations increase data size, choosing appropriate encoding method is important
- All methods use UTF-8 charset for string processing
- Runtime exceptions will be thrown in error cases, recommend adding try-catch handling 