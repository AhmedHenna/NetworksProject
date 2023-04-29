package model.devices;

import events.Event;
import events.arp.ArpRequestEvent;
import events.arp.ArpResponseEvent;
import events.tcp.*;
import model.IpAddress;
import model.IpAddressMacMapping;
import model.Link;
import model.TcpConnection;
import model.packet.IpPayload;
import model.packet.Packet;
import model.packet.transport.TcpPayload;

import java.util.ArrayList;

public class Client extends Device {

    private final ArrayList<IpAddress> pendingArpRequests = new ArrayList<>();
    private final ArrayList<IpAddressMacMapping> ipAddressMacMappings = new ArrayList<>();

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