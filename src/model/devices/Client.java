package model.devices;

import events.Event;
import events.arp.ArpRequestEvent;
import events.arp.ArpResponseEvent;
import events.tcp.*;
import model.*;
import model.packet.IpPayload;
import model.packet.Packet;
import model.packet.transport.TcpPayload;

import java.util.*;

public class Client extends Device {

    private final ArrayList<IpAddress> pendingArpRequests = new ArrayList<>();
    private final ArrayList<IpAddressMacMapping> ipAddressMacMappings = new ArrayList<>();

    private final ArrayList<TcpCurrentSendingState> currentSendingStates = new ArrayList<>();

    public Client(String name, String macAddress, IpAddress ipAddress, IpAddress subnetMask, Device defaultGateway, Link networkLink) {
        super(name, macAddress, ipAddress, subnetMask, defaultGateway, networkLink);
    }

    public void sendEvent(Event event) {
        Device source = event.getSource();

        if (!currentIsSource(source)) {
            return;
        }

        source.processSentEvent(networkLink.getLinkedDevice(), event);
        networkLink.getLinkedDevice().processReceivedEvent(this, event);
    }

    @Override
    protected void processSentEvent(Device destination, Event event) {
        logSentEvent(event, destination);
        if (event instanceof ArpRequestEvent) {
            processSentArpRequestEvent((ArpRequestEvent) event);
        } else if (event instanceof TcpSynEvent || event instanceof TcpSynAckEvent) {
            processSentTcpSynEvent(event);
        } else if (event instanceof TcpAckEvent) {
            processSentAckEvent((TcpAckEvent) event);
        } else if (event instanceof TcpFinEvent) {
            processSentTcpFinEvent((TcpFinEvent) event);
        } else if (event instanceof TcpFinAckEvent) {
            processSentTcpFinAckEvent((TcpFinAckEvent) event);
        } else if (event instanceof TcpSendDataEvent) {
            processSentTcpSendDataEvent((TcpSendDataEvent) event);
        }
    }

    private void processSentArpRequestEvent(ArpRequestEvent event) {
        Packet packet = event.getPacket();
        IpPayload ipPayload = packet.getIpPayload();
        pendingArpRequests.add(ipPayload.getDestinationIp());
    }

