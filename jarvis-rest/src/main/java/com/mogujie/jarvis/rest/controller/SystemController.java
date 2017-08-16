/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年6月18日 下午3:19:28
 */
package com.mogujie.jarvis.rest.controller;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mogujie.jarvis.core.domain.AkkaType;
import com.mogujie.jarvis.protocol.AppAuthProtos.AppAuth;
import com.mogujie.jarvis.protocol.SystemProtos.*;
import com.mogujie.jarvis.rest.RestResult;
import com.mogujie.jarvis.rest.utils.JsonParameters;
import com.mogujie.jarvis.rest.vo.SystemStatusResultVo;

/**
 * @author muming,hejian
 */
@Path("api/system")
public class SystemController extends AbstractController {

    /**
     * 修改——系统状态
     */
    @POST
    @Path("status")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult setStatus(@FormParam("user") String user,
                             @FormParam("appToken") String appToken,
                             @FormParam("appName") String appName,
                             @FormParam("parameters") String parameters) {
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            JsonParameters para = new JsonParameters(parameters);
            int status = para.getIntegerNotNull("status");
            RestUpdateSystemStatusRequest request = RestUpdateSystemStatusRequest.newBuilder()
                    .setAppAuth(appAuth)
                    .setUser(user)
                    .setStatus(status)
                    .build();
            ServerUpdateSystemStatusResponse response = (ServerUpdateSystemStatusResponse) callActor(AkkaType.SERVER, request);

            return response.getSuccess() ? successResult() : errorResult(response.getMessage());
        } catch (Exception e) {
            LOGGER.error("", e);
            return errorResult(e);
        }
    }

    /**
     * 获得——系统状态
     */
    @POST
    @Path("status/get")
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult getStatus(@FormParam("user") String user,
                             @FormParam("appToken") String appToken,
                             @FormParam("appName") String appName,
                             @FormParam("parameters") String parameters) {
        try {
            AppAuth appAuth = AppAuth.newBuilder().setName(appName).setToken(appToken).build();

            RestGetSystemStatusRequest request = RestGetSystemStatusRequest.newBuilder()
                    .setAppAuth(appAuth)
                    .setUser(user)
                    .build();
            ServerGetSystemStatusResponse response = (ServerGetSystemStatusResponse) callActor(AkkaType.SERVER, request);

            return response.getSuccess() ? successResult(new SystemStatusResultVo().setStatus(response.getStatus())) : errorResult(response.getMessage());
        } catch (Exception e) {
            LOGGER.error("", e);
            return errorResult(e);
        }
    }

}
