package cn.zzzmh.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Properties;
import java.io.InputStream;

/**
 * MySQL工具类
 * 基于FastJSON2提供MySQL数据库操作
 * 
 * @author zzzmh
 * @since 1.0.0
 */
public class MySqlUtils {

    private static String host;
    private static int port;
    private static String username;
    private static String password;
    private static String database;
    private static Connection globalConnection;
    
    // 添加JVM关闭钩子，自动关闭连接
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            closeGlobalConnection(); // 使用统一的关闭方法
        }));
    }

    /**
     * 私有构造函数，防止实例化
     */
    private MySqlUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 初始化数据库连接（从配置文件读取参数）
     * 配置文件：resources/config.properties
     * 参数：url, username, password
     * 
     * Configuration file example:
     * url=jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
     * username=your_username
     * password=your_password
     * 
     * @return 数据库连接
     */
    public static Connection init() {
        try {
            Properties props = new Properties();
            InputStream inputStream = MySqlUtils.class.getClassLoader().getResourceAsStream("config.properties");
            
            if (inputStream == null) {
                throw new IllegalStateException(
                    "Configuration file not found: Please create 'config.properties' file in your project's 'src/main/resources/' directory.\n" +
                    "Example content:\n" +
                    "url=jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true\n" +
                    "username=your_username\n" +
                    "password=your_password"
                );
            }
            
            props.load(inputStream);
            inputStream.close();
            
            String url = props.getProperty("url");
            String username = props.getProperty("username");
            String password = props.getProperty("password");
            
            // 检查每个必需参数并给出具体提示
            if (url == null && username == null && password == null) {
                throw new IllegalArgumentException(
                    "Configuration file 'config.properties' is empty or missing required parameters.\n" +
                    "Please add the following properties:\n" +
                    "url=jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true\n" +
                    "username=your_username\n" +
                    "password=your_password"
                );
            }
            
            if (url == null) {
                throw new IllegalArgumentException(
                    "Missing required parameter 'url' in config.properties.\n" +
                    "Please add: url=jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
                );
            }
            
            if (username == null) {
                throw new IllegalArgumentException(
                    "Missing required parameter 'username' in config.properties.\n" +
                    "Please add: username=your_username"
                );
            }
            
            if (password == null) {
                throw new IllegalArgumentException(
                    "Missing required parameter 'password' in config.properties.\n" +
                    "Please add: password=your_password"
                );
            }
            
            // 确保配置文件的连接成为globalConnection（优先级最高）
            Connection connection = DriverManager.getConnection(url, username, password);
            
            // 强制设置为全局连接，即使之前已经初始化过
            parseUrlAndSetGlobalParams(url, username, password);
            globalConnection = connection;
            
            return connection;
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database connection from config.properties: " + e.getMessage(), e);
        }
    }

    /**
     * 初始化数据库连接参数并返回连接
     * 
     * @param host 数据库主机
     * @param port 数据库端口
     * @param username 用户名
     * @param password 密码
     * @param database 数据库名
     * @return 数据库连接
     */
    public static Connection init(String host, int port, String username, String password, String database) {
        String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", 
                                 host, port, database);
        return init(url, username, password);
    }

    /**
     * 初始化数据库连接参数并返回连接
     * 
     * @param url 数据库连接URL
     * @param username 用户名
     * @param password 密码
     * @return 数据库连接
     */
    public static Connection init(String url, String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            
            // 只在第一次init时设置全局连接和参数（从URL解析基础信息）
            if (globalConnection == null) {
                // 从URL中解析基础信息用于createConnection方法
                parseUrlAndSetGlobalParams(url, username, password);
                globalConnection = connection;
            }
            
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database connection. Please check your connection parameters: " + e.getMessage(), e);
        }
    }

    /**
     * 创建新的数据库连接
     * 
     * @return 数据库连接
     */
    public static Connection createConnection() {
        if (host == null || database == null) {
            throw new IllegalStateException(
                "Database parameters not initialized. Please call MySqlUtils.init() method first.\n" +
                "Options:\n" +
                "1. MySqlUtils.init() - read from config.properties file\n" +
                "2. MySqlUtils.init(host, port, username, password, database) - use parameters\n" +
                "3. MySqlUtils.init(url, username, password) - use JDBC URL"
            );
        }
        try {
            String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", 
                                     host, port, database);
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create database connection: " + e.getMessage(), e);
        }
    }

    // ========== 自定义SQL操作 ==========

    /**
     * 执行自定义查询SQL（使用全局连接）
     * 
     * @param sql 查询SQL语句
     * @param params 参数
     * @return 查询结果JSONArray
     */
    public static JSONArray executeQuery(String sql, Object... params) {
        ensureInitialized();
        return executeQuery(globalConnection, sql, params);
    }

    /**
     * 执行自定义查询SQL（使用全局连接，无参数）
     * 
     * @param sql 查询SQL语句
     * @return 查询结果JSONArray
     */
    public static JSONArray executeQuery(String sql) {
        ensureInitialized();
        return executeQuery(sql, new Object[0]);
    }

    /**
     * 执行自定义查询SQL（使用指定连接）
     * 
     * @param connection 数据库连接
     * @param sql 查询SQL语句
     * @param params 参数
     * @return 查询结果JSONArray
     */
    public static JSONArray executeQuery(Connection connection, String sql, Object... params) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                return resultSetToJSONArray(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("执行查询失败: " + sql, e);
        }
    }

    /**
     * 执行自定义查询SQL（使用指定连接，无参数）
     * 
     * @param connection 数据库连接
     * @param sql 查询SQL语句
     * @return 查询结果JSONArray
     */
    public static JSONArray executeQuery(Connection connection, String sql) {
        return executeQuery(connection, sql, new Object[0]);
    }

    /**
     * 执行自定义更新SQL（使用全局连接）
     * 
     * @param sql 更新SQL语句
     * @param params 参数
     * @return 影响的行数
     */
    public static int executeUpdate(String sql, Object... params) {
        ensureInitialized();
        return executeUpdate(globalConnection, sql, params);
    }

    /**
     * 执行自定义更新SQL（使用全局连接，无参数）
     * 
     * @param sql 更新SQL语句
     * @return 影响的行数
     */
    public static int executeUpdate(String sql) {
        ensureInitialized();
        return executeUpdate(sql, new Object[0]);
    }

    /**
     * 执行自定义更新SQL（使用指定连接）
     * 
     * @param connection 数据库连接
     * @param sql 更新SQL语句
     * @param params 参数
     * @return 影响的行数
     */
    public static int executeUpdate(Connection connection, String sql, Object... params) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("执行更新失败: " + sql, e);
        }
    }

    /**
     * 执行自定义更新SQL（使用指定连接，无参数）
     * 
     * @param connection 数据库连接
     * @param sql 更新SQL语句
     * @return 影响的行数
     */
    public static int executeUpdate(Connection connection, String sql) {
        return executeUpdate(connection, sql, new Object[0]);
    }

    /**
     * 执行自定义插入SQL并返回主键（使用全局连接）
     * 
     * @param sql 插入SQL语句
     * @param keyType 主键类型
     * @param params 参数
     * @param <T> 主键类型泛型
     * @return 生成的主键
     */
    public static <T> T executeInsert(String sql, Class<T> keyType, Object... params) {
        ensureInitialized();
        return executeInsert(globalConnection, sql, keyType, params);
    }

    /**
     * 执行自定义插入SQL并返回主键（使用全局连接，无参数）
     * 
     * @param sql 插入SQL语句
     * @param keyType 主键类型
     * @param <T> 主键类型泛型
     * @return 生成的主键
     */
    public static <T> T executeInsert(String sql, Class<T> keyType) {
        ensureInitialized();
        return executeInsert(sql, keyType, new Object[0]);
    }

    /**
     * 执行自定义插入SQL并返回主键（使用指定连接）
     * 
     * @param connection 数据库连接
     * @param sql 插入SQL语句
     * @param keyType 主键类型
     * @param params 参数
     * @param <T> 主键类型泛型
     * @return 生成的主键
     */
    @SuppressWarnings("unchecked")
    public static <T> T executeInsert(Connection connection, String sql, Class<T> keyType, Object... params) {
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Object key = generatedKeys.getObject(1);
                        return convertKey(key, keyType);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("执行插入失败: " + sql, e);
        }
        return null;
    }

    /**
     * 执行自定义插入SQL并返回主键（使用指定连接，无参数）
     * 
     * @param connection 数据库连接
     * @param sql 插入SQL语句
     * @param keyType 主键类型
     * @param <T> 主键类型泛型
     * @return 生成的主键
     */
    public static <T> T executeInsert(Connection connection, String sql, Class<T> keyType) {
        return executeInsert(connection, sql, keyType, new Object[0]);
    }

    // ========== 标准查询操作 ==========

    /**
     * 根据ID查询单条记录（使用全局连接）
     * 
     * @param tableName 表名
     * @param id 主键ID
     * @return 查询结果JSONObject
     */
    public static JSONObject selectById(String tableName, Object id) {
        ensureInitialized();
        return selectById(globalConnection, tableName, id);
    }

    /**
     * 根据ID查询单条记录（使用指定连接）
     * 
     * @param connection 数据库连接
     * @param tableName 表名
     * @param id 主键ID
     * @return 查询结果JSONObject
     */
    public static JSONObject selectById(Connection connection, String tableName, Object id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetToJSONObject(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询失败", e);
        }
        return null;
    }

    /**
     * 查询所有记录（使用全局连接）
     * 
     * @param tableName 表名
     * @return 查询结果JSONArray
     */
    public static JSONArray selectAll(String tableName) {
        ensureInitialized();
        return selectAll(globalConnection, tableName);
    }

    /**
     * 查询所有记录（使用指定连接）
     * 
     * @param connection 数据库连接
     * @param tableName 表名
     * @return 查询结果JSONArray
     */
    public static JSONArray selectAll(Connection connection, String tableName) {
        String sql = "SELECT * FROM " + tableName;
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return resultSetToJSONArray(rs);
        } catch (SQLException e) {
            throw new RuntimeException("查询失败", e);
        }
    }

    /**
     * 条件查询（使用全局连接）
     * 
     * @param tableName 表名
     * @param whereClause WHERE条件（不包含WHERE关键字）
     * @param params 参数数组
     * @return 查询结果JSONArray
     */
    public static JSONArray selectByCondition(String tableName, String whereClause, Object... params) {
        ensureInitialized();
        return selectByCondition(globalConnection, tableName, whereClause, params);
    }

    /**
     * 条件查询（使用全局连接，无参数）
     * 
     * @param tableName 表名
     * @param whereClause WHERE条件（不包含WHERE关键字）
     * @return 查询结果JSONArray
     */
    public static JSONArray selectByCondition(String tableName, String whereClause) {
        ensureInitialized();
        return selectByCondition(tableName, whereClause, new Object[0]);
    }

    /**
     * 条件查询（使用指定连接）
     * 
     * @param connection 数据库连接
     * @param tableName 表名
     * @param whereClause WHERE条件（不包含WHERE关键字）
     * @param params 参数数组
     * @return 查询结果JSONArray
     */
    public static JSONArray selectByCondition(Connection connection, String tableName, String whereClause, Object... params) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + whereClause;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                return resultSetToJSONArray(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询失败", e);
        }
    }

    /**
     * 条件查询（使用指定连接，无参数）
     * 
     * @param connection 数据库连接
     * @param tableName 表名
     * @param whereClause WHERE条件（不包含WHERE关键字）
     * @return 查询结果JSONArray
     */
    public static JSONArray selectByCondition(Connection connection, String tableName, String whereClause) {
        return selectByCondition(connection, tableName, whereClause, new Object[0]);
    }

    // ========== 泛型插入操作 ==========

    /**
     * 插入数据（使用全局连接）
     * 
     * @param tableName 表名
     * @param data 数据JSONObject
     * @param keyType 主键类型
     * @param <T> 主键类型泛型
     * @return 生成的主键
     */
    public static <T> T insert(String tableName, JSONObject data, Class<T> keyType) {
        ensureInitialized();
        return insert(globalConnection, tableName, data, keyType);
    }

    /**
     * 批量插入数据（使用全局连接）
     * 
     * @param tableName 表名
     * @param dataArray 数据JSONArray
     * @param keyType 主键类型
     * @param batchSize 批量大小
     * @param <T> 主键类型泛型
     * @return 生成的主键列表
     */
    public static <T> List<T> batchInsert(String tableName, JSONArray dataArray, Class<T> keyType, int batchSize) {
        ensureInitialized();
        return batchInsert(globalConnection, tableName, dataArray, keyType, batchSize);
    }

    /**
     * 批量插入数据（使用全局连接，默认批量大小1000）
     * 
     * @param tableName 表名
     * @param dataArray 数据JSONArray
     * @param keyType 主键类型
     * @param <T> 主键类型泛型
     * @return 生成的主键列表
     */
    public static <T> List<T> batchInsert(String tableName, JSONArray dataArray, Class<T> keyType) {
        return batchInsert(tableName, dataArray, keyType, 1000);
    }

    /**
     * 插入数据（使用指定连接）
     * 
     * @param connection 数据库连接
     * @param tableName 表名
     * @param data 数据JSONObject
     * @param keyType 主键类型
     * @param <T> 主键类型泛型
     * @return 生成的主键
     */
    @SuppressWarnings("unchecked")
    public static <T> T insert(Connection connection, String tableName, JSONObject data, Class<T> keyType) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("插入数据不能为空");
        }
        
        try {
            // 如果是String类型主键，自动生成UUID
            if (keyType == String.class) {
                List<String> primaryKeys = getPrimaryKeyColumns(connection, tableName);
                if (!primaryKeys.isEmpty()) {
                    String uuid = UUID.randomUUID().toString().replace("-", "");
                    data.put(primaryKeys.get(0), uuid);
                }
            }
            
            List<String> columns = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            
            for (String key : data.keySet()) {
                columns.add(key);
                values.add(data.get(key));
            }
            
            String sql = "INSERT INTO " + tableName + " (" + String.join(",", columns) + ") VALUES (" + 
                         String.join(",", columns.stream().map(c -> "?").toArray(String[]::new)) + ")";
            
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < values.size(); i++) {
                    stmt.setObject(i + 1, values.get(i));
                }
                
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    // 如果是String类型，直接返回生成的UUID
                    if (keyType == String.class) {
                        List<String> primaryKeys = getPrimaryKeyColumns(connection, tableName);
                        if (!primaryKeys.isEmpty()) {
                            return (T) data.getString(primaryKeys.get(0));
                        }
                    }
                    
                    // 其他类型从数据库获取
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            Object key = generatedKeys.getObject(1);
                            return convertKey(key, keyType);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("插入失败", e);
        }
        return null;
    }

    /**
     * 批量插入数据（使用指定连接）
     * 
     * @param connection 数据库连接
     * @param tableName 表名
     * @param dataArray 数据JSONArray
     * @param keyType 主键类型
     * @param batchSize 批量大小
     * @param <T> 主键类型泛型
     * @return 生成的主键列表
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> batchInsert(Connection connection, String tableName, JSONArray dataArray, Class<T> keyType, int batchSize) {
        if (dataArray == null || dataArray.isEmpty()) {
            throw new IllegalArgumentException("Batch insert data cannot be empty");
        }
        
        if (batchSize <= 0) {
            throw new IllegalArgumentException("Batch size must be greater than 0");
        }
        
        List<T> allKeys = new ArrayList<>();
        
        try {
            // 获取主键字段信息（用于String类型UUID生成）
            List<String> primaryKeys = null;
            if (keyType == String.class) {
                primaryKeys = getPrimaryKeyColumns(connection, tableName);
            }
            
            // 获取第一条数据的字段信息
            JSONObject firstData = dataArray.getJSONObject(0);
            if (firstData == null || firstData.isEmpty()) {
                throw new IllegalArgumentException("First data record cannot be empty");
            }
            
            List<String> columns = new ArrayList<>(firstData.keySet());
            
            // 构建SQL语句
            String sql = "INSERT INTO " + tableName + " (" + String.join(",", columns) + ") VALUES (" + 
                         String.join(",", columns.stream().map(c -> "?").toArray(String[]::new)) + ")";
            
            // 分批处理
            for (int i = 0; i < dataArray.size(); i += batchSize) {
                int endIndex = Math.min(i + batchSize, dataArray.size());
                List<T> batchKeys = processBatch(connection, sql, dataArray, i, endIndex, columns, primaryKeys, keyType);
                allKeys.addAll(batchKeys);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Batch insert failed: " + e.getMessage(), e);
        }
        
        return allKeys;
    }

    /**
     * 批量插入数据（使用指定连接，默认批量大小1000）
     * 
     * @param connection 数据库连接
     * @param tableName 表名
     * @param dataArray 数据JSONArray
     * @param keyType 主键类型
     * @param <T> 主键类型泛型
     * @return 生成的主键列表
     */
    public static <T> List<T> batchInsert(Connection connection, String tableName, JSONArray dataArray, Class<T> keyType) {
        return batchInsert(connection, tableName, dataArray, keyType, 1000);
    }

    /**
     * 处理单批数据插入
     */
    @SuppressWarnings("unchecked")
    private static <T> List<T> processBatch(Connection connection, String sql, JSONArray dataArray, 
                                          int startIndex, int endIndex, List<String> columns,
                                          List<String> primaryKeys, Class<T> keyType) throws SQLException {
        
        List<T> batchKeys = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // 添加批量数据
            for (int i = startIndex; i < endIndex; i++) {
                JSONObject data = dataArray.getJSONObject(i);
                if (data == null) {
                    continue; // 跳过空数据
                }
                
                // 为String类型主键生成UUID
                if (keyType == String.class && primaryKeys != null && !primaryKeys.isEmpty()) {
                    String uuid = UUID.randomUUID().toString().replace("-", "");
                    data.put(primaryKeys.get(0), uuid);
                }
                
                // 设置参数
                for (int j = 0; j < columns.size(); j++) {
                    String column = columns.get(j);
                    Object value = data.get(column);
                    stmt.setObject(j + 1, value);
                }
                
                stmt.addBatch();
            }
            
            // 执行批量插入
            int[] results = stmt.executeBatch();
            
            // 获取生成的主键
            if (keyType == String.class && primaryKeys != null && !primaryKeys.isEmpty()) {
                // String类型主键直接从数据中获取
                String primaryKeyColumn = primaryKeys.get(0);
                for (int i = startIndex; i < endIndex; i++) {
                    JSONObject data = dataArray.getJSONObject(i);
                    if (data != null && results[i - startIndex] > 0) {
                        T key = (T) data.getString(primaryKeyColumn);
                        batchKeys.add(key);
                    }
                }
            } else {
                // 其他类型主键从数据库获取
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    while (generatedKeys.next()) {
                        Object key = generatedKeys.getObject(1);
                        batchKeys.add(convertKey(key, keyType));
                    }
                }
            }
        }
        
        return batchKeys;
    }

    // ========== 更新操作 ==========

    /**
     * 根据ID更新数据（使用全局连接）
     * 
     * @param tableName 表名
     * @param id 主键ID
     * @param data 更新数据JSONObject
     * @return 是否更新成功
     */
    public static boolean updateById(String tableName, Object id, JSONObject data) {
        ensureInitialized();
        return updateById(globalConnection, tableName, id, data);
    }

    /**
     * 根据ID更新数据（使用指定连接）
     * 
     * @param connection 数据库连接
     * @param tableName 表名
     * @param id 主键ID
     * @param data 更新数据JSONObject
     * @return 是否更新成功
     */
    public static boolean updateById(Connection connection, String tableName, Object id, JSONObject data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("更新数据不能为空");
        }
        
        List<String> setParts = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        
        for (String key : data.keySet()) {
            setParts.add(key + " = ?");
            values.add(data.get(key));
        }
        values.add(id);
        
        String sql = "UPDATE " + tableName + " SET " + String.join(",", setParts) + " WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("更新失败", e);
        }
    }

    // ========== 删除操作 ==========

    /**
     * 根据ID删除数据（使用全局连接）
     * 
     * @param tableName 表名
     * @param id 主键ID
     * @return 是否删除成功
     */
    public static boolean deleteById(String tableName, Object id) {
        ensureInitialized();
        return deleteById(globalConnection, tableName, id);
    }

    /**
     * 根据ID删除数据（使用指定连接）
     * 
     * @param connection 数据库连接
     * @param tableName 表名
     * @param id 主键ID
     * @return 是否删除成功
     */
    public static boolean deleteById(Connection connection, String tableName, Object id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("删除失败", e);
        }
    }

    // ========== 事务支持 ==========

    /**
     * 事务回调接口（无返回值）
     */
    @FunctionalInterface
    public interface TransactionCallback {
        void execute(Connection connection) throws Exception;
    }

    /**
     * 事务函数接口（有返回值）
     */
    @FunctionalInterface
    public interface TransactionFunction<T> {
        T execute(Connection connection) throws Exception;
    }

    /**
     * 在事务中执行操作（无返回值）
     * 
     * @param callback 事务回调
     */
    public static void executeInTransaction(TransactionCallback callback) {
        Connection conn = null;
        try {
            conn = createConnection();
            conn.setAutoCommit(false); // 开启事务
            
            callback.execute(conn); // 执行用户业务逻辑
            
            conn.commit(); // 提交事务
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // 回滚事务
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Transaction rollback failed: " + rollbackEx.getMessage(), rollbackEx);
                }
            }
            throw new RuntimeException("Transaction execution failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // 恢复自动提交
                    conn.close(); // 关闭连接
                } catch (SQLException closeEx) {
                    // 静默处理关闭异常
                }
            }
        }
    }

    /**
     * 在事务中执行操作（有返回值）
     * 
     * @param function 事务函数
     * @param <T> 返回值类型
     * @return 执行结果
     */
    public static <T> T executeInTransaction(TransactionFunction<T> function) {
        Connection conn = null;
        try {
            conn = createConnection();
            conn.setAutoCommit(false); // 开启事务
            
            T result = function.execute(conn); // 执行用户业务逻辑并获取结果
            
            conn.commit(); // 提交事务
            return result;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // 回滚事务
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Transaction rollback failed: " + rollbackEx.getMessage(), rollbackEx);
                }
            }
            throw new RuntimeException("Transaction execution failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // 恢复自动提交
                    conn.close(); // 关闭连接
                } catch (SQLException closeEx) {
                    // 静默处理关闭异常
                }
            }
        }
    }

    // ========== 辅助方法 ==========

    /**
     * 确保数据库连接已初始化（懒加载）
     */
    private static void ensureInitialized() {
        if (globalConnection == null) {
            try {
                // 尝试自动从配置文件初始化
                init();
            } catch (RuntimeException e) {
                throw new RuntimeException(
                    "Database connection not initialized and auto-initialization failed.\n" +
                    "Please either:\n" +
                    "1. Call MySqlUtils.init() explicitly, or\n" +
                    "2. Create 'config.properties' file in 'src/main/resources/' with:\n" +
                    "   url=jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true\n" +
                    "   username=your_username\n" +
                    "   password=your_password\n\n" +
                    "Original error: " + e.getMessage(), e
                );
            }
        }
    }

    /**
     * 从URL解析并设置全局参数
     */
    private static void parseUrlAndSetGlobalParams(String url, String username, String password) {
        try {
            // 简单解析URL，提取基本信息
            // 例如：jdbc:mysql://localhost:3306/testdb?...
            String[] parts = url.split("//")[1].split("/");
            String hostPort = parts[0];
            String databasePart = parts.length > 1 ? parts[1].split("\\?")[0] : "";
            
            String[] hostPortArray = hostPort.split(":");
            MySqlUtils.host = hostPortArray[0];
            MySqlUtils.port = hostPortArray.length > 1 ? Integer.parseInt(hostPortArray[1]) : 3306;
            MySqlUtils.database = databasePart;
            MySqlUtils.username = username;
            MySqlUtils.password = password;
        } catch (Exception e) {
            // 解析失败时设置默认值
            MySqlUtils.host = "localhost";
            MySqlUtils.port = 3306;
            MySqlUtils.database = "";
            MySqlUtils.username = username;
            MySqlUtils.password = password;
        }
    }

    /**
     * 获取表的主键列名
     */
    private static List<String> getPrimaryKeyColumns(Connection connection, String tableName) throws SQLException {
        List<String> primaryKeys = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet rs = metaData.getPrimaryKeys(database, null, tableName)) {
            while (rs.next()) {
                primaryKeys.add(rs.getString("COLUMN_NAME"));
            }
        }
        return primaryKeys;
    }

    /**
     * 转换主键类型
     */
    @SuppressWarnings("unchecked")
    private static <T> T convertKey(Object key, Class<T> keyType) {
        if (key == null) {
            return null;
        }
        
        if (keyType == Long.class) {
            return (T) Long.valueOf(key.toString());
        } else if (keyType == Integer.class) {
            return (T) Integer.valueOf(key.toString());
        } else if (keyType == String.class) {
            return (T) key.toString();
        } else {
            return (T) key;
        }
    }

    /**
     * ResultSet转JSONObject，处理时间戳问题
     */
    private static JSONObject resultSetToJSONObject(ResultSet rs) throws SQLException {
        JSONObject json = new JSONObject();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnLabel(i);
            Object value = rs.getObject(i);
            
            // 处理时间类型，转换为时间戳
            if (value instanceof Timestamp) {
                json.put(columnName, ((Timestamp) value).getTime());
            } else if (value instanceof java.sql.Date) {
                json.put(columnName, ((java.sql.Date) value).getTime());
            } else if (value instanceof java.sql.Time) {
                json.put(columnName, ((java.sql.Time) value).getTime());
            } else if (value instanceof LocalDateTime) {
                json.put(columnName, ((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            } else {
                json.put(columnName, value);
            }
        }
        return json;
    }

    /**
     * ResultSet转JSONArray
     */
    private static JSONArray resultSetToJSONArray(ResultSet rs) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        while (rs.next()) {
            jsonArray.add(resultSetToJSONObject(rs));
        }
        return jsonArray;
    }

    /**
     * 关闭指定连接（静默关闭，不抛出异常）
     * 
     * @param connection 要关闭的数据库连接
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                // 静默处理关闭异常
            }
        }
    }

    /**
     * 关闭全局连接（静默关闭，不抛出异常）
     */
    public static void closeGlobalConnection() {
        if (globalConnection != null) {
            try {
                if (!globalConnection.isClosed()) {
                    globalConnection.close();
                }
                globalConnection = null; // 置空引用
            } catch (Exception e) {
                // 静默处理关闭异常
            }
        }
    }
} 