package com.nicky.monitor.ui.event;

import com.nicky.monitor.ui.components.PortComboBox;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class PortCustomValueEvent {
    @Autowired
    private PortComboBox portComboBox;

    @PostConstruct
    public void register(){
        portComboBox.addCustomValueSetListener(event -> portComboBox.setValue(event.getDetail()));
    }
}
