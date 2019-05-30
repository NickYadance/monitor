package com.nicky.monitor.ui.components;

import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.UIScope;

import javax.annotation.PostConstruct;

@org.springframework.stereotype.Component
@UIScope
public class NifSubmitButton implements UiComponent {
    private Button button;
    private Icon icon;
    private String text;

    @Override
    @PostConstruct
    public void init() {
        icon = new Icon(VaadinIcon.ARROW_RIGHT);
        text = "Start listening";
        button = new Button(text, icon, event -> {
        });
    }

    @Override
    public Component get() {
        return button;
    }
}
