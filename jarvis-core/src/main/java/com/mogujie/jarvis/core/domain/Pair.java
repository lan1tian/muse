/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年8月31日 下午2:54:51
 */
package com.mogujie.jarvis.core.domain;

import java.util.Objects;

public class Pair<A, B> {

    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Pair<?, ?>) {
            Pair<?, ?> other = (Pair<?, ?>) obj;
            return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second);
        }

        return false;
    }

    @Override
    public String toString() {
        return "Pair {first=" + Objects.toString(first) + ", second=" + Objects.toString(second) + "}";
    }

}
