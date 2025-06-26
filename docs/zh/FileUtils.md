# 📁 FileUtils 使用文档

## 概述

FileUtils是一个全面的文件操作工具类，提供文件读写、目录管理、文件复制移动、临时文件处理等功能，支持多种编码格式和文件类型。

## 🚀 快速开始

### 基础文件读写

```java
// 读取文件内容为字符串
String content = FileUtils.readString("path/to/file.txt");

// 写入字符串到文件
FileUtils.writeString("path/to/output.txt", "Hello World");

// 读取字节数组
byte[] bytes = FileUtils.readBytes("path/to/binary.dat");

// 写入字节数组
FileUtils.writeBytes("path/to/output.bin", bytes);
```

### 指定编码的文件操作

```java
// 指定编码读取文件
String content = FileUtils.readString("path/to/file.txt", "GBK");

// 指定编码写入文件
FileUtils.writeString("path/to/output.txt", "中文内容", "UTF-8");

// 按行读取文件
List<String> lines = FileUtils.readLines("path/to/file.txt");

// 写入多行内容
List<String> lines = Arrays.asList("第一行", "第二行", "第三行");
FileUtils.writeLines("path/to/output.txt", lines);
```

## 📋 文件操作详解

### 文件读取操作

```java
// === 字符串读取 ===

// 默认UTF-8编码
String content = FileUtils.readString("config.properties");

// 指定编码
String gbkContent = FileUtils.readString("legacy.txt", "GBK");

// === 字节数组读取 ===

// 读取二进制文件
byte[] imageData = FileUtils.readBytes("image.jpg");

// 读取加密文件
byte[] encryptedData = FileUtils.readBytes("encrypted.dat");

// === 按行读取 ===

// 读取所有行
List<String> allLines = FileUtils.readLines("data.csv");

// 指定编码读取行
List<String> lines = FileUtils.readLines("data.txt", "GBK");

// === 流式读取（大文件）===

// 处理大文件，逐行处理
try (BufferedReader reader = Files.newBufferedReader(Paths.get("large.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        // 处理每一行
        System.out.println(line);
    }
}
```

### 文件写入操作

```java
// === 字符串写入 ===

// 覆盖写入
FileUtils.writeString("output.txt", "新内容");

// 指定编码写入
FileUtils.writeString("output.txt", "中文内容", "UTF-8");

// === 字节数组写入 ===

// 写入二进制数据
byte[] data = "Hello".getBytes();
FileUtils.writeBytes("binary.dat", data);

// === 按行写入 ===

List<String> lines = Arrays.asList(
    "第一行数据",
    "第二行数据",
    "第三行数据"
);
FileUtils.writeLines("data.txt", lines);

// 指定编码写入行
FileUtils.writeLines("data.txt", lines, "GBK");

// === 追加写入 ===

// 追加字符串
FileUtils.appendString("log.txt", "新的日志条目\n");

// 追加行
FileUtils.appendLines("log.txt", Arrays.asList("日志1", "日志2"));
```

## 📂 目录操作

### 目录创建和删除

```java
// 创建目录
boolean created = FileUtils.createDirectory("path/to/new/dir");

// 创建多级目录
boolean created = FileUtils.createDirectories("path/to/deep/nested/dirs");

// 删除空目录
boolean deleted = FileUtils.deleteDirectory("path/to/empty/dir");

// 递归删除目录及其内容
boolean deleted = FileUtils.deleteDirectoryRecursively("path/to/dir");

// 清空目录（保留目录本身）
boolean cleared = FileUtils.clearDirectory("path/to/dir");
```

### 目录遍历

```java
// 列出目录下的所有文件
List<String> files = FileUtils.listFiles("path/to/directory");

// 列出目录下的所有子目录
List<String> dirs = FileUtils.listDirectories("path/to/directory");

// 递归列出所有文件
List<String> allFiles = FileUtils.listFilesRecursively("path/to/directory");

// 按扩展名过滤文件
List<String> txtFiles = FileUtils.listFilesByExtension("path/to/directory", "txt");

// 按模式过滤文件
List<String> logFiles = FileUtils.listFilesByPattern("path/to/logs", "*.log");
```

