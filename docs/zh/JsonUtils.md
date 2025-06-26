# 🔍 JsonUtils 使用文档

## 概述

JsonUtils是基于FastJSON2和JSONPath的JSON处理工具类，提供JSONPath查询、深度操作、类型安全转换、模板处理等高级JSON操作功能。

## 🚀 快速开始

### JSONPath查询

```java
// 创建测试JSON数据
JSONObject data = JSON.parseObject("""
{
    "user": {
        "name": "张三",
        "age": 25,
        "hobbies": ["编程", "阅读", "旅行"]
    },
    "orders": [
        {"id": 1, "amount": 100.0},
        {"id": 2, "amount": 200.0}
    ]
}
""");

// JSONPath查询
String userName = JsonUtils.getByPath(data, "$.user.name"); // "张三"
Integer userAge = JsonUtils.getByPath(data, "$.user.age");  // 25
JSONArray hobbies = JsonUtils.getByPath(data, "$.user.hobbies");
Double firstOrderAmount = JsonUtils.getByPath(data, "$.orders[0].amount"); // 100.0
```

### 类型安全查询

```java
// 安全的类型转换查询
String name = JsonUtils.getString(data, "$.user.name");           // "张三"
Integer age = JsonUtils.getInteger(data, "$.user.age");           // 25
Double amount = JsonUtils.getDouble(data, "$.orders[0].amount");  // 100.0
Boolean active = JsonUtils.getBoolean(data, "$.user.active");     // null (不存在)

// 带默认值的安全查询
String status = JsonUtils.getString(data, "$.user.status", "active");    // "active"
Integer level = JsonUtils.getInteger(data, "$.user.level", 1);           // 1
Double discount = JsonUtils.getDouble(data, "$.discount", 0.0);          // 0.0
```

## 📋 JSONPath查询详解

### 基础路径语法

```java
JSONObject testData = JSON.parseObject("""
{
    "store": {
        "book": [
            {
                "category": "fiction",
                "author": "J.K. Rowling",
                "title": "Harry Potter",
                "price": 8.95
            },
            {
                "category": "fiction", 
                "author": "J.R.R. Tolkien",
                "title": "The Lord of the Rings",
                "price": 22.99
            }
        ],
        "bicycle": {
            "color": "red",
            "price": 19.95
        }
    }
}
""");

// 基础路径查询
JSONArray books = JsonUtils.getByPath(testData, "$.store.book");
String firstBookTitle = JsonUtils.getString(testData, "$.store.book[0].title");
String bicycleColor = JsonUtils.getString(testData, "$.store.bicycle.color");

// 所有书籍的价格
List<Double> bookPrices = JsonUtils.getByPath(testData, "$.store.book[*].price");

// 过滤查询（价格大于10的书）
JSONArray expensiveBooks = JsonUtils.getByPath(testData, "$.store.book[?(@.price > 10)]");
```

### 高级路径查询

```java
// 递归查询所有价格
List<Double> allPrices = JsonUtils.getByPath(testData, "$..price");

// 条件过滤
JSONArray fictionBooks = JsonUtils.getByPath(testData, "$.store.book[?(@.category == 'fiction')]");

// 多条件过滤
JSONArray affordableBooks = JsonUtils.getByPath(testData, "$.store.book[?(@.price < 20 && @.category == 'fiction')]");

// 数组长度查询
Integer bookCount = JsonUtils.getByPath(testData, "$.store.book.length()");
```

## 🔄 JSON深度操作

### 深度合并

```java
JSONObject obj1 = JSON.parseObject("""
{
    "user": {
        "name": "张三",
        "age": 25,
        "profile": {
            "email": "zhangsan@example.com"
        }
    },
    "preferences": ["theme1"]
}
""");

JSONObject obj2 = JSON.parseObject("""
{
    "user": {
        "age": 26,
        "profile": {
            "phone": "13800138000",
            "address": "北京市"
        }
    },
    "preferences": ["theme2", "theme3"],
    "settings": {
        "language": "zh-CN"
    }
}
""");

// 深度合并
JSONObject merged = JsonUtils.deepMerge(obj1, obj2);
// 结果: user.age = 26, user.profile包含email、phone、address
//      preferences = ["theme2", "theme3"]（数组被替换）
//      新增settings字段

// 深度合并（数组追加模式）
JSONObject merged2 = JsonUtils.deepMergeArrays(obj1, obj2);
// 结果: preferences = ["theme1", "theme2", "theme3"]（数组被合并）
```

