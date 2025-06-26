# 📁 FileUtils Documentation

## Overview

FileUtils is a comprehensive file operation utility class providing file read/write, directory management, file copy/move, temporary file handling, and supports multiple encoding formats and file types.

## 🚀 Quick Start

### Basic File Read/Write

```java
// Read file content as string
String content = FileUtils.readString("path/to/file.txt");

// Write string to file
FileUtils.writeString("path/to/output.txt", "Hello World");

// Read byte array
byte[] bytes = FileUtils.readBytes("path/to/binary.dat");

// Write byte array
FileUtils.writeBytes("path/to/output.bin", bytes);
```

### File Operations with Encoding

```java
// Read file with specified encoding
String content = FileUtils.readString("path/to/file.txt", "GBK");

// Write file with specified encoding
FileUtils.writeString("path/to/output.txt", "Chinese content", "UTF-8");

// Read file by lines
List<String> lines = FileUtils.readLines("path/to/file.txt");

// Write multiple lines
List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
FileUtils.writeLines("path/to/output.txt", lines);
```

## 📂 Directory Operations

### Directory Creation and Deletion

```java
// Create directory
boolean created = FileUtils.createDirectory("path/to/new/dir");

// Create nested directories
boolean created = FileUtils.createDirectories("path/to/deep/nested/dirs");

// Delete empty directory
boolean deleted = FileUtils.deleteDirectory("path/to/empty/dir");

// Recursively delete directory and its contents
boolean deleted = FileUtils.deleteDirectoryRecursively("path/to/dir");

// Clear directory (keep directory itself)
boolean cleared = FileUtils.clearDirectory("path/to/dir");
```

### Directory Traversal

```java
// List all files in directory
List<String> files = FileUtils.listFiles("path/to/directory");

// List all subdirectories
List<String> dirs = FileUtils.listDirectories("path/to/directory");

// Recursively list all files
List<String> allFiles = FileUtils.listFilesRecursively("path/to/directory");

// Filter files by extension
List<String> txtFiles = FileUtils.listFilesByExtension("path/to/directory", "txt");

// Filter files by pattern
List<String> logFiles = FileUtils.listFilesByPattern("path/to/logs", "*.log");
```

### Directory Information

```java
// Calculate directory size
long dirSize = FileUtils.getDirectorySize("path/to/directory");

// Count files in directory
int fileCount = FileUtils.countFiles("path/to/directory");

// Count subdirectories
int dirCount = FileUtils.countDirectories("path/to/directory");

// Recursively count files
int totalFiles = FileUtils.countFilesRecursively("path/to/directory");

// Get directory tree structure
String treeStructure = FileUtils.getDirectoryTree("path/to/directory");
```

## 📋 File Management Operations

### File Copy

```java
// Copy file
boolean copied = FileUtils.copyFile("source.txt", "destination.txt");

// Copy to directory
boolean copied = FileUtils.copyFileToDirectory("file.txt", "target/directory/");

// Copy directory
boolean copied = FileUtils.copyDirectory("source/dir", "target/dir");

// Copy with file attributes preserved
boolean copied = FileUtils.copyFileWithAttributes("source.txt", "target.txt");
```

### File Move and Rename

```java
// Move file
boolean moved = FileUtils.moveFile("old/path/file.txt", "new/path/file.txt");

// Move to directory
boolean moved = FileUtils.moveFileToDirectory("file.txt", "target/directory/");

// Rename file
boolean renamed = FileUtils.renameFile("old-name.txt", "new-name.txt");

// Move directory
boolean moved = FileUtils.moveDirectory("old/directory", "new/directory");
```

### File Deletion

```java
// Delete file
boolean deleted = FileUtils.deleteFile("path/to/file.txt");

// Safe delete (no exception thrown)
boolean deleted = FileUtils.deleteFileSafely("path/to/file.txt");

// Batch delete files
List<String> filesToDelete = Arrays.asList("file1.txt", "file2.txt", "file3.txt");
int deletedCount = FileUtils.deleteFiles(filesToDelete);

// Delete files by pattern
int deletedCount = FileUtils.deleteFilesByPattern("temp/", "*.tmp");
```

## 📄 File Information

### Basic File Information

```java
// Check if file exists
boolean exists = FileUtils.fileExists("path/to/file.txt");

// Check if is file
boolean isFile = FileUtils.isFile("path/to/item");

// Check if is directory
boolean isDirectory = FileUtils.isDirectory("path/to/item");

// Get file size
long size = FileUtils.getFileSize("path/to/file.txt");

// Get formatted file size
String formattedSize = FileUtils.getFormattedFileSize("path/to/file.txt");
// Output: "1.5 MB", "256 KB", "3.2 GB", etc.
```

### File Time Information

```java
// Get file last modified time
long lastModified = FileUtils.getLastModifiedTime("path/to/file.txt");

// Get file creation time
long creationTime = FileUtils.getCreationTime("path/to/file.txt");

// Get file last access time
long lastAccessTime = FileUtils.getLastAccessTime("path/to/file.txt");

// Get formatted time display
String modifiedTimeStr = FileUtils.getFormattedLastModifiedTime("path/to/file.txt");
```

### File Path Operations

```java
// Get file name (with extension)
String fileName = FileUtils.getFileName("path/to/file.txt"); // "file.txt"

// Get base name (without extension)
String baseName = FileUtils.getBaseName("path/to/file.txt"); // "file"

// Get file extension
String extension = FileUtils.getExtension("path/to/file.txt"); // "txt"

// Get parent directory path
String parentDir = FileUtils.getParentDirectory("path/to/file.txt"); // "path/to"

// Get absolute path
String absolutePath = FileUtils.getAbsolutePath("relative/path/file.txt");

// Normalize path
String normalizedPath = FileUtils.normalizePath("path/../to/./file.txt"); // "to/file.txt"
```

## 🗂️ Temporary File Management

### Temporary File Creation

```java
// Create temporary file
String tempFile = FileUtils.createTempFile();

// Create temporary file with prefix
String tempFile = FileUtils.createTempFile("myapp-");

// Create temporary file with prefix and suffix
String tempFile = FileUtils.createTempFile("data-", ".tmp");

// Create temporary file in specified directory
String tempFile = FileUtils.createTempFileInDirectory("temp/", "cache-", ".dat");

// Create temporary directory
String tempDir = FileUtils.createTempDirectory();

// Create temporary directory with prefix
String tempDir = FileUtils.createTempDirectory("workspace-");
```

### Temporary File Management

```java
// Cleanup temporary files
FileUtils.cleanupTempFiles();

// Cleanup temporary files in specified directory
FileUtils.cleanupTempFiles("temp/");

// Cleanup expired temporary files (older than specified hours)
FileUtils.cleanupExpiredTempFiles(24); // Clean files older than 24 hours

// Get system temporary directory
String systemTempDir = FileUtils.getSystemTempDirectory();
```

## 📝 Notes

- All file paths support both relative and absolute paths
- Default encoding is UTF-8, recommend explicitly specifying encoding format
- Large file operations may consume significant memory, pay attention to memory management
- File operation failures will throw RuntimeException
- Temporary files are automatically cleaned up when JVM exits
- Directory operations automatically create necessary parent directories
- File move is actually a rename operation; cross-disk moves will copy then delete 