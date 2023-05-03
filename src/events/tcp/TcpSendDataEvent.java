package events.tcp;

import devices.Device;
import model.packet.transport.TcpPayload;

import java.util.ArrayList;

public class TcpSendDataEvent extends TcpEvent {
    private final byte[] data;

    private final int windowSize;

    public TcpSendDataEvent(Device source, Device destination, byte[] data, int sourcePort, int destinationPort, int windowSize) {
        super(source, destination, sourcePort, destinationPort);
        this.data = data;
        this.windowSize = windowSize;
        recreatePacket();
    }

    public byte[] getData() {
        return data;
    }

    public int getWindowSize() {
        return windowSize;
    }

    @Override
    protected ArrayList<TcpPayload.Flag> getFlags() {
        return new ArrayList<>();
    }
}
