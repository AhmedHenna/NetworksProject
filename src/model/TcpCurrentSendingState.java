package model;

import events.tcp.TcpSendDataSegmentEvent;

import java.util.HashMap;
import java.util.Queue;
import java.util.Set;

public class TcpCurrentSendingState {
    private final TcpConnection connection;

    private final Set<Integer> acknowledgedNumbers;
    private final Queue<TcpSendDataSegmentEvent> pendingSendDataEvents;
    private final HashMap<Integer, TcpSendDataSegmentEvent> sentDataEvents;

    private final int lastSequenceNumber;

    public TcpCurrentSendingState(TcpConnection connection, Queue<TcpSendDataSegmentEvent> pendingSendDataEvents,
                                  HashMap<Integer, TcpSendDataSegmentEvent> sentDataEvents, int lastSequenceNumber,
                                  Set<Integer> acknowledgedNumbers
    ) {
        this.connection = connection;
        this.pendingSendDataEvents = pendingSendDataEvents;
        this.sentDataEvents = sentDataEvents;
        this.lastSequenceNumber = lastSequenceNumber;
        this.acknowledgedNumbers = acknowledgedNumbers;
    }

    public TcpConnection getConnection() {
        return connection;
    }

    public Queue<TcpSendDataSegmentEvent> getPendingSendDataEvents() {
        return pendingSendDataEvents;
    }

    public HashMap<Integer, TcpSendDataSegmentEvent> getSentDataEvents() {
        return sentDataEvents;
    }

    public Set<Integer> getAcknowledgedNumbers() {
        return acknowledgedNumbers;
    }

    public int getLastSequenceNumber() {
        return lastSequenceNumber;
    }

}
