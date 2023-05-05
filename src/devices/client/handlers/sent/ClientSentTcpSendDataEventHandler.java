package devices.client.handlers.sent;

import devices.Device;
import devices.client.Client;
import devices.client.ClientEventHandler;
import devices.client.ClientUtil;
import events.Event;
import events.tcp.TcpSendDataEvent;
import events.tcp.TcpSendDataSegmentEvent;
import model.TcpConnection;
import model.TcpCurrentSendingState;
import model.packet.transport.TcpPayload;

import java.util.*;

public class ClientSentTcpSendDataEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event e) {
        TcpSendDataEvent event = (TcpSendDataEvent) e;
        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();
        TcpConnection currentConnection = ClientUtil.getSendingTcpConnection(event.getPacket());

        if (ClientUtil.hasTcpConnection(client, currentConnection)) {
            byte[] data = event.getData();
            byte[][] segments = splitIntoSegments(data);
            Queue<TcpSendDataSegmentEvent> sendDataSegmentEvents = new LinkedList<>();
            HashMap<Integer, TcpSendDataSegmentEvent> sentDataSegmentEvent = new HashMap<>();
            int sequenceNumber = 1;
            for (byte[] segment : segments) {
                TcpSendDataSegmentEvent sendDataSegmentEvent =
                        new TcpSendDataSegmentEvent(event.getSource(), event.getDestination(), segment,
                                tcpPayload.getSourcePort(), tcpPayload.getDestinationPort(), sequenceNumber,
                                new String(segment), System.currentTimeMillis(), event.getWindowSize()
                        );
                sendDataSegmentEvents.add(sendDataSegmentEvent);
                sequenceNumber += segment.length;
            }


            TcpCurrentSendingState currentSendingState =
                    new TcpCurrentSendingState(currentConnection, sendDataSegmentEvents, sentDataSegmentEvent,
                            sequenceNumber, new HashSet<>()
                    );
            client.currentSendingStates.add(currentSendingState);

            for (int i = 0; i < event.getWindowSize(); i++) {
                if (!sendDataSegmentEvents.isEmpty()) {
                    TcpSendDataSegmentEvent sendDataSegmentEvent = sendDataSegmentEvents.remove();
                    sentDataSegmentEvent.put(sendDataSegmentEvent.getSequenceNumber(), sendDataSegmentEvent);

                    client.sendEvent(sendDataSegmentEvent);
                }
            }
        }
    }

    //Based on https://stackoverflow.com/a/39788851
    private byte[][] splitIntoSegments(byte[] data) {
        if (data.length <= Device.MSS) {
            return new byte[][]{data};
        }
        int lastArrayLength = data.length % Device.MSS;
        int numberOfSegments = data.length / Device.MSS + (lastArrayLength > 0 ? 1 : 0);
        byte[][] segments = new byte[numberOfSegments][];

        for (int i = 0; i < (lastArrayLength > 0 ? numberOfSegments - 1 : numberOfSegments); i++) {
            segments[i] = Arrays.copyOfRange(data, i * Device.MSS, i * Device.MSS + Device.MSS);
        }
        if (lastArrayLength > 0) {
            segments[numberOfSegments - 1] = Arrays.copyOfRange(data, (numberOfSegments - 1) * Device.MSS,
                    (numberOfSegments - 1) * Device.MSS + lastArrayLength
            );
        }
        return segments;
    }


}
