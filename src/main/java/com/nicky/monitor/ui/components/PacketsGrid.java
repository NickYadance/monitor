package com.nicky.monitor.ui.components;

import com.nicky.monitor.model.PacketInfo;
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
public class PacketsGrid extends Grid<PacketInfo> {
    @Getter
    private CircularFifoQueue<PacketInfo> packets;

    @Autowired
    private SerializableFunction<String, Component> htmlSerializer;

    private final int gridMaxRows = 10;

    @PostConstruct
    public void init() {
        packets = new CircularFifoQueue<>(gridMaxRows);
        setDataProvider(DataProvider.ofCollection(this.packets));
        setItemDetailsRenderer(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getPacket().toString())));
        addColumn(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getLocalDateTime()))).setHeader("Time");
        addColumn(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getL4Name()))).setHeader("Protocol");
        addColumn(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getPacketLength()))).setHeader("Length");
        addColumn(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getUnknownRawData()))).setHeader("Raw data");
        addColumn(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getSrcIpv4() + ":" + packetInfo.getL4SrcPort()))).setHeader("Src addr");
        addColumn(new ComponentRenderer<>(packetInfo -> htmlSerializer.apply(packetInfo.getDestIpv4() + ":" + packetInfo.getL4DestPort()))).setHeader("Dest addr");
    }
}