### 深度拷贝

```java
JSONObject original = JSON.parseObject("""
{
    "data": {
        "items": [{"id": 1}, {"id": 2}],
        "meta": {"total": 2}
    }
}
""");

// 深度拷贝
JSONObject copy = JsonUtils.deepCopy(original);

// 修改拷贝不会影响原对象
JsonUtils.setByPath(copy, "$.data.meta.total", 3);
// original.data.meta.total 仍然是 2

// 深度拷贝到指定类型
User user = JsonUtils.deepCopyTo(userJson, User.class);
```

### 深度比较

```java
JSONObject obj1 = JSON.parseObject("{\"a\": {\"b\": 1, \"c\": [1, 2]}}");
JSONObject obj2 = JSON.parseObject("{\"a\": {\"b\": 1, \"c\": [1, 2]}}");
JSONObject obj3 = JSON.parseObject("{\"a\": {\"b\": 2, \"c\": [1, 2]}}");

// 深度比较
boolean equal1 = JsonUtils.deepEquals(obj1, obj2); // true
boolean equal2 = JsonUtils.deepEquals(obj1, obj3); // false

// 获取差异
List<String> differences = JsonUtils.getDifferences(obj1, obj3);
// ["$.a.b: 1 != 2"]

// 比较并获取详细差异报告
JSONObject diffReport = JsonUtils.compareAndGetDifferences(obj1, obj3);
```

## 🛠️ JSON路径设置和删除

### 设置值

```java
JSONObject data = new JSONObject();

// 设置简单值
JsonUtils.setByPath(data, "$.user.name", "李四");
JsonUtils.setByPath(data, "$.user.age", 30);

// 设置数组
JsonUtils.setByPath(data, "$.user.hobbies", Arrays.asList("游泳", "跑步"));

// 设置嵌套对象
JSONObject profile = new JSONObject();
profile.put("email", "lisi@example.com");
JsonUtils.setByPath(data, "$.user.profile", profile);

// 动态创建路径
JsonUtils.setByPath(data, "$.settings.theme.color", "blue");
// 自动创建 settings 和 theme 对象
```

### 删除值

```java
// 删除字段
boolean removed = JsonUtils.removeByPath(data, "$.user.age");

// 删除数组元素
boolean removed = JsonUtils.removeByPath(data, "$.user.hobbies[0]");

// 批量删除
List<String> pathsToRemove = Arrays.asList("$.user.profile.email", "$.settings.theme");
int removedCount = JsonUtils.removePaths(data, pathsToRemove);
```

## 🔍 JSON查询和过滤

### 条件查询

```java
JSONArray users = JSON.parseArray("""
[
    {"id": 1, "name": "张三", "age": 25, "city": "北京"},
    {"id": 2, "name": "李四", "age": 30, "city": "上海"},
    {"id": 3, "name": "王五", "age": 28, "city": "北京"}
]
""");

// 过滤年龄大于25的用户
JSONArray filtered = JsonUtils.filter(users, item -> {
    Integer age = JsonUtils.getInteger(item, "$.age");
    return age != null && age > 25;
});

// 使用JSONPath过滤
JSONArray beijingUsers = JsonUtils.getByPath(users, "$[?(@.city == '北京')]");

// 复杂条件过滤
JSONArray youngBeijingUsers = JsonUtils.getByPath(users, "$[?(@.city == '北京' && @.age < 30)]");
```

### 数据变换

