package com.nicky.monitor.core;

import com.nicky.monitor.ui.listener.StatusListener;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@SpringComponent
@UIScope
public class Monitor {
    @Autowired
    private MonitorCore core;

    @Autowired
    private MonitorConfig config;

    @Getter
    private List<PcapNetworkInterface> nifs;

    @PostConstruct
    public void init(){
        try {
            this.nifs = Pcaps.findAllDevs();
            core.setPacketListener(event -> {});
            core.setStatusListener(status -> {});
        } catch (Exception e){
            log.error("list devices error", e);
        }
    }

    public void addPacketListener(PacketListener packetListener){
        core.setPacketListener(packetListener);
    }

    public void addStatusListener(StatusListener statusListener){
        core.setStatusListener(statusListener);
    }

    public void start(int nifId, String targetProxy, String targetPort){
        boolean needToRestart = false;
        if (nifs.get(nifId) != null && !nifs.get(nifId).equals(config.getNif())){
            config.setNif(nifs.get(nifId));
            needToRestart = true;
        }

        if (targetProxy != null && !targetProxy.equals(config.getProxy())){
            config.setProxy(targetProxy);
            needToRestart = true;
        }

        if (targetPort != null && !targetPort.equals(config.getPort())){
            config.setPort(targetPort);
            needToRestart = true;
        }

        if (!core.isOpen()){
            needToRestart = true;
        }

        if (needToRestart){
            core.start();
        }
    }

    public void shutdown(){
        core.shutdown();
    }
}
