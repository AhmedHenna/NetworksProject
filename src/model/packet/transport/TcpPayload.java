package model.packet.transport;

import java.util.ArrayList;

public class TcpPayload extends TransportPayload {
    private int sequenceNumber;
    private int acknowledgmentNumber;
    private ArrayList<Flag> flags;

    public TcpPayload(int sourcePort, int destinationPort, String payload, int sequenceNumber, int acknowledgmentNumber, ArrayList<Flag> flags) {
        super(sourcePort, destinationPort, payload);
        this.sequenceNumber = sequenceNumber;
        this.acknowledgmentNumber = acknowledgmentNumber;
        this.flags = flags;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getAcknowledgmentNumber() {
        return acknowledgmentNumber;
    }

    public ArrayList<Flag> getFlags() {
        return flags;
    }

    public enum Flag {
        ACK,
        URG,
        PSH,
        RST,
        SYN,
        FIN
    }
}
