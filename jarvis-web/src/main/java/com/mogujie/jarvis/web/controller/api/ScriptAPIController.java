package com.mogujie.jarvis.web.controller.api;

import com.mogujie.jarvis.web.entity.vo.ScriptVo;
import com.mogujie.jarvis.web.service.ScriptService;
import com.mogujie.jarvis.web.utils.MessageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author muming
 */
@Controller
@RequestMapping(value = "/api/script")
public class ScriptAPIController {

    @Autowired
    private ScriptService scriptService;

    /**
     * 检索Script
     * @return
     */
    @RequestMapping("/queryScript")
    @ResponseBody
    public Object queryScript( @RequestParam(defaultValue = "") String name) {

        Map<String, Object> map = new HashMap<>();
        try {
            if(name != null){
                name = name.trim();
            }
            List<ScriptVo> list = scriptService.queryScript(name);
            map.put("code", MessageStatus.SUCCESS.getValue());
            map.put("msg", MessageStatus.SUCCESS.getText());
            map.put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", e.getMessage());
        }
        return map;
    }

    /**
     * 检索Script
     * @return
     */
    @RequestMapping("/getScriptById")
    @ResponseBody
    public Object queryScript(Integer id) {

        Map<String, Object> map = new HashMap<>();
        try {
            ScriptVo vo = scriptService.getScriptById(id);
            map.put("code", MessageStatus.SUCCESS.getValue());
            map.put("msg", MessageStatus.SUCCESS.getText());
            map.put("data", vo);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", e.getMessage());
        }
        return map;
    }



}
