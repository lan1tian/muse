package com.mogujie.jarvis.rest.controller;

import com.mogujie.jarvis.core.domain.*;
import com.mogujie.jarvis.core.util.JsonHelper;

import com.mogujie.jarvis.protocol.AppAuthProtos.AppAuth;
import com.mogujie.jarvis.protocol.AlarmProtos.RestCreateAlarmRequest;
import com.mogujie.jarvis.protocol.AlarmProtos.ServerCreateAlarmResponse;
import com.mogujie.jarvis.protocol.AlarmProtos.RestModifyAlarmRequest;
import com.mogujie.jarvis.protocol.AlarmProtos.ServerModifyAlarmResponse;
import com.mogujie.jarvis.protocol.AlarmProtos.RestDeleteAlarmRequest;
import com.mogujie.jarvis.protocol.AlarmProtos.ServerDeleteAlarmResponse;

import com.mogujie.jarvis.rest.RestResult;
import com.mogujie.jarvis.rest.utils.ValidUtils;
import com.mogujie.jarvis.rest.vo.AlarmVo;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author muming
 */
@Path("api/alarm")
public class AlarmController extends AbstractController {


//    /**
//     * 追加——报警信息
//     */
//    @POST
//    @Path("query")
//    @Produces(MediaType.APPLICATION_JSON)
//    public RestResult query(@FormParam("user") String user,
//                          @FormParam("appToken") String appToken,
//                          @FormParam("appName") String appName,
//                          @FormParam("parameters") String parameters) {
//        try {
//            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();
//
//            AlarmQueryVo vo = JsonHelper.fromJson(parameters, AlarmQueryVo.class);
//            ValidUtils.checkAlarmQuery(vo);
//
//            RestCreateAlarmRequest request = RestCreateAlarmRequest.newBuilder()
//                    .setAppAuth(appAuth)
//                    .setUser(user)
//                    .setJobId(vo.getJobId())
//                    .setAlarmType(vo.getAlarmType())
//                    .setReciever(vo.getReceiver())
//                    .setStatus(vo.getStatus())
//                    .build();
//
//            ServerCreateAlarmResponse response = (ServerCreateAlarmResponse) callActor(AkkaType.SERVER, request);
//            if (response.getSuccess()) {
//                return successResult();
//            } else {
//                return errorResult(response.getMessage());
//            }
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//            return errorResult(e.getMessage());
//        }
//    }


    /**
     * 追加——报警信息
     */
    @POST
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult add(@FormParam("user") String user,
                          @FormParam("appToken") String appToken,
                          @FormParam("appName") String appName,
                          @FormParam("parameters") String parameters) {
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            AlarmVo vo = JsonHelper.fromJson(parameters, AlarmVo.class);
            ValidUtils.checkAlarm(OperationMode.ADD, vo);

            RestCreateAlarmRequest request = RestCreateAlarmRequest.newBuilder()
                    .setAppAuth(appAuth)
                    .setUser(user)
                    .setJobId(vo.getJobId())
                    .setAlarmType(vo.getAlarmType())
                    .setReciever(vo.getReceiver())
                    .setStatus(vo.getStatus())
                    .build();

            ServerCreateAlarmResponse response = (ServerCreateAlarmResponse) callActor(AkkaType.SERVER, request);
            return response.getSuccess() ? successResult() : errorResult(response.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return errorResult(e);
        }
    }

    /**
     * 修改——报警信息
     */
    @POST
    @Path("edit")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult edit(@FormParam("user") String user,
                           @FormParam("appName") String appName,
                           @FormParam("appToken") String appToken,
                           @FormParam("parameters") String parameters) {
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            AlarmVo vo = JsonHelper.fromJson(parameters, AlarmVo.class);
            ValidUtils.checkAlarm(OperationMode.EDIT, vo);

            RestModifyAlarmRequest.Builder builder = RestModifyAlarmRequest.newBuilder();
            builder.setAppAuth(appAuth).setUser(user).setJobId(vo.getJobId());
            if (vo.getAlarmType() != null) {
                builder.setAlarmType(vo.getAlarmType());
            }
            if (vo.getReceiver() != null) {
                builder.setReciever(vo.getReceiver());
            }
            if (vo.getStatus() != null) {
                builder.setStatus(vo.getStatus());
            }
            RestModifyAlarmRequest request = builder.build();

            ServerModifyAlarmResponse response = (ServerModifyAlarmResponse) callActor(AkkaType.SERVER, request);
            return response.getSuccess() ? successResult() : errorResult(response.getMessage());
        } catch (Exception e) {
            LOGGER.error("", e);
            return errorResult(e);
        }
    }

    /**
     * 删除——报警信息
     */
    @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult delete(@FormParam("user") String user,
                             @FormParam("appName") String appName,
                             @FormParam("appToken") String appToken,
                             @FormParam("parameters") String parameters) {
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            AlarmVo vo = JsonHelper.fromJson(parameters, AlarmVo.class);
            ValidUtils.checkAlarm(OperationMode.DELETE, vo);

            RestDeleteAlarmRequest request = RestDeleteAlarmRequest.newBuilder()
                    .setAppAuth(appAuth)
                    .setUser(user)
                    .setJobId(vo.getJobId())
                    .build();

            ServerDeleteAlarmResponse response = (ServerDeleteAlarmResponse) callActor(AkkaType.SERVER, request);
            return response.getSuccess() ? successResult() : errorResult(response.getMessage());
        } catch (Exception e) {
            LOGGER.error("", e);
            return errorResult(e);
        }
    }

}
