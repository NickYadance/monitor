package com.nicky.monitor.ui.components;

import com.nicky.monitor.core.Monitor;
import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Component
@UIScope
public class PacketsListBox implements UiComponent {
    @Autowired
    private PacketTextGrid packetTextGrid;

    @Autowired
    private Renderer<String> renderer;

    @Autowired
    private Monitor monitor;

    @Getter
    private ListBox<String> listBox;

    @Override
    @PostConstruct
    public void init() {
        listBox = new ListBox<>();
        listBox.setDataProvider(DataProvider.ofCollection(monitor.getPackets()));
        listBox.setRenderer((ComponentRenderer<Component, String>)renderer);
        listBox.addValueChangeListener(event -> {
            if (packetTextGrid.getPacketText().size() == 0){
                packetTextGrid.getPacketText().add(event.getValue());
            } else {
                packetTextGrid.getPacketText().set(0, event.getValue());
            }
            packetTextGrid.getGrid().getDataProvider().refreshAll();
        });
    }

    @Override
    public Component get() {
        return listBox;
    }
}
