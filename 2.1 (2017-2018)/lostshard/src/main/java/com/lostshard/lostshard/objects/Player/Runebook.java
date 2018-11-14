package com.lostshard.lostshard.Objects.Player;

import com.google.common.collect.ForwardingMap;

import java.util.*;

public class Runebook extends ForwardingMap<String, Rune> {

    private final Map<String, Rune> runes;

    public Runebook() {
        this.runes = new HashMap<>();
    }

    @Override
    protected Map<String, Rune> delegate() {
        return this.runes;
    }

    public void add(Rune rune) {
        this.runes.put(rune.getLabel(), rune);
    }

    public void remove(Rune rune) {
        this.runes.remove(rune.getLabel());
    }
}
