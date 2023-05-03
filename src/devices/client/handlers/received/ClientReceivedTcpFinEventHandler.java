package devices.client.handlers.received;

import devices.client.Client;
import devices.client.ClientEventHandler;
import devices.client.ClientUtil;
import events.Event;
import events.tcp.TcpFinAckEvent;
import events.tcp.TcpFinEvent;
import model.TcpConnection;
import model.packet.transport.TcpPayload;

public class ClientReceivedTcpFinEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event event) {
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();
        TcpConnection currentConnection = ClientUtil.getReceivingTcpConnection(event.getPacket());
        if (ClientUtil.hasTcpConnection(client, currentConnection)) {
            TcpFinAckEvent tcpFinAckEvent = new TcpFinAckEvent(client, event.getSource(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort());
            client.sendEvent(tcpFinAckEvent);

            if (!ClientUtil.hasInitiatedFin(client, currentConnection)) {
                TcpFinEvent tcpFinEvent = new TcpFinEvent(client, event.getSource(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort());
                client.sendEvent(tcpFinEvent);
                client.tcpConnectionsWithFinReceived.add(currentConnection);
            }
        }
    }
}
