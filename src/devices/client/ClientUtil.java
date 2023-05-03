package devices.client;

import model.TcpConnection;
import model.TcpCurrentReceivingState;
import model.TcpCurrentSendingState;
import model.packet.IpPayload;
import model.packet.Packet;
import model.packet.transport.TcpPayload;

import java.util.ArrayList;

public class ClientUtil {

    public static void deleteConnection(Client client, TcpConnection connection) {
        TcpCurrentReceivingState currentReceivingState = getCurrentReceivingState(client, connection);
        if (currentReceivingState != null) {
            client.log("Received Data: " + currentReceivingState.getData());
            client.currentReceivingStates.remove(currentReceivingState);
        }
        deleteFromConnections(client.tcpConnectionsWithFinReceived, connection);
        deleteFromConnections(client.tcpConnectionsWithFinInitiated, connection);
        deleteFromConnections(client.tcpConnections, connection);
    }

    public static TcpConnection getSendingTcpConnection(Packet packet) {
        IpPayload ipPayload = packet.getIpPayload();
        TcpPayload tcpPayload = (TcpPayload) ipPayload.getTransportPayload();
        return new TcpConnection(ipPayload.getDestinationIp(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort());
    }

    public static TcpConnection getReceivingTcpConnection(Packet packet) {
        IpPayload ipPayload = packet.getIpPayload();
        TcpPayload tcpPayload = (TcpPayload) ipPayload.getTransportPayload();
        return new TcpConnection(ipPayload.getSourceIp(), tcpPayload.getSourcePort(), tcpPayload.getDestinationPort());
    }


    public static TcpCurrentSendingState getCurrentSendingState(Client client, TcpConnection connection) {
        for (TcpCurrentSendingState s : client.currentSendingStates) {
            if (s.getConnection().getDestinationIp().equals(connection.getDestinationIp())
                    && s.getConnection().getDestinationPort() == connection.getDestinationPort()
                    && s.getConnection().getSourcePort() == connection.getSourcePort()
            ) {
                return s;
            }
        }
        return null;
    }

    public static TcpCurrentReceivingState getCurrentReceivingState(Client client, TcpConnection connection) {
        for (TcpCurrentReceivingState s : client.currentReceivingStates) {
            if (s.getConnection().getDestinationIp().equals(connection.getDestinationIp())
                    && s.getConnection().getDestinationPort() == connection.getDestinationPort()
                    && s.getConnection().getSourcePort() == connection.getSourcePort()
            ) {
                return s;
            }
        }
        return null;
    }

    public static boolean hasTcpConnection(Client client, TcpConnection connection) {
        return connectionsContain(client.tcpConnections, connection);
    }

    public static boolean hasSentSyn(Client client, TcpConnection connection) {
        return connectionsContain(client.tcpConnectionsWithSynSent, connection);

    }

    public static boolean hasInitiatedFin(Client client, TcpConnection connection) {
        return connectionsContain(client.tcpConnectionsWithFinInitiated, connection);
    }

    public static boolean hasReceivedFin(Client client, TcpConnection connection) {
        return connectionsContain(client.tcpConnectionsWithFinReceived, connection);
    }

    public static boolean connectionsContain(ArrayList<TcpConnection> connections, TcpConnection connection) {
        for (TcpConnection c : connections) {
            if (c.getDestinationIp().equals(connection.getDestinationIp())
                    && c.getDestinationPort() == connection.getDestinationPort()
                    && c.getSourcePort() == connection.getSourcePort()
            ) {
                return true;
            }
        }
        return false;
    }

    public static void deleteFromConnections(ArrayList<TcpConnection> connections, TcpConnection connection) {
        connections.removeIf(c -> c.getDestinationIp().equals(connection.getDestinationIp())
                && c.getDestinationPort() == connection.getDestinationPort()
                && c.getSourcePort() == connection.getSourcePort()
        );

    }

}
