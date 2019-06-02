package com.nicky.monitor.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;

@Route("main")
@Theme(value = Material.class)
@org.springframework.stereotype.Component
@UIScope
public class MainUI extends VerticalLayout {
    @Autowired
    @Qualifier("nifComboBox")
    private UiComponent nifComboBox;

    @Autowired
    @Qualifier("nifSubmitButton")
    private UiComponent nifSubmitButton;

    @Autowired
    @Qualifier("packetsListBox")
    private UiComponent packetsListBox;

    @Autowired
    @Qualifier("packetTextGrid")
    private UiComponent packetTextArea;

    @Autowired
    @Qualifier("portNumberField")
    private UiComponent portNumberField;

    @Autowired
    @Qualifier("proxyTextField")
    private UiComponent proxyTextField;

    @PostConstruct
    public void init(){
        add(
                new H1("Bandwidth Monitor"),
                nifComboBox.get(),
                nifSubmitButton.get(),
                packetsListBox.get(),
                packetTextArea.get(),
                portNumberField.get(),
                proxyTextField.get()
        );
    }
}
