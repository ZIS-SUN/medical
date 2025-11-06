package com.medical.controller;

import com.medical.common.Result;
import com.medical.util.FileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/upload")
@Tag(name = "文件上传接口", description = "文件上传相关接口")
public class FileController {

    @Autowired
    private FileUtil fileUtil;

    @PostMapping
    @Operation(summary = "上传文件")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        try {
            String url = fileUtil.upload(file);

            Map<String, String> data = new HashMap<>();
            data.put("url", url);

            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
