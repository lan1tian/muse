/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年11月30日 下午3:43:27
 */

package com.mogujie.jarvis.server.scheduler.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.jgrapht.graph.DefaultEdge;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author guangming
 *
 */
public enum TaskGraph {
    INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger();

    private Map<Long, DAGTask> taskMap = new ConcurrentHashMap<>();
    private DirectedAcyclicGraph<DAGTask, DefaultEdge> dag = new DirectedAcyclicGraph<DAGTask, DefaultEdge>(DefaultEdge.class);

    public Map<Long, DAGTask> getTaskMap() {
        return taskMap;
    }

    public DAGTask getTask(long taskId) {
        return taskMap.get(taskId);
    }

    public synchronized void clear() {
        taskMap.clear();
        Set<DAGTask> allTasks = dag.vertexSet();
        if (allTasks != null) {
            List<DAGTask> tmpTasks = new ArrayList<DAGTask>();
            tmpTasks.addAll(dag.vertexSet());
            dag.removeAllVertices(tmpTasks);
        }
    }

    public synchronized void addTask(long taskId, DAGTask dagTask) {
        if (!taskMap.containsKey(taskId)) {
            taskMap.put(taskId, dagTask);
            dag.addVertex(dagTask);
        }
    }

    public synchronized void removeTask(long taskId) {
        DAGTask dagTask = taskMap.get(taskId);
        if (dagTask != null) {
            taskMap.remove(taskId);
            dag.removeVertex(dagTask);
        }
    }

    public synchronized void addDependency(long parentId, long childId) {
        DAGTask parent = taskMap.get(parentId);
        DAGTask child = taskMap.get(childId);
        if (parent != null && child != null) {
            try {
                dag.addDagEdge(parent, child);
                LOGGER.info("add dependency from {} to {}", parent, child);
            } catch (CycleFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public synchronized List<DAGTask> getParents(long taskId) {
        List<DAGTask> parents = new ArrayList<DAGTask>();
        DAGTask dagTask = taskMap.get(taskId);
        if (dagTask != null) {
            Set<DefaultEdge> inEdges = dag.incomingEdgesOf(dagTask);
            if (inEdges != null) {
                for (DefaultEdge edge : inEdges) {
                    parents.add(dag.getEdgeSource(edge));
                }
            }
        }
        return parents;
    }

    public synchronized List<DAGTask> getChildren(long taskId) {
        List<DAGTask> children = new ArrayList<DAGTask>();
        DAGTask dagTask = taskMap.get(taskId);
        if (dagTask != null) {
            Set<DefaultEdge> inEdges = dag.outgoingEdgesOf(dagTask);
            if (inEdges != null) {
                for (DefaultEdge edge : inEdges) {
                    children.add(dag.getEdgeTarget(edge));
                }
            }
        }
        return children;
    }

    public static Map<Long, List<Long>> convert2TaskMap(List<DAGTask> dagTasks) {
        Map<Long, List<Long>> taskMap = Maps.newHashMap();
        for (DAGTask dagTask : dagTasks) {
            long jobId = dagTask.getJobId();
            long taskId = dagTask.getTaskId();
            if (taskMap.containsKey(jobId)) {
                List<Long> taskList = taskMap.get(jobId);
                taskList.add(taskId);
            } else {
                List<Long> taskList = Lists.newArrayList(taskId);
                taskMap.put(jobId, taskList);
            }
        }
        return taskMap;
    }
}
