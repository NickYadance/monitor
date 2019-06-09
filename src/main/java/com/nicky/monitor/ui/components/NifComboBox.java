package com.nicky.monitor.ui.components;

import com.nicky.monitor.core.Monitor;
import com.nicky.monitor.model.NifComboBoxModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.pcap4j.core.PcapNetworkInterface;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
public class NifComboBox extends ComboBox<NifComboBoxModel> {
    @Autowired
    private Monitor monitor;

    @Autowired
    private SerializableFunction<String, Component> serializableFunction;

    @PostConstruct
    public void init(){
        List<NifComboBoxModel> nifComboBoxModels = monitor.getNifs().stream()
                .map(this::nifToDomain)
                .collect(Collectors.toList());
        for (int i = 0; i < nifComboBoxModels.size(); i++) {
            nifComboBoxModels.get(i).setId(i);
        }
        setLabel("Choose the network interface");
        setItems(nifComboBoxModels);
        setRenderer(new ComponentRenderer<>(nifComboBoxModel -> serializableFunction.apply(nifComboBoxModel.toString())));
        setItemLabelGenerator(NifComboBoxModel::toLabelString);
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