```java
// 提取字段
List<String> names = JsonUtils.map(users, item -> JsonUtils.getString(item, "$.name"));

// 数据转换
JSONArray transformed = JsonUtils.map(users, item -> {
    JSONObject newItem = new JSONObject();
    newItem.put("userId", JsonUtils.getInteger(item, "$.id"));
    newItem.put("fullName", JsonUtils.getString(item, "$.name"));
    newItem.put("isYoung", JsonUtils.getInteger(item, "$.age") < 30);
    return newItem;
});

// 分组
Map<String, JSONArray> groupedByCity = JsonUtils.groupBy(users, item -> 
    JsonUtils.getString(item, "$.city"));
```

## 📊 JSON模板和填充

### 模板填充

```java
// 定义模板
JSONObject template = JSON.parseObject("""
{
    "user": {
        "name": "${user.name}",
        "welcome": "欢迎 ${user.name}，您的等级是 ${user.level}"
    },
    "timestamp": "${current.time}",
    "config": {
        "theme": "${settings.theme|default:light}"
    }
}
""");

// 准备数据
JSONObject data = JSON.parseObject("""
{
    "user": {
        "name": "张三",
        "level": "VIP"
    },
    "current": {
        "time": "2023-12-01 14:30:25"
    },
    "settings": {}
}
""");

// 填充模板
JSONObject result = JsonUtils.fillTemplate(template, data);
// result.user.welcome = "欢迎 张三，您的等级是 VIP"
// result.config.theme = "light" (使用默认值)
```

### 批量模板处理

```java
// 批量用户数据
JSONArray userList = JSON.parseArray("""
[
    {"name": "张三", "level": "VIP"},
    {"name": "李四", "level": "普通"}
]
""");

// 用户通知模板
JSONObject notificationTemplate = JSON.parseObject("""
{
    "title": "欢迎 ${name}",
    "content": "您好 ${name}，您的会员等级是 ${level}",
    "timestamp": "${current.time}"
}
""");

// 批量生成通知
JSONArray notifications = JsonUtils.batchFillTemplate(notificationTemplate, userList, 
    user -> {
        JSONObject context = JsonUtils.deepCopy(user);
        context.put("current", new JSONObject() {{
            put("time", DateTimeUtils.getCurrentTimeString());
        }});
        return context;
    });
```

## 🔧 JSON工具方法

### JSON压缩和美化

```java
JSONObject data = JSON.parseObject("{\"user\":{\"name\":\"张三\",\"age\":25}}");

// 压缩JSON（移除空格）
String compressed = JsonUtils.compress(data);
// {"user":{"name":"张三","age":25}}

// 美化JSON（格式化缩进）
String prettified = JsonUtils.prettify(data);
// {
//   "user" : {
//     "name" : "张三",
//     "age" : 25
//   }
// }

// 自定义缩进
String customFormatted = JsonUtils.prettify(data, 4); // 4个空格缩进
```

### JSON验证

```java
// 验证JSON格式
boolean isValid = JsonUtils.isValidJson("{\"name\": \"test\"}"); // true
boolean isValid = JsonUtils.isValidJson("{name: test}");        // false

// 验证JSONPath
boolean isValidPath = JsonUtils.isValidPath("$.user.name");     // true
boolean isValidPath = JsonUtils.isValidPath("$.user[name");     // false

// 验证JSON Schema（简化版本）
boolean isValid = JsonUtils.validateStructure(data, requiredFields);
```

### JSON统计信息

```java
// 获取JSON统计信息
JSONObject stats = JsonUtils.getStatistics(data);
// {
//   "totalFields": 10,
//   "maxDepth": 3,
//   "arrayCount": 2,
//   "objectCount": 5
// }

// 获取所有路径
List<String> allPaths = JsonUtils.getAllPaths(data);
// ["$.user", "$.user.name", "$.user.age", "$.user.hobbies", "$.user.hobbies[0]", ...]

// 获取叶子节点路径
List<String> leafPaths = JsonUtils.getLeafPaths(data);
// ["$.user.name", "$.user.age", "$.user.hobbies[0]", "$.user.hobbies[1]", ...]
```

## ⚡ 实际应用场景

### API响应处理

