package devices.client;

import devices.Device;
import devices.client.handlers.received.*;
import devices.client.handlers.sent.*;
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

    public final ArrayList<TcpConnection> tcpConnections = new ArrayList<>();
    public final ArrayList<TcpConnection> tcpConnectionsWithSynSent = new ArrayList<>();
    public final ArrayList<TcpConnection> tcpConnectionsWithFinInitiated = new ArrayList<>();
    public final ArrayList<TcpConnection> tcpConnectionsWithFinReceived = new ArrayList<>();
    public final ArrayList<IpAddress> pendingArpRequests = new ArrayList<>();
    public final ArrayList<IpAddressMacMapping> ipAddressMacMappings = new ArrayList<>();
    public final ArrayList<TcpCurrentSendingState> currentSendingStates = new ArrayList<>();
    public final ArrayList<TcpCurrentReceivingState> currentReceivingStates = new ArrayList<>();


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
    public void processSentEvent(Device destination, Event event) {
        logSentEvent(event, destination);
        if (event instanceof ArpRequestEvent) {
            new ClientSentArpRequestEventHandler().processEvent(this,event);
        } else if (event instanceof TcpSynEvent || event instanceof TcpSynAckEvent) {
            new ClientSentTcpSynEventHandler().processEvent(this,event);
        } else if (event instanceof TcpAckEvent) {
            new ClientSentTcpAckEventHandler().processEvent(this, event);
        } else if (event instanceof TcpFinEvent) {
            new ClientSentTcpFinEventHandler().processEvent(this, event);
        } else if (event instanceof TcpFinAckEvent) {
            new ClientSentTcpFinAckEventHandler().processEvent(this, event);
        } else if (event instanceof TcpSendDataEvent) {
            new ClientSentTcpSendDataEventHandler().processEvent(this,event);
        }
    }

    @Override
    public void processReceivedEvent(Device source, Event event) {
        logReceivedEvent(event, source);
        if (event instanceof ArpRequestEvent) {
            new ClientReceivedArpRequestEventHandler().processEvent(this, event);
        } else if (event instanceof ArpResponseEvent) {
            new ClientReceivedArpResponseEventHandler().processEvent(this, event);
        } else if (event instanceof TcpSynEvent) {
            new ClientReceivedTcpSynEventHandler().processEvent(this, event);
        } else if (event instanceof TcpSynAckEvent) {
            new ClientReceivedTcpSynAckEventHandler().processEvent(this, event);
        } else if (event instanceof TcpAckEvent) {
            new ClientReceivedTcpAckEventHandler().processEvent(this, event);
        } else if (event instanceof TcpFinEvent) {
            new ClientReceivedTcpFinEventHandler().processEvent(this, event);
        } else if (event instanceof TcpFinAckEvent) {
            new ClientReceivedTcpFinAckEventHandler().processEvent(this, event);
        } else if (event instanceof TcpAckDataSegmentEvent) {
            new ClientReceivedTcpAckDataSegmentEventHandler().processEvent(this, event);
        } else if (event instanceof TcpSendDataSegmentEvent) {
            new ClientReceivedTcpSendDataSegmentEventHandler().processEvent(this, event);
        }
    }
}