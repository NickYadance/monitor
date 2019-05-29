package com.nicky.monitor.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Component
public class IpPacketMonitor {
    private List<PcapNetworkInterface> nifs = new ArrayList<>();

    @PostConstruct
    public void init(){
        try {
            this.nifs = Pcaps.findAllDevs();
        } catch (Exception e){
            log.error("list devices error", e);
        }
    }
}
