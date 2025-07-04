# 🌐 HttpUtils 使用文档

## 概述

HttpUtils是基于HttpURLConnection和FastJSON2的HTTP请求工具类，提供简洁易用的REST API调用接口，专注于JSON数据处理。

## 🚀 快速开始

### 基础GET请求

```java
// 简单GET请求
JSONObject response = HttpUtils.get("https://api.example.com/users");

// 带参数的GET请求
JSONObject params = new JSONObject();
params.put("page", 1);
params.put("size", 10);
JSONObject response = HttpUtils.get("https://api.example.com/users", params);

// 带请求头的GET请求
JSONObject headers = new JSONObject();
headers.put("Authorization", "Bearer your-token");
headers.put("Accept", "application/json");
JSONObject response = HttpUtils.get("https://api.example.com/users", params, headers);
```

### 基础POST请求

```java
// 简单POST请求
JSONObject requestBody = new JSONObject();
requestBody.put("name", "张三");
requestBody.put("email", "zhangsan@example.com");
JSONObject response = HttpUtils.post("https://api.example.com/users", requestBody);

// 带请求头的POST请求
JSONObject headers = new JSONObject();
headers.put("Authorization", "Bearer your-token");
headers.put("Content-Type", "application/json");
JSONObject response = HttpUtils.post("https://api.example.com/users", requestBody, headers);
```

## 📋 HTTP方法支持

### GET请求

```java
// 基础GET
JSONObject response = HttpUtils.get("https://api.example.com/data");

// 带查询参数
JSONObject params = new JSONObject();
params.put("keyword", "搜索关键词");
params.put("category", "技术");
JSONObject response = HttpUtils.get("https://api.example.com/search", params);

// 完整参数GET请求
JSONObject headers = new JSONObject();
headers.put("User-Agent", "MyApp/1.0");
String proxy = "proxy.company.com:8080"; // 代理设置
JSONObject response = HttpUtils.get("https://api.example.com/data", params, headers, proxy);
```

### POST请求

```java
// 创建用户
JSONObject userData = new JSONObject();
userData.put("username", "testuser");
userData.put("password", "123456");
userData.put("email", "test@example.com");
JSONObject response = HttpUtils.post("https://api.example.com/users", userData);

// 带认证的POST请求
JSONObject headers = new JSONObject();
headers.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
headers.put("X-API-Key", "your-api-key");
JSONObject response = HttpUtils.post("https://api.example.com/protected", userData, headers);
```

### 🔷 二进制POST请求

```java
// 请求可能返回二进制数据的API
JSONObject requestData = new JSONObject();
requestData.put("fileId", "12345");
requestData.put("format", "png");

JSONObject headers = new JSONObject();
headers.put("Authorization", "Bearer your-token");

// 使用postForBinary方法处理可能的二进制响应
JSONObject response = HttpUtils.postForBinary("https://api.example.com/generate-image", 
                                             requestData, headers);

// 智能处理响应
if (response.getBooleanValue("isBinary")) {
    // 处理二进制响应（如生成的图片）
    String base64Image = response.getString("base64");
    String contentType = response.getString("contentType");
    System.out.println("生成了" + contentType + "类型的文件");
} else {
    // 处理JSON响应（如错误信息）
    String message = response.getString("message");
    System.out.println("API返回: " + message);
}
```

### PUT/DELETE请求

```java
// PUT更新
JSONObject updateData = new JSONObject();
updateData.put("status", "active");
updateData.put("lastLogin", System.currentTimeMillis());
JSONObject response = HttpUtils.put("https://api.example.com/users/123", updateData);

// DELETE删除
JSONObject response = HttpUtils.delete("https://api.example.com/users/123");
```

## 🔧 高级功能

### 代理设置

```java
// 使用HTTP代理
String proxyConfig = "proxy.company.com:8080";
JSONObject response = HttpUtils.get("https://api.example.com/data", null, null, proxyConfig);

JSONObject response = HttpUtils.post("https://api.example.com/users", userData, headers, proxyConfig);
```

