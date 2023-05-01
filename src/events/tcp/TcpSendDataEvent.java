package events.tcp;

import model.devices.Device;
import model.packet.IpPayload;
import model.packet.Packet;
import model.packet.transport.TcpPayload;

import java.util.ArrayList;

public class TcpSendDataEvent extends TcpEvent {
    private final byte[] data;

    public TcpSendDataEvent(Device source, Device destination, byte[] data, int sourcePort, int destinationPort) {
        super(source, destination, sourcePort, destinationPort);
        this.data = data;
        recreatePacket();
    }

    public byte[] getData() {
        return data;
    }

    @Override
    protected ArrayList<TcpPayload.Flag> getFlags() {
        return new ArrayList<>();
    }
}