### 目录信息统计

```java
// 计算目录大小
long dirSize = FileUtils.getDirectorySize("path/to/directory");

// 统计目录中的文件数量
int fileCount = FileUtils.countFiles("path/to/directory");

// 统计子目录数量
int dirCount = FileUtils.countDirectories("path/to/directory");

// 递归统计文件数量
int totalFiles = FileUtils.countFilesRecursively("path/to/directory");

// 获取目录树结构
String treeStructure = FileUtils.getDirectoryTree("path/to/directory");
```

## 📋 文件管理操作

### 文件复制

```java
// 复制文件
boolean copied = FileUtils.copyFile("source.txt", "destination.txt");

// 复制到目录
boolean copied = FileUtils.copyFileToDirectory("file.txt", "target/directory/");

// 复制目录
boolean copied = FileUtils.copyDirectory("source/dir", "target/dir");

// 复制时保留文件属性
boolean copied = FileUtils.copyFileWithAttributes("source.txt", "target.txt");
```

### 文件移动和重命名

```java
// 移动文件
boolean moved = FileUtils.moveFile("old/path/file.txt", "new/path/file.txt");

// 移动到目录
boolean moved = FileUtils.moveFileToDirectory("file.txt", "target/directory/");

// 重命名文件
boolean renamed = FileUtils.renameFile("old-name.txt", "new-name.txt");

// 移动目录
boolean moved = FileUtils.moveDirectory("old/directory", "new/directory");
```

### 文件删除

```java
// 删除文件
boolean deleted = FileUtils.deleteFile("path/to/file.txt");

// 安全删除（不抛异常）
boolean deleted = FileUtils.deleteFileSafely("path/to/file.txt");

// 批量删除文件
List<String> filesToDelete = Arrays.asList("file1.txt", "file2.txt", "file3.txt");
int deletedCount = FileUtils.deleteFiles(filesToDelete);

// 按模式删除文件
int deletedCount = FileUtils.deleteFilesByPattern("temp/", "*.tmp");
```

## 📄 文件信息获取

### 基础文件信息

```java
// 检查文件是否存在
boolean exists = FileUtils.fileExists("path/to/file.txt");

// 检查是否是文件
boolean isFile = FileUtils.isFile("path/to/item");

// 检查是否是目录
boolean isDirectory = FileUtils.isDirectory("path/to/item");

// 获取文件大小
long size = FileUtils.getFileSize("path/to/file.txt");

// 获取格式化的文件大小
String formattedSize = FileUtils.getFormattedFileSize("path/to/file.txt");
// 输出: "1.5 MB", "256 KB", "3.2 GB" 等
```

### 文件时间信息

```java
// 获取文件最后修改时间
long lastModified = FileUtils.getLastModifiedTime("path/to/file.txt");

// 获取文件创建时间
long creationTime = FileUtils.getCreationTime("path/to/file.txt");

// 获取文件最后访问时间
long lastAccessTime = FileUtils.getLastAccessTime("path/to/file.txt");

// 格式化时间显示
String modifiedTimeStr = FileUtils.getFormattedLastModifiedTime("path/to/file.txt");
```

### 文件路径操作

```java
// 获取文件名（含扩展名）
String fileName = FileUtils.getFileName("path/to/file.txt"); // "file.txt"

// 获取文件名（不含扩展名）
String baseName = FileUtils.getBaseName("path/to/file.txt"); // "file"

// 获取文件扩展名
String extension = FileUtils.getExtension("path/to/file.txt"); // "txt"

// 获取父目录路径
String parentDir = FileUtils.getParentDirectory("path/to/file.txt"); // "path/to"

// 获取绝对路径
String absolutePath = FileUtils.getAbsolutePath("relative/path/file.txt");

// 标准化路径
String normalizedPath = FileUtils.normalizePath("path/../to/./file.txt"); // "to/file.txt"
```

## 🗂️ 临时文件管理

### 临时文件创建

