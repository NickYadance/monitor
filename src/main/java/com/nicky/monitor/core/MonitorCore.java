package com.nicky.monitor.core;

import com.nicky.monitor.config.MonitorConfig;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.pcap4j.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Component
@UIScope
public class MonitorCore {
    @Autowired
    private MonitorConfig config;

    @Getter
    private CircularFifoQueue<String> packets;

    private PcapHandle handle;
    private ExecutorService pool = Executors.newSingleThreadExecutor();
    private Future future;

    @PostConstruct
    public void init(){
        packets = new CircularFifoQueue<>(config.getListSize());
    }

    public void start(){
        if (future != null && !future.isCancelled()){
            future.cancel(true);
        }

        if (handle != null && handle.isOpen()){
            handle.close();
        }

        PacketListener listener = packetListener();
        String filter = config.getFilter();
        PcapNetworkInterface nif = config.getNif();
        try {
            handle = nif.openLive(config.getSnapshotLength(),
                    PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,
                    config.getReadTimeout());
            handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
            future = CompletableFuture.runAsync(() -> {
                try {
                    handle.loop(config.getMaxPacket(), listener);
                } catch (PcapNativeException | InterruptedException | NotOpenException e) {
                    log.error("error while listening", e);
                }
            }, pool);
        } catch (PcapNativeException | NotOpenException e) {
            log.error("error while listening", e);
        }
    }

    private PacketListener packetListener(){
        return packet -> packets.add(packet.toString());
    }
}