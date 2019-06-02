package com.nicky.monitor.model;

import lombok.Getter;
import lombok.Setter;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.util.LinkLayerAddress;

import java.io.Serializable;
import java.util.List;

import static com.nicky.monitor.constants.HtmlTag.HTML_BREAK;

@Setter
@Getter
public class NifComboBoxModel implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private List<LinkLayerAddress> linkLayerAddress;
    private List<PcapAddress> address;

    /**
     * Used to show label in ComboBox selected text field
     * No html tag combined
     * @return selected text field label
     */
    public String toLabelString(){
        StringBuilder sb = new StringBuilder();
        if (this.getId() != null && this.getName() != null) {
            sb.append("NIF[").append(this.getId()).append("]: ").append(this.getName());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div><pre>");
        if (this.getId() != null && this.getName() != null) {
            sb.append("NIF[").append(this.getId()).append("]: ").append(this.getName()).append(HTML_BREAK);
        }
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
