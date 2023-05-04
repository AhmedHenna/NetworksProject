package devices.client.handlers.received;

import devices.Device;
import devices.client.Client;
import devices.client.ClientEventHandler;
import devices.client.ClientUtil;
import events.Event;
import events.tcp.TcpAckDataSegmentEvent;
import events.tcp.TcpSendDataSegmentEvent;
import model.TcpConnection;
import model.TcpCurrentReceivingState;
import model.packet.transport.TcpPayload;

import java.util.Map;
import java.util.TreeMap;

public class ClientReceivedTcpSendDataSegmentEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event e) {
        TcpSendDataSegmentEvent event = (TcpSendDataSegmentEvent) e;

        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();

        TcpConnection currentConnection = ClientUtil.getReceivingTcpConnection(event.getPacket());
        TcpCurrentReceivingState currentReceivingState = ClientUtil.getCurrentReceivingState(client, currentConnection);
        if (currentReceivingState == null && ClientUtil.hasTcpConnection(client, currentConnection)) {
            currentReceivingState = new TcpCurrentReceivingState(currentConnection, new TreeMap<>());
            client.currentReceivingStates.add(currentReceivingState);
        }
        if (currentReceivingState != null) {
            TreeMap<Integer, byte[]> currentReceivedData = currentReceivingState.getCurrentReceivedData();
            byte[] data = tcpPayload.getPayload();

            if (isValidChecksum(data, event.getChecksum())) {
                currentReceivedData.put(event.getSequenceNumber(), data);
            }

            int ackNumber = currentReceivedData.lastKey() + currentReceivedData.lastEntry().getValue().length;
            int expectedSeqNumber = 1;
            int prevSequenceNumber = 1;
            for (Map.Entry<Integer, byte[]> entry : currentReceivedData.entrySet()) {
                int seqNum = entry.getKey();
                if (seqNum != expectedSeqNumber) {
                    ackNumber = prevSequenceNumber;
                    break;
                }

                prevSequenceNumber = seqNum;
                expectedSeqNumber = seqNum + entry.getValue().length;
            }

            TcpAckDataSegmentEvent tcpAckDataSegmentEvent = new TcpAckDataSegmentEvent(client, event.getSource(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort(), ackNumber, "", Device.WINDOW_SIZE);
            client.sendEvent(tcpAckDataSegmentEvent);
        }
    }

    public boolean isValidChecksum(byte[] data, String checksum) {
        return new String(data).equals(checksum);
    }

}
