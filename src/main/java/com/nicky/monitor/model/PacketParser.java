/**
 * *****************************************************************************
 * Copyright (c) 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************
 */
/*



 */
package com.nicky.monitor.model;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.pcap4j.packet.*;
import org.pcap4j.util.ByteArrays;

import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Parse the packet
 *
 * @author GeorgeKh
 */
@Slf4j
@NoArgsConstructor
public class PacketParser implements Serializable {
    private static int total_octetes = 0;
    PacketInfo packetInfo = null;

    /**
     * Format payload
     *
     * @param payLoad
     * @return
     */
    static String formatPayLoad(String payLoad) {
        int line = 0;
        String dim;
        StringBuilder finalHex = new StringBuilder();
        char[] arr = payLoad.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            line++;
            dim = "";
            if (line == 16) {
                dim = "\n";
                line = 0;
            }
            int nVal = (int) arr[i];
            // Is character ISO control
            boolean bISO = Character.isISOControl(arr[i]);
            // Is Ignorable identifier
            boolean bIgnorable = Character.isIdentifierIgnorable(arr[i]);
            // Remove tab and other unwanted characters..
            if (nVal == 9 || bISO || bIgnorable) {
                arr[i] = '.';
            } else if (nVal > 255) {
                arr[i] = '.';
            }
            if (arr[i] == '\n' || arr[i] == '\r' || arr[i] == ' ' || arr[i] == '\0') {
                finalHex.append('.').append(dim);
            } else {
                finalHex.append(arr[i]).append(dim);
            }
        }
        return finalHex.toString();
    }


    /**
     *
     * @param packet
     */
    public static PacketInfo parsePacket(Packet packet) {
        total_octetes = 0;
        return extractPacketInfo(packet);
    }

    /**
     * Extract packet info
     *
     * @param packet
     * @return
     */
    private static PacketInfo extractPacketInfo(Packet packet) {
        PacketInfo packetInfo = new PacketInfo();
        packetInfo.setLocalDateTime(DateTimeFormatter.ofPattern("MM-dd HH:mm:ss").format(LocalDateTime.now()));
        if (packet != null) {
            packetInfo.setPacket(packet);
            packetInfo.setPacketLength(packet.length() + "b");
            packetInfo.setPacketHex(formatHex(DatatypeConverter.printHexBinary(packet.getRawData())));
            packetInfo.setPacketRawData(formatPayLoad(new String(packet.getRawData())));
        }

        // defien whether packet has vlan or not
        if(packet.get(Dot1qVlanTagPacket.class) != null){
            packetInfo.setVlanPacket(true);
        }
        // If the packet has Ethernet
        if (packet.get(EthernetPacket.class) != null) {
            packetInfo.setEthernetHex(getHeaderOffset(packet.get(EthernetPacket.class).getHeader().toHexString().toUpperCase()));
            packetInfo.setDestMac(packet.get(EthernetPacket.class).getHeader().getDstAddr().toString());
            packetInfo.setSrcMac(packet.get(EthernetPacket.class).getHeader().getSrcAddr().toString());
            if (packet.get(EthernetPacket.class).getPad().length > 0){
                packetInfo.setEthernetRawData(ByteArrays.toHexString(packet.get(EthernetPacket.class).getPad(), " "));
            }
        }

        // if the packet has IPV4
        if (packet.get(IpV4Packet.class) != null) {
            packetInfo.setIpv4Hex(getHeaderOffset(packet.get(IpV4Packet.class).getHeader().toHexString().toUpperCase()));
            packetInfo.setIpv4RawData(new String());
            packetInfo.setDestIpv4(packet.get(IpV4Packet.class).getHeader().getDstAddr().getHostAddress());
            packetInfo.setSrcIpv4(packet.get(IpV4Packet.class).getHeader().getSrcAddr().getHostAddress());
        }

        // if the packet has TCP
        if (packet.get(TcpPacket.class) != null) {
            packetInfo.setL4Name("TCP");
            packetInfo.setL4Hex(getHeaderOffset(packet.get(TcpPacket.class).getHeader().toHexString().toUpperCase()));
            packetInfo.setL4SrcPort(String.valueOf(packet.get(TcpPacket.class).getHeader().getSrcPort().valueAsInt()));
            packetInfo.setL4DestPort(String.valueOf(packet.get(TcpPacket.class).getHeader().getDstPort().valueAsInt()));
            packetInfo.setL4RawData(new String());
            if (packet.get(TcpPacket.class).getPayload() != null) {
                packetInfo.setPacketPayLoad(getHeaderOffset(spaceHex(Hex.encodeHexString(packet.get(TcpPacket.class).getPayload().getRawData()))));
            } else {
                packetInfo.setPacketPayLoad(null);
            }
        }

        // if the packet has UDP
        if (packet.get(UdpPacket.class) != null) {
            packetInfo.setL4Name("UDP");
            packetInfo.setL4Hex(getHeaderOffset(packet.get(UdpPacket.class).getHeader().toHexString().toUpperCase()));
            packetInfo.setL4RawData(new String());

            if (packet.get(UdpPacket.class).getPayload() != null) {
                packetInfo.setPacketPayLoad(getHeaderOffset(spaceHex(Hex.encodeHexString(packet.get(UdpPacket.class).getPayload().getRawData()))));
            } else {
                packetInfo.setPacketPayLoad(null);
            }
        }

        // if packet has Unknown packet
        if (packet.get(UnknownPacket.class) != null && packet.get(UnknownPacket.class).length() > 0){
            packetInfo.setUnknownRawData(ByteArrays.toHexString(packet.get(UnknownPacket.class).getRawData(), " "));
        }
        return packetInfo;
    }

    /**
     * Spacing the hex
     *
     * @param hexString
     * @return
     */
    private static String spaceHex(String hexString) {
        char[] hexChar = hexString.toCharArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < hexChar.length; i++) {
            result.append(hexChar[i]);
            if (i % 2 != 0) {
                result.append(' ');
            }
        }
        return result.toString().trim().toUpperCase();
    }

    /**
     * Format Hex
     *
     * @param hex
     * @return
     */
    private static String formatHex(String hex) {
        int block = 0, line = 0, small = 0, octets = 0;
        String[] chare = hex.split("(?!^)");
        String dim;
        StringBuilder finalHex = new StringBuilder("0000 ");
        for (int i = 0; i < chare.length; i++) {
            block++;
            line++;
            small++;
            dim = "";
            if (small == 2) {
                dim = " ";
                small = 0;
                octets++;
            }
            if (block == 8) {
                dim = "  ";
                block = 0;
            }
            if (line == 32) {
                dim = "\n" + String.format("%04X", octets) + " ";
                line = 0;
            }
            finalHex.append(chare[i]).append(dim);
        }
        return finalHex.toString().toUpperCase();
    }

    /**
     * Return header offset
     *
     * @param header
     * @return
     */
    private static String getHeaderOffset(String header) {
        int offset = total_octetes;
        String[] headerOctets = header.split(" ");
        total_octetes += headerOctets.length;

        return String.format("%04X", offset) + "-" + String.format("%04X", total_octetes) + " " + header;
    }
}
