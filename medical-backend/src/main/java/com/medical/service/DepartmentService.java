package com.medical.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medical.common.PageResult;
import com.medical.dto.response.DepartmentVO;
import com.medical.entity.Department;
import com.medical.mapper.DepartmentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 科室服务
 *
 * @author medical-system
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 获取科室树形列表
     */
    public List<DepartmentVO> getDepartmentTree() {
        // 查询所有科室
        List<Department> allDepartments = departmentMapper.selectList(
                new LambdaQueryWrapper<Department>()
                        .orderByAsc(Department::getSort)
        );

        // 构建树形结构
        return buildTree(allDepartments, 0L);
    }

    /**
     * 构建树形结构
     */
    private List<DepartmentVO> buildTree(List<Department> allDepartments, Long parentId) {
        List<DepartmentVO> result = new ArrayList<>();

        for (Department department : allDepartments) {
            if (department.getParentId().equals(parentId)) {
                DepartmentVO vo = new DepartmentVO();
                BeanUtils.copyProperties(department, vo);

                // 递归查找子科室
                List<DepartmentVO> children = buildTree(allDepartments, department.getId());
                if (!children.isEmpty()) {
                    vo.setChildren(children);
                }

                result.add(vo);
            }
        }

        return result;
    }

    /**
     * 获取所有科室（平铺列表）
     */
    public List<DepartmentVO> getAllDepartments() {
        List<Department> departments = departmentMapper.selectList(
                new LambdaQueryWrapper<Department>()
                        .orderByAsc(Department::getSort)
        );

        return departments.stream().map(dept -> {
            DepartmentVO vo = new DepartmentVO();
            BeanUtils.copyProperties(dept, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取科室列表（管理端，分页）
     */
    public PageResult<Department> getDepartmentList(String type, Integer page, Integer pageSize) {
        Page<Department> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        
        if (type != null && !type.isEmpty()) {
            // 根据类型筛选（例如：只显示一级科室或二级科室）
            if ("parent".equals(type)) {
                wrapper.eq(Department::getParentId, 0);
            } else if ("child".equals(type)) {
                wrapper.ne(Department::getParentId, 0);
            }
        }
        
        wrapper.orderByAsc(Department::getSort);
        
        Page<Department> pageResult = departmentMapper.selectPage(pageParam, wrapper);
        return new PageResult<>(pageResult.getTotal(), pageResult.getRecords());
    }
}
