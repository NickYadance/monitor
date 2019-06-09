package com.nicky.monitor.ui.event;

import com.nicky.monitor.core.Monitor;
import com.nicky.monitor.ui.components.NifShutdownButton;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class NifShutdownButtonClickedEvent {
    @Autowired
    private Monitor monitor;

    @Autowired
    private NifShutdownButton nifShutdownButton;

    @PostConstruct
    public void register(){
        nifShutdownButton.addClickListener(event -> {
            monitor.shutdown();
        });
    }
}