    private void processSentTcpSynEvent(Event event) {
        IpPayload ipPayload = event.getPacket().getIpPayload();
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();
        tcpConnectionsWithSynSent.add(new TcpConnection(ipPayload.getDestinationIp(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort()));
    }

    private void processSentAckEvent(TcpAckEvent event) {
        IpPayload ipPayload = event.getPacket().getIpPayload();
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();

        TcpConnection currentConnection = new TcpConnection(ipPayload.getDestinationIp(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort());

        if (hasSentSyn(currentConnection)) {
            deleteFromConnections(tcpConnectionsWithSynSent, currentConnection);
            tcpConnections.add(currentConnection);
        }
    }

    private void processSentTcpFinEvent(TcpFinEvent event) {
        IpPayload ipPayload = event.getPacket().getIpPayload();
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();

        TcpConnection currentConnection = new TcpConnection(ipPayload.getDestinationIp(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort());

        if (!hasReceivedFin(currentConnection)) {
            tcpConnectionsWithFinInitiated.add(currentConnection);
        }
    }

    private void processSentTcpFinAckEvent(TcpFinAckEvent event) {
        IpPayload ipPayload = event.getPacket().getIpPayload();
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();

        TcpConnection currentConnection = new TcpConnection(ipPayload.getDestinationIp(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort());

        if (hasInitiatedFin(currentConnection)) {
            deleteFromConnections(tcpConnectionsWithFinReceived, currentConnection);
            deleteFromConnections(tcpConnectionsWithFinInitiated, currentConnection);
            deleteFromConnections(tcpConnections, currentConnection);
        }
    }

    private void processSentTcpSendDataEvent(TcpSendDataEvent event) {
        IpPayload ipPayload = event.getPacket().getIpPayload();
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();

        TcpConnection currentConnection = new TcpConnection(ipPayload.getDestinationIp(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort());

        if (hasTcpConnection(currentConnection)) {
            byte[] data = event.getData();
            byte[][] segments = splitIntoSegments(data);
            Stack<TcpSendDataSegmentEvent> sendDataSegmentEvents = new Stack<>();
            HashMap<Integer, TcpSendDataSegmentEvent> sentDataSegmentEvent = new HashMap<>();
            int sequenceNumber = 1;
            for (int i = 0; i < segments.length; i++) {
                byte[] segment = segments[i];
                TcpSendDataSegmentEvent sendDataSegmentEvent = new TcpSendDataSegmentEvent(event.getSource(), event.getDestination(), segment, tcpPayload.getSourcePort(), tcpPayload.getDestinationPort(), sequenceNumber, new String(segment), System.currentTimeMillis(), event.getWindowSize());
                sendDataSegmentEvents.push(sendDataSegmentEvent);
                if (i != segments.length - 1) {
                    sequenceNumber += segment.length;
                }
            }

            for (int i = 0; i < event.getWindowSize(); i++) {
                if (!sendDataSegmentEvents.empty()) {
                    TcpSendDataSegmentEvent sendDataSegmentEvent = sendDataSegmentEvents.pop();
                    sentDataSegmentEvent.put(sendDataSegmentEvent.getSequenceNumber(), sendDataSegmentEvent);
                    sendEvent(sendDataSegmentEvent);
                }
            }
            TcpCurrentSendingState currentSendingState = new TcpCurrentSendingState(currentConnection, sendDataSegmentEvents, sentDataSegmentEvent, sequenceNumber, new HashSet<>());
            currentSendingStates.add(currentSendingState);
        }
    }

    //Based on https://stackoverflow.com/a/39788851
    private byte[][] splitIntoSegments(byte[] data) {
        if (data.length <= Device.MSS) {
            return new byte[][]{data};
        }
        int lastArrayLength = data.length % Device.MSS;
        int numberOfSegments = data.length / Device.MSS + (lastArrayLength > 0 ? 1 : 0);
        byte[][] segments = new byte[numberOfSegments][];

        for (int i = 0; i < (lastArrayLength > 0 ? numberOfSegments - 1 : numberOfSegments); i++) {
            segments[i] = Arrays.copyOfRange(data, i * Device.MSS, i * Device.MSS + Device.MSS);
        }
        if (lastArrayLength > 0) {
            segments[numberOfSegments - 1] = Arrays.copyOfRange(data, (numberOfSegments - 1) * Device.MSS, (numberOfSegments - 1) * Device.MSS + lastArrayLength);
        }
        return segments;
    }


    @Override
    protected void processReceivedEvent(Device source, Event event) {
        logReceivedEvent(event, source);
        if (event instanceof ArpRequestEvent) {
            processReceivedArpRequestEvent((ArpRequestEvent) event);
        } else if (event instanceof ArpResponseEvent) {
            processReceivedArpResponseEvent((ArpResponseEvent) event);
        } else if (event instanceof TcpSynEvent) {
            processReceivedTcpSynEvent((TcpSynEvent) event);
        } else if (event instanceof TcpSynAckEvent) {
            processReceivedTcpSynAckEvent((TcpSynAckEvent) event);
        } else if (event instanceof TcpAckEvent) {
            processReceivedTcpAckEvent((TcpAckEvent) event);
        } else if (event instanceof TcpFinEvent) {
            processReceivedTcpFinEvent((TcpFinEvent) event);
        } else if (event instanceof TcpFinAckEvent) {
            processReceivedTcpFinAckEvent((TcpFinAckEvent) event);
        } else if (event instanceof TcpAckDataSegmentEvent) {
            processReceivedTcpAckDataSegmentEvent((TcpAckDataSegmentEvent) event);
        }
    }

    private void processReceivedArpRequestEvent(ArpRequestEvent event) {
        Packet packet = event.getPacket();
        IpPayload ipPayload = packet.getIpPayload();
        if (ipPayload.getDestinationIp().equals(getIpAddress())) {
            ArpResponseEvent response = new ArpResponseEvent(this, event.getSource());
            sendEvent(response);
        }
    }

    private void processReceivedArpResponseEvent(ArpResponseEvent event) {
        Packet packet = event.getPacket();
        IpPayload ipPayload = packet.getIpPayload();

        if (pendingArpRequests.contains(ipPayload.getSourceIp())) {
            pendingArpRequests.remove(ipPayload.getSourceIp());
            ipAddressMacMappings.add(new IpAddressMacMapping(ipPayload.getSourceIp(), packet.getSourceMacAddress()));
        }
    }

    private void processReceivedTcpSynEvent(TcpSynEvent event) {
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();

        TcpSynAckEvent tcpSynAckEvent = new TcpSynAckEvent(this, event.getSource(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort());
        sendEvent(tcpSynAckEvent);
    }

    private void processReceivedTcpSynAckEvent(TcpSynAckEvent event) {
        IpPayload ipPayload = event.getPacket().getIpPayload();
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();

        TcpConnection currentConnection = new TcpConnection(ipPayload.getSourceIp(), tcpPayload.getSourcePort(), tcpPayload.getDestinationPort());

        if (hasSentSyn(currentConnection)) {
            TcpAckEvent tcpAckEvent = new TcpAckEvent(this, event.getSource(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort());
            sendEvent(tcpAckEvent);
        }
    }

    private void processReceivedTcpAckEvent(TcpAckEvent event) {
        IpPayload ipPayload = event.getPacket().getIpPayload();
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();

        TcpConnection currentConnection = new TcpConnection(ipPayload.getSourceIp(), tcpPayload.getSourcePort(), tcpPayload.getDestinationPort());

        if (hasSentSyn(currentConnection)) {
            deleteFromConnections(tcpConnectionsWithSynSent, currentConnection);
            tcpConnections.add(currentConnection);
        }
    }

    private void processReceivedTcpFinEvent(TcpFinEvent event) {
        IpPayload ipPayload = event.getPacket().getIpPayload();
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();

        TcpConnection currentConnection = new TcpConnection(ipPayload.getSourceIp(), tcpPayload.getSourcePort(), tcpPayload.getDestinationPort());

        if (hasTcpConnection(currentConnection)) {
            TcpFinAckEvent tcpFinAckEvent = new TcpFinAckEvent(this, event.getSource(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort());
            sendEvent(tcpFinAckEvent);

            if (!hasInitiatedFin(currentConnection)) {
                TcpFinEvent tcpFinEvent = new TcpFinEvent(this, event.getSource(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort());
                sendEvent(tcpFinEvent);
                tcpConnectionsWithFinReceived.add(currentConnection);
            }
        }
    }

    private void processReceivedTcpFinAckEvent(TcpFinAckEvent event) {
        IpPayload ipPayload = event.getPacket().getIpPayload();
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();

        TcpConnection currentConnection = new TcpConnection(ipPayload.getSourceIp(), tcpPayload.getSourcePort(), tcpPayload.getDestinationPort());

        if (hasReceivedFin(currentConnection)) {
            deleteFromConnections(tcpConnectionsWithFinReceived, currentConnection);
            deleteFromConnections(tcpConnectionsWithFinInitiated, currentConnection);
            deleteFromConnections(tcpConnections, currentConnection);
        }
    }

    private void processReceivedTcpAckDataSegmentEvent(TcpAckDataSegmentEvent event) {
        IpPayload ipPayload = event.getPacket().getIpPayload();
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();

        TcpConnection currentConnection = new TcpConnection(ipPayload.getSourceIp(), tcpPayload.getSourcePort(), tcpPayload.getDestinationPort());
        TcpCurrentSendingState currentSendingState = getCurrentSendingState(currentConnection);
        if (currentSendingState != null) {
            int ackNumber = event.getAcknowledgmentNumber();
            Set<Integer> acknowledgedNumbers = currentSendingState.getAcknowledgedNumbers();
            Stack<TcpSendDataSegmentEvent> pendingSendDataEvents = currentSendingState.getPendingSendDataEvents();
            HashMap<Integer, TcpSendDataSegmentEvent> sentEvents = currentSendingState.getSentDataEvents();
            acknowledgedNumbers.add(ackNumber);

            int resentEvents = 0;
            for (Map.Entry<Integer, TcpSendDataSegmentEvent> sentEvent : sentEvents.entrySet()) {
                if (sentEvent.getKey() == ackNumber && resentEvents < event.getWindowSize()) {
                    sentEvents.put(sentEvent.getValue().getSequenceNumber(), sentEvent.getValue());
                    sendEvent(sentEvent.getValue());
                    resentEvents++;
                }
            }

            if (resentEvents < event.getWindowSize()) {
                for (int i = 0; i < event.getWindowSize() - resentEvents; i++) {
                    if (!currentSendingState.getPendingSendDataEvents().empty()) {
                        TcpSendDataSegmentEvent sendDataSegmentEvent = pendingSendDataEvents.pop();
                        sentEvents.put(sendDataSegmentEvent.getSequenceNumber(), sendDataSegmentEvent);
                        sendEvent(sendDataSegmentEvent);
                    }
                }
            }

            if (pendingSendDataEvents.empty() && acknowledgedNumbers.size() == sentEvents.size() && acknowledgedNumbers.contains(currentSendingState.getLastSequenceNumber())) {
                currentSendingStates.remove(currentSendingState);
            }

        }

    }


    private TcpCurrentSendingState getCurrentSendingState(TcpConnection connection) {
        for (TcpCurrentSendingState s : currentSendingStates) {
            if (s.getConnection().getDestinationIp().equals(connection.getDestinationIp())
                    && s.getConnection().getDestinationPort() == connection.getDestinationPort()
                    && s.getConnection().getSourcePort() == connection.getSourcePort()
            ) {
                return s;
            }
        }
        return null;
    }

    private boolean hasTcpConnection(TcpConnection connection) {
        return connectionsContain(tcpConnections, connection);
    }

    private boolean hasSentSyn(TcpConnection connection) {
        return connectionsContain(tcpConnectionsWithSynSent, connection);

    }

    private boolean hasInitiatedFin(TcpConnection connection) {
        return connectionsContain(tcpConnectionsWithFinInitiated, connection);
    }

    private boolean hasReceivedFin(TcpConnection connection) {
        return connectionsContain(tcpConnectionsWithFinReceived, connection);
    }

    private boolean connectionsContain(ArrayList<TcpConnection> connections, TcpConnection connection) {
        for (TcpConnection c : connections) {
            if (c.getDestinationIp().equals(connection.getDestinationIp())
                    && c.getDestinationPort() == connection.getDestinationPort()
                    && c.getSourcePort() == connection.getSourcePort()
            ) {
                return true;
            }
        }
        return false;
    }

    private void deleteFromConnections(ArrayList<TcpConnection> connections, TcpConnection connection) {
        connections.removeIf(c -> c.getDestinationIp().equals(connection.getDestinationIp())
                && c.getDestinationPort() == connection.getDestinationPort()
                && c.getSourcePort() == connection.getSourcePort()
        );

    }


}