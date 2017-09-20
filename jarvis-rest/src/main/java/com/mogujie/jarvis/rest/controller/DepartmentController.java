package com.mogujie.jarvis.rest.controller;

import com.google.common.base.Splitter;
import com.mogujie.jarvis.core.domain.AkkaType;
import com.mogujie.jarvis.protocol.AppAuthProtos;
import com.mogujie.jarvis.protocol.DepartmentProtos;
import com.mogujie.jarvis.rest.RestResult;
import com.mogujie.jarvis.rest.utils.JsonParameters;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/25
 * time: 下午1:57
 */
@Path("api/department")
public class DepartmentController extends AbstractController {

  protected static final Logger LOGGER = LogManager.getLogger();

  /**
   * add department
   *
   * @throws Exception
   */
  @POST
  @Path("submit")
  @Produces(MediaType.APPLICATION_JSON)
  public RestResult submit(@FormParam("appName") String appName, @FormParam("appToken") String appToken,
      @FormParam("user") String user, @FormParam("parameters") String parameters) {
    LOGGER.info("新建部门");
    try {
      AppAuthProtos.AppAuth appAuth = AppAuthProtos.AppAuth.newBuilder().setName(appName).setToken(appToken).build();
      JsonParameters para = new JsonParameters(parameters);
      String name = para.getString("departmentName");

      DepartmentProtos.RestCreateDepartmentRequest.Builder builder =
          DepartmentProtos.RestCreateDepartmentRequest.newBuilder().setAppAuth(appAuth).setUser(user).setName(name);

      DepartmentProtos.ServerCreateDepartmentResponse response =
          (DepartmentProtos.ServerCreateDepartmentResponse) callActor(AkkaType.SERVER, builder.build());

      return response.getSuccess() ? successResult() : errorResult(response.getMessage());
    } catch (Exception e) {
      LOGGER.error("", e);
      return errorResult(e);
    }
  }

  /**
   *
   * @param appName
   * @param appToken
   * @param user
   * @param parameters
   * @return
   */
  @POST
  @Path("addDepartBizMap")
  @Produces(MediaType.APPLICATION_JSON)
  public RestResult addDepartBizmap(@FormParam("appName") String appName, @FormParam("appToken") String appToken,
      @FormParam("user") String user, @FormParam("parameters") String parameters) {
    try {
      AppAuthProtos.AppAuth appAuth = AppAuthProtos.AppAuth.newBuilder().setName(appName).setToken(appToken).build();
      JsonParameters para = new JsonParameters(parameters);

      String departmentId = para.getString("departmentNameR");
      String bizId = para.getString("bizGroupNameR");
      List<String> bizIds = Splitter.on(",").splitToList(bizId);

      DepartmentProtos.ServerCreateDepartmentBizMapResponse response = null;
      for (String tmpBizId : bizIds) {
        DepartmentProtos.RestCreateDepartmentBizMapRequest.Builder builder =
            DepartmentProtos.RestCreateDepartmentBizMapRequest.newBuilder().setAppAuth(appAuth).setUser(user)
                .setBizId(Float.valueOf(tmpBizId).intValue()).setDepartmentId(Float.valueOf(departmentId).intValue());

        response = (DepartmentProtos.ServerCreateDepartmentBizMapResponse) callActor(AkkaType.SERVER, builder.build());
      }

      return response.getSuccess() ? successResult() : errorResult(response.getMessage());
    } catch (Exception e) {
      LOGGER.error("", e);
      return errorResult(e);
    }
  }

  /**
   *
   * @param user
   * @param appName
   * @param appToken
   * @param parameters
   * @return
   */
  @POST
  @Path("editDepartment")
  @Produces(MediaType.APPLICATION_JSON)
  public RestResult editDepartment(@FormParam("user") String user, @FormParam("appName") String appName,
      @FormParam("appToken") String appToken, @FormParam("parameters") String parameters) {

    try {
      AppAuthProtos.AppAuth appAuth = AppAuthProtos.AppAuth.newBuilder().setName(appName).setToken(appToken).build();
      JsonParameters para = new JsonParameters(parameters);

      String departmentName = para.getString("departmentName");
      String departmentId = para.getString("departmentNameR");
      DepartmentProtos.RestModifyDepartmentRequest.Builder builder =
          DepartmentProtos.RestModifyDepartmentRequest.newBuilder().setAppAuth(appAuth).setUser(user)
              .setName(departmentName).setId(Float.valueOf(departmentId).intValue());
      DepartmentProtos.ServerModifyDepartmentResponse response =
          (DepartmentProtos.ServerModifyDepartmentResponse) callActor(AkkaType.SERVER, builder.build());

      return response.getSuccess() ? successResult() : errorResult(response.getMessage());
    } catch (Exception e) {
      LOGGER.error("", e);
      return errorResult(e);
    }
  }

