package com.nicky.monitor.ui.components;

import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class NifSubmitButton implements UiComponent {
    @Getter
    private Button button;

    @Override
    @PostConstruct
    public void init() {
        button = new Button("Start listening", new Icon(VaadinIcon.ARROW_RIGHT));
    }

    @Override
    public Component get() {
        return button;
    }
}
