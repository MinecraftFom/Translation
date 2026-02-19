package com.fomdev.translation.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TranslationEvent extends Event {
    protected final String original;
    protected final String target;

    public TranslationEvent(String original, String target) {
        this.original = original;
        this.target = target;
    }

    public String getOriginal() {
        return original;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }
}