package com.nicky.monitor.ui.components;

import com.nicky.monitor.core.IpPacketMonitor;
import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.Getter;
import lombok.Setter;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.util.LinkLayerAddress;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;


@org.springframework.stereotype.Component
public class NifGrid implements UiComponent {
    private static final int PAGE_SIZE = 10;
    private static final String HTML_BREAK = "<br>";

    @Setter
    @Getter
    private static class NifDomain {
        private Integer id;
        private String name;
        private String description;
        private List<LinkLayerAddress> linkLayerAddress;
        private List<PcapAddress> address;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("<div><pre>");
            sb.append("NIF[").append(this.getId()).append("]: ").append(this.getName()).append(HTML_BREAK);

            if (this.getDescription() != null) {
                sb.append("      : description: [").append(this.getDescription()).append("]").append(HTML_BREAK);
            }

            if (this.getLinkLayerAddress() != null) {
                this.getLinkLayerAddress().forEach(layerAddress -> sb.append("      : Link layer address: [").append(layerAddress).append("]").append(HTML_BREAK));
            }

            if (this.getAddress() != null){
                this.getAddress().forEach(pcapAddress -> sb.append("      : Net layer address: ").append(HTML_BREAK)
                        .append("            : Address: ").append(pcapAddress.getAddress()).append(HTML_BREAK)
                        .append("            : Netmask: ").append(pcapAddress.getNetmask()).append(HTML_BREAK)
                        .append("            : BroadcastAddress: ").append(pcapAddress.getBroadcastAddress()).append(HTML_BREAK)
                        .append("            : DestinationAddress: ").append(pcapAddress.getDestinationAddress()).append(HTML_BREAK));
            }
            sb.append("</pre></div>");
            return sb.toString();
        }
    }

    @Autowired
    private IpPacketMonitor monitor;

    private ListBox<NifDomain> listBox;
    private List<NifDomain> nifDomains;

    @PostConstruct
    public void init(){
        List<PcapNetworkInterface> nifs = monitor.getNifs();
        nifDomains = nifs.stream()
                .map(this::nifToDomain)
                .collect(Collectors.toList());
        listBox = new ListBox<>();
        listBox.setDataProvider(new ListDataProvider<>(nifDomains));
        listBox.setRenderer(new ComponentRenderer<>(nifDomain -> new Html(nifDomain.toString())));
    }

    @Override
    public Component get() {
        return listBox;
    }

    private NifDomain nifToDomain(PcapNetworkInterface networkInterface){
        NifDomain nifDomain = new NifDomain();
        nifDomain.setName(networkInterface.getName());
        nifDomain.setDescription(networkInterface.getDescription());
        nifDomain.setLinkLayerAddress(networkInterface.getLinkLayerAddresses());
        nifDomain.setAddress(networkInterface.getAddresses());
        return nifDomain;
    }
}
