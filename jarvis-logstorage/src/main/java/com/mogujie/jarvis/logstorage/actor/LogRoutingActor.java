package com.mogujie.jarvis.logstorage.actor;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mogujie.jarvis.protocol.LogProtos.LogStorageReadLogResponse;
import com.mogujie.jarvis.protocol.LogProtos.LogStorageWriteLogResponse;
import com.mogujie.jarvis.protocol.LogProtos.RestReadLogRequest;
import com.mogujie.jarvis.protocol.LogProtos.WorkerWriteLogRequest;
import com.mogujie.jarvis.protocol.LogProtos.LogStorageHeartBeatResponse;
import com.mogujie.jarvis.protocol.LogProtos.WorkerHeartBeatRequest;


import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * @author wuya
 */
public class LogRoutingActor extends UntypedActor {

    private final static Logger logger = LogManager.getLogger();
    private int size;
    private static List<ActorRef> writerActors = new ArrayList<ActorRef>();

    public LogRoutingActor(int size) {
        this.size = size;
        for (int i = 0; i < size; i++) {
            writerActors.add(getContext().actorOf(LogWriterActor.props()));
        }
    }

    public static Props props(int size) {
        return Props.create(LogRoutingActor.class, size);
    }

    @Override
    public void onReceive(Object obj) throws Exception {
        if (obj instanceof WorkerWriteLogRequest) {
            writeLog((WorkerWriteLogRequest) obj);
        } else if (obj instanceof RestReadLogRequest) {
            readLog((RestReadLogRequest) obj);
        } else if (obj instanceof WorkerHeartBeatRequest) {
            heartbeat((WorkerHeartBeatRequest) obj);
        } else {
            unhandled(obj);
        }
    }

    private void writeLog(WorkerWriteLogRequest request) {
        try {
            String fullId = request.getFullId();
            int hashcode = fullId.hashCode();
            writerActors.get((hashcode == Integer.MIN_VALUE ? 0 : Math.abs(hashcode)) % size).forward(request, getContext());
        } catch (Exception e) {
            LogStorageWriteLogResponse response = LogStorageWriteLogResponse.newBuilder().setSuccess(false)
                    .setMessage(e.getMessage() != null ? e.getMessage() : e.toString()).build();
            getSender().tell(response, getSelf());
            logger.error(e);
            throw e;
        }
    }

    private void readLog(RestReadLogRequest request) {
        try {
            ActorRef ref = getContext().actorOf(LogReaderActor.props());
            ref.forward(request, getContext());
            ref.forward(PoisonPill.getInstance(), getContext());
        } catch (Exception e) {
            LogStorageReadLogResponse response = LogStorageReadLogResponse.newBuilder().setSuccess(false)
                    .setMessage(e.getMessage() != null ? e.getMessage() : e.toString()).build();
            getSender().tell(response, getSelf());
            logger.error(e);
            throw e;
        }
    }

    private void heartbeat(WorkerHeartBeatRequest request) {
        try {
            ActorRef ref = getContext().actorOf(HeartbeatActor.props());
            ref.forward(request, getContext());
        } catch (Exception e) {
            LogStorageHeartBeatResponse response = LogStorageHeartBeatResponse.newBuilder().setSuccess(false)
                    .setMessage(e.getMessage() != null ? e.getMessage() : e.toString()).build();
            getSender().tell(response, getSelf());
            logger.error(e);
            throw e;
        }
    }

}
