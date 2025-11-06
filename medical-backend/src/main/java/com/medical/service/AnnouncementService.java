package com.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medical.common.BusinessException;
import com.medical.common.PageResult;
import com.medical.entity.Announcement;
import com.medical.mapper.AnnouncementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 公告服务
 *
 * @author medical-system
 */
@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    /**
     * 获取公告列表（公开接口，只显示已发布的）
     */
    public List<Announcement> getAnnouncementList() {
        return announcementMapper.selectList(
                new LambdaQueryWrapper<Announcement>()
                        .eq(Announcement::getStatus, 1)
                        .orderByDesc(Announcement::getPublishTime)
                        .last("LIMIT 10")
        );
    }

    /**
     * 获取公告列表（管理端，分页查询所有公告）
     */
    public PageResult<Announcement> getAnnouncementList(Integer page, Integer pageSize) {
        Page<Announcement> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Announcement::getPublishTime);
        
        Page<Announcement> pageResult = announcementMapper.selectPage(pageParam, wrapper);
        return new PageResult<>(pageResult.getTotal(), pageResult.getRecords());
    }

    /**
     * 创建公告
     */
    public void createAnnouncement(Announcement announcement) {
        if (announcement.getStatus() == 1) {
            announcement.setPublishTime(LocalDateTime.now());
        }
        announcementMapper.insert(announcement);
    }

    /**
     * 更新公告
     */
    public void updateAnnouncement(Long id, Announcement announcement) {
        Announcement existingAnnouncement = announcementMapper.selectById(id);
        if (existingAnnouncement == null) {
            throw new BusinessException(404, "公告不存在");
        }
        
        announcement.setId(id);
        
        // 如果从未发布改为发布，设置发布时间
        if (existingAnnouncement.getStatus() == 0 && announcement.getStatus() == 1) {
            announcement.setPublishTime(LocalDateTime.now());
        }
        
        announcementMapper.updateById(announcement);
    }

    /**
     * 删除公告
     */
    public void deleteAnnouncement(Long id) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new BusinessException(404, "公告不存在");
        }
        announcementMapper.deleteById(id);
    }

    /**
     * 切换公告状态（发布/取消发布）
     */
    public void toggleAnnouncementStatus(Long id, Integer status) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new BusinessException(404, "公告不存在");
        }
        
        Announcement update = new Announcement();
        update.setId(id);
        update.setStatus(status);
        
        // 如果是发布操作且之前未发布，设置发布时间
        if (status == 1 && announcement.getStatus() == 0) {
            update.setPublishTime(LocalDateTime.now());
        }
        
        announcementMapper.updateById(update);
    }
}
