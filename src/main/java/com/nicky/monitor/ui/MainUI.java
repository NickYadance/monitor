package com.nicky.monitor.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;

@Route("main")
@Theme(value = Material.class)
@org.springframework.stereotype.Component
public class MainUI extends VerticalLayout {
    @Autowired
    @Qualifier("nifGrid")
    private UiComponent nifGridComponent;

    @PostConstruct
    public void init(){
        add(
                new H1("Bandwidth Monitor"),
                nifGridComponent.get()
        );
    }
}
