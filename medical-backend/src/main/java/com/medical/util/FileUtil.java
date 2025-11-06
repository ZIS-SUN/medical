package com.medical.util;

import com.medical.common.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传工具类
 *
 * @author medical-system
 */
@Component
public class FileUtil {

    @Value("${file.upload.path}")
    private String uploadPath;

    /**
     * 允许的文件扩展名
     */
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png");

    /**
     * 最大文件大小（5MB）
     */
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件访问路径
     */
    public String upload(MultipartFile file) throws IOException {
        // 1. 校验文件
        if (file.isEmpty()) {
            throw new BusinessException(400, "文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException(400, "文件名不能为空");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 白名单校验
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new BusinessException(400, "只支持jpg/png格式");
        }

        // 大小校验
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(400, "文件大小不能超过5MB");
        }

        // 2. 生成文件名
        String filename = UUID.randomUUID().toString().replace("-", "") + extension;
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fullPath = uploadPath + "/" + datePath;

        // 3. 创建目录
        File dir = new File(fullPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 4. 保存文件
        File dest = new File(dir, filename);
        file.transferTo(dest);

        // 5. 返回访问路径
        return "/uploads/" + datePath + "/" + filename;
    }
}
