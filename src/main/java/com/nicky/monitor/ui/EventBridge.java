package com.nicky.monitor.ui;

import com.nicky.monitor.core.Monitor;
import com.nicky.monitor.model.PacketParser;
import com.nicky.monitor.ui.components.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class EventBridge {
    @Autowired
    private Monitor monitor;

    @Autowired
    private PacketsGrid packetsGrid;

    @Autowired
    private NifComboBox nifComboBox;

    @Autowired
    private ProxyComboBox proxyComboBox;

    @Autowired
    private PortComboBox portComboBox;

    @Autowired
    private NifSubmitButton nifSubmitButton;

    @Autowired
    private NifShutdownButton nifShutdownButton;

    @PostConstruct
    public void init(){
        nifSubmitButtonClickEvent();
        nifShutdownButtonClickEvent();
        monitorPacketEvent();
        monitorStatusEvent();
        portComboBoxCustomValueEvent();
    }

    private void nifSubmitButtonClickEvent(){
        nifSubmitButton.addClickListener(event -> {
            if (nifComboBox.getValue() == null
                    || StringUtils.isEmpty(proxyComboBox.getValue())
                    || StringUtils.isEmpty(portComboBox.getValue())){
                Notification.show("Need device config", 2500, Notification.Position.TOP_CENTER);
                return;
            }
            monitor.start(nifComboBox.getValue().getId(),
                    proxyComboBox.getValue(),
                    portComboBox.getValue().trim());
        });
    }

    private void nifShutdownButtonClickEvent(){
        nifShutdownButton.addClickListener(event -> {
            monitor.shutdown();
        });
    }

    private void monitorPacketEvent(){
        monitor.addPacketListener(packet -> packetsGrid
                .getUI()
                .ifPresent(ui -> ui.access(() -> {
                    packetsGrid.getPackets().offer(PacketParser.parsePacket(packet));
                    packetsGrid.getDataProvider().refreshAll();
                })));
    }

    private void monitorStatusEvent(){
        monitor.addStatusListener(status -> {
            if (status){
                nifSubmitButton.setVisible(false);
                nifShutdownButton.setVisible(true);
                packetsGrid.getPackets().clear();
                packetsGrid
                        .getUI()
                        .ifPresent(ui -> ui.access(() -> packetsGrid.getDataProvider().refreshAll()));
            } else {
                nifSubmitButton.setVisible(true);
                nifShutdownButton.setVisible(false);
            }
        });
    }

    private void portComboBoxCustomValueEvent(){
        portComboBox.addCustomValueSetListener(event -> portComboBox.setValue(event.getDetail()));
    }
}
