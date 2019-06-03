package com.nicky.monitor.core;

import com.nicky.monitor.config.MonitorConfig;
import com.vaadin.flow.spring.annotation.SpringComponent;
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
public class MonitorCore {
    @Autowired
    private MonitorConfig config;

    @Setter
    private PacketListener packetListener;

    private PcapHandle handle;
    private ExecutorService pool;
    private Future future;

    @PostConstruct
    public void init(){
        pool = Executors.newSingleThreadExecutor();
    }

    public void start(){
        if (future != null && !future.isCancelled()){
            future.cancel(true);
        }

        if (handle != null && handle.isOpen()){
            handle.close();
        }

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
                }
            }, pool);
        } catch (PcapNativeException | NotOpenException e) {
            log.error("error while listening", e);
        }
    }
}