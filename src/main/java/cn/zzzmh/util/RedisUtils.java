package cn.zzzmh.util;

import com.alibaba.fastjson2.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.InputStream;
import java.util.Properties;

/**
 * Redis工具类
 * 基于Jedis和FastJSON2提供Redis操作
 * 
 * @author zzzmh
 * @since 1.0.0
 */
public class RedisUtils {

    private static String host;
    private static int port;
    private static String password;
    private static int database;
    private static JedisPool jedisPool;

    // 添加JVM关闭钩子，自动关闭连接池
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            closePool();
        }));
    }

    /**
     * 私有构造函数，防止实例化
     */
    private RedisUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

         /**
      * 初始化Redis连接（从配置文件读取参数）
      * 配置文件：resources/config.properties
      * 参数：redis.host, redis.port, redis.password, redis.database, redis.pool.*
      * 
      * Configuration file example:
      * redis.host=localhost
      * redis.port=6379
      * redis.password=
      * redis.database=0
      * redis.pool.maxTotal=200
      * redis.pool.maxIdle=50
      * redis.pool.minIdle=10
      * redis.pool.timeout=3000
      * 
      * @return Jedis连接池
      */
     public static JedisPool init() {
         try {
             Properties props = new Properties();
             InputStream inputStream = RedisUtils.class.getClassLoader().getResourceAsStream("config.properties");
             
             if (inputStream == null) {
                 throw new IllegalStateException(
                     "Configuration file not found: Please create 'config.properties' file in your project's 'src/main/resources/' directory.\n" +
                     "Example Redis configuration:\n" +
                     "redis.host=localhost\n" +
                     "redis.port=6379\n" +
                     "redis.password=\n" +
                     "redis.database=0\n" +
                     "# Optional pool configuration:\n" +
                     "redis.pool.maxTotal=200\n" +
                     "redis.pool.maxIdle=50\n" +
                     "redis.pool.minIdle=10\n" +
                     "redis.pool.timeout=3000"
                 );
             }
             
             props.load(inputStream);
             inputStream.close();
             
             String redisHost = props.getProperty("redis.host");
             String redisPort = props.getProperty("redis.port");
             String redisPassword = props.getProperty("redis.password");
             String redisDatabase = props.getProperty("redis.database");
             
             // 连接池配置参数
             String maxTotal = props.getProperty("redis.pool.maxTotal");
             String maxIdle = props.getProperty("redis.pool.maxIdle");
             String minIdle = props.getProperty("redis.pool.minIdle");
             String timeout = props.getProperty("redis.pool.timeout");
             
             // 检查必需参数
             if (redisHost == null) {
                 throw new IllegalArgumentException(
                     "Missing required parameter 'redis.host' in config.properties.\n" +
                     "Please add: redis.host=localhost"
                 );
             }
             
             // 设置默认值
             int port = redisPort != null ? Integer.parseInt(redisPort) : 6379;
             String password = (redisPassword != null && !redisPassword.trim().isEmpty()) ? redisPassword : null;
             int database = redisDatabase != null ? Integer.parseInt(redisDatabase) : 0;
             
             // 连接池参数默认值（生产环境友好）
             int poolMaxTotal = maxTotal != null ? Integer.parseInt(maxTotal) : 200;
             int poolMaxIdle = maxIdle != null ? Integer.parseInt(maxIdle) : 50;
             int poolMinIdle = minIdle != null ? Integer.parseInt(minIdle) : 10;
             int poolTimeout = timeout != null ? Integer.parseInt(timeout) : 3000;
             
             return init(redisHost, port, password, database, poolMaxTotal, poolMaxIdle, poolMinIdle, poolTimeout);
             
         } catch (IllegalArgumentException | IllegalStateException e) {
             throw e;
         } catch (Exception e) {
             throw new RuntimeException("Failed to initialize Redis connection from config.properties: " + e.getMessage(), e);
         }
     }

         /**
      * 初始化Redis连接（使用默认连接池配置）
      * 
      * @param host Redis主机
      * @param port Redis端口
      * @param password Redis密码（可为null）
      * @param database Redis数据库索引
      * @return Jedis连接池
      */
     public static JedisPool init(String host, int port, String password, int database) {
         // 使用默认连接池配置
         return init(host, port, password, database, 200, 50, 10, 3000);
     }

     /**
      * 初始化Redis连接（自定义连接池配置）
      * 
      * @param host Redis主机
      * @param port Redis端口
      * @param password Redis密码（可为null）
      * @param database Redis数据库索引
      * @param maxTotal 连接池最大连接数
      * @param maxIdle 连接池最大空闲连接数
      * @param minIdle 连接池最小空闲连接数
      * @param timeout 连接超时时间（毫秒）
      * @return Jedis连接池
      */
     public static JedisPool init(String host, int port, String password, int database, 
                                  int maxTotal, int maxIdle, int minIdle, int timeout) {
         try {
             // 强制设置为全局连接池，即使之前已经初始化过
             RedisUtils.host = host;
             RedisUtils.port = port;
             RedisUtils.password = password;
             RedisUtils.database = database;
             
             // 关闭旧连接池
             if (jedisPool != null && !jedisPool.isClosed()) {
                 jedisPool.close();
             }
             
             // 创建连接池配置
             JedisPoolConfig config = new JedisPoolConfig();
             config.setMaxTotal(maxTotal);
             config.setMaxIdle(maxIdle);
             config.setMinIdle(minIdle);
             config.setTestOnBorrow(true);
             config.setTestOnReturn(true);
             config.setTestWhileIdle(true);
             config.setBlockWhenExhausted(true);
             config.setMaxWaitMillis(timeout);
             
             // 创建连接池
             if (password != null && !password.trim().isEmpty()) {
                 jedisPool = new JedisPool(config, host, port, timeout, password, database);
             } else {
                 jedisPool = new JedisPool(config, host, port, timeout, null, database);
             }
             
             return jedisPool;
             
         } catch (Exception e) {
             throw new RuntimeException("Failed to initialize Redis connection: " + e.getMessage(), e);
         }
     }

    /**
     * 创建新的Jedis连接
     * 
     * @return Jedis连接
     */
    public static Jedis createConnection() {
        ensureInitialized();
        return jedisPool.getResource();
    }

    // ========== String操作 ==========

    /**
     * 设置字符串值（永不过期）
     * 
     * @param key 键
     * @param value 值
     */
    public static void set(String key, String value) {
        try (Jedis jedis = createConnection()) {
            jedis.set(key, value);
        } catch (Exception e) {
            throw new RuntimeException("Redis set operation failed: " + e.getMessage(), e);
        }
    }

         /**
      * 设置字符串值（指定过期时间）
      * 
      * @param key 键
      * @param value 值
      * @param expireMillis 过期时间（毫秒）
      */
     public static void set(String key, String value, long expireMillis) {
         try (Jedis jedis = createConnection()) {
             jedis.psetex(key, expireMillis, value);
         } catch (Exception e) {
             throw new RuntimeException("Redis set with expire operation failed: " + e.getMessage(), e);
         }
     }

    /**
     * 获取字符串值
     * 
     * @param key 键
     * @return 值，不存在返回null
     */
    public static String get(String key) {
        try (Jedis jedis = createConnection()) {
            return jedis.get(key);
        } catch (Exception e) {
            throw new RuntimeException("Redis get operation failed: " + e.getMessage(), e);
        }
    }

    // ========== JSON操作 ==========

    /**
     * 设置JSON对象（永不过期）
     * 
     * @param key 键
     * @param jsonObject JSON对象
     */
    public static void setJson(String key, JSONObject jsonObject) {
        if (jsonObject == null) {
            throw new IllegalArgumentException("JSON object cannot be null");
        }
        set(key, jsonObject.toJSONString());
    }

         /**
      * 设置JSON对象（指定过期时间）
      * 
      * @param key 键
      * @param jsonObject JSON对象
      * @param expireMillis 过期时间（毫秒）
      */
     public static void setJson(String key, JSONObject jsonObject, long expireMillis) {
         if (jsonObject == null) {
             throw new IllegalArgumentException("JSON object cannot be null");
         }
         set(key, jsonObject.toJSONString(), expireMillis);
     }

    /**
     * 获取JSON对象
     * 
     * @param key 键
     * @return JSON对象，不存在返回null
     */
    public static JSONObject getJson(String key) {
        String jsonString = get(key);
        if (jsonString == null) {
            return null;
        }
        try {
            return JSONObject.parseObject(jsonString);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON from Redis: " + e.getMessage(), e);
        }
    }

    // ========== 计数器操作 ==========

    /**
     * 递增计数器
     * 
     * @param key 键
     * @return 递增后的值
     */
    public static Long increment(String key) {
        try (Jedis jedis = createConnection()) {
            return jedis.incr(key);
        } catch (Exception e) {
            throw new RuntimeException("Redis increment operation failed: " + e.getMessage(), e);
        }
    }

    /**
     * 递增计数器（指定步长）
     * 
     * @param key 键
     * @param delta 递增步长
     * @return 递增后的值
     */
    public static Long increment(String key, long delta) {
        try (Jedis jedis = createConnection()) {
            return jedis.incrBy(key, delta);
        } catch (Exception e) {
            throw new RuntimeException("Redis increment by delta operation failed: " + e.getMessage(), e);
        }
    }

    /**
     * 递减计数器
     * 
     * @param key 键
     * @return 递减后的值
     */
    public static Long decrement(String key) {
        try (Jedis jedis = createConnection()) {
            return jedis.decr(key);
        } catch (Exception e) {
            throw new RuntimeException("Redis decrement operation failed: " + e.getMessage(), e);
        }
    }

    /**
     * 递减计数器（指定步长）
     * 
     * @param key 键
     * @param delta 递减步长
     * @return 递减后的值
     */
    public static Long decrement(String key, long delta) {
        try (Jedis jedis = createConnection()) {
            return jedis.decrBy(key, delta);
        } catch (Exception e) {
            throw new RuntimeException("Redis decrement by delta operation failed: " + e.getMessage(), e);
        }
    }

    // ========== 通用操作 ==========

    /**
     * 删除键
     * 
     * @param key 键
     * @return 是否删除成功
     */
    public static boolean delete(String key) {
        try (Jedis jedis = createConnection()) {
            return jedis.del(key) > 0;
        } catch (Exception e) {
            throw new RuntimeException("Redis delete operation failed: " + e.getMessage(), e);
        }
    }

    /**
     * 检查键是否存在
     * 
     * @param key 键
     * @return 是否存在
     */
    public static boolean exists(String key) {
        try (Jedis jedis = createConnection()) {
            return jedis.exists(key);
        } catch (Exception e) {
            throw new RuntimeException("Redis exists operation failed: " + e.getMessage(), e);
        }
    }

         /**
      * 设置键的过期时间
      * 
      * @param key 键
      * @param expireMillis 过期时间（毫秒）
      * @return 是否设置成功
      */
     public static boolean expire(String key, long expireMillis) {
         try (Jedis jedis = createConnection()) {
             return jedis.pexpire(key, expireMillis) == 1;
         } catch (Exception e) {
             throw new RuntimeException("Redis expire operation failed: " + e.getMessage(), e);
         }
     }

    // ========== 连接管理 ==========

    /**
     * 关闭连接池（静默关闭，不抛出异常）
     */
    public static void closePool() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            try {
                jedisPool.close();
            } catch (Exception e) {
                // 静默处理关闭异常
            }
        }
    }

    // ========== 辅助方法 ==========

        /**
     * 确保Redis连接已初始化（懒加载）
     */
    private static void ensureInitialized() {
        if (jedisPool == null || jedisPool.isClosed()) {
            try {
                // 尝试自动从配置文件初始化
                init();
            } catch (IllegalArgumentException | IllegalStateException e) {
                // 配置文件不存在或参数缺失时，使用默认值连接本地Redis
                try {
                    init("localhost", 6379, null, 0);
                } catch (Exception defaultInitEx) {
                    throw new RuntimeException(
                        "Redis connection failed with default settings (localhost:6379, no password, database 0).\n" +
                        "Please ensure Redis server is running on localhost:6379, or configure connection:\n" +
                        "Option 1: Call RedisUtils.init(host, port, password, database) explicitly\n" +
                        "Option 2: Create 'config.properties' file in 'src/main/resources/' with:\n" +
                        "   redis.host=your_redis_host\n" +
                        "   redis.port=6379\n" +
                        "   redis.password=your_password\n" +
                        "   redis.database=0\n" +
                        "   # Optional pool configuration:\n" +
                        "   redis.pool.maxTotal=200\n" +
                        "   redis.pool.maxIdle=50\n" +
                        "   redis.pool.minIdle=10\n" +
                        "   redis.pool.timeout=3000\n\n" +
                        "Config file error: " + e.getMessage() + "\n" +
                        "Default connection error: " + defaultInitEx.getMessage(), defaultInitEx
                    );
                }
            }
        }
    }
} 