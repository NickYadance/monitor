package com.nicky.monitor.ui.components;

import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class NifShutdownButton implements UiComponent {
    @Getter
    private Button button;

    @Override
    @PostConstruct
    public void init() {
        button = new Button();
        button.setVisible(false);
    }
}