```java
// 创建临时文件
String tempFile = FileUtils.createTempFile();

// 创建带前缀的临时文件
String tempFile = FileUtils.createTempFile("myapp-");

// 创建带前缀和后缀的临时文件
String tempFile = FileUtils.createTempFile("data-", ".tmp");

// 在指定目录创建临时文件
String tempFile = FileUtils.createTempFileInDirectory("temp/", "cache-", ".dat");

// 创建临时目录
String tempDir = FileUtils.createTempDirectory();

// 创建带前缀的临时目录
String tempDir = FileUtils.createTempDirectory("workspace-");
```

### 临时文件管理

```java
// 清理临时文件
FileUtils.cleanupTempFiles();

// 清理指定目录的临时文件
FileUtils.cleanupTempFiles("temp/");

// 清理过期临时文件（超过指定小时数）
FileUtils.cleanupExpiredTempFiles(24); // 清理24小时前的临时文件

// 获取系统临时目录
String systemTempDir = FileUtils.getSystemTempDirectory();
```

## ⚡ 实际应用场景

### 配置文件处理

```java
public class ConfigFileManager {
    
    private static final String CONFIG_PATH = "config/app.properties";
    
    /**
     * 加载配置文件
     */
    public static Properties loadConfig() {
        Properties props = new Properties();
        try {
            if (FileUtils.fileExists(CONFIG_PATH)) {
                String content = FileUtils.readString(CONFIG_PATH);
                props.load(new StringReader(content));
            }
        } catch (Exception e) {
            throw new RuntimeException("配置文件加载失败", e);
        }
        return props;
    }
    
    /**
     * 保存配置文件
     */
    public static void saveConfig(Properties props) {
        try {
            StringWriter writer = new StringWriter();
            props.store(writer, "Application Configuration");
            
            // 确保目录存在
            String parentDir = FileUtils.getParentDirectory(CONFIG_PATH);
            FileUtils.createDirectories(parentDir);
            
            FileUtils.writeString(CONFIG_PATH, writer.toString());
        } catch (Exception e) {
            throw new RuntimeException("配置文件保存失败", e);
        }
    }
    
    /**
     * 备份配置文件
     */
    public static void backupConfig() {
        if (FileUtils.fileExists(CONFIG_PATH)) {
            String timestamp = DateTimeUtils.formatCurrent("yyyyMMdd-HHmmss");
            String backupPath = CONFIG_PATH + ".backup." + timestamp;
            FileUtils.copyFile(CONFIG_PATH, backupPath);
        }
    }
}
```

### 日志文件管理

```java
public class LogFileManager {
    
    private static final String LOG_DIR = "logs/";
    private static final long MAX_LOG_SIZE = 10 * 1024 * 1024; // 10MB
    
    /**
     * 写入日志
     */
    public static void writeLog(String level, String message) {
        String timestamp = DateTimeUtils.formatCurrent("yyyy-MM-dd HH:mm:ss");
        String logEntry = String.format("[%s] %s - %s%n", timestamp, level, message);
        
        String todayLogFile = LOG_DIR + "app-" + DateTimeUtils.formatCurrent("yyyy-MM-dd") + ".log";
        
        // 确保日志目录存在
        FileUtils.createDirectories(LOG_DIR);
        
        // 检查日志文件大小，必要时轮转
        if (FileUtils.fileExists(todayLogFile) && 
            FileUtils.getFileSize(todayLogFile) > MAX_LOG_SIZE) {
            rotateLogFile(todayLogFile);
        }
        
        FileUtils.appendString(todayLogFile, logEntry);
    }
    
    /**
     * 轮转日志文件
     */
    private static void rotateLogFile(String logFile) {
        String timestamp = DateTimeUtils.formatCurrent("HHmmss");
        String rotatedFile = logFile.replace(".log", "-" + timestamp + ".log");
        FileUtils.moveFile(logFile, rotatedFile);
    }
    
    /**
     * 清理过期日志
     */
    public static void cleanupOldLogs(int daysToKeep) {
        List<String> logFiles = FileUtils.listFilesByPattern(LOG_DIR, "*.log");
        long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24 * 60 * 60 * 1000L);
        
        for (String logFile : logFiles) {
            if (FileUtils.getLastModifiedTime(logFile) < cutoffTime) {
                FileUtils.deleteFile(logFile);
            }
        }
    }
}
```

