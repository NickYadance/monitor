package com.nicky.monitor.ui.listener;

@FunctionalInterface
public interface StatusListener {
    public void applyStatus(Boolean status);
}