### 通用请求方法

```java
// 完整的通用请求
JSONObject params = new JSONObject();
params.put("filter", "active");

JSONObject headers = new JSONObject();
headers.put("Authorization", "Bearer token");
headers.put("Accept-Language", "zh-CN");

JSONObject requestBody = new JSONObject();
requestBody.put("action", "update");

String proxy = "proxy.server.com:3128";

JSONObject response = HttpUtils.request(
    "PATCH",                           // HTTP方法
    "https://api.example.com/resource", // URL
    params,                            // 查询参数
    headers,                           // 请求头
    requestBody,                       // 请求体
    proxy                              // 代理配置
);
```

## 📡 请求头管理

### 常用请求头设置

```java
JSONObject headers = new JSONObject();

// 认证相关
headers.put("Authorization", "Bearer your-jwt-token");
headers.put("X-API-Key", "your-api-key");
headers.put("X-Auth-Token", "session-token");

// 内容类型
headers.put("Content-Type", "application/json;charset=UTF-8");
headers.put("Accept", "application/json");

// 客户端信息
headers.put("User-Agent", "MyApp/1.0.0 (Windows NT 10.0)");
headers.put("X-Requested-With", "XMLHttpRequest");

// 国际化
headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");

JSONObject response = HttpUtils.post("https://api.example.com/data", requestBody, headers);
```

## 🔄 响应处理

### 自动JSON解析

```java
// 成功响应会自动解析为JSONObject
JSONObject response = HttpUtils.get("https://api.example.com/user/123");

// 访问响应数据
String userName = response.getString("name");
Integer userAge = response.getInteger("age");
JSONArray hobbies = response.getJSONArray("hobbies");

// 处理嵌套对象
JSONObject profile = response.getJSONObject("profile");
String avatar = profile.getString("avatar");
```

### 非JSON响应处理

```java
// 如果响应不是JSON格式，会自动包装
JSONObject response = HttpUtils.get("https://api.example.com/plain-text");

// 原始内容在data字段中
String plainText = response.getString("data");
```

### 🔷 二进制响应处理

HttpUtils提供了智能的二进制响应处理功能，能自动识别并处理图片、音频、视频、PDF等二进制内容。

```java
// 下载图片文件
JSONObject headers = new JSONObject();
headers.put("Authorization", "Bearer your-token");

JSONObject response = HttpUtils.postForBinary("https://api.example.com/files/download", 
                                             requestBody, headers);

// 检查是否为二进制响应
if (response.getBooleanValue("isBinary")) {
    String contentType = response.getString("contentType");
    Integer fileSize = response.getInteger("size");
    String base64Data = response.getString("base64");
    String dataUrl = response.getString("dataUrl");
    
    System.out.println("文件类型: " + contentType);
    System.out.println("文件大小: " + fileSize + " 字节");
    
    // 可以直接使用dataUrl在HTML中显示
    // <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...">
    
    // 或者将base64数据保存为文件
    byte[] fileBytes = java.util.Base64.getDecoder().decode(base64Data);
    java.nio.file.Files.write(java.nio.file.Paths.get("downloaded_file.png"), fileBytes);
}
```

#### 支持的二进制文件类型

- **图片文件**: `image/*` (PNG, JPEG, GIF, WebP等)
- **音频文件**: `audio/*` (MP3, WAV, OGG等)  
- **视频文件**: `video/*` (MP4, AVI, MOV等)
- **PDF文档**: `application/pdf`
- **通用二进制**: `application/octet-stream`
- **其他包含"binary"关键字的内容类型**

#### 智能响应识别

HttpUtils会根据响应的`Content-Type`头自动判断是否为二进制内容：