### 数据导入导出

```java
public class DataImportExport {
    
    /**
     * 导出JSON数据到文件
     */
    public static void exportJsonData(JSONArray data, String filePath) {
        String jsonString = JSON.toJSONString(data, true); // 格式化输出
        FileUtils.writeString(filePath, jsonString);
    }
    
    /**
     * 从文件导入JSON数据
     */
    public static JSONArray importJsonData(String filePath) {
        if (!FileUtils.fileExists(filePath)) {
            throw new RuntimeException("数据文件不存在: " + filePath);
        }
        
        String jsonString = FileUtils.readString(filePath);
        return JSON.parseArray(jsonString);
    }
    
    /**
     * 导出CSV数据
     */
    public static void exportCsvData(List<List<String>> data, String filePath) {
        List<String> csvLines = new ArrayList<>();
        
        for (List<String> row : data) {
            String csvLine = row.stream()
                .map(field -> "\"" + field.replace("\"", "\"\"") + "\"")
                .collect(Collectors.joining(","));
            csvLines.add(csvLine);
        }
        
        FileUtils.writeLines(filePath, csvLines);
    }
    
    /**
     * 导入CSV数据
     */
    public static List<List<String>> importCsvData(String filePath) {
        List<String> lines = FileUtils.readLines(filePath);
        List<List<String>> data = new ArrayList<>();
        
        for (String line : lines) {
            List<String> fields = parseCsvLine(line);
            data.add(fields);
        }
        
        return data;
    }
    
    private static List<String> parseCsvLine(String line) {
        // 简化的CSV解析（实际应用中建议使用专业的CSV库）
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        fields.add(currentField.toString());
        return fields;
    }
}
```

### 文件缓存管理

```java
public class FileCacheManager {
    
    private static final String CACHE_DIR = "cache/";
    private static final long CACHE_EXPIRE_TIME = 24 * 60 * 60 * 1000L; // 24小时
    
    /**
     * 缓存数据到文件
     */
    public static void cacheData(String key, String data) {
        String cacheFile = CACHE_DIR + HashUtils.md5(key) + ".cache";
        
        // 确保缓存目录存在
        FileUtils.createDirectories(CACHE_DIR);
        
        // 创建缓存项
        JSONObject cacheItem = new JSONObject();
        cacheItem.put("key", key);
        cacheItem.put("data", data);
        cacheItem.put("timestamp", System.currentTimeMillis());
        
        FileUtils.writeString(cacheFile, cacheItem.toJSONString());
    }
    
    /**
     * 从缓存获取数据
     */
    public static String getCachedData(String key) {
        String cacheFile = CACHE_DIR + HashUtils.md5(key) + ".cache";
        
        if (!FileUtils.fileExists(cacheFile)) {
            return null;
        }
        
        try {
            String content = FileUtils.readString(cacheFile);
            JSONObject cacheItem = JSON.parseObject(content);
            
            long timestamp = cacheItem.getLongValue("timestamp");
            if (System.currentTimeMillis() - timestamp > CACHE_EXPIRE_TIME) {
                // 缓存过期，删除文件
                FileUtils.deleteFile(cacheFile);
                return null;
            }
            
            return cacheItem.getString("data");
            
        } catch (Exception e) {
            // 缓存文件损坏，删除
            FileUtils.deleteFile(cacheFile);
            return null;
        }
    }
    
    /**
     * 清理过期缓存
     */
    public static void cleanupExpiredCache() {
        if (!FileUtils.isDirectory(CACHE_DIR)) {
            return;
        }
        
        List<String> cacheFiles = FileUtils.listFilesByExtension(CACHE_DIR, "cache");
        long currentTime = System.currentTimeMillis();
        
        for (String cacheFile : cacheFiles) {
            try {
                String content = FileUtils.readString(cacheFile);
                JSONObject cacheItem = JSON.parseObject(content);
                long timestamp = cacheItem.getLongValue("timestamp");
                
                if (currentTime - timestamp > CACHE_EXPIRE_TIME) {
                    FileUtils.deleteFile(cacheFile);
                }
                
            } catch (Exception e) {
                // 文件损坏，直接删除
                FileUtils.deleteFile(cacheFile);
            }
        }
    }
}
```

