package com.nicky.monitor.ui.components;

import com.nicky.monitor.constants.HtmlTag;
import com.nicky.monitor.model.PacketInfo;
import com.nicky.monitor.model.PacketParser;
import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.pcap4j.packet.Packet;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class PacketsListBox implements UiComponent {
    @Getter
    private ListBox<Packet> listBox;

    @Getter
    private CircularFifoQueue<Packet> packets;

    private int listBoxSize = 100;
    private PacketParser packetParser = new PacketParser();

    @Override
    @PostConstruct
    public void init() {
        packets = new CircularFifoQueue<>(listBoxSize);
        listBox = new ListBox<>();
        listBox.setWidth("220px");
        listBox.setDataProvider(DataProvider.ofCollection(packets));
        listBox.setRenderer(new ComponentRenderer<>(packet -> {
            PacketInfo packetInfo = new PacketInfo();
            packetParser.parsePacket(packet, packetInfo);
            return new Html("<div><pre>" +
                    "[" + packetInfo.getL4Name() + "(" + packet.length() + "b)" + "]: " + HtmlTag.HTML_BREAK +
                    "[src]: " + packetInfo.getSrcIpv4() + ":" + packetInfo.getL4SrcPort() + HtmlTag.HTML_BREAK +
                    "[dst]: " + packetInfo.getDestIpv4() + ":" + packetInfo.getL4DestPort() +
                    "<hr></pre></div>");
        }));
    }

    @Override
    public Component get() {
        return listBox;
    }
}
