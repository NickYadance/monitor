package com.nicky.monitor.ui.components;

import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Component
@UIScope
public class PacketTextGrid implements UiComponent {
    @Autowired
    private Renderer<String> renderer;

    @Getter
    private Grid<String> grid;

    @Getter
    private List<String> packetText;

    @Override
    @PostConstruct
    public void init() {
        packetText = new ArrayList<>();
        grid = new Grid<>();
        grid.setWidthFull();
        grid.setDataProvider(DataProvider.ofCollection(this.packetText));
        grid.addColumn(renderer).setHeader("Ip Packet");
    }

    @Override
    public Component get() {
        return grid;
    }
}
