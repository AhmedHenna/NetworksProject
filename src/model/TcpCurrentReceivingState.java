package model;

import java.util.Map;
import java.util.TreeMap;

public class TcpCurrentReceivingState {

    private final TcpConnection connection;
    private final TreeMap<Integer, byte[]> currentReceivedData;

    public TcpCurrentReceivingState(TcpConnection connection, TreeMap<Integer, byte[]> currentReceivedData) {
        this.connection = connection;
        this.currentReceivedData = currentReceivedData;
    }

    public TcpConnection getConnection() {
        return connection;
    }

    public TreeMap<Integer, byte[]> getCurrentReceivedData() {
        return currentReceivedData;
    }

    public String getData() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Integer, byte[]> entry : currentReceivedData.entrySet()) {
            builder.append(new String(entry.getValue()));
        }

        return builder.toString();
    }
}
