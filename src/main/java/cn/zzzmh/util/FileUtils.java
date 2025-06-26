package cn.zzzmh.util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文件操作工具类
 * 提供文件读写、目录操作、文件管理等功能
 * 
 * @author zzzmh
 * @since 1.0.0
 */
public class FileUtils {

    private static final int BUFFER_SIZE = 8192; // 8KB缓冲区
    private static final String TEMP_PREFIX = "zzzmh_temp_";

    /**
     * 私有构造函数，防止实例化
     */
    private FileUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ==================== 文件读写操作 ====================

    /**
     * 读取文件内容为字符串（UTF-8编码）
     * 
     * @param filePath 文件路径
     * @return 文件内容
     */
    public static String readString(String filePath) {
        return readString(filePath, StandardCharsets.UTF_8);
    }

    /**
     * 读取文件内容为字符串
     * 
     * @param filePath 文件路径
     * @param charset 字符编码
     * @return 文件内容
     */
    public static String readString(String filePath, Charset charset) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        if (charset == null) {
            throw new IllegalArgumentException("Charset cannot be null");
        }
        
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)), charset);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    /**
     * 读取文件所有行
     * 
     * @param filePath 文件路径
     * @return 行列表
     */
    public static List<String> readLines(String filePath) {
        return readLines(filePath, StandardCharsets.UTF_8);
    }

    /**
     * 读取文件所有行
     * 
     * @param filePath 文件路径
     * @param charset 字符编码
     * @return 行列表
     */
    public static List<String> readLines(String filePath, Charset charset) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        if (charset == null) {
            throw new IllegalArgumentException("Charset cannot be null");
        }
        
        try {
            return Files.readAllLines(Paths.get(filePath), charset);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read lines from file: " + filePath, e);
        }
    }

    /**
     * 读取文件为字节数组
     * 
     * @param filePath 文件路径
     * @return 字节数组
     */
    public static byte[] readBytes(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read bytes from file: " + filePath, e);
        }
    }

    /**
     * 写入字符串到文件（UTF-8编码，覆盖模式）
     * 
     * @param filePath 文件路径
     * @param content 文件内容
     */
    public static void writeString(String filePath, String content) {
        writeString(filePath, content, StandardCharsets.UTF_8, false);
    }

    /**
     * 写入字符串到文件
     * 
     * @param filePath 文件路径
     * @param content 文件内容
     * @param charset 字符编码
     * @param append 是否追加模式
     */
    public static void writeString(String filePath, String content, Charset charset, boolean append) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        if (charset == null) {
            throw new IllegalArgumentException("Charset cannot be null");
        }
        
        try {
            Path path = Paths.get(filePath);
            createParentDirectories(path);
            
            if (append) {
                Files.write(path, content.getBytes(charset), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else {
                Files.write(path, content.getBytes(charset));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write string to file: " + filePath, e);
        }
    }

    /**
     * 写入行列表到文件
     * 
     * @param filePath 文件路径
     * @param lines 行列表
     */
    public static void writeLines(String filePath, List<String> lines) {
        writeLines(filePath, lines, StandardCharsets.UTF_8, false);
    }

    /**
     * 写入行列表到文件
     * 
     * @param filePath 文件路径
     * @param lines 行列表
     * @param charset 字符编码
     * @param append 是否追加模式
     */
    public static void writeLines(String filePath, List<String> lines, Charset charset, boolean append) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        if (lines == null) {
            throw new IllegalArgumentException("Lines cannot be null");
        }
        if (charset == null) {
            throw new IllegalArgumentException("Charset cannot be null");
        }
        
        try {
            Path path = Paths.get(filePath);
            createParentDirectories(path);
            
            if (append) {
                Files.write(path, lines, charset, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else {
                Files.write(path, lines, charset);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write lines to file: " + filePath, e);
        }
    }

    /**
     * 写入字节数组到文件
     * 
     * @param filePath 文件路径
     * @param data 字节数组
     */
    public static void writeBytes(String filePath, byte[] data) {
        writeBytes(filePath, data, false);
    }

    /**
     * 写入字节数组到文件
     * 
     * @param filePath 文件路径
     * @param data 字节数组
     * @param append 是否追加模式
     */
    public static void writeBytes(String filePath, byte[] data, boolean append) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        
        try {
            Path path = Paths.get(filePath);
            createParentDirectories(path);
            
            if (append) {
                Files.write(path, data, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } else {
                Files.write(path, data);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write bytes to file: " + filePath, e);
        }
    }

    // ==================== 文件复制移动 ====================

    /**
     * 复制文件
     * 
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     */
    public static void copyFile(String sourcePath, String targetPath) {
        copyFile(sourcePath, targetPath, true);
    }

    /**
     * 复制文件
     * 
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @param replaceExisting 是否覆盖已存在的文件
     */
    public static void copyFile(String sourcePath, String targetPath, boolean replaceExisting) {
        if (sourcePath == null) {
            throw new IllegalArgumentException("Source path cannot be null");
        }
        if (targetPath == null) {
            throw new IllegalArgumentException("Target path cannot be null");
        }
        
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            
            createParentDirectories(target);
            
            if (replaceExisting) {
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(source, target);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy file from " + sourcePath + " to " + targetPath, e);
        }
    }

    /**
     * 移动文件
     * 
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     */
    public static void moveFile(String sourcePath, String targetPath) {
        moveFile(sourcePath, targetPath, true);
    }

    /**
     * 移动文件
     * 
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @param replaceExisting 是否覆盖已存在的文件
     */
    public static void moveFile(String sourcePath, String targetPath, boolean replaceExisting) {
        if (sourcePath == null) {
            throw new IllegalArgumentException("Source path cannot be null");
        }
        if (targetPath == null) {
            throw new IllegalArgumentException("Target path cannot be null");
        }
        
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            
            createParentDirectories(target);
            
            if (replaceExisting) {
                Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.move(source, target);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to move file from " + sourcePath + " to " + targetPath, e);
        }
    }

    /**
     * 复制目录及其所有内容
     * 
     * @param sourcePath 源目录路径
     * @param targetPath 目标目录路径
     */
    public static void copyDirectory(String sourcePath, String targetPath) {
        if (sourcePath == null) {
            throw new IllegalArgumentException("Source path cannot be null");
        }
        if (targetPath == null) {
            throw new IllegalArgumentException("Target path cannot be null");
        }
        
        try {
            Path source = Paths.get(sourcePath);
            Path target = Paths.get(targetPath);
            
            if (!Files.exists(source)) {
                throw new RuntimeException("Source directory does not exist: " + sourcePath);
            }
            
            if (!Files.isDirectory(source)) {
                throw new RuntimeException("Source is not a directory: " + sourcePath);
            }
            
            Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDir = target.resolve(source.relativize(dir));
                    Files.createDirectories(targetDir);
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = target.resolve(source.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy directory from " + sourcePath + " to " + targetPath, e);
        }
    }

    // ==================== 文件删除 ====================

    /**
     * 删除文件
     * 
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        try {
            return Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + filePath, e);
        }
    }

    /**
     * 删除目录及其所有内容
     * 
     * @param directoryPath 目录路径
     * @return 是否删除成功
     */
    public static boolean deleteDirectory(String directoryPath) {
        if (directoryPath == null) {
            throw new IllegalArgumentException("Directory path cannot be null");
        }
        
        try {
            Path directory = Paths.get(directoryPath);
            
            if (!Files.exists(directory)) {
                return false;
            }
            
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete directory: " + directoryPath, e);
        }
    }

    // ==================== 目录操作 ====================

    /**
     * 创建目录（包括父目录）
     * 
     * @param directoryPath 目录路径
     */
    public static void createDirectories(String directoryPath) {
        if (directoryPath == null) {
            throw new IllegalArgumentException("Directory path cannot be null");
        }
        
        try {
            Files.createDirectories(Paths.get(directoryPath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories: " + directoryPath, e);
        }
    }

    /**
     * 列出目录下的所有文件和子目录
     * 
     * @param directoryPath 目录路径
     * @return 文件和目录列表
     */
    public static List<String> listFiles(String directoryPath) {
        if (directoryPath == null) {
            throw new IllegalArgumentException("Directory path cannot be null");
        }
        
        try {
            Path directory = Paths.get(directoryPath);
            
            if (!Files.exists(directory)) {
                throw new RuntimeException("Directory does not exist: " + directoryPath);
            }
            
            if (!Files.isDirectory(directory)) {
                throw new RuntimeException("Path is not a directory: " + directoryPath);
            }
            
            try (Stream<Path> paths = Files.list(directory)) {
                return paths.map(Path::toString).collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to list files in directory: " + directoryPath, e);
        }
    }

    /**
     * 递归列出目录下的所有文件
     * 
     * @param directoryPath 目录路径
     * @return 文件列表
     */
    public static List<String> listAllFiles(String directoryPath) {
        if (directoryPath == null) {
            throw new IllegalArgumentException("Directory path cannot be null");
        }
        
        try {
            Path directory = Paths.get(directoryPath);
            
            if (!Files.exists(directory)) {
                throw new RuntimeException("Directory does not exist: " + directoryPath);
            }
            
            if (!Files.isDirectory(directory)) {
                throw new RuntimeException("Path is not a directory: " + directoryPath);
            }
            
            try (Stream<Path> paths = Files.walk(directory)) {
                return paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to list all files in directory: " + directoryPath, e);
        }
    }

    /**
     * 根据扩展名过滤文件
     * 
     * @param directoryPath 目录路径
     * @param extension 文件扩展名（如：.txt、.java）
     * @return 符合条件的文件列表
     */
    public static List<String> listFilesByExtension(String directoryPath, String extension) {
        if (directoryPath == null) {
            throw new IllegalArgumentException("Directory path cannot be null");
        }
        if (extension == null) {
            throw new IllegalArgumentException("Extension cannot be null");
        }
        
        String normalizedExtension = extension.startsWith(".") ? extension : "." + extension;
        
        return listAllFiles(directoryPath).stream()
            .filter(filePath -> filePath.toLowerCase().endsWith(normalizedExtension.toLowerCase()))
            .collect(Collectors.toList());
    }

    // ==================== 文件信息 ====================

    /**
     * 判断文件是否存在
     * 
     * @param filePath 文件路径
     * @return 是否存在
     */
    public static boolean exists(String filePath) {
        if (filePath == null) {
            return false;
        }
        return Files.exists(Paths.get(filePath));
    }

    /**
     * 判断是否为文件
     * 
     * @param filePath 文件路径
     * @return 是否为文件
     */
    public static boolean isFile(String filePath) {
        if (filePath == null) {
            return false;
        }
        return Files.isRegularFile(Paths.get(filePath));
    }

    /**
     * 判断是否为目录
     * 
     * @param directoryPath 目录路径
     * @return 是否为目录
     */
    public static boolean isDirectory(String directoryPath) {
        if (directoryPath == null) {
            return false;
        }
        return Files.isDirectory(Paths.get(directoryPath));
    }

    /**
     * 获取文件大小（字节）
     * 
     * @param filePath 文件路径
     * @return 文件大小
     */
    public static long getFileSize(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        try {
            return Files.size(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to get file size: " + filePath, e);
        }
    }

    /**
     * 获取文件大小的友好显示字符串
     * 
     * @param filePath 文件路径
     * @return 文件大小字符串（如：1.5MB）
     */
    public static String getFileSizeString(String filePath) {
        return formatFileSize(getFileSize(filePath));
    }

    /**
     * 格式化文件大小为友好显示字符串
     * 
     * @param size 文件大小（字节）
     * @return 格式化后的大小字符串
     */
    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        }
        
        double kb = size / 1024.0;
        if (kb < 1024) {
            return String.format("%.1f KB", kb);
        }
        
        double mb = kb / 1024.0;
        if (mb < 1024) {
            return String.format("%.1f MB", mb);
        }
        
        double gb = mb / 1024.0;
        if (gb < 1024) {
            return String.format("%.1f GB", gb);
        }
        
        double tb = gb / 1024.0;
        return String.format("%.1f TB", tb);
    }

    /**
     * 获取文件扩展名
     * 
     * @param filePath 文件路径
     * @return 文件扩展名（包含点号，如：.txt）
     */
    public static String getFileExtension(String filePath) {
        if (filePath == null) {
            return "";
        }
        
        String fileName = Paths.get(filePath).getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        
        return fileName.substring(lastDotIndex);
    }

    /**
     * 获取文件名（不包含路径和扩展名）
     * 
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (filePath == null) {
            return "";
        }
        
        String fileName = Paths.get(filePath).getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        
        if (lastDotIndex == -1) {
            return fileName;
        }
        
        return fileName.substring(0, lastDotIndex);
    }

    /**
     * 获取文件的最后修改时间戳
     * 
     * @param filePath 文件路径
     * @return 最后修改时间戳（毫秒）
     */
    public static long getLastModifiedTime(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        try {
            return Files.getLastModifiedTime(Paths.get(filePath)).toMillis();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get last modified time: " + filePath, e);
        }
    }

    // ==================== 临时文件 ====================

    /**
     * 创建临时文件
     * 
     * @return 临时文件路径
     */
    public static String createTempFile() {
        return createTempFile(null);
    }

    /**
     * 创建临时文件
     * 
     * @param suffix 文件后缀（如：.txt）
     * @return 临时文件路径
     */
    public static String createTempFile(String suffix) {
        try {
            Path tempFile = Files.createTempFile(TEMP_PREFIX, suffix);
            return tempFile.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp file", e);
        }
    }

    /**
     * 创建临时目录
     * 
     * @return 临时目录路径
     */
    public static String createTempDirectory() {
        try {
            Path tempDir = Files.createTempDirectory(TEMP_PREFIX);
            return tempDir.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp directory", e);
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 创建父目录（如果不存在）
     * 
     * @param path 文件路径
     */
    private static void createParentDirectories(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    /**
     * 计算目录大小
     * 
     * @param directoryPath 目录路径
     * @return 目录大小（字节）
     */
    public static long getDirectorySize(String directoryPath) {
        if (directoryPath == null) {
            throw new IllegalArgumentException("Directory path cannot be null");
        }
        
        try {
            Path directory = Paths.get(directoryPath);
            
            if (!Files.exists(directory)) {
                throw new RuntimeException("Directory does not exist: " + directoryPath);
            }
            
            if (!Files.isDirectory(directory)) {
                throw new RuntimeException("Path is not a directory: " + directoryPath);
            }
            
            try (Stream<Path> paths = Files.walk(directory)) {
                return paths
                    .filter(Files::isRegularFile)
                    .mapToLong(path -> {
                        try {
                            return Files.size(path);
                        } catch (IOException e) {
                            return 0L;
                        }
                    })
                    .sum();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to calculate directory size: " + directoryPath, e);
        }
    }

    /**
     * 清空目录（删除目录下所有文件和子目录，但保留目录本身）
     * 
     * @param directoryPath 目录路径
     */
    public static void cleanDirectory(String directoryPath) {
        if (directoryPath == null) {
            throw new IllegalArgumentException("Directory path cannot be null");
        }
        
        try {
            Path directory = Paths.get(directoryPath);
            
            if (!Files.exists(directory)) {
                return;
            }
            
            if (!Files.isDirectory(directory)) {
                throw new RuntimeException("Path is not a directory: " + directoryPath);
            }
            
            try (Stream<Path> paths = Files.list(directory)) {
                paths.forEach(path -> {
                    try {
                        if (Files.isDirectory(path)) {
                            deleteDirectory(path.toString());
                        } else {
                            Files.delete(path);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to delete: " + path, e);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to clean directory: " + directoryPath, e);
        }
    }
} 