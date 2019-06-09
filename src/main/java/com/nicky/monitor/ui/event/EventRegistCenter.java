package com.nicky.monitor.ui.event;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class EventRegistCenter {
    @Autowired
    private MonitorStatusChangedEvent monitorStatusChangedEvent;

    @Autowired
    private NifShutdownButtonClickedEvent nifShutdownButtonClickedEvent;

    @Autowired
    private NifSubmitButtonClickedEvent nifSubmitButtonClickedEvent;

    @Autowired
    private PacketArrivedEvent packetArrivedEvent;

    @Autowired
    private PortCustomValueEvent portCustomValueEvent;
}
