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
        nifComboBox.getComboBox().setWidth("450px");
        nifComboBox.getComboBox().setMinWidth("450px");

        proxyComboBox.getComboBox().setWidth("450px");
        proxyComboBox.getComboBox().setMinWidth("450px");

        portComboBox.getComboBox().setWidth("450px");
        portComboBox.getComboBox().setMinWidth("450px");

        packetsGrid.getGrid().setWidthFull();
        packetsGrid.getGrid().setPageSize(10);

        this.add(
                new H1("Bandwidth Monitor"),
                nifComboBox.getComboBox(),
                proxyComboBox.getComboBox(),
                portComboBox.getComboBox(),
                nifSubmitButton.getButton(),
                nifShutdownButton.getButton(),
                packetsGrid.getGrid()
        );
    }
}
