package com.nicky.monitor.ui;

import com.nicky.monitor.core.Monitor;
import com.nicky.monitor.model.PacketParser;
import com.nicky.monitor.ui.components.*;
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
    private ProxyTextField proxyTextField;

    @Autowired
    private PortNumberField portNumberField;

    @Autowired
    private NifSubmitButton nifSubmitButton;

    @PostConstruct
    public void init(){
        nifSubmitButtonClickEvent();
        packetMonitorEvent();
    }

    private void nifSubmitButtonClickEvent(){
        nifSubmitButton.getButton().addClickListener(event -> {
            String proxy = proxyTextField.getTextField().getValue();
            Double port = portNumberField.getNumberField().getValue();
            String portString;
            proxy = (StringUtils.isEmpty(proxy)) ? "" : proxy;
            portString = port == null ? "" : String.valueOf(port.intValue());
            monitor.start(nifComboBox.getComboBox().getValue().getId(), proxy, portString);
        });
    }

    private void packetMonitorEvent(){
        monitor.addPacketListener(packet -> packetsGrid.getGrid()
        .getUI()
        .ifPresent(ui -> ui.access(() -> {
            packetsGrid.getPackets().offer(PacketParser.parsePacket(packet));
            packetsGrid.getGrid().getDataProvider().refreshAll();
        })));
    }
}
