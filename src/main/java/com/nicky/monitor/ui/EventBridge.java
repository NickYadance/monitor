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
        nifSubmitButton.getButton().addClickListener(event -> {
            if (nifComboBox.getComboBox().getValue() == null
                    || StringUtils.isEmpty(proxyComboBox.getComboBox().getValue())
                    || StringUtils.isEmpty(portComboBox.getComboBox().getValue())){
                Notification.show("Need device config", 2500, Notification.Position.TOP_CENTER);
                return;
            }
            monitor.start(nifComboBox.getComboBox().getValue().getId(),
                    proxyComboBox.getComboBox().getValue(),
                    portComboBox.getComboBox().getValue().trim());
        });
    }

    private void nifShutdownButtonClickEvent(){
        nifShutdownButton.getButton().addClickListener(event -> {
            monitor.shutdown();
        });
    }

    private void monitorPacketEvent(){
        monitor.addPacketListener(packet -> packetsGrid.getGrid()
                .getUI()
                .ifPresent(ui -> ui.access(() -> {
                    packetsGrid.getPackets().offer(PacketParser.parsePacket(packet));
                    packetsGrid.getGrid().getDataProvider().refreshAll();
                })));
    }

    private void monitorStatusEvent(){
        monitor.addStatusListener(status -> {
            if (status){
                nifSubmitButton.getButton().setVisible(false);
                nifShutdownButton.getButton().setVisible(true);
                packetsGrid.getPackets().clear();
                packetsGrid.getGrid()
                        .getUI()
                        .ifPresent(ui -> ui.access(() -> packetsGrid.getGrid().getDataProvider().refreshAll()));
            } else {
                nifSubmitButton.getButton().setVisible(true);
                nifShutdownButton.getButton().setVisible(false);
            }
        });
    }

    private void portComboBoxCustomValueEvent(){
        portComboBox.getComboBox().addCustomValueSetListener(event -> portComboBox.getComboBox().setValue(event.getDetail()));
    }
}