## 🔧 高级功能

### 文件监控

```java
public class FileWatcher {
    
    /**
     * 监控文件变化
     */
    public static void watchFileChanges(String filePath, Runnable callback) {
        // 简化的文件监控实现
        long lastModified = FileUtils.getLastModifiedTime(filePath);
        
        // 在实际应用中，这里应该使用WatchService或定时器
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            private long lastCheck = lastModified;
            
            @Override
            public void run() {
                if (FileUtils.fileExists(filePath)) {
                    long currentModified = FileUtils.getLastModifiedTime(filePath);
                    if (currentModified > lastCheck) {
                        lastCheck = currentModified;
                        callback.run();
                    }
                }
            }
        }, 1000, 1000); // 每秒检查一次
    }
}
```

### 文件压缩和解压

```java
public class FileCompressionUtils {
    
    /**
     * 压缩文件到ZIP
     */
    public static void zipFiles(List<String> filePaths, String zipPath) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {
            
            for (String filePath : filePaths) {
                if (FileUtils.fileExists(filePath)) {
                    byte[] fileData = FileUtils.readBytes(filePath);
                    String fileName = FileUtils.getFileName(filePath);
                    
                    ZipEntry entry = new ZipEntry(fileName);
                    zos.putNextEntry(entry);
                    zos.write(fileData);
                    zos.closeEntry();
                }
            }
            
        } catch (IOException e) {
            throw new RuntimeException("文件压缩失败", e);
        }
    }
    
    /**
     * 解压ZIP文件
     */
    public static void unzipFile(String zipPath, String extractDir) {
        FileUtils.createDirectories(extractDir);
        
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath))) {
            
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String entryPath = extractDir + "/" + entry.getName();
                
                if (entry.isDirectory()) {
                    FileUtils.createDirectories(entryPath);
                } else {
                    byte[] buffer = new byte[1024];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        baos.write(buffer, 0, len);
                    }
                    
                    FileUtils.writeBytes(entryPath, baos.toByteArray());
                }
                
                zis.closeEntry();
            }
            
        } catch (IOException e) {
            throw new RuntimeException("文件解压失败", e);
        }
    }
}
```

## 📊 文件格式支持

### 格式化文件大小

```java
public class FileSizeFormatter {
    
    /**
     * 格式化字节大小
     */
    public static String formatSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }
    
    /**
     * 解析大小字符串为字节数
     */
    public static long parseSize(String sizeStr) {
        sizeStr = sizeStr.trim().toUpperCase();
        
        if (sizeStr.endsWith("B")) {
            return Long.parseLong(sizeStr.substring(0, sizeStr.length() - 1).trim());
        } else if (sizeStr.endsWith("KB")) {
            return (long) (Double.parseDouble(sizeStr.substring(0, sizeStr.length() - 2).trim()) * 1024);
        } else if (sizeStr.endsWith("MB")) {
            return (long) (Double.parseDouble(sizeStr.substring(0, sizeStr.length() - 2).trim()) * 1024 * 1024);
        } else if (sizeStr.endsWith("GB")) {
            return (long) (Double.parseDouble(sizeStr.substring(0, sizeStr.length() - 2).trim()) * 1024 * 1024 * 1024);
        } else {
            return Long.parseLong(sizeStr);
        }
    }
}
```

## 📝 注意事项

- 所有文件路径支持相对路径和绝对路径
- 默认编码为UTF-8，建议明确指定编码格式
- 大文件操作可能消耗大量内存，注意内存管理
- 文件操作失败会抛出RuntimeException异常
- 临时文件会在JVM退出时自动清理
- 目录操作会自动创建必要的父目录
- 文件移动实际上是重命名操作，跨磁盘移动会先复制后删除 