package com.nicky.monitor.ui.components;

import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class PortNumberField implements UiComponent {
    @Getter
    private NumberField numberField;

    @Override
    @PostConstruct
    public void init() {
        numberField = new NumberField("Port");
        numberField.setPattern("[0-9]*");
        numberField.setMin(0.0D);
        numberField.setMax(65535.0D);
    }

    @Override
    public Component get() {
        return numberField;
    }
}