```java
// 智能识别响应类型的通用方法
JSONObject response = HttpUtils.requestForBinary("GET", 
                                                "https://api.example.com/resource", 
                                                headers, null, null);

if (response.getBooleanValue("isBinary")) {
    // 处理二进制数据
    handleBinaryResponse(response);
} else {
    // 处理JSON数据
    handleJsonResponse(response);
}
```

#### 二进制响应数据结构

二进制响应返回的JSONObject包含以下字段：

```json
{
  "isBinary": true,
  "contentType": "image/png",
  "size": 15234,
  "base64": "iVBORw0KGgoAAAANSUhEUgAA...",
  "dataUrl": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA..."
}
```

- `isBinary`: 布尔值，标识这是二进制响应
- `contentType`: 原始的Content-Type响应头
- `size`: 文件大小（字节数）
- `base64`: Base64编码的文件内容
- `dataUrl`: 可直接用于HTML的Data URL格式

## ⚡ 实际应用示例

### 用户认证

```java
// 登录获取Token
JSONObject loginData = new JSONObject();
loginData.put("username", "user@example.com");
loginData.put("password", "password123");

JSONObject loginResponse = HttpUtils.post("https://api.example.com/auth/login", loginData);
String accessToken = loginResponse.getString("access_token");

// 使用Token访问受保护资源
JSONObject headers = new JSONObject();
headers.put("Authorization", "Bearer " + accessToken);

JSONObject userInfo = HttpUtils.get("https://api.example.com/user/profile", null, headers);
```

### 文件上传模拟

```java
// 模拟表单数据上传
JSONObject fileData = new JSONObject();
fileData.put("filename", "document.pdf");
fileData.put("content", EncoderUtils.base64EncodeString("文件内容"));
fileData.put("type", "application/pdf");

JSONObject headers = new JSONObject();
headers.put("Content-Type", "application/json");
headers.put("X-Upload-Type", "base64");

JSONObject response = HttpUtils.post("https://api.example.com/files/upload", fileData, headers);
```

### 分页数据获取

```java
// 分页获取数据
public JSONArray getAllUsers() {
    JSONArray allUsers = new JSONArray();
    int page = 1;
    int pageSize = 100;
    
    while (true) {
        JSONObject params = new JSONObject();
        params.put("page", page);
        params.put("size", pageSize);
        
        JSONObject response = HttpUtils.get("https://api.example.com/users", params);
        JSONArray users = response.getJSONArray("data");
        
        if (users.isEmpty()) {
            break; // 没有更多数据
        }
        
        allUsers.addAll(users);
        page++;
    }
    
    return allUsers;
}
```

### API错误处理

```java
try {
    JSONObject response = HttpUtils.get("https://api.example.com/data");
    
    // 检查业务状态码
    Integer code = response.getInteger("code");
    if (code != null && code != 200) {
        String message = response.getString("message");
        throw new RuntimeException("API错误: " + message);
    }
    
    // 处理成功响应
    JSONArray data = response.getJSONArray("data");
    // ...
    
} catch (RuntimeException e) {
    if (e.getMessage().contains("HTTP请求失败")) {
        System.err.println("网络错误: " + e.getMessage());
    } else {
        System.err.println("请求处理错误: " + e.getMessage());
    }
}
```

## ⚙️ 配置说明

### 默认配置

- **连接超时**: 30秒
- **读取超时**: 30秒  
- **默认Content-Type**: `application/json;charset=UTF-8`
- **默认Accept**: `application/json`

### 网络配置

HttpUtils使用系统默认的网络配置，支持：
- HTTP/HTTPS协议
- 系统代理设置
- 自定义代理配置
- 标准HTTP状态码处理

## 📝 注意事项

- 所有请求方法都返回JSONObject，确保响应格式一致性
- HTTP错误状态码会抛出RuntimeException异常
- 自动处理URL编码，支持中文参数
- 支持代理配置，格式为`host:port`
- 连接和读取超时均为30秒，适合大多数API调用场景
- 非JSON响应会自动包装在`{"data": "原始内容"}`结构中 