package devices.client.handlers.received;

import devices.client.Client;
import devices.client.ClientEventHandler;
import events.Event;
import model.IpAddressMacMapping;
import model.packet.IpPayload;
import model.packet.Packet;

public class ClientReceivedArpResponseEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event event) {
        Packet packet = event.getPacket();
        IpPayload ipPayload = packet.getIpPayload();
        if (client.pendingArpRequests.contains(ipPayload.getSourceIp())) {
            client.pendingArpRequests.remove(ipPayload.getSourceIp());
            client.ipAddressMacMappings.add(
                    new IpAddressMacMapping(ipPayload.getSourceIp(), packet.getSourceMacAddress()));
        }
    }
}
