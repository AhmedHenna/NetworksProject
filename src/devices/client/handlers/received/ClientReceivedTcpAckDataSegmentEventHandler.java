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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class ClientReceivedTcpAckDataSegmentEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event e) {
        TcpAckDataSegmentEvent event = (TcpAckDataSegmentEvent)e;
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();
        TcpConnection currentConnection = ClientUtil.getReceivingTcpConnection(event.getPacket());
        TcpCurrentSendingState currentSendingState = ClientUtil.getCurrentSendingState(client, currentConnection);

        if (currentSendingState != null) {
            int ackNumber = event.getAcknowledgmentNumber();
            Set<Integer> acknowledgedNumbers = currentSendingState.getAcknowledgedNumbers();
            Stack<TcpSendDataSegmentEvent> pendingSendDataEvents = currentSendingState.getPendingSendDataEvents();
            HashMap<Integer, TcpSendDataSegmentEvent> sentEvents = currentSendingState.getSentDataEvents();
            acknowledgedNumbers.add(ackNumber);

            int resentEvents = 0;
            for (Map.Entry<Integer, TcpSendDataSegmentEvent> sentEvent : sentEvents.entrySet()) {
                if (sentEvent.getKey() == ackNumber && resentEvents < event.getWindowSize()) {
                    sentEvents.put(sentEvent.getValue().getSequenceNumber(), sentEvent.getValue());
                    client.sendEvent(sentEvent.getValue());
                    resentEvents++;
                }
            }

            if (resentEvents < event.getWindowSize()) {
                for (int i = 0; i < event.getWindowSize() - resentEvents; i++) {
                    if (!currentSendingState.getPendingSendDataEvents().empty()) {
                        TcpSendDataSegmentEvent sendDataSegmentEvent = pendingSendDataEvents.pop();
                        sentEvents.put(sendDataSegmentEvent.getSequenceNumber(), sendDataSegmentEvent);
                        client.sendEvent(sendDataSegmentEvent);
                    }
                }
            }

            if (pendingSendDataEvents.empty() && acknowledgedNumbers.size() == sentEvents.size() && acknowledgedNumbers.contains(currentSendingState.getLastSequenceNumber())) {
                TcpFinEvent tcpFinEvent = new TcpFinEvent(client, event.getSource(), tcpPayload.getDestinationPort(), tcpPayload.getSourcePort());
                client.sendEvent(tcpFinEvent);
                client.currentSendingStates.remove(currentSendingState);
            }

        }

    }
}
