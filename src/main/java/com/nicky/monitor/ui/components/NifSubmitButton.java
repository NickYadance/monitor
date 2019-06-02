package com.nicky.monitor.ui.components;

import com.nicky.monitor.core.Monitor;
import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@org.springframework.stereotype.Component
@UIScope
public class NifSubmitButton implements UiComponent {
    @Autowired
    private NifComboBox nifComboBox;

    @Autowired
    private PortNumberField portNumberField;

    @Autowired
    private ProxyTextField proxyTextField;

    @Autowired
    private PacketsListBox listBox;

    @Autowired
    private Monitor monitor;

    private Button button;

    @Override
    @PostConstruct
    public void init() {
        button = new Button("Start listening",
                new Icon(VaadinIcon.ARROW_RIGHT),
                event -> {
            monitor.start(nifComboBox.getComboBox().getValue().getId(),
                            proxyTextField.getTextField().getValue(),
                            String.valueOf(portNumberField.getNumberField().getValue().intValue()));
            // todo: fix this shit
            listBox.getListBox().getDataProvider().refreshAll();
                });
    }

    @Override
    public Component get() {
        return button;
    }
}
