package com.medical.controller;

import com.medical.common.Result;
import com.medical.entity.Announcement;
import com.medical.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公告控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/announcements")
@Tag(name = "公告接口", description = "公告查询相关接口")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping
    @Operation(summary = "获取公告列表")
    public Result<List<Announcement>> getAnnouncementList() {
        List<Announcement> list = announcementService.getAnnouncementList();
        return Result.success(list);
    }
}
