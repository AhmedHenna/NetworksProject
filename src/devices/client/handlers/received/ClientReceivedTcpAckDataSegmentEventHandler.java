package devices.client.handlers.received;

import devices.client.Client;
import devices.client.ClientEventHandler;
import devices.client.ClientUtil;
import events.Event;
import events.tcp.TcpAckDataSegmentEvent;
import events.tcp.TcpFinEvent;
import events.tcp.TcpSendDataSegmentEvent;
import model.TcpConnection;
import model.TcpCurrentSendingState;
import model.packet.transport.TcpPayload;

import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

public class ClientReceivedTcpAckDataSegmentEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event e) {
        TcpAckDataSegmentEvent event = (TcpAckDataSegmentEvent) e;
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();
        TcpConnection currentConnection = ClientUtil.getReceivingTcpConnection(event.getPacket());
        TcpCurrentSendingState currentSendingState = ClientUtil.getCurrentSendingState(client, currentConnection);

        if (currentSendingState != null) {
            int ackNumber = event.getAcknowledgmentNumber();
            Set<Integer> acknowledgedNumbers = currentSendingState.getAcknowledgedNumbers();
            Queue<TcpSendDataSegmentEvent> pendingSendDataEvents = currentSendingState.getPendingSendDataEvents();
            TreeMap<Integer, TcpSendDataSegmentEvent> sentEvents = currentSendingState.getSentDataEvents();
            acknowledgedNumbers.add(ackNumber);

            client.log("Received new window size for", event.getSource().toString(),
                    String.valueOf(event.getWindowSize())
            );

            int numOfSentEvents = 0;
            for (int i = 0; i < event.getWindowSize(); i++) {
                if (!currentSendingState.getPendingSendDataEvents().isEmpty()) {
                    TcpSendDataSegmentEvent sendDataSegmentEvent = pendingSendDataEvents.remove();
                    sentEvents.put(sendDataSegmentEvent.getSequenceNumber(), sendDataSegmentEvent);
                    client.sendEvent(sendDataSegmentEvent);
                    numOfSentEvents++;
                }
            }
            currentSendingState.setWindowSizeAvailableForReSender(event.getWindowSize() - numOfSentEvents);


            if (pendingSendDataEvents.isEmpty() && acknowledgedNumbers.size() == sentEvents.size() && acknowledgedNumbers.contains(
                    currentSendingState.getLastAckNumber())) {
                TcpFinEvent tcpFinEvent = new TcpFinEvent(client, event.getSource(), tcpPayload.getDestinationPort(),
                        tcpPayload.getSourcePort()
                );
                client.sendEvent(tcpFinEvent);
                client.currentSendingStates.remove(currentSendingState);
            }

        }

    }
}
