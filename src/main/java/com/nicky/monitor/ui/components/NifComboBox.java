package com.nicky.monitor.ui.components;

import com.nicky.monitor.core.Monitor;
import com.nicky.monitor.model.NifComboBoxModel;
import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import org.pcap4j.core.PcapNetworkInterface;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
public class NifComboBox implements UiComponent {
    @Autowired
    private Monitor monitor;

    @Getter
    private ComboBox<NifComboBoxModel> comboBox;

    private List<NifComboBoxModel> nifComboBoxModels;

    @Override
    @PostConstruct
    public void init(){
        List<PcapNetworkInterface> nifs = monitor.getNifs();
        nifComboBoxModels = nifs.stream()
                .map(this::nifToDomain)
                .collect(Collectors.toList());
        for (int i = 0; i < nifComboBoxModels.size(); i++) {
            nifComboBoxModels.get(i).setId(i);
        }
        comboBox = new ComboBox<>();
        comboBox.setLabel("Choose the network interface");
        comboBox.setItems(this.nifComboBoxModels);
        comboBox.setRenderer(new ComponentRenderer<>(nifComboBoxModel -> new Html(nifComboBoxModel.toString())));
        comboBox.setItemLabelGenerator(NifComboBoxModel::toLabelString);
    }

    private NifComboBoxModel nifToDomain(PcapNetworkInterface networkInterface){
        NifComboBoxModel nifComboBoxModel = new NifComboBoxModel();
        nifComboBoxModel.setName(networkInterface.getName());
        nifComboBoxModel.setDescription(networkInterface.getDescription());
        nifComboBoxModel.setLinkLayerAddress(networkInterface.getLinkLayerAddresses());
        nifComboBoxModel.setAddress(networkInterface.getAddresses());
        return nifComboBoxModel;
    }
}