  @POST
  @Path("editDepartmentBizGroupMap")
  @Produces(MediaType.APPLICATION_JSON)
  public RestResult editDepartmentBizGroupMap(@FormParam("user") String user, @FormParam("appName") String appName,
      @FormParam("appToken") String appToken, @FormParam("parameters") String parameters) {
    try {
      AppAuthProtos.AppAuth appAuth = AppAuthProtos.AppAuth.newBuilder().setName(appName).setToken(appToken).build();
      JsonParameters para = new JsonParameters(parameters);

      String departmentId = para.getString("departmentNameR");
      String bizId = para.getString("bizGroupNameR");
      List<String> bizIds = Splitter.on(",").splitToList(bizId);

      // 1. delete map by departmentId
      DepartmentProtos.RestDeleteDepartmentBizMapByDepartmentIdRequest.Builder builder =
          DepartmentProtos.RestDeleteDepartmentBizMapByDepartmentIdRequest.newBuilder().setAppAuth(appAuth).setUser(user)
              .setDepartmentId(Float.valueOf(departmentId).intValue());

      DepartmentProtos.ServerDeleteDepartmentBizMapByDepartmentIdResponse response =
          (DepartmentProtos.ServerDeleteDepartmentBizMapByDepartmentIdResponse) callActor(AkkaType.SERVER, builder.build());

      if (response.getSuccess()) {
        // 2. add new department and bizGroup map
        DepartmentProtos.ServerCreateDepartmentBizMapResponse newResponse = null;
        for (String tmpBizId : bizIds) {
          DepartmentProtos.RestCreateDepartmentBizMapRequest.Builder newBuilder =
              DepartmentProtos.RestCreateDepartmentBizMapRequest.newBuilder().setAppAuth(appAuth).setUser(user)
                  .setBizId(Float.valueOf(tmpBizId).intValue()).setDepartmentId(Float.valueOf(departmentId).intValue());

          newResponse = (DepartmentProtos.ServerCreateDepartmentBizMapResponse) callActor(AkkaType.SERVER, newBuilder.build());
        }

        return newResponse.getSuccess() ? successResult() : errorResult(newResponse.getMessage());
      } else {
        return errorResult(response.getMessage());
      }
    } catch (Exception e) {
      LOGGER.error("", e);
      return errorResult(e);
    }
  }


  @POST
  @Path("deleteDepartment")
  @Produces(MediaType.APPLICATION_JSON)
  public RestResult deleteDepartmentAndMap(@FormParam("user") String user, @FormParam("appName") String appName,
      @FormParam("appToken") String appToken, @FormParam("parameters") String parameters) {
    try {
      AppAuthProtos.AppAuth appAuth = AppAuthProtos.AppAuth.newBuilder().setName(appName).setToken(appToken).build();
      JsonParameters para = new JsonParameters(parameters);

      String departmentId = para.getString("departmentNameR");

      // 1. delete map by departmentId
      DepartmentProtos.RestDeleteDepartmentBizMapByDepartmentIdRequest.Builder builder =
          DepartmentProtos.RestDeleteDepartmentBizMapByDepartmentIdRequest.newBuilder().setAppAuth(appAuth).setUser(user)
              .setDepartmentId(Float.valueOf(departmentId).intValue());

      DepartmentProtos.ServerDeleteDepartmentBizMapByDepartmentIdResponse response =
          (DepartmentProtos.ServerDeleteDepartmentBizMapByDepartmentIdResponse) callActor(AkkaType.SERVER, builder.build());

      if (response.getSuccess()) {
        // 2. delete department
        DepartmentProtos.RestDeleteDepartmentRequest.Builder newBuild = DepartmentProtos.RestDeleteDepartmentRequest.newBuilder()
            .setAppAuth(appAuth).setUser(user).setId(Float.valueOf(departmentId).intValue());
        DepartmentProtos.ServerDeleteDepartmentResponse newResponse = (DepartmentProtos.ServerDeleteDepartmentResponse)callActor(AkkaType.SERVER, newBuild.build());

        return newResponse.getSuccess() ? successResult() : errorResult(newResponse.getMessage());
      } else {
        return errorResult(response.getMessage());
      }

    } catch (Exception e) {
      LOGGER.error("", e);
      return errorResult(e);
    }
  }
}
