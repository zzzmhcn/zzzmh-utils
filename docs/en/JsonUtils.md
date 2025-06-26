# 🔍 JsonUtils Documentation

## Overview

JsonUtils is a JSON processing utility class based on FastJSON2 and JSONPath, providing JSONPath queries, deep operations, type-safe conversions, template processing, and other advanced JSON operations.

## 🚀 Quick Start

### JSONPath Queries

```java
// Create test JSON data
JSONObject data = JSON.parseObject("""
{
    "user": {
        "name": "John Doe",
        "age": 25,
        "hobbies": ["programming", "reading", "traveling"]
    },
    "orders": [
        {"id": 1, "amount": 100.0},
        {"id": 2, "amount": 200.0}
    ]
}
""");

// JSONPath queries
String userName = JsonUtils.getByPath(data, "$.user.name"); // "John Doe"
Integer userAge = JsonUtils.getByPath(data, "$.user.age");  // 25
JSONArray hobbies = JsonUtils.getByPath(data, "$.user.hobbies");
Double firstOrderAmount = JsonUtils.getByPath(data, "$.orders[0].amount"); // 100.0
```

### Type-Safe Queries

```java
// Safe type conversion queries
String name = JsonUtils.getString(data, "$.user.name");           // "John Doe"
Integer age = JsonUtils.getInteger(data, "$.user.age");           // 25
Double amount = JsonUtils.getDouble(data, "$.orders[0].amount");  // 100.0
Boolean active = JsonUtils.getBoolean(data, "$.user.active");     // null (doesn't exist)

// Safe queries with default values
String status = JsonUtils.getString(data, "$.user.status", "active");    // "active"
Integer level = JsonUtils.getInteger(data, "$.user.level", 1);           // 1
Double discount = JsonUtils.getDouble(data, "$.discount", 0.0);          // 0.0
```

## 📋 JSONPath Query Details

### Basic Path Syntax

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

// Basic path queries
JSONArray books = JsonUtils.getByPath(testData, "$.store.book");
String firstBookTitle = JsonUtils.getString(testData, "$.store.book[0].title");
String bicycleColor = JsonUtils.getString(testData, "$.store.bicycle.color");

// All book prices
List<Double> bookPrices = JsonUtils.getByPath(testData, "$.store.book[*].price");

// Filter query (books with price > 10)
JSONArray expensiveBooks = JsonUtils.getByPath(testData, "$.store.book[?(@.price > 10)]");
```

### Advanced Path Queries

```java
// Recursive query for all prices
List<Double> allPrices = JsonUtils.getByPath(testData, "$..price");

// Conditional filtering
JSONArray fictionBooks = JsonUtils.getByPath(testData, "$.store.book[?(@.category == 'fiction')]");

// Multi-condition filtering
JSONArray affordableBooks = JsonUtils.getByPath(testData, "$.store.book[?(@.price < 20 && @.category == 'fiction')]");

// Array length query
Integer bookCount = JsonUtils.getByPath(testData, "$.store.book.length()");
```

## 🔄 JSON Deep Operations

### Deep Merge

```java
JSONObject obj1 = JSON.parseObject("""
{
    "user": {
        "name": "John",
        "age": 25,
        "profile": {
            "email": "john@example.com"
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
            "phone": "123-456-7890",
            "address": "New York"
        }
    },
    "preferences": ["theme2", "theme3"],
    "settings": {
        "language": "en-US"
    }
}
""");

// Deep merge
JSONObject merged = JsonUtils.deepMerge(obj1, obj2);
// Result: user.age = 26, user.profile contains email, phone, address
//         preferences = ["theme2", "theme3"] (array replaced)
//         settings field added

// Deep merge with array append mode
JSONObject merged2 = JsonUtils.deepMergeArrays(obj1, obj2);
// Result: preferences = ["theme1", "theme2", "theme3"] (arrays merged)
```

### Deep Copy

```java
JSONObject original = JSON.parseObject("""
{
    "data": {
        "items": [{"id": 1}, {"id": 2}],
        "meta": {"total": 2}
    }
}
""");

// Deep copy
JSONObject copy = JsonUtils.deepCopy(original);

// Modifying copy doesn't affect original
JsonUtils.setByPath(copy, "$.data.meta.total", 3);
// original.data.meta.total remains 2

// Deep copy to specified type
User user = JsonUtils.deepCopyTo(userJson, User.class);
```

### Deep Comparison

```java
JSONObject obj1 = JSON.parseObject("{\"a\": {\"b\": 1, \"c\": [1, 2]}}");
JSONObject obj2 = JSON.parseObject("{\"a\": {\"b\": 1, \"c\": [1, 2]}}");
JSONObject obj3 = JSON.parseObject("{\"a\": {\"b\": 2, \"c\": [1, 2]}}");

// Deep comparison
boolean equal1 = JsonUtils.deepEquals(obj1, obj2); // true
boolean equal2 = JsonUtils.deepEquals(obj1, obj3); // false

// Get differences
List<String> differences = JsonUtils.getDifferences(obj1, obj3);
// ["$.a.b: 1 != 2"]

// Compare and get detailed difference report
JSONObject diffReport = JsonUtils.compareAndGetDifferences(obj1, obj3);
```

## 🛠️ JSON Path Setting and Deletion

### Setting Values

```java
JSONObject data = new JSONObject();

// Set simple values
JsonUtils.setByPath(data, "$.user.name", "Jane Doe");
JsonUtils.setByPath(data, "$.user.age", 30);

