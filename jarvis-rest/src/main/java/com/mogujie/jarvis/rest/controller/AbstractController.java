package com.mogujie.jarvis.rest.controller;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSelection;
import akka.pattern.Patterns;
import akka.util.Timeout;

import com.google.protobuf.GeneratedMessage;
import com.mogujie.jarvis.core.domain.AkkaType;
import com.mogujie.jarvis.rest.MsgCode;
import com.mogujie.jarvis.rest.RestAkka;
import com.mogujie.jarvis.rest.RestResult;
import com.mogujie.jarvis.rest.vo.AbstractVo;

/**
 * 控制器父类
 */
public abstract class AbstractController {

    private ActorSelection workerActor;
    private ActorSelection serverActor;
    private ActorSelection logstorageActor;

    protected static final Timeout TIMEOUT = new Timeout(Duration.create(30, TimeUnit.SECONDS));
    protected static final Logger LOGGER = LogManager.getLogger();

    public AbstractController() {

    }

    /**
     * 调用Actor
     *
     * @param request
     * @param timeout
     * @return
     * @throws java.lang.Exception
     */
    protected GeneratedMessage callActor(AkkaType akkaType, GeneratedMessage request, Timeout timeout) throws Exception {

        ActorSelection actor;

        if (akkaType == AkkaType.SERVER) {
            if (serverActor == null) {
                serverActor = RestAkka.getActor(akkaType);
            }
            actor = serverActor;

        } else if (akkaType == AkkaType.WORKER) {
            if (workerActor == null) {
                workerActor = RestAkka.getActor(akkaType);
            }
            actor = workerActor;

        } else if (akkaType == AkkaType.LOGSTORAGE) {
            if (logstorageActor == null) {
                logstorageActor = RestAkka.getActor(akkaType);
            }

            actor = logstorageActor;
        } else {
            return null;
        }

        //timeout
        LOGGER.info("actor="+actor.pathString()+", "+actor);
        LOGGER.info("request="+request);
        Future<Object> future = Patterns.ask(actor, request, timeout);
        GeneratedMessage response = (GeneratedMessage) Await.result(future, timeout.duration());
        return response;

    }

    protected GeneratedMessage callActor(AkkaType akkaType, GeneratedMessage request) throws Exception {
        return callActor(akkaType, request, TIMEOUT);
    }

    /**
     * 返回错误结果
     *
     * @param ex
     * @return
     */
    protected RestResult errorResult(Exception ex) {
        String msg = ex.getMessage() == null ? ex.toString() : ex.getMessage();
        return errorResult(MsgCode.UNDEFINE_ERROR, msg);
    }

    /**
     * 返回错误结果
     *
     * @param msg
     * @return
     */
    protected RestResult errorResult(String msg) {
        return errorResult(MsgCode.UNDEFINE_ERROR, msg);
    }

    /**
     * 返回错误结果
     *
     * @param msg
     * @return
     */
    protected RestResult errorResult(int code, String msg) {
        RestResult result = new RestResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 成功结果
     *
     * @return
     */
    protected RestResult successResult() {
        return new RestResult(MsgCode.SUCCESS);
    }

    /**
     * 成功结果
     *
     * @param data
     * @return
     */
    protected RestResult successResult(AbstractVo data) {
        RestResult result = new RestResult (MsgCode.SUCCESS);
        result.setData(data);
        return result;
    }

}
