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
package com.nicky.monitor.model;

import lombok.Data;
import org.pcap4j.packet.Packet;

import java.io.Serializable;

/**
 *
 * @author GeorgeKh
 */
@Data
public class PacketInfo implements Serializable {

    private String localDateTime = null;

    private Packet packet = null;
    private String packetLength = null;
    private String packetHex = null;
    private String packetRawData = null;
    private String packetPayLoad = null;

    private String ethernetHex = null;
    private String ethernetRawData = null;
    private String destMac = null;
    private String srcMac = null;

    private String ipv4Hex = null;
    private String ipv4RawData = null;
    private String destIpv4 = null;
    private String srcIpv4 = null;

    private String l4Name = null;
    private String l4Hex = null;
    private String l4RawData = null;
    private String l4SrcPort = null;
    private String l4DestPort = null;

    private String unknownRawData = null;

    private boolean vlanPacket = false;
    private long timeStamp;
}
