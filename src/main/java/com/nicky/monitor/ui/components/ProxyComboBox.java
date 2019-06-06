package com.nicky.monitor.ui.components;

import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@SpringComponent
@UIScope
public class ProxyComboBox implements UiComponent {
    @Getter
    private ComboBox<String> comboBox;

    @Autowired
    private SerializableFunction<String, Component> serializableFunction;

    private Map<String, String> portDescription = new HashMap<>();

    @Override
    @PostConstruct
    public void init() {
        initData();
        comboBox = new ComboBox<>("Choose the proxy");
        comboBox.setItems(portDescription.keySet());
        comboBox.setRenderer(new ComponentRenderer<>(item -> serializableFunction.apply(item + ":" + portDescription.get(item))));
        comboBox.setPattern("[a-z]*");
        comboBox.setPlaceholder("Default all proxy");
    }

    private void initData(){
        // Linux proxy
        portDescription.put("tcp", "tcp");
        portDescription.put("udp", "udp");
    }
}
