package devices.client;

import devices.Device;
import devices.DeviceUtil;
import devices.client.handlers.received.*;
import devices.client.handlers.sent.*;
import events.Event;
import events.EventWithDirectSourceDestination;
import events.arp.ArpRequestEvent;
import events.arp.ArpResponseEvent;
import events.tcp.*;
import model.*;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class Client extends Device {

    public final ArrayList<TcpConnection> tcpConnections = new ArrayList<>();
    public final ArrayList<TcpConnection> tcpConnectionsWithSynSent = new ArrayList<>();
    public final ArrayList<TcpConnection> tcpConnectionsWithFinInitiated = new ArrayList<>();
    public final ArrayList<TcpConnection> tcpConnectionsWithFinReceived = new ArrayList<>();
    public final ArrayList<IpAddress> pendingArpRequests = new ArrayList<>();
    public final ArrayList<IpAddressMacMapping> ipAddressMacMappings = new ArrayList<>();
    public final ArrayList<TcpCurrentSendingState> currentSendingStates = new ArrayList<>();
    public final ArrayList<TcpCurrentReceivingState> currentReceivingStates = new ArrayList<>();
    public final ClientReSender reSender = new ClientReSender(this, currentSendingStates);


    public Client(String name, String macAddress, IpAddress ipAddress, IpAddress subnetMask, Device defaultGateway,
                  Link networkLink, BlockingQueue<EventWithDirectSourceDestination> eventQueue
    ) {
        super(name, macAddress, ipAddress, subnetMask, defaultGateway, networkLink, eventQueue);
        reSender.start();
    }

    public void sendEvent(Event event) {
        pauseBeforeSending(event);
        Device source = event.getSource();

        if (!currentIsSource(source)) {
            return;
        }

        boolean shouldSend;
        if (DeviceUtil.isInSameNetwork(this, event.getDestination())) {
            shouldSend = source.processSentEvent(networkLink.getLinkedDevice(), event);
            if (shouldSend) sendEventToDevice(networkLink.getLinkedDevice(), event);
        } else {
            shouldSend = source.processSentEvent(getDefaultGateway(), event);
            if (shouldSend) sendEventToDevice(getDefaultGateway(), event);
        }
    }

    @Override
    public boolean processSentEvent(Device destination, Event event) {
        logSentEvent(event, destination);
        boolean shouldSend = true;
        if (event instanceof ArpRequestEvent) {
            new ClientSentArpRequestEventHandler().processEvent(this, event);
        } else if (event instanceof TcpSynEvent || event instanceof TcpSynAckEvent) {
            new ClientSentTcpSynEventHandler().processEvent(this, event);
        } else if (event instanceof TcpAckEvent) {
            new ClientSentTcpAckEventHandler().processEvent(this, event);
        } else if (event instanceof TcpFinEvent) {
            new ClientSentTcpFinEventHandler().processEvent(this, event);
        } else if (event instanceof TcpFinAckEvent) {
            new ClientSentTcpFinAckEventHandler().processEvent(this, event);
        } else if (event instanceof TcpSendDataEvent) {
            new ClientSentTcpSendDataEventHandler().processEvent(this, event);
            shouldSend = false;
        }

        callOnSentListeners(event);
        return shouldSend;
    }

    @Override
    public void processReceivedEvent(Device source, Event event) {
        pauseBeforeReceiving();
        if (event.getDestination() != this) {
            return; //Ignore any received event that is not meant for this client
        }
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

        callOnReceivedListeners(event);
    }
}