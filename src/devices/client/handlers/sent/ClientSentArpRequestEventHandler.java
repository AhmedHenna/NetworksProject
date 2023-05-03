package devices.client.handlers.sent;

import devices.client.Client;
import devices.client.ClientEventHandler;
import events.Event;
import model.packet.IpPayload;
import model.packet.Packet;

public class ClientSentArpRequestEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event event) {
        Packet packet = event.getPacket();
        IpPayload ipPayload = packet.getIpPayload();
        client.pendingArpRequests.add(ipPayload.getDestinationIp());
    }
}
