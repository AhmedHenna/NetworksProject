package devices.client.handlers.received;

import devices.client.Client;
import devices.client.ClientEventHandler;
import devices.client.ClientUtil;
import events.Event;
import events.tcp.TcpAckEvent;
import model.TcpConnection;
import model.packet.transport.TcpPayload;

public class ClientReceivedTcpSynAckEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event event) {
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();
        TcpConnection currentConnection = ClientUtil.getReceivingTcpConnection(event.getPacket());
        if (ClientUtil.hasSentSyn(client, currentConnection)) {
            TcpAckEvent tcpAckEvent = new TcpAckEvent(client, event.getSource(), tcpPayload.getDestinationPort(),
                    tcpPayload.getSourcePort()
            );
            client.sendEvent(tcpAckEvent);
        }
    }
}
