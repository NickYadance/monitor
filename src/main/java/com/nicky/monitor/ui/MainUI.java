package com.nicky.monitor.ui;

import com.nicky.monitor.ui.components.*;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Route("main")
@Push
@Theme(value = Material.class)
@SpringComponent
@UIScope
public class MainUI extends VerticalLayout {
    @Autowired
    private NifComboBox nifComboBox;

    @Autowired
    private NifSubmitButton nifSubmitButton;

    @Autowired
    private NifShutdownButton nifShutdownButton;

    @Autowired
    private PacketsGrid packetsGrid;

    @Autowired
    private PortComboBox portComboBox;

    @Autowired
    private ProxyComboBox proxyComboBox;

    /**
     * We need autowire EventBridge here cause it's UiScope
     * Otherwise it won't initialize
     */
    @Autowired
    private EventBridge eventBridge;

    @PostConstruct
    public void init(){
        nifComboBox.setWidth("450px");
        nifComboBox.setMinWidth("450px");

        proxyComboBox.setWidth("450px");
        proxyComboBox.setMinWidth("450px");

        portComboBox.setWidth("450px");
        portComboBox.setMinWidth("450px");

        packetsGrid.setWidthFull();
        packetsGrid.setPageSize(10);

        this.add(
                new H1("Bandwidth Monitor"),
                nifComboBox,
                proxyComboBox,
                portComboBox,
                nifSubmitButton,
                nifShutdownButton,
                packetsGrid
        );
    }
}
