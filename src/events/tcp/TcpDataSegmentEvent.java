package events.tcp;

import devices.Device;
import model.packet.IpPayload;
import model.packet.Packet;
import model.packet.transport.TcpPayload;

import java.util.ArrayList;

abstract class TcpDataSegmentEvent extends TcpEvent {
    private final byte[] data;
    private final int sequenceNumber;
    private final int acknowledgmentNumber;

    private final String checksum;

    private final int windowSize;


    public TcpDataSegmentEvent(Device source, Device destination, byte[] data, int sourcePort, int destinationPort,
                               int sequenceNumber, int acknowledgmentNumber, String checksum, int windowSize
    ) {
        super(source, destination, sourcePort, destinationPort);
        this.data = data;
        this.sequenceNumber = sequenceNumber;
        this.acknowledgmentNumber = acknowledgmentNumber;
        this.checksum = checksum;
        this.windowSize = windowSize;
        recreatePacket();
    }

    public byte[] getData() {
        return data;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getAcknowledgmentNumber() {
        return acknowledgmentNumber;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public String getChecksum() {
        return checksum;
    }

    @Override
    public Packet createPacket() {
        TcpPayload tcpPayload =
                new TcpPayload(sourcePort, destinationPort, data, sequenceNumber, acknowledgmentNumber, flags,
                        checksum
                );
        IpPayload ipPayload = new IpPayload(destination.getIpAddress(), source.getIpAddress(), tcpPayload);
        return new Packet(destination.getMacAddress(), source.getMacAddress(), ipPayload);
    }

    @Override
    protected ArrayList<TcpPayload.Flag> getFlags() {
        return new ArrayList<>();
    }
}
