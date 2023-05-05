package devices.client.handlers.sent;

import devices.client.Client;
import devices.client.ClientEventHandler;
import events.Event;
import model.TcpConnection;
import model.packet.IpPayload;
import model.packet.transport.TcpPayload;

public class ClientSentTcpSynEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event event) {
        IpPayload ipPayload = event.getPacket().getIpPayload();
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();
        client.tcpConnectionsWithSynSent.add(
                new TcpConnection(ipPayload.getDestinationIp(), tcpPayload.getDestinationPort(),
                        tcpPayload.getSourcePort()
                ));
    }
}
