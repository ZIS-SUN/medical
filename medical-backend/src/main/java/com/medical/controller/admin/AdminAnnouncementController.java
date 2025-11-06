package com.medical.controller.admin;

import com.medical.common.PageResult;
import com.medical.common.Result;
import com.medical.entity.Announcement;
import com.medical.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端公告控制器
 *
 * @author medical-system
 */
@RestController
@RequestMapping("/api/admin/announcements")
@Tag(name = "管理端-公告管理", description = "管理端公告管理接口")
public class AdminAnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping
    @Operation(summary = "获取公告列表")
    public Result<PageResult<Announcement>> getAnnouncementList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult<Announcement> result = announcementService.getAnnouncementList(page, pageSize);
        return Result.success(result);
    }

    @PostMapping
    @Operation(summary = "创建公告")
    public Result<Void> createAnnouncement(@Validated @RequestBody Announcement announcement) {
        announcementService.createAnnouncement(announcement);
        return Result.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新公告")
    public Result<Void> updateAnnouncement(
            @Parameter(description = "公告ID") @PathVariable Long id,
            @Validated @RequestBody Announcement announcement
    ) {
        announcementService.updateAnnouncement(id, announcement);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除公告")
    public Result<Void> deleteAnnouncement(@Parameter(description = "公告ID") @PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return Result.success();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "切换公告状态")
    public Result<Void> toggleAnnouncementStatus(
            @Parameter(description = "公告ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status
    ) {
        announcementService.toggleAnnouncementStatus(id, status);
        return Result.success();
    }
}
