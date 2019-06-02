package com.nicky.monitor.ui.components;

import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;

import javax.annotation.PostConstruct;

@org.springframework.stereotype.Component
@UIScope
public class ProxyTextField implements UiComponent {
    @Getter
    private TextField textField;

    @Override
    @PostConstruct
    public void init() {
        textField = new TextField("Proxy");
    }

    @Override
    public Component get() {
        return this.textField;
    }
}
