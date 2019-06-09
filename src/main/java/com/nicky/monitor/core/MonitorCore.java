package com.nicky.monitor.core;

import com.nicky.monitor.ui.listener.StatusListener;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@SpringComponent
@UIScope
public class MonitorCore {
    @Autowired
    private MonitorConfig config;

    @Setter(AccessLevel.PROTECTED)
    private PacketListener packetListener;

    @Setter(AccessLevel.PROTECTED)
    private StatusListener statusListener;

    private PcapHandle handle;
    private ExecutorService pool;
    private Future future;

    @PostConstruct
    public void init(){
        pool = Executors.newSingleThreadExecutor();
    }

    void start(){
        String filter = config.getFilter();
        PcapNetworkInterface nif = config.getNif();
        try {
            handle = nif.openLive(config.getSnapshotLength(),
                    PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,
                    config.getReadTimeout());
            handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
            future = CompletableFuture.runAsync(() -> {
                try {
                    handle.loop(config.getMaxPacket(), packetListener);
                } catch (PcapNativeException | InterruptedException | NotOpenException e) {
                    log.error("error while listening", e);
                } finally {
                    synchronized (this){
                        if (handle != null && handle.isOpen()){
                            handle.close();
                        }
                    }
                }
            }, pool);
            statusListener.applyStatus(true);
        } catch (PcapNativeException | NotOpenException e) {
            log.error("error while listening", e);
            statusListener.applyStatus(false);
        }
    }

    void shutdown(){
        if (handle != null && handle.isOpen()){
            try {
                handle.breakLoop();
            } catch (Exception e){
                log.error("cannot open handle", e);
            }
        }

        if (future != null && !future.isCancelled()){
            future.cancel(true);
        }
        statusListener.applyStatus(false);
    }

    boolean isOpen(){
        return handle != null && handle.isOpen() && future != null && !future.isDone();
    }
}