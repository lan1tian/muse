package com.mogujie.jarvis.rest.controller;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.reflect.TypeToken;
import com.mogujie.jarvis.core.domain.AkkaType;
import com.mogujie.jarvis.core.domain.AppStatus;
import com.mogujie.jarvis.core.domain.OperationMode;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.protocol.AppAuthProtos.AppAuth;
import com.mogujie.jarvis.protocol.ApplicationProtos;
import com.mogujie.jarvis.protocol.ApplicationProtos.AppCounterEntry;
import com.mogujie.jarvis.protocol.ApplicationProtos.RestCreateApplicationRequest;
import com.mogujie.jarvis.protocol.ApplicationProtos.RestDeleteApplicationRequest;
import com.mogujie.jarvis.protocol.ApplicationProtos.RestModifyApplicationRequest;
import com.mogujie.jarvis.protocol.ApplicationProtos.RestSearchAppCounterRequest;
import com.mogujie.jarvis.protocol.ApplicationProtos.RestSetApplicationWorkerGroupRequest;
import com.mogujie.jarvis.protocol.ApplicationProtos.ServerCreateApplicationResponse;
import com.mogujie.jarvis.protocol.ApplicationProtos.ServerDeleteApplicationResponse;
import com.mogujie.jarvis.protocol.ApplicationProtos.ServerModifyApplicationResponse;
import com.mogujie.jarvis.protocol.ApplicationProtos.ServerSearchAppCounterResponse;
import com.mogujie.jarvis.protocol.ApplicationProtos.ServerSetApplicationWorkerGroupResponse;
import com.mogujie.jarvis.rest.RestResult;
import com.mogujie.jarvis.rest.utils.JsonParameters;
import com.mogujie.jarvis.rest.utils.ValidUtils;
import com.mogujie.jarvis.rest.utils.ValidUtils.CheckMode;
import com.mogujie.jarvis.rest.vo.AppCounterVo;
import com.mogujie.jarvis.rest.vo.AppResultVo;
import com.mogujie.jarvis.rest.vo.AppWorkerGroupVo;

/**
 * @author muming
 */
@Path("api/app")
public class AppController extends AbstractController {

