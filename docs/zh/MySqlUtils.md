# 🗄️ MySqlUtils 使用文档

## 概述

MySqlUtils是基于FastJSON2的MySQL数据库操作工具类，提供简洁易用的数据库操作接口。

## 🚀 快速开始

### 初始化连接

**方式1：配置文件初始化（推荐）**

在 `src/main/resources/config.properties` 中创建配置文件：

```properties
url=jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
username=your_username
password=your_password
```

创建配置文件后，可以直接使用任何方法，会自动初始化：

```java
// 直接使用，无需手动初始化
JSONObject user = MySqlUtils.selectById("users", 1);
```

**方式2：参数初始化**

```java
MySqlUtils.init("localhost", 3306, "username", "password", "database");
// 或者
Connection conn = MySqlUtils.init("localhost", 3306, "username", "password", "database");
```

**方式3：URL初始化**

```java
Connection conn = MySqlUtils.init("jdbc:mysql://localhost:3306/database", "username", "password");
```

## 📋 基础操作

### 查询操作

```java
// 根据ID查询
JSONObject user = MySqlUtils.selectById("users", 1);

// 查询所有数据
JSONArray users = MySqlUtils.selectAll("users");

// 条件查询
JSONArray adults = MySqlUtils.selectByCondition("users", "age > ?", 18);
JSONArray result = MySqlUtils.selectByCondition("users", "name = ? AND age > ?", "张三", 20);
```

### 插入操作

```java
// 单条插入
JSONObject userData = new JSONObject();
userData.put("name", "张三");
userData.put("age", 25);

Long userId = MySqlUtils.insert("users", userData, Long.class);
String productId = MySqlUtils.insert("products", productData, String.class); // 自动生成UUID

// 批量插入
JSONArray userArray = new JSONArray();
// ... 添加多条数据
List<Long> userIds = MySqlUtils.batchInsert("users", userArray, Long.class, 500); // 每批500条
List<Long> ids = MySqlUtils.batchInsert("users", userArray, Long.class); // 默认1000条
```

### 更新操作

```java
JSONObject updateData = new JSONObject();
updateData.put("age", 26);
updateData.put("email", "new@example.com");

boolean success = MySqlUtils.updateById("users", 1, updateData);
```

### 删除操作

```java
boolean success = MySqlUtils.deleteById("users", 1);
```

## 🔧 自定义SQL

```java
// 自定义查询
JSONArray result = MySqlUtils.executeQuery("SELECT * FROM users WHERE age BETWEEN ? AND ?", 18, 65);

// 自定义更新
int affected = MySqlUtils.executeUpdate("UPDATE users SET status = ? WHERE age < ?", "inactive", 18);

// 自定义插入并返回主键
Long newId = MySqlUtils.executeInsert("INSERT INTO users (name, age) VALUES (?, ?)", Long.class, "李四", 30);
```

## 💼 事务支持

```java
// 无返回值事务
MySqlUtils.executeInTransaction(conn -> {
    MySqlUtils.insert(conn, "users", userData, Long.class);
    MySqlUtils.updateById(conn, "orders", orderId, orderData);
    MySqlUtils.deleteById(conn, "temp_data", tempId);
});

// 有返回值事务
Long newUserId = MySqlUtils.executeInTransaction(conn -> {
    Long userId = MySqlUtils.insert(conn, "users", userData, Long.class);
    MySqlUtils.insert(conn, "user_profiles", profileData, String.class);
    return userId;
});
```

## 🔌 连接管理

### 连接机制说明

- **全局连接**：首次连接的数据库会默认成为全局连接，用于所有不指定连接的操作
- **自动关闭**：JVM关闭时会自动关闭全局连接，无需手动处理
- **连接复用**：全局连接会被复用，提高性能

### 创建和使用连接

```java
// 创建新连接
Connection conn = MySqlUtils.createConnection();

// 使用指定连接进行操作
JSONObject user = MySqlUtils.selectById(conn, "users", 1);
Long id = MySqlUtils.insert(conn, "users", userData, Long.class);
```

### 关闭连接（良好习惯）

```java
// 关闭指定连接（推荐做法）
Connection conn = MySqlUtils.createConnection();
try {
    // ... 执行数据库操作
    JSONObject user = MySqlUtils.selectById(conn, "users", 1);
} finally {
    MySqlUtils.closeConnection(conn); // 静默关闭，不抛异常
}

// 关闭全局连接（通常不需要手动调用）
MySqlUtils.closeGlobalConnection(); // 静默关闭，不抛异常
```

### 连接管理最佳实践

1. **日常使用**：直接使用全局连接方法，无需手动管理
2. **特殊场景**：需要多个连接时，使用`createConnection()`创建新连接
3. **资源清理**：自定义连接使用完毕后，调用`closeConnection()`关闭
4. **程序退出**：全局连接会自动关闭，无需手动处理

## 📝 注意事项

- 支持主键类型：`Long.class`、`Integer.class`、`String.class`
- String类型主键会自动生成32位UUID（无横杠）
- 时间类型自动转换为时间戳（long类型）
- 所有方法都支持懒加载，可直接使用无需手动初始化 