package devices.client.handlers.sent;

import devices.client.Client;
import devices.client.ClientEventHandler;
import devices.client.ClientUtil;
import events.Event;
import model.TcpConnection;

public class ClientSentTcpAckEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event event) {
        TcpConnection currentConnection = ClientUtil.getSendingTcpConnection(event.getPacket());
        if (ClientUtil.hasSentSyn(client, currentConnection)) {
            ClientUtil.deleteFromConnections(client.tcpConnectionsWithSynSent, currentConnection);
            client.tcpConnections.add(currentConnection);
        }
    }
}