    /**
     * 追加app
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

            JsonParameters paras = new JsonParameters(parameters);
            String applicationName = paras.getStringNotEmpty("applicationName");
            String owner = paras.getStringNotEmpty("owner");
            Integer status = paras.getInteger("status", AppStatus.ENABLE.getValue());
            Integer maxConcurrency = paras.getInteger("maxConcurrency", 10);

            ValidUtils.checkAppVo(CheckMode.ADD, applicationName, owner, status, maxConcurrency);
            RestCreateApplicationRequest request = RestCreateApplicationRequest.newBuilder().setAppAuth(appAuth).setUser(user)
                    .setAppName(applicationName.trim()).setOwner(owner)
                    .setStatus(status).setMaxConcurrency(maxConcurrency)
                    .build();

            ServerCreateApplicationResponse response = (ServerCreateApplicationResponse) callActor(AkkaType.SERVER, request);
            return response.getSuccess() ? successResult(new AppResultVo().setAppId(response.getAppId()))
                    : errorResult(response.getMessage());

        } catch (Exception e) {
            LOGGER.error("", e);
            return errorResult(e);
        }
    }

    /**
     * 修改app
     */
    @POST
    @Path("edit")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult update(@FormParam("user") String user,
                             @FormParam("appName") String appName,
                             @FormParam("appToken") String appToken,
                             @FormParam("parameters") String parameters) {
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            JsonParameters paras = new JsonParameters(parameters);
            Integer appId = paras.getIntegerNotNull("appId");
            String applicationName = paras.getString("applicationName");
            String owner = paras.getString("owner");
            Integer status = paras.getInteger("status");
            Integer maxConcurrency = paras.getInteger("maxConcurrency");

            ValidUtils.checkAppVo(CheckMode.EDIT, applicationName, owner, status, maxConcurrency);
            RestModifyApplicationRequest.Builder builder = RestModifyApplicationRequest.newBuilder();
            builder.setAppAuth(appAuth).setUser(user).setAppId(appId);
            if (applicationName != null) {
                builder.setAppName(applicationName.trim());
            }
            if (owner != null) {
                builder.setOwner(owner.trim());
            }
            if (status != null) {
                builder.setStatus(status);
            }
            if (maxConcurrency != null) {
                builder.setMaxConcurrency(maxConcurrency);
            }
            RestModifyApplicationRequest request = builder.build();

            ServerModifyApplicationResponse response = (ServerModifyApplicationResponse) callActor(AkkaType.SERVER, request);
            return response.getSuccess() ? successResult() : errorResult(response.getMessage());
        } catch (Exception e) {
            LOGGER.error("", e);
            return errorResult(e);
        }
    }

    /**
     * 删除app
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

            JsonParameters paras = new JsonParameters(parameters);
            int appId = paras.getIntegerNotNull("appId");

            RestDeleteApplicationRequest request = RestDeleteApplicationRequest.newBuilder().setAppAuth(appAuth)
                    .setUser(user).setAppId(appId).build();

            ServerDeleteApplicationResponse response = (ServerDeleteApplicationResponse) callActor(AkkaType.SERVER, request);
            return response.getSuccess() ? successResult() : errorResult(response.getMessage());
        } catch (Exception e) {
            LOGGER.error("", e);
            return errorResult(e);
        }
    }

    /**
     * app/workerGroup追加
     */
    @POST
    @Path("/workerGroup/add")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult wgSet(@FormParam("user") String user,
                            @FormParam("appName") String appName,
                            @FormParam("appToken") String appToken,
                            @FormParam("parameters") String parameters) {
        try {
            RestSetApplicationWorkerGroupRequest request = _workerGroup(OperationMode.ADD, user, appName, appToken, parameters);
            ServerSetApplicationWorkerGroupResponse response = (ServerSetApplicationWorkerGroupResponse) callActor(AkkaType.SERVER, request);
            return response.getSuccess() ? successResult() : errorResult(response.getMessage());
        } catch (Exception e) {
            LOGGER.error("", e);
            return errorResult(e);
        }
    }

    /**
     * app/workerGroup追加
     */
    @POST
    @Path("/workerGroup/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult wgDelete(@FormParam("user") String user,
                               @FormParam("appName") String appName,
                               @FormParam("appToken") String appToken,
                               @FormParam("parameters") String parameters) {
        try {
            RestSetApplicationWorkerGroupRequest request = _workerGroup(OperationMode.DELETE, user, appName, appToken, parameters);
            ServerSetApplicationWorkerGroupResponse response = (ServerSetApplicationWorkerGroupResponse) callActor(AkkaType.SERVER, request);
            return response.getSuccess() ? successResult() : errorResult(response.getMessage());
        } catch (Exception e) {
            LOGGER.error("", e);
            return errorResult(e);
        }
    }

    @GET
    @Path("query/appcounter")
    public RestResult queryAppCounter(@QueryParam("user") String user, @QueryParam("appName") String appName, @QueryParam("appToken") String appToken) {
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            RestSearchAppCounterRequest request = RestSearchAppCounterRequest.newBuilder().setAppAuth(appAuth).build();
            ServerSearchAppCounterResponse response = (ServerSearchAppCounterResponse) callActor(AkkaType.SERVER, request);
            if (response.getSuccess()) {
                AppCounterVo vo = new AppCounterVo();
                List<AppCounterEntry> appCounterEnties = response.getAppCounterEntryList();
                for (AppCounterEntry entry : appCounterEnties) {
                    vo.setCounter(entry.getAppId(), entry.getCounter());
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

    private RestSetApplicationWorkerGroupRequest _workerGroup(OperationMode mode, String user,
                                                              String appName, String appToken, String parameters) {

        AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

        Type mapType = new TypeToken<List<AppWorkerGroupVo.AppWorkerGroupEntry>>() {
        }.getType();
        List<AppWorkerGroupVo.AppWorkerGroupEntry> list = JsonHelper.fromJson(parameters, mapType);

        // 构造请求
        RestSetApplicationWorkerGroupRequest.Builder builder = RestSetApplicationWorkerGroupRequest.newBuilder();
        if (list != null && list.size() > 0) {
            for (AppWorkerGroupVo.AppWorkerGroupEntry e : list) {
                ValidUtils.checkAppWorkerGroup(mode, e.getAppId(), e.getWorkerGroupId());
                ApplicationProtos.AppWorkerGroupEntry data = ApplicationProtos.AppWorkerGroupEntry
                        .newBuilder()
                        .setAppId(e.getAppId())
                        .setWorkerGroupId(e.getWorkerGroupId())
                        .build();
                builder.addAppWorkerGroups(data);
            }
        }

        RestSetApplicationWorkerGroupRequest request = builder
                .setAppAuth(appAuth)
                .setUser(user)
                .setMode(mode.getValue())
                .build();

        return request;

    }

}
