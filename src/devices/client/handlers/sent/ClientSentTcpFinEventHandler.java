package devices.client.handlers.sent;

import devices.client.Client;
import devices.client.ClientEventHandler;
import devices.client.ClientUtil;
import events.Event;
import model.TcpConnection;

public class ClientSentTcpFinEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event event) {
        TcpConnection currentConnection = ClientUtil.getSendingTcpConnection(event.getPacket());
        if (!ClientUtil.hasReceivedFin(client, currentConnection)) {
            client.tcpConnectionsWithFinInitiated.add(currentConnection);
        }
    }
}
