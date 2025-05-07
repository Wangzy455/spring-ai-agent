package com.wzy.springaiagent.common.pojo.enums;

/**
 * 文件类型枚举
 */
public enum FileType {
    IMAGE("图片", "jpg, png, gif, bmp"),
    VIDEO("视频", "mp4, avi, mov, mkv"),
    DOCUMENT("文档", "doc, docx, pdf, txt"),
    AUDIO("音频", "mp3, wav, flac"),
    ARCHIVE("压缩文件", "zip, rar, tar, gz"),
    UNKNOWN("未知", "");

    private final String description;
    private final String extensions;

    FileType(String description, String extensions) {
        this.description = description;
        this.extensions = extensions;
    }

    public String getDescription() {
        return description;
    }

    public String getExtensions() {
        return extensions;
    }

    /**
     * 根据文件扩展名获取文件类型
     * @param extension 文件扩展名
     * @return 对应的FileType枚举值，如果找不到则返回UNKNOWN
     */
    public static FileType fromExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return UNKNOWN;
        }
        for (FileType fileType : FileType.values()) {
            if (fileType.extensions.contains(extension.toLowerCase())) {
                return fileType;
            }
        }
        return UNKNOWN;
    }
}
