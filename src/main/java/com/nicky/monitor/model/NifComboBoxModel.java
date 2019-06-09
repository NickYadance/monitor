package com.nicky.monitor.model;

import lombok.Getter;
import lombok.Setter;
import org.pcap4j.core.PcapAddress;
import org.pcap4j.util.LinkLayerAddress;

import java.io.Serializable;
import java.util.List;

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
        if (this.getId() != null && this.getName() != null) {
            sb.append("NIF[").append(this.getId()).append("]: ").append(this.getName()).append(System.lineSeparator());
        }
        if (this.getDescription() != null) {
            sb.append("      : description: [").append(this.getDescription()).append("]").append(System.lineSeparator());
        }
        if (this.getLinkLayerAddress() != null) {
            this.getLinkLayerAddress().forEach(layerAddress -> sb.append("      : Link layer address: [").append(layerAddress).append("]").append(System.lineSeparator()));
        }
        if (this.getAddress() != null){
            this.getAddress().forEach(pcapAddress -> sb.append("      : Net layer address: ").append(System.lineSeparator())
                    .append("            : Address: ").append(pcapAddress.getAddress()).append(System.lineSeparator())
                    .append("            : Netmask: ").append(pcapAddress.getNetmask()).append(System.lineSeparator())
                    .append("            : BroadcastAddress: ").append(pcapAddress.getBroadcastAddress()).append(System.lineSeparator())
                    .append("            : DestinationAddress: ").append(pcapAddress.getDestinationAddress()).append(System.lineSeparator()));
        }
        return sb.toString();
    }
}
