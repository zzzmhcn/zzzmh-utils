# 🗄️ MySqlUtils Documentation

## Overview

MySqlUtils is a MySQL database operation utility class based on FastJSON2, providing simple and easy-to-use database operation interfaces.

## 🚀 Quick Start

### Initialize Connection

**Method 1: Configuration file initialization (Recommended)**

Create configuration file in `src/main/resources/config.properties`:

```properties
url=jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
username=your_username
password=your_password
```

After creating the configuration file, you can directly use any method, it will auto-initialize:

```java
// Use directly without manual initialization
JSONObject user = MySqlUtils.selectById("users", 1);
```

**Method 2: Parameter initialization**

```java
MySqlUtils.init("localhost", 3306, "username", "password", "database");
// Or
Connection conn = MySqlUtils.init("localhost", 3306, "username", "password", "database");
```

**Method 3: URL initialization**

```java
Connection conn = MySqlUtils.init("jdbc:mysql://localhost:3306/database", "username", "password");
```

## 📋 Basic Operations

### Query Operations

```java
// Query by ID
JSONObject user = MySqlUtils.selectById("users", 1);

// Query all data
JSONArray users = MySqlUtils.selectAll("users");

// Conditional query
JSONArray adults = MySqlUtils.selectByCondition("users", "age > ?", 18);
JSONArray result = MySqlUtils.selectByCondition("users", "name = ? AND age > ?", "John", 20);
```

### Insert Operations

```java
// Single insert
JSONObject userData = new JSONObject();
userData.put("name", "John");
userData.put("age", 25);

Long userId = MySqlUtils.insert("users", userData, Long.class);
String productId = MySqlUtils.insert("products", productData, String.class); // Auto-generate UUID

// Batch insert
JSONArray userArray = new JSONArray();
// ... add multiple records
List<Long> userIds = MySqlUtils.batchInsert("users", userArray, Long.class, 500); // 500 per batch
List<Long> ids = MySqlUtils.batchInsert("users", userArray, Long.class); // Default 1000 per batch
```

### Update Operations

```java
JSONObject updateData = new JSONObject();
updateData.put("age", 26);
updateData.put("email", "new@example.com");

boolean success = MySqlUtils.updateById("users", 1, updateData);
```

### Delete Operations

```java
boolean success = MySqlUtils.deleteById("users", 1);
```

## 🔧 Custom SQL

```java
// Custom query
JSONArray result = MySqlUtils.executeQuery("SELECT * FROM users WHERE age BETWEEN ? AND ?", 18, 65);

// Custom update
int affected = MySqlUtils.executeUpdate("UPDATE users SET status = ? WHERE age < ?", "inactive", 18);

// Custom insert with generated key
Long newId = MySqlUtils.executeInsert("INSERT INTO users (name, age) VALUES (?, ?)", Long.class, "Jane", 30);
```

## 💼 Transaction Support

```java
// Transaction without return value
MySqlUtils.executeInTransaction(conn -> {
    MySqlUtils.insert(conn, "users", userData, Long.class);
    MySqlUtils.updateById(conn, "orders", orderId, orderData);
    MySqlUtils.deleteById(conn, "temp_data", tempId);
});

// Transaction with return value
Long newUserId = MySqlUtils.executeInTransaction(conn -> {
    Long userId = MySqlUtils.insert(conn, "users", userData, Long.class);
    MySqlUtils.insert(conn, "user_profiles", profileData, String.class);
    return userId;
});
```

## 🔌 Connection Management

### Connection Mechanism

- **Global Connection**: The first connected database becomes the global connection, used for all operations without specifying connection
- **Auto Close**: Global connection will be automatically closed when JVM shuts down, no manual handling required
- **Connection Reuse**: Global connection is reused to improve performance

### Create and Use Connections

```java
// Create new connection
Connection conn = MySqlUtils.createConnection();

// Use specific connection for operations
JSONObject user = MySqlUtils.selectById(conn, "users", 1);
Long id = MySqlUtils.insert(conn, "users", userData, Long.class);
```

### Close Connections (Good Practice)

```java
// Close specific connection (recommended practice)
Connection conn = MySqlUtils.createConnection();
try {
    // ... perform database operations
    JSONObject user = MySqlUtils.selectById(conn, "users", 1);
} finally {
    MySqlUtils.closeConnection(conn); // Silent close, no exceptions thrown
}

// Close global connection (usually no need to call manually)
MySqlUtils.closeGlobalConnection(); // Silent close, no exceptions thrown
```

### Connection Management Best Practices

1. **Daily Usage**: Directly use global connection methods, no manual management needed
2. **Special Scenarios**: Use `createConnection()` to create new connections when multiple connections needed
3. **Resource Cleanup**: Call `closeConnection()` to close custom connections after use
4. **Program Exit**: Global connection will be closed automatically, no manual handling required

## 📝 Notes

- Supported primary key types: `Long.class`, `Integer.class`, `String.class`
- String primary keys automatically generate 32-character UUID (without hyphens)
- Time types are automatically converted to timestamps (long type)
- All methods support lazy loading and can be used directly without manual initialization 