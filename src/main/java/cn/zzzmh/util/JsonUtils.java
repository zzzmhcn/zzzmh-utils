package cn.zzzmh.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * JSON增强工具类
 * 基于FastJSON2提供JSON路径查询、合并、转换等高级功能
 * 
 * @author zzzmh
 * @since 1.0.0
 */
public class JsonUtils {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 私有构造函数，防止实例化
     */
    private JsonUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ==================== 基础转换 ====================

    /**
     * 对象转JSON字符串
     * 
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJsonString(Object obj) {
        if (obj == null) {
            return "null";
        }
        return JSON.toJSONString(obj);
    }

    /**
     * 对象转JSON字符串（美化格式）
     * 
     * @param obj 对象
     * @return 美化后的JSON字符串
     */
    public static String toJsonStringPretty(Object obj) {
        if (obj == null) {
            return "null";
        }
        return JSON.toJSONString(obj, PrettyFormat);
    }

    /**
     * JSON字符串转JSONObject
     * 
     * @param jsonString JSON字符串
     * @return JSONObject
     */
    public static JSONObject parseObject(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return new JSONObject();
        }
        try {
            return JSON.parseObject(jsonString);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON string to JSONObject: " + jsonString, e);
        }
    }

    /**
     * JSON字符串转JSONArray
     * 
     * @param jsonString JSON字符串
     * @return JSONArray
     */
    public static JSONArray parseArray(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return new JSONArray();
        }
        try {
            return JSON.parseArray(jsonString);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON string to JSONArray: " + jsonString, e);
        }
    }

    /**
     * JSON字符串转指定类型对象
     * 
     * @param jsonString JSON字符串
     * @param clazz 目标类型
     * @param <T> 泛型类型
     * @return 转换后的对象
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON string cannot be null or empty");
        }
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        try {
            return JSON.parseObject(jsonString, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON string to " + clazz.getSimpleName() + ": " + jsonString, e);
        }
    }

    // ==================== JSON路径查询 ====================

    /**
     * 根据JSON路径获取值
     * 
     * @param json JSON对象或数组
     * @param path JSON路径（如：$.user.name、$[0].id）
     * @return 查询结果
     */
    public static Object getByPath(Object json, String path) {
        if (json == null) {
            return null;
        }
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
        try {
            return JSONPath.eval(json, path);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get value by path: " + path, e);
        }
    }

    /**
     * 根据JSON路径获取字符串值
     * 
     * @param json JSON对象或数组
     * @param path JSON路径
     * @return 字符串值
     */
    public static String getStringByPath(Object json, String path) {
        Object value = getByPath(json, path);
        return value != null ? value.toString() : null;
    }

    /**
     * 根据JSON路径获取整数值
     * 
     * @param json JSON对象或数组
     * @param path JSON路径
     * @return 整数值
     */
    public static Integer getIntegerByPath(Object json, String path) {
        Object value = getByPath(json, path);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot convert value to Integer: " + value, e);
        }
    }

    /**
     * 根据JSON路径获取长整数值
     * 
     * @param json JSON对象或数组
     * @param path JSON路径
     * @return 长整数值
     */
    public static Long getLongByPath(Object json, String path) {
        Object value = getByPath(json, path);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot convert value to Long: " + value, e);
        }
    }

    /**
     * 根据JSON路径获取布尔值
     * 
     * @param json JSON对象或数组
     * @param path JSON路径
     * @return 布尔值
     */
    public static Boolean getBooleanByPath(Object json, String path) {
        Object value = getByPath(json, path);
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return Boolean.parseBoolean(value.toString());
    }

    /**
     * 根据JSON路径设置值
     * 
     * @param json JSON对象
     * @param path JSON路径
     * @param value 要设置的值
     */
    public static void setByPath(JSONObject json, String path, Object value) {
        if (json == null) {
            throw new IllegalArgumentException("JSON object cannot be null");
        }
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
        try {
            JSONPath.set(json, path, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set value by path: " + path, e);
        }
    }

    /**
     * 根据JSON路径删除值
     * 
     * @param json JSON对象
     * @param path JSON路径
     * @return 是否删除成功
     */
    public static boolean removeByPath(JSONObject json, String path) {
        if (json == null) {
            throw new IllegalArgumentException("JSON object cannot be null");
        }
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Path cannot be null or empty");
        }
        try {
            JSONPath.remove(json, path);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove value by path: " + path, e);
        }
    }

    // ==================== JSON合并 ====================

    /**
     * 深度合并两个JSONObject（target中的值会被source覆盖）
     * 
     * @param target 目标对象
     * @param source 源对象
     * @return 合并后的新对象
     */
    public static JSONObject merge(JSONObject target, JSONObject source) {
        if (target == null && source == null) {
            return new JSONObject();
        }
        if (target == null) {
            return deepCopy(source);
        }
        if (source == null) {
            return deepCopy(target);
        }
        
        JSONObject result = deepCopy(target);
        mergeRecursive(result, source);
        return result;
    }

    /**
     * 递归合并JSON对象
     */
    private static void mergeRecursive(JSONObject target, JSONObject source) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = entry.getKey();
            Object sourceValue = entry.getValue();
            Object targetValue = target.get(key);
            
            if (sourceValue instanceof JSONObject && targetValue instanceof JSONObject) {
                mergeRecursive((JSONObject) targetValue, (JSONObject) sourceValue);
            } else {
                target.put(key, deepCopyValue(sourceValue));
            }
        }
    }

    /**
     * 合并多个JSONObject
     * 
     * @param jsonObjects JSON对象数组（后面的会覆盖前面的）
     * @return 合并后的对象
     */
    public static JSONObject mergeMultiple(JSONObject... jsonObjects) {
        if (jsonObjects == null || jsonObjects.length == 0) {
            return new JSONObject();
        }
        
        JSONObject result = new JSONObject();
        for (JSONObject json : jsonObjects) {
            if (json != null) {
                result = merge(result, json);
            }
        }
        return result;
    }

    // ==================== JSON拷贝 ====================

    /**
     * 深度拷贝JSONObject
     * 
     * @param json 源JSON对象
     * @return 拷贝后的新对象
     */
    public static JSONObject deepCopy(JSONObject json) {
        if (json == null) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(json));
    }

    /**
     * 深度拷贝JSONArray
     * 
     * @param json 源JSON数组
     * @return 拷贝后的新数组
     */
    public static JSONArray deepCopy(JSONArray json) {
        if (json == null) {
            return null;
        }
        return JSON.parseArray(JSON.toJSONString(json));
    }

    /**
     * 深度拷贝值
     */
    private static Object deepCopyValue(Object value) {
        if (value instanceof JSONObject) {
            return deepCopy((JSONObject) value);
        } else if (value instanceof JSONArray) {
            return deepCopy((JSONArray) value);
        } else {
            return value;
        }
    }

    // ==================== JSON比较 ====================

    /**
     * 比较两个JSON对象是否相等（深度比较）
     * 
     * @param json1 JSON对象1
     * @param json2 JSON对象2
     * @return 是否相等
     */
    public static boolean equals(JSONObject json1, JSONObject json2) {
        if (json1 == json2) {
            return true;
        }
        if (json1 == null || json2 == null) {
            return false;
        }
        return json1.toString().equals(json2.toString());
    }

    /**
     * 比较两个JSON数组是否相等（深度比较）
     * 
     * @param json1 JSON数组1
     * @param json2 JSON数组2
     * @return 是否相等
     */
    public static boolean equals(JSONArray json1, JSONArray json2) {
        if (json1 == json2) {
            return true;
        }
        if (json1 == null || json2 == null) {
            return false;
        }
        return json1.toString().equals(json2.toString());
    }

    /**
     * 获取两个JSON对象的差异键集合
     * 
     * @param json1 JSON对象1
     * @param json2 JSON对象2
     * @return 差异键集合
     */
    public static Set<String> getDifferentKeys(JSONObject json1, JSONObject json2) {
        if (json1 == null && json2 == null) {
            return new HashSet<>();
        }
        if (json1 == null) {
            return new HashSet<>(json2.keySet());
        }
        if (json2 == null) {
            return new HashSet<>(json1.keySet());
        }
        
        Set<String> differentKeys = new HashSet<>();
        Set<String> allKeys = new HashSet<>(json1.keySet());
        allKeys.addAll(json2.keySet());
        
        for (String key : allKeys) {
            Object value1 = json1.get(key);
            Object value2 = json2.get(key);
            
            if (!Objects.equals(value1, value2)) {
                differentKeys.add(key);
            }
        }
        
        return differentKeys;
    }

    // ==================== JSON转换 ====================

    /**
     * JSONArray转List
     * 
     * @param jsonArray JSON数组
     * @param clazz 元素类型
     * @param <T> 泛型类型
     * @return List
     */
    public static <T> List<T> toList(JSONArray jsonArray, Class<T> clazz) {
        if (jsonArray == null) {
            return new ArrayList<>();
        }
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        try {
            return jsonArray.toJavaList(clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSONArray to List<" + clazz.getSimpleName() + ">", e);
        }
    }

    /**
     * List转JSONArray
     * 
     * @param list 列表
     * @return JSONArray
     */
    public static JSONArray fromList(List<?> list) {
        if (list == null) {
            return new JSONArray();
        }
        return new JSONArray(list);
    }

    /**
     * Map转JSONObject
     * 
     * @param map Map对象
     * @return JSONObject
     */
    public static JSONObject fromMap(Map<String, Object> map) {
        if (map == null) {
            return new JSONObject();
        }
        return new JSONObject(map);
    }

    /**
     * JSONObject转Map
     * 
     * @param json JSON对象
     * @return Map
     */
    public static Map<String, Object> toMap(JSONObject json) {
        if (json == null) {
            return new HashMap<>();
        }
        return new HashMap<>(json);
    }

    // ==================== JSON验证 ====================

    /**
     * 验证字符串是否为有效的JSON
     * 
     * @param jsonString JSON字符串
     * @return 是否有效
     */
    public static boolean isValidJson(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return false;
        }
        try {
            JSON.parse(jsonString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证字符串是否为有效的JSONObject
     * 
     * @param jsonString JSON字符串
     * @return 是否有效
     */
    public static boolean isValidJsonObject(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return false;
        }
        try {
            Object parsed = JSON.parse(jsonString);
            return parsed instanceof JSONObject;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证字符串是否为有效的JSONArray
     * 
     * @param jsonString JSON字符串
     * @return 是否有效
     */
    public static boolean isValidJsonArray(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return false;
        }
        try {
            Object parsed = JSON.parse(jsonString);
            return parsed instanceof JSONArray;
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== JSON模板 ====================

    /**
     * 使用数据填充JSON模板
     * 
     * @param template 模板字符串（支持${key}占位符）
     * @param data 数据对象
     * @return 填充后的JSON字符串
     */
    public static String fillTemplate(String template, JSONObject data) {
        if (template == null) {
            throw new IllegalArgumentException("Template cannot be null");
        }
        if (data == null) {
            data = new JSONObject();
        }
        
        String result = template;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }
        return result;
    }

    /**
     * 从JSON对象提取指定字段创建新对象
     * 
     * @param json 源JSON对象
     * @param fields 要提取的字段名数组
     * @return 新的JSON对象
     */
    public static JSONObject extract(JSONObject json, String... fields) {
        if (json == null) {
            return new JSONObject();
        }
        if (fields == null || fields.length == 0) {
            return new JSONObject();
        }
        
        JSONObject result = new JSONObject();
        for (String field : fields) {
            if (json.containsKey(field)) {
                result.put(field, json.get(field));
            }
        }
        return result;
    }

    /**
     * 从JSON对象排除指定字段创建新对象
     * 
     * @param json 源JSON对象
     * @param fields 要排除的字段名数组
     * @return 新的JSON对象
     */
    public static JSONObject exclude(JSONObject json, String... fields) {
        if (json == null) {
            return new JSONObject();
        }
        if (fields == null || fields.length == 0) {
            return deepCopy(json);
        }
        
        JSONObject result = deepCopy(json);
        for (String field : fields) {
            result.remove(field);
        }
        return result;
    }

    // ==================== JSON压缩和美化 ====================

    /**
     * 压缩JSON字符串（移除空白字符）
     * 
     * @param jsonString JSON字符串
     * @return 压缩后的字符串
     */
    public static String compress(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return jsonString;
        }
        try {
            Object parsed = JSON.parse(jsonString);
            return JSON.toJSONString(parsed);
        } catch (Exception e) {
            throw new RuntimeException("Failed to compress JSON string", e);
        }
    }

    /**
     * 美化JSON字符串（格式化输出）
     * 
     * @param jsonString JSON字符串
     * @return 美化后的字符串
     */
    public static String prettify(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return jsonString;
        }
        try {
            Object parsed = JSON.parse(jsonString);
            return JSON.toJSONString(parsed, PrettyFormat);
        } catch (Exception e) {
            throw new RuntimeException("Failed to prettify JSON string", e);
        }
    }

    // ==================== 批量处理 ====================

    /**
     * 批量处理JSONArray中的每个元素
     * 
     * @param jsonArray JSON数组
     * @param processor 处理函数
     * @return 处理后的新数组
     */
    public static JSONArray processArray(JSONArray jsonArray, Function<Object, Object> processor) {
        if (jsonArray == null) {
            return new JSONArray();
        }
        if (processor == null) {
            throw new IllegalArgumentException("Processor cannot be null");
        }
        
        JSONArray result = new JSONArray();
        for (Object item : jsonArray) {
            result.add(processor.apply(item));
        }
        return result;
    }

    /**
     * 过滤JSONArray中的元素
     * 
     * @param jsonArray JSON数组
     * @param path JSON路径
     * @param expectedValue 期望值
     * @return 过滤后的新数组
     */
    public static JSONArray filter(JSONArray jsonArray, String path, Object expectedValue) {
        if (jsonArray == null) {
            return new JSONArray();
        }
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        
        JSONArray result = new JSONArray();
        for (Object item : jsonArray) {
            try {
                Object value = JSONPath.eval(item, path);
                if (Objects.equals(value, expectedValue)) {
                    result.add(item);
                }
            } catch (Exception e) {
                // 忽略路径不存在的情况
            }
        }
        return result;
    }

    /**
     * 对JSONArray进行分组
     * 
     * @param jsonArray JSON数组
     * @param path 分组字段路径
     * @return 分组后的Map
     */
    public static Map<String, JSONArray> groupBy(JSONArray jsonArray, String path) {
        if (jsonArray == null) {
            return new HashMap<>();
        }
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        
        Map<String, JSONArray> groups = new HashMap<>();
        for (Object item : jsonArray) {
            try {
                Object value = JSONPath.eval(item, path);
                String key = value != null ? value.toString() : "null";
                groups.computeIfAbsent(key, k -> new JSONArray()).add(item);
            } catch (Exception e) {
                // 忽略路径不存在的情况
                groups.computeIfAbsent("undefined", k -> new JSONArray()).add(item);
            }
        }
        return groups;
    }

    // ==================== 类型安全获取 ====================

    /**
     * 安全获取字符串值（提供默认值）
     * 
     * @param json JSON对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 字符串值
     */
    public static String getString(JSONObject json, String key, String defaultValue) {
        if (json == null || !json.containsKey(key)) {
            return defaultValue;
        }
        Object value = json.get(key);
        return value != null ? value.toString() : defaultValue;
    }

    /**
     * 安全获取整数值（提供默认值）
     * 
     * @param json JSON对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 整数值
     */
    public static Integer getInteger(JSONObject json, String key, Integer defaultValue) {
        if (json == null || !json.containsKey(key)) {
            return defaultValue;
        }
        try {
            return json.getInteger(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 安全获取长整数值（提供默认值）
     * 
     * @param json JSON对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 长整数值
     */
    public static Long getLong(JSONObject json, String key, Long defaultValue) {
        if (json == null || !json.containsKey(key)) {
            return defaultValue;
        }
        try {
            return json.getLong(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 安全获取布尔值（提供默认值）
     * 
     * @param json JSON对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 布尔值
     */
    public static Boolean getBoolean(JSONObject json, String key, Boolean defaultValue) {
        if (json == null || !json.containsKey(key)) {
            return defaultValue;
        }
        try {
            return json.getBoolean(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }
} 