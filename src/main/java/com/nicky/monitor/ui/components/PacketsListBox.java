package com.nicky.monitor.ui.components;

import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class PacketsListBox implements UiComponent {
    @Autowired
    private Renderer<String> renderer;

    @Getter
    private ListBox<String> listBox;

    @Getter
    private CircularFifoQueue<String> packets;

    private int listBoxSize = 100;

    @Override
    @PostConstruct
    public void init() {
        packets = new CircularFifoQueue<>(listBoxSize);
        listBox = new ListBox<>();
        listBox.setDataProvider(DataProvider.ofCollection(packets));
        listBox.setRenderer((ComponentRenderer<Component, String>)renderer);
    }

    @Override
    public Component get() {
        return listBox;
    }
}
