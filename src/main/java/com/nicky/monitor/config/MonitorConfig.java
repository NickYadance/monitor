package com.nicky.monitor.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.pcap4j.core.PcapNetworkInterface;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MonitorConfig {
    private static final String META_PORT = "port";

    private int snapshotLength = 65536;
    private int readTimeout = 50;
    private int maxPacket = -1;
    private String proxy = "";
    private String port = "";
    private PcapNetworkInterface nif;

    public String getFilter(){
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(proxy)){
            sb.append(proxy).append(" ");
        }
        if (StringUtils.isNotEmpty(port)){
            sb.append(META_PORT).append(" ");
            sb.append(port).append(" ");
        }
        return sb.toString();
    }
}
