package com.nicky.monitor.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@SpringComponent
@UIScope
public class ProxyComboBox extends ComboBox<String>{
    @Autowired
    public ProxyComboBox(){
        super("Choose the proxy");
    }
    
    @Autowired
    private SerializableFunction<String, Component> serializableFunction;

    private Map<String, String> portDescription = new HashMap<>();

    @PostConstruct
    public void init() {
        initData();
        setItems(portDescription.keySet());
        setRenderer(new ComponentRenderer<>(item -> serializableFunction.apply(item + ":" + portDescription.get(item))));
        setPattern("[a-z]*");
        setPlaceholder("Default all proxy");
    }

    private void initData(){
        // Linux proxy
        portDescription.put("tcp", "tcp");
        portDescription.put("udp", "udp");
    }
}
