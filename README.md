# 🛠️ zzzmh-utils

[English](#english) | [中文](#中文)

## English

A comprehensive Java 8 utility library providing common functions for database, cache, HTTP, encryption, encoding, file operations and more.

### 📦 Maven Dependency

Add this dependency to your project:

```xml
<dependency>
    <groupId>cn.zzzmh.commons</groupId>
    <artifactId>zzzmh-utils</artifactId>
    <version>1.0.1</version>
</dependency>
```

### 🎯 Optional Dependencies

**For MySQL operations** (only if using MySqlUtils):
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

**For Redis operations** (only if using RedisUtils):
```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>4.4.6</version>
</dependency>
```

> 💡 **Note**: MySQL and Redis dependencies are optional. You only need to add them if you plan to use MySqlUtils or RedisUtils respectively.

### 📋 Utility Classes

| Tool | Description | Documentation |
|------|-------------|---------------|
| 🗄️ MySqlUtils | MySQL database operations with CRUD, transactions, and batch processing | [📖 Documentation](docs/en/MySqlUtils.md) |
| 🔴 RedisUtils | Redis operations with String, JSON storage and counter functions | [📖 Documentation](docs/en/RedisUtils.md) |
| 🌐 HttpUtils | HTTP request utility for REST API calls with JSON processing | [📖 Documentation](docs/en/HttpUtils.md) |
| 🔐 AESUtils | AES encryption/decryption with Base36 and Base64 encoding support | [📖 Documentation](docs/en/AESUtils.md) |
| 🔤 EncoderUtils | Base36 and Base64 encoding/decoding utility | [📖 Documentation](docs/en/EncoderUtils.md) |
| 🔒 HashUtils | Hash calculation utility (MD5, SHA1, SHA256) for strings and files | [📖 Documentation](docs/en/HashUtils.md) |
| 📅 DateTimeUtils | Comprehensive time processing utility with formatting and calculation | [📖 Documentation](docs/en/DateTimeUtils.md) |
| 📁 FileUtils | File and directory operations with encoding support | [📖 Documentation](docs/en/FileUtils.md) |
| 🔍 JsonUtils | Advanced JSON processing with JSONPath queries and deep operations | [📖 Documentation](docs/en/JsonUtils.md) |

### 📄 License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

---

## 中文

基于Java 8的全面工具类库，提供数据库、缓存、HTTP、加密、编码、文件操作等常用功能。

### 📦 Maven依赖

在你的项目中添加以下依赖：

```xml
<dependency>
    <groupId>cn.zzzmh.commons</groupId>
    <artifactId>zzzmh-utils</artifactId>
    <version>1.0.1</version>
</dependency>
```

### 🎯 可选依赖

**MySQL数据库操作** (仅在使用MySqlUtils时需要)：
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

**Redis缓存操作** (仅在使用RedisUtils时需要)：
```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>4.4.6</version>
</dependency>
```

> 💡 **说明**: MySQL和Redis依赖是可选的，只有在使用对应的工具类时才需要添加。如果你不使用MySqlUtils或RedisUtils，可以不添加这些依赖。

### 📋 工具类目录

| 工具类 | 描述 | 文档链接 |
|--------|------|----------|
| 🗄️ MySqlUtils | MySQL数据库操作工具，支持CRUD、事务、批量操作 | [📖 详细文档](docs/zh/MySqlUtils.md) |
| 🔴 RedisUtils | Redis缓存操作工具，支持字符串、JSON存储和计数器功能 | [📖 详细文档](docs/zh/RedisUtils.md) |
| 🌐 HttpUtils | HTTP请求工具，提供REST API调用和JSON数据处理 | [📖 详细文档](docs/zh/HttpUtils.md) |
| 🔐 AESUtils | AES加密解密工具，支持Base36和Base64编码 | [📖 详细文档](docs/zh/AESUtils.md) |
| 🔤 EncoderUtils | Base36和Base64编码解码工具 | [📖 详细文档](docs/zh/EncoderUtils.md) |
| 🔒 HashUtils | 哈希计算工具，支持MD5、SHA1、SHA256算法 | [📖 详细文档](docs/zh/HashUtils.md) |
| 📅 DateTimeUtils | 全面的时间处理工具，提供格式化、解析、计算功能 | [📖 详细文档](docs/zh/DateTimeUtils.md) |
| 📁 FileUtils | 文件和目录操作工具，支持多种编码格式 | [📖 详细文档](docs/zh/FileUtils.md) |
| 🔍 JsonUtils | 高级JSON处理工具，支持JSONPath查询和深度操作 | [📖 详细文档](docs/zh/JsonUtils.md) |

### 📄 许可证

本项目采用GNU通用公共许可证v3.0 - 详见[LICENSE](LICENSE)文件。 