```java
public class ApiResponseProcessor {
    
    /**
     * 处理分页API响应
     */
    public static JSONObject processPaginatedResponse(JSONObject response) {
        JSONObject result = new JSONObject();
        
        // 提取数据
        JSONArray items = JsonUtils.getByPath(response, "$.data.items");
        Integer total = JsonUtils.getInteger(response, "$.data.total");
        Integer page = JsonUtils.getInteger(response, "$.data.page", 1);
        Integer pageSize = JsonUtils.getInteger(response, "$.data.pageSize", 10);
        
        // 计算分页信息
        int totalPages = (int) Math.ceil((double) total / pageSize);
        boolean hasNext = page < totalPages;
        boolean hasPrev = page > 1;
        
        // 构建结果
        result.put("items", items);
        result.put("pagination", new JSONObject() {{
            put("total", total);
            put("page", page);
            put("pageSize", pageSize);
            put("totalPages", totalPages);
            put("hasNext", hasNext);
            put("hasPrev", hasPrev);
        }});
        
        return result;
    }
    
    /**
     * 提取错误信息
     */
    public static String extractErrorMessage(JSONObject errorResponse) {
        // 尝试多种可能的错误信息路径
        String[] errorPaths = {
            "$.error.message",
            "$.message",
            "$.data.error",
            "$.errors[0].message"
        };
        
        for (String path : errorPaths) {
            String message = JsonUtils.getString(errorResponse, path);
            if (message != null && !message.isEmpty()) {
                return message;
            }
        }
        
        return "未知错误";
    }
}
```

### 配置文件处理

```java
public class ConfigProcessor {
    
    /**
     * 处理分层配置
     */
    public static JSONObject processConfig(JSONObject baseConfig, JSONObject userConfig) {
        // 深度合并配置
        JSONObject mergedConfig = JsonUtils.deepMerge(baseConfig, userConfig);
        
        // 处理配置继承
        processConfigInheritance(mergedConfig);
        
        // 验证必需配置
        validateRequiredConfig(mergedConfig);
        
        // 设置默认值
        setDefaultValues(mergedConfig);
        
        return mergedConfig;
    }
    
    private static void processConfigInheritance(JSONObject config) {
        // 处理 ${parent.key} 形式的继承
        List<String> allPaths = JsonUtils.getAllPaths(config);
        
        for (String path : allPaths) {
            Object value = JsonUtils.getByPath(config, path);
            if (value instanceof String) {
                String strValue = (String) value;
                if (strValue.startsWith("${") && strValue.endsWith("}")) {
                    String refPath = "$." + strValue.substring(2, strValue.length() - 1);
                    Object refValue = JsonUtils.getByPath(config, refPath);
                    if (refValue != null) {
                        JsonUtils.setByPath(config, path, refValue);
                    }
                }
            }
        }
    }
    
    private static void validateRequiredConfig(JSONObject config) {
        String[] requiredPaths = {
            "$.database.url",
            "$.database.username",
            "$.server.port"
        };
        
        for (String path : requiredPaths) {
            if (JsonUtils.getByPath(config, path) == null) {
                throw new RuntimeException("缺少必需配置: " + path);
            }
        }
    }
    
    private static void setDefaultValues(JSONObject config) {
        Map<String, Object> defaults = Map.of(
            "$.server.port", 8080,
            "$.server.timeout", 30000,
            "$.logging.level", "INFO"
        );
        
        for (Map.Entry<String, Object> entry : defaults.entrySet()) {
            if (JsonUtils.getByPath(config, entry.getKey()) == null) {
                JsonUtils.setByPath(config, entry.getKey(), entry.getValue());
            }
        }
    }
}
```

### 数据清洗和转换

