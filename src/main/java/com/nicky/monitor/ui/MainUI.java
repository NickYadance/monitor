package com.nicky.monitor.ui;

import com.nicky.monitor.ui.components.*;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
    private PortNumberField portNumberField;

    @Autowired
    private ProxyTextField proxyTextField;

    /**
     * We need autowire EventBridge here cause it's UiScope
     * Otherwise it won't initialize
     */
    @Autowired
    private EventBridge eventBridge;

    private HorizontalLayout nifFormHLayout;

    @PostConstruct
    public void init(){
        nifComboBox.getComboBox().setWidth("450px");
        nifComboBox.getComboBox().setMinWidth("450px");

        nifFormHLayout = new HorizontalLayout();
        nifFormHLayout.setWidthFull();
        nifFormHLayout.add(
                portNumberField.getNumberField(),
                proxyTextField.getTextField()
        );

        packetsGrid.getGrid().setWidthFull();
        packetsGrid.getGrid().setPageSize(10);

        this.add(
                new H1("Bandwidth Monitor"),
                nifComboBox.getComboBox(),
                nifFormHLayout,
                nifSubmitButton.getButton(),
                nifShutdownButton.getButton(),
                packetsGrid.getGrid()
        );
    }
}
