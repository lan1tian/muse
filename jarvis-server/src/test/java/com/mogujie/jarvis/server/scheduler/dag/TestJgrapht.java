/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月25日 下午3:19:53
 */

package com.mogujie.jarvis.server.scheduler.dag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author guangming
 *
 */
public class TestJgrapht {

    class MyVertex {
        private int id;
        private String name;
        public MyVertex(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        @Override
        public String toString() {
            return "[id=" + id + ", name=" + name + "]";
        }
    }

    /**
     *     v1
     *    / \
     *   v2  v3
     *    \ /
     *     v4
     */
    @Test
    public void testAddVertex() throws CycleFoundException {
        DirectedAcyclicGraph<MyVertex, DefaultEdge> g1 = new DirectedAcyclicGraph<MyVertex, DefaultEdge>(DefaultEdge.class);
        MyVertex v1 = new MyVertex(1, "v1");
        MyVertex v2 = new MyVertex(2, "v2");
        MyVertex v3 = new MyVertex(3, "v3");
        MyVertex v4 = new MyVertex(4, "v4");
        g1.addVertex(v1);
        g1.addVertex(v2);
        g1.addVertex(v3);
        g1.addVertex(v4);
        g1.addDagEdge(v1, v2);
        g1.addDagEdge(v1, v3);
        g1.addDagEdge(v2, v4);
        g1.addDagEdge(v3, v4);

        Set<DefaultEdge> v1in = g1.incomingEdgesOf(v1);
        Assert.assertEquals(0, v1in.size());
        Set<DefaultEdge> v1out = g1.outgoingEdgesOf(v1);
        Assert.assertEquals(2, v1out.size());
        for (DefaultEdge ege : v1out) {
            System.out.println(g1.getEdgeTarget(ege).toString());
        }

        Set<DefaultEdge> v2in = g1.incomingEdgesOf(v2);
        Assert.assertEquals(1, v2in.size());
        Set<DefaultEdge> v2out = g1.outgoingEdgesOf(v2);
        Assert.assertEquals(1, v2out.size());

        Set<DefaultEdge> v3in = g1.incomingEdgesOf(v3);
        Assert.assertEquals(1, v3in.size());
        Set<DefaultEdge> v3out = g1.outgoingEdgesOf(v3);
        Assert.assertEquals(1, v3out.size());

        Set<DefaultEdge> v4in = g1.incomingEdgesOf(v4);
        Assert.assertEquals(2, v4in.size());
        for (DefaultEdge ege : v4in) {
            System.out.println(g1.getEdgeSource(ege).toString());
        }
    }

    /**
     *     v1
     *    / \    -->  v2  v3
     *   v2  v3
     */
    @Test
    public void testRemoveVertex() throws CycleFoundException {
        DirectedAcyclicGraph<MyVertex, DefaultEdge> g1 = new DirectedAcyclicGraph<MyVertex, DefaultEdge>(DefaultEdge.class);
        MyVertex v1 = new MyVertex(1, "v1");
        MyVertex v2 = new MyVertex(2, "v2");
        MyVertex v3 = new MyVertex(3, "v3");
        g1.addVertex(v1);
        g1.addVertex(v2);
        g1.addVertex(v3);
        g1.addDagEdge(v1, v2);
        g1.addDagEdge(v1, v3);

        Set<DefaultEdge> v1out = g1.outgoingEdgesOf(v1);
        Assert.assertEquals(2, v1out.size());
        Set<DefaultEdge> v2in = g1.incomingEdgesOf(v2);
        Assert.assertEquals(1, v2in.size());
        Set<DefaultEdge> v3in = g1.incomingEdgesOf(v3);
        Assert.assertEquals(1, v3in.size());

        g1.removeVertex(v1);
        v2in = g1.incomingEdgesOf(v2);
        Assert.assertEquals(0, v2in.size());
        v3in = g1.incomingEdgesOf(v3);
        Assert.assertEquals(0, v3in.size());

    }

    /**
     *     v1  <--
     *    / \    |
     *   v2  v3  |
     *    \ /    |
     *     v4  ---
     */
    @Test
    public void testCycleGrapht() {
        DirectedAcyclicGraph<MyVertex, DefaultEdge> g1 = new DirectedAcyclicGraph<MyVertex, DefaultEdge>(DefaultEdge.class);
        MyVertex v1 = new MyVertex(1, "v1");
        MyVertex v2 = new MyVertex(2, "v2");
        MyVertex v3 = new MyVertex(3, "v3");
        MyVertex v4 = new MyVertex(4, "v4");
        g1.addVertex(v1);
        g1.addVertex(v2);
        g1.addVertex(v3);
        g1.addVertex(v4);
        try {
            g1.addDagEdge(v1, v2);
            Assert.assertTrue(true);
        } catch (CycleFoundException e) {
            Assert.assertTrue(false);
        }
        try {
            g1.addDagEdge(v1, v3);
            Assert.assertTrue(true);
        } catch (CycleFoundException e) {
            Assert.assertTrue(false);
        }
        try {
            g1.addDagEdge(v2, v4);
            Assert.assertTrue(true);
        } catch (CycleFoundException e) {
            Assert.assertTrue(false);
        }
        try {
            g1.addDagEdge(v3, v4);
            Assert.assertTrue(true);
        } catch (CycleFoundException e) {
            Assert.assertTrue(false);
        }
        //cycle
        try {
            g1.addDagEdge(v4, v1);
            Assert.assertTrue(false);
        } catch (CycleFoundException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testNotExistedException() throws CycleFoundException {
        DirectedAcyclicGraph<MyVertex, DefaultEdge> g1 = new DirectedAcyclicGraph<MyVertex, DefaultEdge>(DefaultEdge.class);
        MyVertex v1 = new MyVertex(1, "v1");
        MyVertex v2 = new MyVertex(2, "v2");

        try {
            g1.addDagEdge(v1, v2);
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

        List<MyVertex> parents = getParents(g1, v1);
        Assert.assertEquals(0, parents.size());
    }

    private List<MyVertex> getParents( DirectedAcyclicGraph<MyVertex, DefaultEdge> dag, MyVertex dagJob) {
        List<MyVertex> parents = new ArrayList<MyVertex>();
        try {
            Set<DefaultEdge> inEdges = dag.incomingEdgesOf(dagJob);
            if (inEdges != null) {
                for (DefaultEdge edge : inEdges) {
                    parents.add(dag.getEdgeSource(edge));
                }
            }
        } catch (Exception e) {
            //
        }

        return parents;
    }
}
