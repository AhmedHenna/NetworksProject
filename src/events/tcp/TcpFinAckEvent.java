package events.tcp;

import model.devices.Device;
import model.packet.transport.TcpPayload;

import java.util.ArrayList;

public class TcpFinAckEvent extends TcpEvent {
    public TcpFinAckEvent(Device source, Device destination, int sourcePort, int destinationPort) {
        super(source, destination, sourcePort, destinationPort);
    }

    @Override
    protected ArrayList<TcpPayload.Flag> getFlags() {
        ArrayList<TcpPayload.Flag> flags = new ArrayList<>();
        flags.add(TcpPayload.Flag.FIN);
        flags.add(TcpPayload.Flag.ACK);
        return flags;
    }
}
