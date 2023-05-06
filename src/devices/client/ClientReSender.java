package devices.client;

import devices.Device;
import events.tcp.TcpSendDataSegmentEvent;
import model.TcpCurrentSendingState;

import java.util.*;

public class ClientReSender extends Thread {

    private final Client client;
    private final ArrayList<TcpCurrentSendingState> currentSendingStates;

    public ClientReSender(Client client, ArrayList<TcpCurrentSendingState> currentSendingStates) {
        this.client = client;
        this.currentSendingStates = currentSendingStates;
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            for(TcpCurrentSendingState currentSendingState: currentSendingStates) {
                TreeMap<Integer, TcpSendDataSegmentEvent> sentEvents = currentSendingState.getSentDataEvents();
                Set<Integer> ackNumbers = currentSendingState.getAcknowledgedNumbers();
                int windowSize = currentSendingState.getWindowSizeAvailableForReSender();

                int resentEvents = 0;
                for (Map.Entry<Integer, TcpSendDataSegmentEvent> sentEvent : sentEvents.entrySet()) {
                    int ackNumberOfEvent = sentEvent.getKey() + sentEvent.getValue().getData().length;

                    if (resentEvents < windowSize && !ackNumbers.contains(ackNumberOfEvent) && sentEvent.getValue().getSentAt() + Device.SENT_SEGMENT_TIMEOUT < System.currentTimeMillis()) {
                        sentEvent.getValue().setSentAt(System.currentTimeMillis());
                        sentEvent.getValue().updateTimestamp();
                        sentEvents.put(sentEvent.getValue().getSequenceNumber(), sentEvent.getValue());
                        client.log("Resending event");
                        client.sendEvent(sentEvent.getValue());
                        resentEvents++;
                    }
                }
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Client resender interrupted: " + client.toString());
            }
        }
    }
}
