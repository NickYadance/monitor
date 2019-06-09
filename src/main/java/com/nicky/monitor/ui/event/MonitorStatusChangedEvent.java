package com.nicky.monitor.ui.event;

import com.nicky.monitor.core.Monitor;
import com.nicky.monitor.ui.components.NifShutdownButton;
import com.nicky.monitor.ui.components.NifSubmitButton;
import com.nicky.monitor.ui.components.PacketsGrid;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class MonitorStatusChangedEvent {
    @Autowired
    private Monitor monitor;

    @Autowired
    private NifSubmitButton nifSubmitButton;

    @Autowired
    private PacketsGrid packetsGrid;

    @Autowired
    private NifShutdownButton nifShutdownButton;

    @PostConstruct
    public void register(){
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
}
