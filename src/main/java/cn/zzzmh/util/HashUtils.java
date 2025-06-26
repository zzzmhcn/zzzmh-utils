package cn.zzzmh.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hash工具类
 * 支持MD5、SHA-1、SHA-256、SHA-384、SHA-512等常用Hash算法
 * 
 * @author zzzmh
 * @since 1.0.0
 */
public class HashUtils {

    private static final int BUFFER_SIZE = 8192; // 8KB缓冲区

    /**
     * 私有构造函数，防止实例化
     */
    private HashUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ==================== MD5算法 ====================

    /**
     * 计算字符串的MD5值
     * 
     * @param data 输入字符串
     * @return MD5值（32位小写十六进制）
     */
    public static String md5(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        return md5(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的MD5值
     * 
     * @param data 输入字节数组
     * @return MD5值（32位小写十六进制）
     */
    public static String md5(byte[] data) {
        return hash(data, "MD5");
    }

    /**
     * 计算文件的MD5值
     * 
     * @param filePath 文件路径
     * @return MD5值（32位小写十六进制）
     */
    public static String md5File(String filePath) {
        return hashFile(filePath, "MD5");
    }

    /**
     * 计算字符串的MD5值（指定大小写）
     * 
     * @param data 输入字符串
     * @param uppercase 是否返回大写
     * @return MD5值（32位十六进制）
     */
    public static String md5Hex(String data, boolean uppercase) {
        String result = md5(data);
        return uppercase ? result.toUpperCase() : result;
    }

    /**
     * 计算字符串的MD5值（Base64格式）
     * 
     * @param data 输入字符串
     * @return MD5值（Base64编码）
     */
    public static String md5Base64(String data) {
        byte[] hashBytes = hashBytes(data.getBytes(StandardCharsets.UTF_8), "MD5");
        return EncoderUtils.base64Encode(hashBytes);
    }

    // ==================== SHA-1算法 ====================

    /**
     * 计算字符串的SHA-1值
     * 
     * @param data 输入字符串
     * @return SHA-1值（40位小写十六进制）
     */
    public static String sha1(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        return sha1(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的SHA-1值
     * 
     * @param data 输入字节数组
     * @return SHA-1值（40位小写十六进制）
     */
    public static String sha1(byte[] data) {
        return hash(data, "SHA-1");
    }

    /**
     * 计算文件的SHA-1值
     * 
     * @param filePath 文件路径
     * @return SHA-1值（40位小写十六进制）
     */
    public static String sha1File(String filePath) {
        return hashFile(filePath, "SHA-1");
    }

    // ==================== SHA-256算法 ====================

    /**
     * 计算字符串的SHA-256值
     * 
     * @param data 输入字符串
     * @return SHA-256值（64位小写十六进制）
     */
    public static String sha256(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        return sha256(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的SHA-256值
     * 
     * @param data 输入字节数组
     * @return SHA-256值（64位小写十六进制）
     */
    public static String sha256(byte[] data) {
        return hash(data, "SHA-256");
    }

    /**
     * 计算文件的SHA-256值
     * 
     * @param filePath 文件路径
     * @return SHA-256值（64位小写十六进制）
     */
    public static String sha256File(String filePath) {
        return hashFile(filePath, "SHA-256");
    }

    /**
     * 计算字符串的SHA-256值（指定大小写）
     * 
     * @param data 输入字符串
     * @param uppercase 是否返回大写
     * @return SHA-256值（64位十六进制）
     */
    public static String sha256Hex(String data, boolean uppercase) {
        String result = sha256(data);
        return uppercase ? result.toUpperCase() : result;
    }

    /**
     * 计算字符串的SHA-256值（Base64格式）
     * 
     * @param data 输入字符串
     * @return SHA-256值（Base64编码）
     */
    public static String sha256Base64(String data) {
        byte[] hashBytes = hashBytes(data.getBytes(StandardCharsets.UTF_8), "SHA-256");
        return EncoderUtils.base64Encode(hashBytes);
    }

    // ==================== SHA-384算法 ====================

    /**
     * 计算字符串的SHA-384值
     * 
     * @param data 输入字符串
     * @return SHA-384值（96位小写十六进制）
     */
    public static String sha384(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        return sha384(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的SHA-384值
     * 
     * @param data 输入字节数组
     * @return SHA-384值（96位小写十六进制）
     */
    public static String sha384(byte[] data) {
        return hash(data, "SHA-384");
    }

    /**
     * 计算文件的SHA-384值
     * 
     * @param filePath 文件路径
     * @return SHA-384值（96位小写十六进制）
     */
    public static String sha384File(String filePath) {
        return hashFile(filePath, "SHA-384");
    }

    // ==================== SHA-512算法 ====================

    /**
     * 计算字符串的SHA-512值
     * 
     * @param data 输入字符串
     * @return SHA-512值（128位小写十六进制）
     */
    public static String sha512(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        return sha512(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算字节数组的SHA-512值
     * 
     * @param data 输入字节数组
     * @return SHA-512值（128位小写十六进制）
     */
    public static String sha512(byte[] data) {
        return hash(data, "SHA-512");
    }

    /**
     * 计算文件的SHA-512值
     * 
     * @param filePath 文件路径
     * @return SHA-512值（128位小写十六进制）
     */
    public static String sha512File(String filePath) {
        return hashFile(filePath, "SHA-512");
    }

    // ==================== 通用方法 ====================

    /**
     * 验证字符串的Hash值是否匹配
     * 
     * @param data 原始数据
     * @param expectedHash 期望的Hash值
     * @param algorithm 算法名称（MD5、SHA-1、SHA-256等）
     * @return 是否匹配
     */
    public static boolean verify(String data, String expectedHash, String algorithm) {
        if (data == null || expectedHash == null || algorithm == null) {
            return false;
        }
        
        try {
            String actualHash = hash(data.getBytes(StandardCharsets.UTF_8), algorithm);
            return actualHash.equalsIgnoreCase(expectedHash);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证文件的Hash值是否匹配
     * 
     * @param filePath 文件路径
     * @param expectedHash 期望的Hash值
     * @param algorithm 算法名称（MD5、SHA-1、SHA-256等）
     * @return 是否匹配
     */
    public static boolean verifyFile(String filePath, String expectedHash, String algorithm) {
        if (filePath == null || expectedHash == null || algorithm == null) {
            return false;
        }
        
        try {
            String actualHash = hashFile(filePath, algorithm);
            return actualHash.equalsIgnoreCase(expectedHash);
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== 内部辅助方法 ====================

    /**
     * 计算字节数组的Hash值
     * 
     * @param data 输入字节数组
     * @param algorithm 算法名称
     * @return Hash值（小写十六进制）
     */
    private static String hash(byte[] data, String algorithm) {
        byte[] hashBytes = hashBytes(data, algorithm);
        return bytesToHex(hashBytes);
    }

    /**
     * 计算字节数组的Hash值（返回字节数组）
     * 
     * @param data 输入字节数组
     * @param algorithm 算法名称
     * @return Hash值字节数组
     */
    private static byte[] hashBytes(byte[] data, String algorithm) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            return digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(algorithm + "算法不支持", e);
        }
    }

    /**
     * 计算文件的Hash值（流式处理）
     * 
     * @param filePath 文件路径
     * @param algorithm 算法名称
     * @return Hash值（小写十六进制）
     */
    private static String hashFile(String filePath, String algorithm) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            
            try (FileInputStream fis = new FileInputStream(filePath);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {
                
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                
                while ((bytesRead = bis.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }
            
            return bytesToHex(digest.digest());
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(algorithm + "算法不支持", e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("文件不存在: " + filePath, e);
        } catch (IOException e) {
            throw new RuntimeException("文件读取失败: " + e.getMessage(), e);
        }
    }

    /**
     * 字节数组转十六进制字符串
     * 
     * @param bytes 字节数组
     * @return 十六进制字符串（小写）
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
} 