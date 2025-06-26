# 🔴 RedisUtils Documentation

## Overview

RedisUtils is a Redis caching operation utility class based on Jedis and FastJSON2, providing String, JSON object storage and counter functions.

## 🚀 Quick Start

### Initialize Connection

**Method 1: Zero-configuration usage (Recommended)**

Use any method directly, it will automatically connect to local Redis (localhost:6379, no password, database 0):

```java
// Use directly without any configuration
String value = RedisUtils.get("test");
RedisUtils.set("key", "value");
```

**Method 2: Configuration file initialization**

Create configuration file in `src/main/resources/config.properties`:

```properties
# Redis basic configuration
redis.host=localhost
redis.port=6379
redis.password=your_password
redis.database=0

# Connection pool configuration (optional)
redis.pool.maxTotal=200
redis.pool.maxIdle=50
redis.pool.minIdle=10
redis.pool.timeout=3000
```

After creating the configuration file, you can directly use any method:

```java
// Automatically read configuration file and initialize
String value = RedisUtils.get("user:1");
```

**Method 3: Code initialization**

```java
// Basic parameter initialization
RedisUtils.init("localhost", 6379, null, 0);

// Full connection pool configuration initialization
RedisUtils.init("localhost", 6379, "password", 0, 200, 50, 10, 3000);
```

## 📋 Basic Operations

### String Operations

```java
// Set string value (never expires)
RedisUtils.set("username", "John");

// Set string value (with expiration time)
RedisUtils.set("session:123", "user_data", 3600000L); // Expires after 1 hour (3600 seconds * 1000 milliseconds)

// Get string value
String username = RedisUtils.get("username");
String session = RedisUtils.get("session:123"); // Returns null after expiration
```

### JSON Object Operations

```java
// Create JSON object
JSONObject user = new JSONObject();
user.put("id", 1);
user.put("name", "John");
user.put("age", 25);

// Set JSON object (never expires)
RedisUtils.setJson("user:1", user);

// Set JSON object (with expiration time)
RedisUtils.setJson("user:1", user, 7200000L); // Expires after 2 hours (7200 seconds * 1000 milliseconds)

// Get JSON object
JSONObject userData = RedisUtils.getJson("user:1");
if (userData != null) {
    String name = userData.getString("name");
    Integer age = userData.getInteger("age");
}
```

### Counter Operations

```java
// Increment counter
Long count1 = RedisUtils.increment("page_views"); // Returns incremented value
Long count2 = RedisUtils.increment("page_views", 5L); // Increment by 5

// Decrement counter
Long count3 = RedisUtils.decrement("inventory"); // Returns decremented value
Long count4 = RedisUtils.decrement("inventory", 3L); // Decrement by 3

// Application scenario examples
Long todayViews = RedisUtils.increment("views:" + LocalDate.now());
Long userPoints = RedisUtils.decrement("user:points:" + userId, 100L);
```

## 🔧 General Operations

```java
// Delete key
boolean deleted = RedisUtils.delete("temp_key");

// Check if key exists
boolean exists = RedisUtils.exists("user:1");

// Set expiration time
boolean success = RedisUtils.expire("session:123", 1800000L); // Expires after 30 minutes (1800 seconds * 1000 milliseconds)
```

## 🔌 Connection Management

### Connection Mechanism

- **Zero-configuration usage**: No configuration needed, automatically connects to local Redis
- **Configuration file priority**: Uses configuration file parameters when available
- **Connection pool management**: Uses Jedis connection pool, supports high concurrency access
- **Auto close**: Connection pool automatically closes when JVM shuts down

### Create Connection

```java
// Get Jedis connection (requires manual closing)
try (Jedis jedis = RedisUtils.createConnection()) {
    // Execute native Redis commands
    String result = jedis.hget("user:1", "name");
    jedis.lpush("queue", "task1", "task2");
}
```

### Close Connection Pool

```java
// Close connection pool (usually no need to call manually)
RedisUtils.closePool(); // Silent close, no exceptions thrown
```

## ⚙️ Connection Pool Configuration

### Default Configuration Values

```properties
redis.pool.maxTotal=200      # Maximum connections
redis.pool.maxIdle=50        # Maximum idle connections
redis.pool.minIdle=10        # Minimum idle connections
redis.pool.timeout=3000      # Connection timeout (milliseconds)
```

### Production Environment Recommendations

```properties
# High concurrency environment configuration
redis.pool.maxTotal=500
redis.pool.maxIdle=100
redis.pool.minIdle=20
redis.pool.timeout=5000

# Medium concurrency environment configuration
redis.pool.maxTotal=200
redis.pool.maxIdle=50
redis.pool.minIdle=10
redis.pool.timeout=3000

# Low concurrency environment configuration
redis.pool.maxTotal=50
redis.pool.maxIdle=20
redis.pool.minIdle=5
redis.pool.timeout=2000
```

## 💡 Usage Scenario Examples

### Cache User Information

```java
// Set user cache
JSONObject user = new JSONObject();
user.put("id", userId);
user.put("name", "John");
user.put("email", "john@example.com");
RedisUtils.setJson("user:" + userId, user, 3600000L); // Cache for 1 hour

// Get user cache
JSONObject cachedUser = RedisUtils.getJson("user:" + userId);
if (cachedUser == null) {
    // Cache doesn't exist, query from database
    cachedUser = getUserFromDatabase(userId);
    RedisUtils.setJson("user:" + userId, cachedUser, 3600000L);
}
```

### Session Management

```java
// Create session
String sessionId = UUID.randomUUID().toString();
JSONObject sessionData = new JSONObject();
sessionData.put("userId", userId);
sessionData.put("loginTime", System.currentTimeMillis());
RedisUtils.setJson("session:" + sessionId, sessionData, 1800000L); // Expires in 30 minutes

// Validate session
JSONObject session = RedisUtils.getJson("session:" + sessionId);
if (session != null) {
    Long userId = session.getLong("userId");
    // Session is valid, renew
    RedisUtils.expire("session:" + sessionId, 1800000L);
}
```

### Counter Statistics

```java
// Page view statistics
String today = LocalDate.now().toString();
RedisUtils.increment("page_views:" + today);
RedisUtils.increment("page_views:" + today + ":" + pageId);

// User points management
RedisUtils.increment("user_points:" + userId, 10L); // Add points
RedisUtils.decrement("user_points:" + userId, 5L);  // Deduct points

// Inventory management
Long remaining = RedisUtils.decrement("product_stock:" + productId);
if (remaining < 0) {
    // Insufficient stock
    RedisUtils.increment("product_stock:" + productId); // Rollback
}
```

## 📝 Notes

- **Default connection**: Zero-configuration connects to localhost:6379, no password, database 0
- **Expiration time**: Unit is milliseconds, no expiration if time parameter not provided
- **JSON serialization**: Uses FastJSON2 for JSON serialization and deserialization
- **Connection pool**: Automatically manages connection pool, no need to manually close connections
- **Exception handling**: All methods throw runtime exceptions with detailed error messages
- **Thread safety**: All methods are thread-safe, supports concurrent access 