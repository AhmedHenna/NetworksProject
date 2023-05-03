package devices.client.handlers.received;

import devices.client.Client;
import devices.client.ClientEventHandler;
import events.Event;
import events.tcp.TcpSynAckEvent;
import model.packet.transport.TcpPayload;

public class ClientReceivedTcpSynEventHandler extends ClientEventHandler {

    @Override
    public void processEvent(Client client, Event event) {
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();
        TcpSynAckEvent tcpSynAckEvent = new TcpSynAckEvent(client, event.getSource(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort());
        client.sendEvent(tcpSynAckEvent);
    }
}