// Set arrays
JsonUtils.setByPath(data, "$.user.hobbies", Arrays.asList("swimming", "running"));

// Set nested objects
JSONObject profile = new JSONObject();
profile.put("email", "jane@example.com");
JsonUtils.setByPath(data, "$.user.profile", profile);

// Dynamically create paths
JsonUtils.setByPath(data, "$.settings.theme.color", "blue");
// Automatically creates settings and theme objects
```

### Deleting Values

```java
// Delete fields
boolean removed = JsonUtils.removeByPath(data, "$.user.age");

// Delete array elements
boolean removed = JsonUtils.removeByPath(data, "$.user.hobbies[0]");

// Batch delete
List<String> pathsToRemove = Arrays.asList("$.user.profile.email", "$.settings.theme");
int removedCount = JsonUtils.removePaths(data, pathsToRemove);
```

## 🔍 JSON Query and Filtering

### Conditional Queries

```java
JSONArray users = JSON.parseArray("""
[
    {"id": 1, "name": "John", "age": 25, "city": "New York"},
    {"id": 2, "name": "Jane", "age": 30, "city": "Los Angeles"},
    {"id": 3, "name": "Bob", "age": 28, "city": "New York"}
]
""");

// Filter users older than 25
JSONArray filtered = JsonUtils.filter(users, item -> {
    Integer age = JsonUtils.getInteger(item, "$.age");
    return age != null && age > 25;
});

// Use JSONPath filtering
JSONArray nyUsers = JsonUtils.getByPath(users, "$[?(@.city == 'New York')]");

// Complex condition filtering
JSONArray youngNyUsers = JsonUtils.getByPath(users, "$[?(@.city == 'New York' && @.age < 30)]");
```

### Data Transformation

```java
// Extract fields
List<String> names = JsonUtils.map(users, item -> JsonUtils.getString(item, "$.name"));

// Data transformation
JSONArray transformed = JsonUtils.map(users, item -> {
    JSONObject newItem = new JSONObject();
    newItem.put("userId", JsonUtils.getInteger(item, "$.id"));
    newItem.put("fullName", JsonUtils.getString(item, "$.name"));
    newItem.put("isYoung", JsonUtils.getInteger(item, "$.age") < 30);
    return newItem;
});

// Grouping
Map<String, JSONArray> groupedByCity = JsonUtils.groupBy(users, item -> 
    JsonUtils.getString(item, "$.city"));
```

## 📊 JSON Templates and Filling

### Template Filling

```java
// Define template
JSONObject template = JSON.parseObject("""
{
    "user": {
        "name": "${user.name}",
        "welcome": "Welcome ${user.name}, your level is ${user.level}"
    },
    "timestamp": "${current.time}",
    "config": {
        "theme": "${settings.theme|default:light}"
    }
}
""");

// Prepare data
JSONObject data = JSON.parseObject("""
{
    "user": {
        "name": "John",
        "level": "VIP"
    },
    "current": {
        "time": "2023-12-01 14:30:25"
    },
    "settings": {}
}
""");

// Fill template
JSONObject result = JsonUtils.fillTemplate(template, data);
// result.user.welcome = "Welcome John, your level is VIP"
// result.config.theme = "light" (using default value)
```

## 🔧 JSON Utility Methods

### JSON Compression and Beautification

```java
JSONObject data = JSON.parseObject("{\"user\":{\"name\":\"John\",\"age\":25}}");

// Compress JSON (remove spaces)
String compressed = JsonUtils.compress(data);
// {"user":{"name":"John","age":25}}

// Beautify JSON (formatted indentation)
String prettified = JsonUtils.prettify(data);
// {
//   "user" : {
//     "name" : "John",
//     "age" : 25
//   }
// }

// Custom indentation
String customFormatted = JsonUtils.prettify(data, 4); // 4 spaces indentation
```

### JSON Validation

```java
// Validate JSON format
boolean isValid = JsonUtils.isValidJson("{\"name\": \"test\"}"); // true
boolean isValid = JsonUtils.isValidJson("{name: test}");        // false

// Validate JSONPath
boolean isValidPath = JsonUtils.isValidPath("$.user.name");     // true
boolean isValidPath = JsonUtils.isValidPath("$.user[name");     // false

// Validate JSON Schema (simplified version)
boolean isValid = JsonUtils.validateStructure(data, requiredFields);
```

### JSON Statistics

```java
// Get JSON statistics
JSONObject stats = JsonUtils.getStatistics(data);
// {
//   "totalFields": 10,
//   "maxDepth": 3,
//   "arrayCount": 2,
//   "objectCount": 5
// }

// Get all paths
List<String> allPaths = JsonUtils.getAllPaths(data);
// ["$.user", "$.user.name", "$.user.age", "$.user.hobbies", "$.user.hobbies[0]", ...]

// Get leaf node paths
List<String> leafPaths = JsonUtils.getLeafPaths(data);
// ["$.user.name", "$.user.age", "$.user.hobbies[0]", "$.user.hobbies[1]", ...]
```

## 📝 Notes

- JSONPath query failures return null, no exceptions thrown
- Deep operations (merge, copy, compare) recursively process nested structures
- Template filling supports default value syntax: `${path|default:value}`
- All path operations support array indexing and filter conditions
- Type-safe methods automatically perform type conversion
- Large JSON object deep operations may consume significant memory and time 