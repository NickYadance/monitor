package com.nicky.monitor.core;

import com.nicky.monitor.config.MonitorConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Monitor {
    @Autowired
    private MonitorCore core;

    @Autowired
    private MonitorConfig config;

    @Getter
    private CircularFifoQueue<String> packets;

    @Getter
    private List<PcapNetworkInterface> nifs = new ArrayList<>();

    @PostConstruct
    public void init(){
        try {
            this.nifs = Pcaps.findAllDevs();
            this.packets = core.getPackets();
        } catch (Exception e){
            log.error("list devices error", e);
        }
    }

    public void start(int nifId, String targetProxy, String targetPort){
        boolean needToRestart = false;
        if (nifs.get(nifId) != config.getNif()){
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

        if (needToRestart){
            core.start();
        }
    }
}
