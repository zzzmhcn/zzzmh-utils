# 🌐 HttpUtils Documentation

## Overview

HttpUtils is an HTTP request utility class based on HttpURLConnection and FastJSON2, providing simple and easy-to-use REST API call interfaces with a focus on JSON data processing.

## 🚀 Quick Start

### Basic GET Requests

```java
// Simple GET request
JSONObject response = HttpUtils.get("https://api.example.com/users");

// GET request with parameters
JSONObject params = new JSONObject();
params.put("page", 1);
params.put("size", 10);
JSONObject response = HttpUtils.get("https://api.example.com/users", params);

// GET request with headers
JSONObject headers = new JSONObject();
headers.put("Authorization", "Bearer your-token");
headers.put("Accept", "application/json");
JSONObject response = HttpUtils.get("https://api.example.com/users", params, headers);
```

### Basic POST Requests

```java
// Simple POST request
JSONObject requestBody = new JSONObject();
requestBody.put("name", "John Doe");
requestBody.put("email", "john@example.com");
JSONObject response = HttpUtils.post("https://api.example.com/users", requestBody);

// POST request with headers
JSONObject headers = new JSONObject();
headers.put("Authorization", "Bearer your-token");
headers.put("Content-Type", "application/json");
JSONObject response = HttpUtils.post("https://api.example.com/users", requestBody, headers);
```

## 📋 HTTP Methods Support

### GET Requests

```java
// Basic GET
JSONObject response = HttpUtils.get("https://api.example.com/data");

// With query parameters
JSONObject params = new JSONObject();
params.put("keyword", "search term");
params.put("category", "technology");
JSONObject response = HttpUtils.get("https://api.example.com/search", params);

// Full parameter GET request
JSONObject headers = new JSONObject();
headers.put("User-Agent", "MyApp/1.0");
String proxy = "proxy.company.com:8080"; // Proxy settings
JSONObject response = HttpUtils.get("https://api.example.com/data", params, headers, proxy);
```

### POST Requests

```java
// Create user
JSONObject userData = new JSONObject();
userData.put("username", "testuser");
userData.put("password", "123456");
userData.put("email", "test@example.com");
JSONObject response = HttpUtils.post("https://api.example.com/users", userData);

// POST with authentication
JSONObject headers = new JSONObject();
headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
headers.put("X-API-Key", "your-api-key");
JSONObject response = HttpUtils.post("https://api.example.com/protected", userData, headers);
```

### PUT/DELETE Requests

```java
// PUT update
JSONObject updateData = new JSONObject();
updateData.put("status", "active");
updateData.put("lastLogin", System.currentTimeMillis());
JSONObject response = HttpUtils.put("https://api.example.com/users/123", updateData);

// DELETE
JSONObject response = HttpUtils.delete("https://api.example.com/users/123");
```

## 🔧 Advanced Features

### Proxy Configuration

```java
// Use HTTP proxy
String proxyConfig = "proxy.company.com:8080";
JSONObject response = HttpUtils.get("https://api.example.com/data", null, null, proxyConfig);

JSONObject response = HttpUtils.post("https://api.example.com/users", userData, headers, proxyConfig);
```

### Generic Request Method

```java
// Complete generic request
JSONObject params = new JSONObject();
params.put("filter", "active");

JSONObject headers = new JSONObject();
headers.put("Authorization", "Bearer token");
headers.put("Accept-Language", "en-US");

JSONObject requestBody = new JSONObject();
requestBody.put("action", "update");

String proxy = "proxy.server.com:3128";

JSONObject response = HttpUtils.request(
    "PATCH",                           // HTTP method
    "https://api.example.com/resource", // URL
    params,                            // Query parameters
    headers,                           // Request headers
    requestBody,                       // Request body
    proxy                              // Proxy configuration
);
```

## ⚙️ Configuration

### Default Settings

- **Connection Timeout**: 30 seconds
- **Read Timeout**: 30 seconds  
- **Default Content-Type**: `application/json;charset=UTF-8`
- **Default Accept**: `application/json`

### Network Configuration

HttpUtils uses system default network configuration, supporting:
- HTTP/HTTPS protocols
- System proxy settings
- Custom proxy configuration
- Standard HTTP status code handling

## 📝 Notes

- All request methods return JSONObject for consistent response format
- HTTP error status codes will throw RuntimeException
- Automatic URL encoding, supports Chinese parameters
- Supports proxy configuration with format `host:port`
- Connection and read timeouts are both 30 seconds, suitable for most API calls
- Non-JSON responses are automatically wrapped in `{"data": "original content"}` structure 