package cn.zzzmh.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP工具类
 * 基于HttpURLConnection实现，专注JSON数据处理
 * 
 * @author zzzmh
 * @since 1.0.0
 */
public class HttpUtils {

    private static final int DEFAULT_TIMEOUT = 30000; // 30秒
    private static final String DEFAULT_CONTENT_TYPE = "application/json;charset=UTF-8";

    /**
     * 私有构造函数，防止实例化
     */
    private HttpUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * GET请求
     * 
     * @param url 请求URL
     * @return 响应JSON对象
     */
    public static JSONObject get(String url) {
        return get(url, null, null, null);
    }

    /**
     * GET请求（带参数）
     * 
     * @param url 请求URL
     * @param params 查询参数
     * @return 响应JSON对象
     */
    public static JSONObject get(String url, JSONObject params) {
        return get(url, params, null, null);
    }

    /**
     * GET请求（带参数和请求头）
     * 
     * @param url 请求URL
     * @param params 查询参数
     * @param headers 请求头
     * @return 响应JSON对象
     */
    public static JSONObject get(String url, JSONObject params, JSONObject headers) {
        return get(url, params, headers, null);
    }

    /**
     * GET请求（完整参数）
     * 
     * @param url 请求URL
     * @param params 查询参数
     * @param headers 请求头
     * @param proxy 代理设置（格式：host:port）
     * @return 响应JSON对象
     */
    public static JSONObject get(String url, JSONObject params, JSONObject headers, String proxy) {
        return request("GET", url, params, headers, null, proxy);
    }

    /**
     * POST请求
     * 
     * @param url 请求URL
     * @param body 请求体
     * @return 响应JSON对象
     */
    public static JSONObject post(String url, JSONObject body) {
        return post(url, body, null, null);
    }

    /**
     * POST请求（带请求头）
     * 
     * @param url 请求URL
     * @param body 请求体
     * @param headers 请求头
     * @return 响应JSON对象
     */
    public static JSONObject post(String url, JSONObject body, JSONObject headers) {
        return post(url, body, headers, null);
    }

    /**
     * POST请求（完整参数）
     * 
     * @param url 请求URL
     * @param body 请求体
     * @param headers 请求头
     * @param proxy 代理设置（格式：host:port）
     * @return 响应JSON对象
     */
    public static JSONObject post(String url, JSONObject body, JSONObject headers, String proxy) {
        return request("POST", url, null, headers, body, proxy);
    }

    /**
     * PUT请求
     * 
     * @param url 请求URL
     * @param body 请求体
     * @return 响应JSON对象
     */
    public static JSONObject put(String url, JSONObject body) {
        return request("PUT", url, null, null, body, null);
    }

    /**
     * DELETE请求
     * 
     * @param url 请求URL
     * @return 响应JSON对象
     */
    public static JSONObject delete(String url) {
        return request("DELETE", url, null, null, null, null);
    }

    /**
     * 通用HTTP请求
     * 
     * @param method 请求方法
     * @param url 请求URL
     * @param params 查询参数（仅用于GET等方法）
     * @param headers 请求头
     * @param body 请求体（用于POST、PUT等方法）
     * @param proxy 代理设置（格式：host:port）
     * @return 响应JSON对象
     */
    public static JSONObject request(String method, String url, JSONObject params, 
                                   JSONObject headers, JSONObject body, String proxy) {
        HttpURLConnection connection = null;
        try {
            // 构建完整URL（添加查询参数）
            String fullUrl = buildUrl(url, params);
            URL urlObj = new URL(fullUrl);
            
            // 设置代理
            if (proxy != null && !proxy.trim().isEmpty()) {
                String[] proxyParts = proxy.split(":");
                if (proxyParts.length == 2) {
                    Proxy proxyObj = new Proxy(Proxy.Type.HTTP, 
                        new InetSocketAddress(proxyParts[0], Integer.parseInt(proxyParts[1])));
                    connection = (HttpURLConnection) urlObj.openConnection(proxyObj);
                } else {
                    connection = (HttpURLConnection) urlObj.openConnection();
                }
            } else {
                connection = (HttpURLConnection) urlObj.openConnection();
            }

            // 设置请求方法
            connection.setRequestMethod(method.toUpperCase());
            
            // 设置超时
            connection.setConnectTimeout(DEFAULT_TIMEOUT);
            connection.setReadTimeout(DEFAULT_TIMEOUT);
            
            // 设置默认请求头
            connection.setRequestProperty("Content-Type", DEFAULT_CONTENT_TYPE);
            connection.setRequestProperty("Accept", "application/json");
            
            // 设置自定义请求头
            if (headers != null) {
                for (Map.Entry<String, Object> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            
            // 发送请求体
            if (body != null && ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method))) {
                connection.setDoOutput(true);
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = body.toJSONString().getBytes(StandardCharsets.UTF_8);
                    os.write(input);
                }
            }
            
            // 获取响应
            int responseCode = connection.getResponseCode();
            String responseBody = readResponse(connection, responseCode);
            
            // 处理响应
            if (responseCode >= 200 && responseCode < 300) {
                // 成功响应，尝试解析JSON
                try {
                    return JSON.parseObject(responseBody);
                } catch (Exception e) {
                    // 如果不是JSON格式，包装成JSON返回
                    JSONObject result = new JSONObject();
                    result.put("data", responseBody);
                    return result;
                }
                         } else {
                 // HTTP错误
                 throw new RuntimeException("HTTP请求失败: " + responseCode + " - " + responseBody);
             }
             
         } catch (RuntimeException e) {
             throw e;
         } catch (MalformedURLException e) {
             throw new RuntimeException("URL格式错误: " + e.getMessage(), e);
         } catch (ConnectException e) {
             throw new RuntimeException("连接超时: " + e.getMessage(), e);
         } catch (SocketTimeoutException e) {
             throw new RuntimeException("读取超时: " + e.getMessage(), e);
         } catch (Exception e) {
             throw new RuntimeException("HTTP请求异常: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 构建完整URL（添加查询参数）
     */
    private static String buildUrl(String url, JSONObject params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        
        StringBuilder sb = new StringBuilder(url);
        boolean hasQuery = url.contains("?");
        
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (hasQuery) {
                sb.append("&");
            } else {
                sb.append("?");
                hasQuery = true;
            }
            
            try {
                sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()))
                  .append("=")
                  .append(URLEncoder.encode(String.valueOf(entry.getValue()), StandardCharsets.UTF_8.name()));
                         } catch (UnsupportedEncodingException e) {
                 // UTF-8编码异常，理论上不会发生
                 throw new RuntimeException("URL编码异常", e);
             }
        }
        
        return sb.toString();
    }

    /**
     * 读取响应内容
     */
    private static String readResponse(HttpURLConnection connection, int responseCode) throws IOException {
        InputStream inputStream;
        if (responseCode >= 200 && responseCode < 300) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
            if (inputStream == null) {
                inputStream = connection.getInputStream();
            }
        }
        
        if (inputStream == null) {
            return "";
        }
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
            return response.toString().trim();
        }
    }
} 