```java
public class DataCleaner {
    
    /**
     * 清洗用户数据
     */
    public static JSONObject cleanUserData(JSONObject userData) {
        JSONObject cleaned = JsonUtils.deepCopy(userData);
        
        // 删除敏感字段
        List<String> sensitiveFields = Arrays.asList(
            "$.password", "$.creditCard", "$.ssn", "$.privateKey"
        );
        JsonUtils.removePaths(cleaned, sensitiveFields);
        
        // 标准化字段名
        standardizeFieldNames(cleaned);
        
        // 验证和修复数据格式
        validateAndFixData(cleaned);
        
        // 添加派生字段
        addDerivedFields(cleaned);
        
        return cleaned;
    }
    
    private static void standardizeFieldNames(JSONObject data) {
        Map<String, String> fieldMapping = Map.of(
            "$.userName", "$.username",
            "$.userEmail", "$.email",
            "$.phoneNum", "$.phone"
        );
        
        for (Map.Entry<String, String> entry : fieldMapping.entrySet()) {
            Object value = JsonUtils.getByPath(data, entry.getKey());
            if (value != null) {
                JsonUtils.removeByPath(data, entry.getKey());
                JsonUtils.setByPath(data, entry.getValue(), value);
            }
        }
    }
    
    private static void validateAndFixData(JSONObject data) {
        // 验证邮箱格式
        String email = JsonUtils.getString(data, "$.email");
        if (email != null && !email.contains("@")) {
            JsonUtils.removeByPath(data, "$.email");
        }
        
        // 验证年龄范围
        Integer age = JsonUtils.getInteger(data, "$.age");
        if (age != null && (age < 0 || age > 150)) {
            JsonUtils.removeByPath(data, "$.age");
        }
        
        // 标准化手机号格式
        String phone = JsonUtils.getString(data, "$.phone");
        if (phone != null) {
            phone = phone.replaceAll("[^0-9]", "");
            if (phone.length() == 11) {
                JsonUtils.setByPath(data, "$.phone", phone);
            } else {
                JsonUtils.removeByPath(data, "$.phone");
            }
        }
    }
    
    private static void addDerivedFields(JSONObject data) {
        // 添加年龄组
        Integer age = JsonUtils.getInteger(data, "$.age");
        if (age != null) {
            String ageGroup;
            if (age < 18) ageGroup = "未成年";
            else if (age < 35) ageGroup = "青年";
            else if (age < 60) ageGroup = "中年";
            else ageGroup = "老年";
            
            JsonUtils.setByPath(data, "$.ageGroup", ageGroup);
        }
        
        // 添加处理时间戳
        JsonUtils.setByPath(data, "$.processedAt", System.currentTimeMillis());
    }
}
```

## 📊 性能优化

### JSONPath缓存

```java
public class JsonPathCache {
    
    private static final Map<String, JSONPath> PATH_CACHE = new ConcurrentHashMap<>();
    
    /**
     * 获取缓存的JSONPath对象
     */
    public static JSONPath getCompiledPath(String path) {
        return PATH_CACHE.computeIfAbsent(path, JSONPath::of);
    }
    
    /**
     * 使用缓存的路径查询
     */
    public static <T> T queryWithCache(Object data, String path) {
        JSONPath compiledPath = getCompiledPath(path);
        return (T) compiledPath.eval(data);
    }
}
```

### 批量操作优化

```java
public class JsonBatchProcessor {
    
    /**
     * 批量路径查询
     */
    public static Map<String, Object> batchQuery(JSONObject data, List<String> paths) {
        Map<String, Object> results = new HashMap<>();
        
        for (String path : paths) {
            try {
                Object value = JsonUtils.getByPath(data, path);
                results.put(path, value);
            } catch (Exception e) {
                results.put(path, null);
            }
        }
        
        return results;
    }
    
    /**
     * 批量设置值
     */
    public static void batchSet(JSONObject data, Map<String, Object> pathValues) {
        for (Map.Entry<String, Object> entry : pathValues.entrySet()) {
            try {
                JsonUtils.setByPath(data, entry.getKey(), entry.getValue());
            } catch (Exception e) {
                System.err.println("设置路径失败: " + entry.getKey());
            }
        }
    }
}
```

## 📝 注意事项

- JSONPath查询失败返回null，不会抛出异常
- 深度操作（合并、拷贝、比较）会递归处理嵌套结构
- 模板填充支持默认值语法：`${path|default:value}`
- 所有路径操作都支持数组索引和过滤条件
- 类型安全方法会自动进行类型转换
- 大型JSON对象的深度操作可能消耗较多内存和时间 