package com.lostshard.lostshard.Objects.Player;

import java.util.*;

import com.google.common.collect.ForwardingSet;

public class Titles extends ForwardingSet<String> {

    private final Set<String> titles;
    private int current;

    public Titles(Set<String> titles, int current) {
        this.titles = titles;
        this.current = current;
    }

    public Titles() {
        this(new HashSet<>(), -1);
    }

    @Override
    protected Set<String> delegate() {
        return titles;
    }

}
