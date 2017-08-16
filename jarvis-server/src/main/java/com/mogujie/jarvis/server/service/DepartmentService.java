/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年3月24日 上午11:11:06
 */

package com.mogujie.jarvis.server.service;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mogujie.jarvis.dao.generate.DepartmentBizMapMapper;
import com.mogujie.jarvis.dao.generate.DepartmentMapper;
import com.mogujie.jarvis.dto.generate.Department;
import com.mogujie.jarvis.dto.generate.DepartmentBizMap;
import com.mogujie.jarvis.dto.generate.DepartmentBizMapExample;
import com.mogujie.jarvis.dto.generate.DepartmentBizMapKey;
import com.mogujie.jarvis.dto.generate.DepartmentExample;

/**
 * @author guangming
 *
 */
@Singleton
public class DepartmentService {

    @Inject
    private DepartmentMapper departmentMapper;

    @Inject
    private DepartmentBizMapMapper mapMapper;

    @Inject
    private DepartmentBizMapMapper departmentBizMapper;

    public Department get(int id) {
        return departmentMapper.selectByPrimaryKey(id);
    }

    public Department getByName(String name) {
        DepartmentExample example = new DepartmentExample();
        example.createCriteria().andNameEqualTo(name);
        List<Department> departments = departmentMapper.selectByExample(example);
        if (departments != null && !departments.isEmpty()) {
            return departments.get(0);
        } else {
            return null;
        }
    }

    public void insert(Department record) {
        departmentMapper.insert(record);
    }

    public void deleteDepartmen(int departmentId) {
        departmentMapper.deleteByPrimaryKey(departmentId);
        DepartmentBizMapExample example = new DepartmentBizMapExample();
        example.createCriteria().andDepartmentIdEqualTo(departmentId);
        mapMapper.deleteByExample(example);
    }

    public void update(Department record) {
        departmentMapper.updateByPrimaryKeySelective(record);
    }

    public void insertMap(DepartmentBizMap record) {
        mapMapper.insert(record);
    }

    public void deleteMap(int departmentId, int bizId) {
        DepartmentBizMapKey key = new DepartmentBizMapKey();
        key.setDepartmentId(departmentId);
        key.setBizId(bizId);
        mapMapper.deleteByPrimaryKey(key);
    }

    public List<Integer> getBizIdsByDepartmentId(int departmentId) {
        DepartmentBizMapExample example = new DepartmentBizMapExample();
        example.createCriteria().andDepartmentIdEqualTo(departmentId);
        List<DepartmentBizMap> maps = mapMapper.selectByExample(example);
        List<Integer> bizIds = new ArrayList<Integer>();
        if (maps != null) {
            for (DepartmentBizMap map : maps) {
                bizIds.add(map.getBizId());
            }
        }
        return bizIds;
    }

    public void checkDuplicateName(String name, Integer ignoreId) throws IllegalArgumentException {
        DepartmentExample example = new DepartmentExample();
        example.createCriteria().andNameEqualTo(name);
        List<Department> list = departmentMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            if (ignoreId != null && list.size() == 1 && list.get(0).getId().equals(ignoreId)) {
                return;
            }
            throw new IllegalArgumentException("department 名称重复了. name:" + name);
        }
    }

    public void checkDuplicateMap(Integer bizId, Integer departmentId) throws IllegalArgumentException {
        DepartmentBizMapExample example = new DepartmentBizMapExample();
        example.createCriteria().andBizIdEqualTo(bizId).andDepartmentIdEqualTo(departmentId);
        List<DepartmentBizMap> list = this.departmentBizMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            throw new IllegalArgumentException(String.format("department_biz_map 复合主键重复. bizId:%s;departmentId:%s", bizId, departmentId));
        }
    }

    public void deleteMapByDepartmentId(Integer departmentId) {
        DepartmentBizMapExample example = new DepartmentBizMapExample();
        example.createCriteria().andDepartmentIdEqualTo(departmentId);
        this.departmentBizMapper.deleteByExample(example);
    }

    public void deleteMapByBizGroupId(Integer bizGroupId) {
        DepartmentBizMapExample example = new DepartmentBizMapExample();
        example.createCriteria().andBizIdEqualTo(bizGroupId);
        this.departmentBizMapper.deleteByExample(example);
    }
}

