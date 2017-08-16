package com.mogujie.jarvis.rest.controller;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mogujie.jarvis.core.domain.WorkerStatus;
import com.mogujie.jarvis.rest.utils.JsonParameters;
import com.mogujie.jarvis.core.domain.AkkaType;
import com.mogujie.jarvis.protocol.AppAuthProtos.AppAuth;
import com.mogujie.jarvis.protocol.WorkerProtos.RestServerModifyWorkerStatusRequest;
import com.mogujie.jarvis.protocol.WorkerProtos.ServerModifyWorkerStatusResponse;
import com.mogujie.jarvis.protocol.HeartBeatProtos.RestQueryWorkerHeartbeatInfoRequest;
import com.mogujie.jarvis.protocol.HeartBeatProtos.ServerQueryWorkerHeartbeatInfoResponse;
import com.mogujie.jarvis.protocol.HeartBeatProtos.WorkerHeartbeatEntry;
import com.mogujie.jarvis.rest.RestResult;
import com.mogujie.jarvis.rest.vo.WorkerHeartbeatVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author muming, hejian
 */
@Path("api/worker")
public class WorkerController extends AbstractController {

    /**
     * 设置worker状态
     */
    @POST
    @Path("status/set")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult statusSet(
            @FormParam("appName") String appName
            , @FormParam("appToken") String appToken
            , @FormParam("user") String user
            , @FormParam("parameters") String parameters) {
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();
            JsonParameters para = new JsonParameters(parameters);
            String ip = para.getStringNotEmpty("ip");
            int port = para.getIntegerNotNull("port");
            int status = para.getIntegerNotNull("status");
            if (!WorkerStatus.isValid(status)) {
                throw new IllegalArgumentException("status值不合法。value:" + status);
            }

            RestServerModifyWorkerStatusRequest request = RestServerModifyWorkerStatusRequest.newBuilder()
                    .setAppAuth(appAuth)
                    .setIp(ip)
                    .setPort(port)
                    .setStatus(status).build();

            ServerModifyWorkerStatusResponse response = (ServerModifyWorkerStatusResponse) callActor(AkkaType.SERVER, request);
            return response.getSuccess() ? successResult() : errorResult(response.getMessage());
        } catch (Exception e) {
            LOGGER.error("", e);
            return errorResult(e);
        }
    }

    /**
     * 获得worker心跳情况
     */
    @POST
    @Path("heartbeat/get")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult heartbeatGet(
            @FormParam("appName") String appName
            , @FormParam("appToken") String appToken
            , @FormParam("user") String user
            , @FormParam("parameters") String parameters) {
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();
            JsonParameters para = new JsonParameters(parameters);
            int workerGroupId = para.getInteger("workerGroupId",0);

            RestQueryWorkerHeartbeatInfoRequest request = RestQueryWorkerHeartbeatInfoRequest.newBuilder()
                    .setAppAuth(appAuth)
                    .setWorkerGroupId(workerGroupId).build();
            ServerQueryWorkerHeartbeatInfoResponse response = (ServerQueryWorkerHeartbeatInfoResponse) callActor(AkkaType.SERVER, request);

            if (response.getSuccess()) {
                WorkerHeartbeatVo vo = new WorkerHeartbeatVo();
                if (response.getInfoList() != null) {
                    List<WorkerHeartbeatVo.WorkerHeartbeatEntry> list = new ArrayList<>();
                    for (WorkerHeartbeatEntry entry : response.getInfoList()) {
                        list.add(new WorkerHeartbeatVo.WorkerHeartbeatEntry().setIp(entry.getIp())
                                .setPort(entry.getPort()).setTaskNum(entry.getTaskNum()));
                    }
                    vo.setList(list);
                }
                return successResult(vo);
            } else {
                return errorResult(response.getMessage());
            }
        } catch (Exception e) {
            LOGGER.error("", e);
            return errorResult(e);
        }
    }


}
