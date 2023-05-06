package model;

import devices.Device;
import events.tcp.TcpSendDataSegmentEvent;

import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

public class TcpCurrentSendingState {
    private final TcpConnection connection;

    private final Set<Integer> acknowledgedNumbers;
    private final Queue<TcpSendDataSegmentEvent> pendingSendDataEvents;
    private final TreeMap<Integer, TcpSendDataSegmentEvent> sentDataEvents;

    private final int lastAckNumber;
    private int windowSizeAvailableForReSender = 0;

    public TcpCurrentSendingState(TcpConnection connection, Queue<TcpSendDataSegmentEvent> pendingSendDataEvents,
                                  TreeMap<Integer, TcpSendDataSegmentEvent> sentDataEvents, int lastAckNumber,
                                  Set<Integer> acknowledgedNumbers
    ) {
        this.connection = connection;
        this.pendingSendDataEvents = pendingSendDataEvents;
        this.sentDataEvents = sentDataEvents;
        this.lastAckNumber = lastAckNumber;
        this.acknowledgedNumbers = acknowledgedNumbers;
    }

    public TcpConnection getConnection() {
        return connection;
    }

    public Queue<TcpSendDataSegmentEvent> getPendingSendDataEvents() {
        return pendingSendDataEvents;
    }

    public TreeMap<Integer, TcpSendDataSegmentEvent> getSentDataEvents() {
        return sentDataEvents;
    }

    public Set<Integer> getAcknowledgedNumbers() {
        return acknowledgedNumbers;
    }

    public int getLastAckNumber() {
        return lastAckNumber;
    }

    public int getWindowSizeAvailableForReSender() {
        return windowSizeAvailableForReSender;
    }

    public void setWindowSizeAvailableForReSender(int windowSizeAvailableForReSender) {
        this.windowSizeAvailableForReSender = windowSizeAvailableForReSender;
    }
}
