package com.nicky.monitor.ui.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class NifSubmitButton extends Button {
    @Autowired
    public NifSubmitButton(){
        super("Start listening", new Icon(VaadinIcon.ARROW_RIGHT));
    }

    @PostConstruct
    public void init() {
    }
}
