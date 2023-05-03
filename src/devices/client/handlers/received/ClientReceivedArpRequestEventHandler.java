package devices.client.handlers.received;

import devices.client.Client;
import devices.client.ClientEventHandler;
import events.Event;
import events.arp.ArpResponseEvent;
import model.packet.IpPayload;
import model.packet.Packet;

public class ClientReceivedArpRequestEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event event) {
        Packet packet = event.getPacket();
        IpPayload ipPayload = packet.getIpPayload();
        if (ipPayload.getDestinationIp().equals(client.getIpAddress())) {
            ArpResponseEvent response = new ArpResponseEvent(client, event.getSource());
            client.sendEvent(response);
        }
    }
}
