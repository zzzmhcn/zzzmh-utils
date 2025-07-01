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

### 🔷 Binary POST Requests

```java
// Request API that may return binary data
JSONObject requestData = new JSONObject();
requestData.put("fileId", "12345");
requestData.put("format", "png");

JSONObject headers = new JSONObject();
headers.put("Authorization", "Bearer your-token");

// Use postForBinary method to handle potential binary responses
JSONObject response = HttpUtils.postForBinary("https://api.example.com/generate-image", 
                                             requestData, headers);

// Smart response handling
if (response.getBooleanValue("isBinary")) {
    // Handle binary response (e.g., generated image)
    String base64Image = response.getString("base64");
    String contentType = response.getString("contentType");
    System.out.println("Generated " + contentType + " file");
} else {
    // Handle JSON response (e.g., error message)
    String message = response.getString("message");
    System.out.println("API returned: " + message);
}
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

## 🔄 Response Handling

### Automatic JSON Parsing

```java
// Successful responses are automatically parsed as JSONObject
JSONObject response = HttpUtils.get("https://api.example.com/user/123");

// Access response data
String userName = response.getString("name");
Integer userAge = response.getInteger("age");
JSONArray hobbies = response.getJSONArray("hobbies");

// Handle nested objects
JSONObject profile = response.getJSONObject("profile");
String avatar = profile.getString("avatar");
```

### Non-JSON Response Handling

```java
// If response is not JSON format, it will be automatically wrapped
JSONObject response = HttpUtils.get("https://api.example.com/plain-text");

// Original content is in the data field
String plainText = response.getString("data");
```

### 🔷 Binary Response Handling

HttpUtils provides intelligent binary response handling that can automatically identify and process images, audio, video, PDF and other binary content.

```java
// Download image file
JSONObject headers = new JSONObject();
headers.put("Authorization", "Bearer your-token");

JSONObject response = HttpUtils.postForBinary("https://api.example.com/files/download", 
                                             requestBody, headers);

// Check if it's a binary response
if (response.getBooleanValue("isBinary")) {
    String contentType = response.getString("contentType");
    Integer fileSize = response.getInteger("size");
    String base64Data = response.getString("base64");
    String dataUrl = response.getString("dataUrl");
    
    System.out.println("File type: " + contentType);
    System.out.println("File size: " + fileSize + " bytes");
    
    // Can directly use dataUrl in HTML
    // <img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA...">
    
    // Or save base64 data as file
    byte[] fileBytes = java.util.Base64.getDecoder().decode(base64Data);
    java.nio.file.Files.write(java.nio.file.Paths.get("downloaded_file.png"), fileBytes);
}
```

#### Supported Binary File Types

- **Image files**: `image/*` (PNG, JPEG, GIF, WebP, etc.)
- **Audio files**: `audio/*` (MP3, WAV, OGG, etc.)  
- **Video files**: `video/*` (MP4, AVI, MOV, etc.)
- **PDF documents**: `application/pdf`
- **Generic binary**: `application/octet-stream`
- **Other content types containing "binary" keyword**

#### Smart Response Recognition

HttpUtils automatically determines whether content is binary based on the `Content-Type` response header:

```java
// Generic method with smart response type recognition
JSONObject response = HttpUtils.requestForBinary("GET", 
                                                "https://api.example.com/resource", 
                                                headers, null, null);

if (response.getBooleanValue("isBinary")) {
    // Handle binary data
    handleBinaryResponse(response);
} else {
    // Handle JSON data
    handleJsonResponse(response);
}
```

#### Binary Response Data Structure

Binary responses return a JSONObject with the following fields:

```json
{
  "isBinary": true,
  "contentType": "image/png",
  "size": 15234,
  "base64": "iVBORw0KGgoAAAANSUhEUgAA...",
  "dataUrl": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA..."
}
```

- `isBinary`: Boolean value indicating this is a binary response
- `contentType`: Original Content-Type response header
- `size`: File size in bytes
- `base64`: Base64-encoded file content
- `dataUrl`: Data URL format ready for HTML use

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