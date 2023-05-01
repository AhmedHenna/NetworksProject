package model;

import events.tcp.TcpSendDataSegmentEvent;

import java.util.ArrayList;

public class TcpCurrentSendingState {
    private TcpConnection connection;
    private ArrayList<TcpSendDataSegmentEvent> pendingSendDataEvents;
    private ArrayList<TcpSendDataSegmentEvent> sentDataEvents;

    private int currentSequenceNumber;

    public TcpCurrentSendingState(TcpConnection connection, ArrayList<TcpSendDataSegmentEvent> pendingSendDataEvents, ArrayList<TcpSendDataSegmentEvent> sentDataEvents, int currentSequenceNumber) {
        this.connection = connection;
        this.pendingSendDataEvents = pendingSendDataEvents;
        this.sentDataEvents = sentDataEvents;
        this.currentSequenceNumber = currentSequenceNumber;
    }

    public TcpConnection getConnection() {
        return connection;
    }

    public ArrayList<TcpSendDataSegmentEvent> getPendingSendDataEvents() {
        return pendingSendDataEvents;
    }

    public ArrayList<TcpSendDataSegmentEvent> getSentDataEvents() {
        return sentDataEvents;
    }

    public int getCurrentSequenceNumber() {
        return currentSequenceNumber;
    }

    public void setCurrentSequenceNumber(int currentSequenceNumber) {
        this.currentSequenceNumber = currentSequenceNumber;
    }
}
