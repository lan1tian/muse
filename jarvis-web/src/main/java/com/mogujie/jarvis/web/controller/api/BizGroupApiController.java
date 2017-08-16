package com.mogujie.jarvis.web.controller.api;

import com.mogujie.jarvis.core.domain.BizGroupStatus;
import com.mogujie.jarvis.web.entity.qo.BizGroupQo;
import com.mogujie.jarvis.web.entity.vo.BizGroupVo;
import com.mogujie.jarvis.web.service.BizGroupService;
import com.mogujie.jarvis.web.utils.MessageStatus;
import com.mogujie.jarvis.web.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by hejian on 16/1/13.
 */
@Controller
@RequestMapping(value = "/api/bizGroup")
public class BizGroupApiController {
    @Autowired
    BizGroupService bizGroupService;

    /*
    * 根据id获取bizGroup的相信信息
    * */
    @RequestMapping(value = "getById")
    @ResponseBody
    public Object getById(BizGroupQo bizGroupQo) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null == bizGroupQo.getId()) {
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", "未传入id");
            map.put("supportFields", Tools.getObjectField(BizGroupQo.class));
            return map;
        }
        try {
            BizGroupVo bizGroupVo = bizGroupService.getById(bizGroupQo.getId());
            map.put("code", MessageStatus.SUCCESS.getValue());
            map.put("msg", MessageStatus.SUCCESS.getText());
            map.put("data", bizGroupVo);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", e.getMessage());
        }

        return map;
    }

    /*
    * 获取满足条件的所有bizGroup
    * */
    @RequestMapping(value = "getAllByCondition")
    @ResponseBody
    public Object getAll(BizGroupQo bizGroupQo) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<BizGroupVo> list = bizGroupService.getAllByCondition(bizGroupQo);
            map.put("code", MessageStatus.SUCCESS.getValue());
            map.put("msg", MessageStatus.SUCCESS.getText());
            map.put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", MessageStatus.SUCCESS.getText());
        }
        return map;
    }

    /*
    * 分页查询
    * */
    @RequestMapping(value = "getPaginationByCondition")
    @ResponseBody
    public Object getPaginationByCondition(BizGroupQo bizGroupQo) {
        Map<String, Object> map = bizGroupService.getPaginationByCondition(bizGroupQo);
        return map;
    }

    /*
    * 获取业务类型的状态
    * */
    @RequestMapping(value = "getBizGroupStatus")
    @ResponseBody
    public Object getBizGroupStatus() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        BizGroupStatus[] bizGroupStatuses = BizGroupStatus.values();
        for (BizGroupStatus bizGroupStatus : bizGroupStatuses) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", bizGroupStatus.getValue());
            map.put("text", bizGroupStatus.getDescription());
            list.add(map);
        }

        return list;
    }


    /*
    * 检查是否存在此业务名的业务类型
    * */
    @RequestMapping(value = "getByName")
    @ResponseBody
    public Object getByName(String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        BizGroupVo bizGroupVo = bizGroupService.getByName(name);
        map.put("data", bizGroupVo);
        return map;
    }

    /*
    * 获取业务类型名，预计业务类型名不会太多，所以全量获取
    * */
    @RequestMapping(value = "getBizGroupName")
    @ResponseBody
    public Object getBizGroupName() {
        List<String> list = bizGroupService.getAllName();
        return list;
    }

    /*
    * 获取维护用户名列表
    * */
    @RequestMapping(value = "getBizGroupOwner")
    @ResponseBody
    public Object getBizGroupOwner() {
        Set<String> set = bizGroupService.getAllOwner();
        return set;
    }


}
