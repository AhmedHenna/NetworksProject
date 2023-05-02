package model;

import events.tcp.TcpSendDataSegmentEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

public class TcpCurrentSendingState {
    private TcpConnection connection;

    private Set<Integer> acknowledgedNumbers;
    private Stack<TcpSendDataSegmentEvent> pendingSendDataEvents;
    private HashMap<Integer, TcpSendDataSegmentEvent> sentDataEvents;

    private int lastSequenceNumber;

    public TcpCurrentSendingState(TcpConnection connection, Stack<TcpSendDataSegmentEvent> pendingSendDataEvents, HashMap<Integer, TcpSendDataSegmentEvent> sentDataEvents, int lastSequenceNumber, Set<Integer> acknowledgedNumbers) {
        this.connection = connection;
        this.pendingSendDataEvents = pendingSendDataEvents;
        this.sentDataEvents = sentDataEvents;
        this.lastSequenceNumber = lastSequenceNumber;
        this.acknowledgedNumbers = acknowledgedNumbers;
    }

    public TcpConnection getConnection() {
        return connection;
    }

    public Stack<TcpSendDataSegmentEvent> getPendingSendDataEvents() {
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
