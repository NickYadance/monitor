package com.nicky.monitor.ui.components;

import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@org.springframework.stereotype.Component
@UIScope
public class PacketsListBox implements UiComponent {
    @Autowired
    private PacketTextGrid packetTextGrid;
    @Autowired
    private Renderer<String> renderer;
    private ListBox<String> listBox;
    private CircularFifoQueue<String> fakePacket;

    @Override
    @PostConstruct
    public void init() {
        fakePacket = new CircularFifoQueue<>(100);
        fakePacket.add("[IPv4 Header (20 bytes)]\n" +
                "  Version: 4 (IPv4)\n" +
                "  IHL: 5 (20 [bytes])\n" +
                "  TOS: [precedence: 0 (Routine)] [tos: 0 (Default)] [mbz: 0]\n" +
                "  Total length: 52 [bytes]\n" +
                "  Identification: 0\n" +
                "  Flags: (Reserved, Don't Fragment, More Fragment) = (false, true, false)\n" +
                "  Fragment offset: 0 (0 [bytes])\n" +
                "  TTL: 64\n" +
                "  Protocol: 6 (TCP)\n" +
                "  Header checksum: 0x0000\n" +
                "  Source address: /127.0.0.1\n" +
                "  Destination address: /127.0.0.1\n" +
                "[TCP Header (32 bytes)]\n" +
                "  Source port: 1999 (unknown)\n" +
                "  Destination port: 61221 (unknown)\n" +
                "  Sequence Number: 3506725693\n" +
                "  Acknowledgment Number: 3218146763\n" +
                "  Data Offset: 8 (32 [bytes])\n" +
                "  Reserved: 0\n" +
                "  URG: false\n" +
                "  ACK: true\n" +
                "  PSH: false\n" +
                "  RST: false\n" +
                "  SYN: false\n" +
                "  FIN: false\n" +
                "  Window: 6379\n" +
                "  Checksum: 0xfe28\n" +
                "  Urgent Pointer: 0\n" +
                "  Option: [Kind: 1 (No Operation)]\n" +
                "  Option: [Kind: 1 (No Operation)]\n" +
                "  Option: [Kind: 8 (Timestamps)] [Length: 10 bytes] [TS Value: 497440538] [TS Echo Reply: 497440538]\n" +
                "]\n");
        fakePacket.add("test 2");
        listBox = new ListBox<>();
        listBox.setDataProvider(DataProvider.fromCallbacks(
                query -> fakePacket.stream(),
                query -> fakePacket.size()));
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
