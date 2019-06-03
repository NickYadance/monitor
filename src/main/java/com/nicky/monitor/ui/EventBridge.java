package com.nicky.monitor.ui;

import com.nicky.monitor.core.Monitor;
import com.nicky.monitor.ui.components.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class EventBridge {
    @Autowired
    private Monitor monitor;

    @Autowired
    private PacketsListBox listBox;

    @Autowired
    private NifComboBox nifComboBox;

    @Autowired
    private ProxyTextField proxyTextField;

    @Autowired
    private PortNumberField portNumberField;

    @Autowired
    private PacketTextGrid packetTextGrid;

    @Autowired
    private NifSubmitButton nifSubmitButton;

    @PostConstruct
    public void init(){
        nifSubmitButtonClickEvent();
        listBoxValueChangeEvent();
        packetMonitorEvent();
    }

    private void nifSubmitButtonClickEvent(){
        nifSubmitButton.getButton().addClickListener(event -> monitor.start(
                nifComboBox.getComboBox().getValue().getId(),
                proxyTextField.getTextField().getValue(),
                String.valueOf(portNumberField.getNumberField().getValue().intValue())));
    }

    private void listBoxValueChangeEvent(){
        listBox.getListBox().addValueChangeListener(event -> {
            if (packetTextGrid.getPacketText().size() == 0){
                packetTextGrid.getPacketText().add(event.getValue());
            } else {
                packetTextGrid.getPacketText().set(0, event.getValue());
            }
            packetTextGrid.getGrid().getDataProvider().refreshAll();
        });
    }

    private void packetMonitorEvent(){
        monitor.addPacketListener(packet -> listBox.getListBox()
                .getUI()
                .ifPresent(ui -> ui.access(() -> {
                    listBox.getPackets().add(packet.toString());
                    listBox.getListBox().getDataProvider().refreshAll();
                    ui.push();
                })));
    }
}
