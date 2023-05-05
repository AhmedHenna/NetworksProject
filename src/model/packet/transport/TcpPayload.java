package model.packet.transport;

import java.util.ArrayList;

public class TcpPayload extends TransportPayload {
    private final int sequenceNumber;
    private final int acknowledgmentNumber;
    private final ArrayList<Flag> flags;

    public TcpPayload(int sourcePort, int destinationPort, byte[] payload, int sequenceNumber, int acknowledgmentNumber,
                      ArrayList<Flag> flags, String checksum
    ) {
        super(sourcePort, destinationPort, payload, checksum);
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
        ACK, URG, PSH, RST, SYN, FIN
    }
}
