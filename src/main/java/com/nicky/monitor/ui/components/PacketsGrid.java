package com.nicky.monitor.ui.components;

import com.nicky.monitor.model.PacketInfo;
import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class PacketsGrid implements UiComponent {
    @Getter
    private Grid<PacketInfo> grid;

    @Getter
    private CircularFifoQueue<PacketInfo> packets;

    @Autowired
    private SerializableFunction<String, Component> htmlSerializer;

    private final int gridMaxRows = 10;

    @Override
    @PostConstruct
    public void init() {
        packets = new CircularFifoQueue<>(gridMaxRows);
        grid = new Grid<>();
        grid.setDataProvider(DataProvider.ofCollection(this.packets));
        grid.setItemDetailsRenderer(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getPacket().toString())));
        grid.addColumn(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getLocalDateTime()))).setHeader("Time");
        grid.addColumn(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getL4Name()))).setHeader("Protocol");
        grid.addColumn(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getPacketLength()))).setHeader("Length");
        grid.addColumn(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getUnknownRawData()))).setHeader("Raw data");
        grid.addColumn(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getSrcIpv4() + ":" + packetInfo.getL4SrcPort()))).setHeader("Src addr");
        grid.addColumn(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getDestIpv4() + ":" + packetInfo.getL4DestPort()))).setHeader("Dest addr");
    }
}
