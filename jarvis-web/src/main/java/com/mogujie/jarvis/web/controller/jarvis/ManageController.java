package com.mogujie.jarvis.web.controller.jarvis;

//import com.mogu.bigdata.admin.client.annotation.Passport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mogujie.jarvis.web.auth.conf.JarvisAuthType;
import com.mogujie.jarvis.web.entity.vo.AppVo;
import com.mogujie.jarvis.web.entity.vo.WorkerGroupVo;
import com.mogujie.jarvis.web.entity.vo.WorkerVo;
import com.mogujie.jarvis.web.service.AppService;
import com.mogujie.jarvis.web.service.WorkerGroupService;
import com.mogujie.jarvis.web.service.WorkerService;


/**
 * Created by hejian on 15/9/15.
 */
@Controller
@RequestMapping("/manage")
public class ManageController {

    @Autowired
    AppService appService;
    @Autowired
    WorkerService workerService;
    @Autowired
    WorkerGroupService workerGroupService;

    @RequestMapping(value = "system")
//    @Passport(JarvisAuthType.manage_system)
    public String system() {
        return "manage/system";
    }

    @RequestMapping(value = "app")
//    @Passport(JarvisAuthType.manage_app)
    public String app(ModelMap modelMap) {
        List<String> appNameList = appService.getAllAppName();
        modelMap.put("appNameList", appNameList);
        return "manage/app";
    }

    @RequestMapping(value = "appDetail")
//    @Passport(JarvisAuthType.manage_app)
    public String appDetail(ModelMap modelMap, Integer appId) throws Exception {
        modelMap.put("appId", appId);
        return "manage/appDetail";
    }

    @RequestMapping(value = "checkAppName")
    @ResponseBody
    public Map<String, Object> checkAppName(Integer appId, String appName) {
        Map<String, Object> result = new HashMap<String, Object>();

        AppVo appVo = appService.getAppByName(appName);
        if (appVo == null) {
            result.put("code", 0);
            result.put("msg", "此应用名不存在");
        } else {
            if (appId == null) {
                result.put("code", 1);
                result.put("msg", "已经存在此名字应用:" + appName);
            } else {
                if (appVo.getAppId().equals(appId)) {
                    result.put("code", 0);
                    result.put("msg", "自身,可以保存");
                } else {
                    result.put("code", 1);
                    result.put("msg", "已经存在此名字其他应用:" + appName);
                }
            }
        }

        return result;
    }


    @RequestMapping(value = "worker")
//    @Passport(JarvisAuthType.manage_worker)
    public String worker(ModelMap modelMap) {
        List<WorkerGroupVo> workerGroupVoList = workerGroupService.getAllWorkerGroup();
        List<String> ipList = workerService.getAllWorkerIp();
        List<Integer> portList = workerService.getAllWorkerPort();

        modelMap.put("workerGroupVoList", workerGroupVoList);
        modelMap.put("ipList", ipList);
        modelMap.put("portList", portList);


        return "manage/worker";
    }

    @RequestMapping(value = "workerAddOrEdit")
//    @Passport(JarvisAuthType.manage_worker)
    public String workerAddOrEdit(ModelMap modelMap, Integer id) {
        if (id != null) {
            WorkerVo workerVo = workerService.getWorkerById(id);
            modelMap.put("workerVo", workerVo);
        }
        List<WorkerGroupVo> workerGroupVoList = workerGroupService.getAllWorkerGroup();
        modelMap.put("workerGroupVoList", workerGroupVoList);
        return "manage/workerAddOrEdit";
    }

    @RequestMapping(value = "checkWorkerExist")
    @ResponseBody
    public Map<String, Object> checkWorkerExist(ModelMap modelMap, Integer id, String ip, Integer port) {
        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> para = new HashMap<String, Object>();
        para.put("ip", ip);
        para.put("port", port);
        WorkerVo workerVo = workerService.getWorkerByIpAndPort(para);

        if (workerVo == null) {
            result.put("code", 0);
            result.put("msg", "不存在，可保存");
        } else {
            if (id != null) {
                if (workerVo.getId().equals(id)) {
                    result.put("code", 0);
                    result.put("msg", "自身,可保存");
                } else {
                    result.put("code", 1);
                    result.put("msg", "已存在IP为" + ip + ",端口为" + port + "的worker,不能更新");
                }
            } else {
                result.put("code", 1);
                result.put("msg", "已存在IP为" + ip + ",端口为" + port + "的worker,不能新增");
            }
        }


        return result;
    }


    @RequestMapping(value = "workerGroupAddOrEdit")
//    @Passport(JarvisAuthType.manage_worker)
    public String workerGroupAddOrEdit(ModelMap modelMap, Integer id) {
        if (id != null) {
            WorkerGroupVo workerGroupVo = workerGroupService.getWorkerGroupById(id);
            modelMap.put("workerGroupVo", workerGroupVo);
        }
        return "manage/workerGroupAddOrEdit";
    }

    @RequestMapping(value = "checkWorkerGroupName")
    @ResponseBody
    public Map<String, Object> checkWorkerGroupName(ModelMap modelMap, Integer id, String name) {
        Map<String, Object> result = new HashMap<String, Object>();

        WorkerGroupVo workerGroupVo = workerGroupService.getWorkerGroupByName(name);
        if (workerGroupVo == null) {
            result.put("code", 0);
            result.put("msg", "不存在，可保存");
        } else {
            if (id != null) {
                if (workerGroupVo.getId().equals(id)) {
                    result.put("code", 0);
                    result.put("msg", "自身,可保存");
                } else {
                    result.put("code", 1);
                    result.put("msg", "已存在名为" + name + "的worker group,不能更新");
                }
            } else {
                result.put("code", 1);
                result.put("msg", "已存在名为" + name + "的worker group,不能新增");
            }
        }
        return result;
    }

    //业务类型首页
    @RequestMapping(value = "/bizGroup")
//    @Passport(JarvisAuthType.manage_biz)
    public String bizGroup(){
        return "manage/bizGroup";
    }
    //单个业务类型详情
    @RequestMapping(value = "bizDetail")
//    @Passport(JarvisAuthType.manage_biz)
    public String bizDetail(ModelMap modelMap,Integer id){
        modelMap.put("id",id);
        return "manage/bizDetail";
    }

}
