package com.nicky.monitor.ui.event;

import com.nicky.monitor.core.Monitor;
import com.nicky.monitor.model.PacketParser;
import com.nicky.monitor.ui.components.PacketsGrid;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class PacketArrivedEvent {
    @Autowired
    private Monitor monitor;

    @Autowired
    private PacketsGrid packetsGrid;

    @PostConstruct
    public void register(){
        monitor.addPacketListener(packet -> packetsGrid
                .getUI()
                .ifPresent(ui -> ui.access(() -> {
                    packetsGrid.getPackets().offer(PacketParser.parsePacket(packet));
                    packetsGrid.getDataProvider().refreshAll();
                })));
    }
}
