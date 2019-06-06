package com.nicky.monitor.ui.components;

import com.nicky.monitor.ui.UiComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@SpringComponent
@UIScope
public class PortComboBox implements UiComponent {
    @Getter
    private ComboBox<String> comboBox;

    @Autowired
    private SerializableFunction<String, Component> serializableFunction;

    private Map<String, String> portDescription = new TreeMap<>(Comparator.comparing(Integer::valueOf));

    @Override
    @PostConstruct
    public void init() {
        initData();
        comboBox = new ComboBox<>("Choose or edit the port");
        comboBox.setItems(portDescription.keySet());
        comboBox.setAllowCustomValue(true);
        comboBox.setRenderer(new ComponentRenderer<>(item -> serializableFunction.apply(item + ":" + portDescription.getOrDefault(item, "Unknown"))));
        comboBox.setPlaceholder("Default all port");
        comboBox.setPattern("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");
    }

    private void initData(){
        // Linux special port
        portDescription.put("20", " FTP Data (For transferring FTP data)");
        portDescription.put("21", " FTP Control (For starting FTP connection)");
        portDescription.put("22", " SSH (For secure remote administration which uses SSL to encrypt the transmission)");
        portDescription.put("23", " Telnet (For insecure remote administration)");
        portDescription.put("25", " SMTP (Mail Transfer Agent for e-mail server such as SEND mail)");
        portDescription.put("53", " DNS (Special service which uses both TCP and UDP)");
        portDescription.put("67", " Bootp");
        portDescription.put("68", " DHCP");
        portDescription.put("69", " TFTP (Trivial file transfer protocol uses udp protocol for connection less transmission of data)");
        portDescription.put("80", " HTTP/WWW(Apache)");
        portDescription.put("88", " Kerberos");
        portDescription.put("110", " POP3 (Mail delivery Agent)");
        portDescription.put("123", " NTP (Network time protocol used for time syncing uses UDP protocol)");
        portDescription.put("137", " NetBIOS (nmbd)");
        portDescription.put("139", " SMB-Samba (smbd)");
        portDescription.put("143", " IMAP");
        portDescription.put("161", " SNMP (For network monitoring)");
        portDescription.put("389", " LDAP (For centralized administration)");
        portDescription.put("443", " HTTPS (HTTP+SSL for secure web access)");
        portDescription.put("514", " Syslogd (udp port)");
        portDescription.put("636", " ldaps (both ctp and udp)");
        portDescription.put("873", " rsync");
        portDescription.put("989", " FTPS-data");
        portDescription.put("990", " FTPS");
        portDescription.put("993", " IMAPS");
        portDescription.put("1194", " openVPN");
        portDescription.put("1812", " RADIUS");
        portDescription.put("995", " POP3s");
        portDescription.put("2049", " NFS (nfsd, rpc.nfsd, rpc, portmap)");
        portDescription.put("2401", " CVS server");
        portDescription.put("3306", " MySql");
        portDescription.put("3690", " SVN");

    }
}
