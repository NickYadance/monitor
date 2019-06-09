package com.nicky.monitor.ui.event;

import com.nicky.monitor.core.Monitor;
import com.nicky.monitor.ui.components.NifComboBox;
import com.nicky.monitor.ui.components.NifSubmitButton;
import com.nicky.monitor.ui.components.PortComboBox;
import com.nicky.monitor.ui.components.ProxyComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class NifSubmitButtonClickedEvent {
    @Autowired
    private NifSubmitButton nifSubmitButton;

    @Autowired
    private NifComboBox nifComboBox;

    @Autowired
    private ProxyComboBox proxyComboBox;

    @Autowired
    private PortComboBox portComboBox;

    @Autowired
    private Monitor monitor;

    @PostConstruct
    public void register(){
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
}
