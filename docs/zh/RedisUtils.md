# 🔴 RedisUtils 使用文档

## 概述

RedisUtils是基于Jedis和FastJSON2的Redis缓存操作工具类，提供字符串、JSON对象存储和计数器功能。

## 🚀 快速开始

### 初始化连接

**方式1：零配置使用（推荐）**

直接使用任何方法，会自动连接本地Redis（localhost:6379，无密码，数据库0）：

```java
// 直接使用，无需任何配置
String value = RedisUtils.get("test");
RedisUtils.set("key", "value");
```

**方式2：配置文件初始化**

在 `src/main/resources/config.properties` 中创建配置文件：

```properties
# Redis基础配置
redis.host=localhost
redis.port=6379
redis.password=your_password
redis.database=0

# 连接池配置（可选）
redis.pool.maxTotal=200
redis.pool.maxIdle=50
redis.pool.minIdle=10
redis.pool.timeout=3000
```

创建配置文件后，可以直接使用任何方法：

```java
// 自动读取配置文件并初始化
String value = RedisUtils.get("user:1");
```

**方式3：代码初始化**

```java
// 基础参数初始化
RedisUtils.init("localhost", 6379, null, 0);

// 完整连接池配置初始化
RedisUtils.init("localhost", 6379, "password", 0, 200, 50, 10, 3000);
```

## 📋 基础操作

### 字符串操作

```java
// 设置字符串值（永不过期）
RedisUtils.set("username", "张三");

// 设置字符串值（指定过期时间）
RedisUtils.set("session:123", "user_data", 3600000L); // 1小时后过期(3600秒*1000毫秒)

// 获取字符串值
String username = RedisUtils.get("username");
String session = RedisUtils.get("session:123"); // 过期后返回null
```

### JSON对象操作

```java
// 创建JSON对象
JSONObject user = new JSONObject();
user.put("id", 1);
user.put("name", "张三");
user.put("age", 25);

// 设置JSON对象（永不过期）
RedisUtils.setJson("user:1", user);

// 设置JSON对象（指定过期时间）
RedisUtils.setJson("user:1", user, 7200000L); // 2小时后过期(7200秒*1000毫秒)

// 获取JSON对象
JSONObject userData = RedisUtils.getJson("user:1");
if (userData != null) {
    String name = userData.getString("name");
    Integer age = userData.getInteger("age");
}
```

### 计数器操作

```java
// 递增计数器
Long count1 = RedisUtils.increment("page_views"); // 返回递增后的值
Long count2 = RedisUtils.increment("page_views", 5L); // 递增5

// 递减计数器
Long count3 = RedisUtils.decrement("inventory"); // 返回递减后的值
Long count4 = RedisUtils.decrement("inventory", 3L); // 递减3

// 应用场景示例
Long todayViews = RedisUtils.increment("views:" + LocalDate.now());
Long userPoints = RedisUtils.decrement("user:points:" + userId, 100L);
```

## 🔧 通用操作

```java
// 删除键
boolean deleted = RedisUtils.delete("temp_key");

// 检查键是否存在
boolean exists = RedisUtils.exists("user:1");

// 设置过期时间
boolean success = RedisUtils.expire("session:123", 1800000L); // 30分钟后过期(1800秒*1000毫秒)
```

## 🔌 连接管理

### 连接机制说明

- **零配置使用**：无需任何配置，自动连接本地Redis
- **配置文件优先**：存在配置文件时优先使用配置文件参数
- **连接池管理**：使用Jedis连接池，支持高并发访问
- **自动关闭**：JVM关闭时自动关闭连接池

### 创建连接

```java
// 获取Jedis连接（需要手动关闭）
try (Jedis jedis = RedisUtils.createConnection()) {
    // 执行原生Redis命令
    String result = jedis.hget("user:1", "name");
    jedis.lpush("queue", "task1", "task2");
}
```

### 关闭连接池

```java
// 关闭连接池（通常不需要手动调用）
RedisUtils.closePool(); // 静默关闭，不抛异常
```

## ⚙️ 连接池配置

### 默认配置值

```properties
redis.pool.maxTotal=200      # 最大连接数
redis.pool.maxIdle=50        # 最大空闲连接数
redis.pool.minIdle=10        # 最小空闲连接数
redis.pool.timeout=3000      # 连接超时时间(毫秒)
```

### 生产环境建议

```properties
# 高并发环境配置
redis.pool.maxTotal=500
redis.pool.maxIdle=100
redis.pool.minIdle=20
redis.pool.timeout=5000

# 中等并发环境配置
redis.pool.maxTotal=200
redis.pool.maxIdle=50
redis.pool.minIdle=10
redis.pool.timeout=3000

# 低并发环境配置
redis.pool.maxTotal=50
redis.pool.maxIdle=20
redis.pool.minIdle=5
redis.pool.timeout=2000
```

## 💡 使用场景示例

### 缓存用户信息

```java
// 设置用户缓存
JSONObject user = new JSONObject();
user.put("id", userId);
user.put("name", "张三");
user.put("email", "zhangsan@example.com");
RedisUtils.setJson("user:" + userId, user, 3600000L); // 缓存1小时

// 获取用户缓存
JSONObject cachedUser = RedisUtils.getJson("user:" + userId);
if (cachedUser == null) {
    // 缓存不存在，从数据库查询
    cachedUser = getUserFromDatabase(userId);
    RedisUtils.setJson("user:" + userId, cachedUser, 3600000L);
}
```

### 会话管理

```java
// 创建会话
String sessionId = UUID.randomUUID().toString();
JSONObject sessionData = new JSONObject();
sessionData.put("userId", userId);
sessionData.put("loginTime", System.currentTimeMillis());
RedisUtils.setJson("session:" + sessionId, sessionData, 1800000L); // 30分钟过期

// 验证会话
JSONObject session = RedisUtils.getJson("session:" + sessionId);
if (session != null) {
    Long userId = session.getLong("userId");
    // 会话有效，续期
    RedisUtils.expire("session:" + sessionId, 1800000L);
}
```

### 计数统计

```java
// 页面访问统计
String today = LocalDate.now().toString();
RedisUtils.increment("page_views:" + today);
RedisUtils.increment("page_views:" + today + ":" + pageId);

// 用户积分管理
RedisUtils.increment("user_points:" + userId, 10L); // 增加积分
RedisUtils.decrement("user_points:" + userId, 5L);  // 扣除积分

// 库存管理
Long remaining = RedisUtils.decrement("product_stock:" + productId);
if (remaining < 0) {
    // 库存不足
    RedisUtils.increment("product_stock:" + productId); // 回滚
}
```

## 📝 注意事项

- **默认连接**：零配置时连接localhost:6379，无密码，数据库0
- **过期时间**：单位为毫秒，不传入过期时间则永不过期
- **JSON序列化**：使用FastJSON2进行JSON序列化和反序列化
- **连接池**：自动管理连接池，无需手动关闭连接
- **异常处理**：所有方法都会抛出运行时异常，包含详细错误信息
- **线程安全**：所有方法都是线程安全的，支持并发访问 