package com.nicky.monitor.ui;

import com.nicky.monitor.core.Monitor;
import com.nicky.monitor.model.NifComboBoxModel;
import com.nicky.monitor.model.PacketParser;
import com.nicky.monitor.ui.components.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
    private ProxyTextField proxyTextField;

    @Autowired
    private PortNumberField portNumberField;

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
    }

    private void nifSubmitButtonClickEvent(){
        nifSubmitButton.getButton().addClickListener(event -> {
            NifComboBoxModel nifModel = nifComboBox.getComboBox().getValue();
            if (nifModel == null){
                Notification.show("Choose the device to listen", 2500, Notification.Position.TOP_CENTER);
                return;
            }
            String proxy = proxyTextField.getTextField().getValue();
            Double port = portNumberField.getNumberField().getValue();
            String portString;
            proxy = StringUtils.isEmpty(proxy) ? "" : proxy;
            portString = port == null ? "" : String.valueOf(port.intValue());
//            monitor.start(nifModel.getId(), proxy, portString);
            monitor.start(7, "tcp", "1999");
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
                nifShutdownButton.getButton().setIcon(new Icon(VaadinIcon.SUN_RISE));
                nifShutdownButton.getButton().setText("Running");
                nifShutdownButton.getButton().setVisible(true);
            } else {
                nifSubmitButton.getButton().setVisible(true);
                nifShutdownButton.getButton().setIcon(new Icon(VaadinIcon.SUN_DOWN));
                nifShutdownButton.getButton().setText("Stopped");
                nifShutdownButton.getButton().setVisible(false);
            }
        });
    }
}
