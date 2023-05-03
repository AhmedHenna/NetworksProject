package devices.client.handlers.received;

import devices.client.Client;
import devices.client.ClientEventHandler;
import devices.client.ClientUtil;
import events.Event;
import model.TcpConnection;

public class ClientReceivedTcpFinAckEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event event) {
        TcpConnection currentConnection = ClientUtil.getReceivingTcpConnection(event.getPacket());
        if (ClientUtil.hasReceivedFin(client,currentConnection)) {
            ClientUtil.deleteConnection(client,currentConnection);
        }
    }
}
