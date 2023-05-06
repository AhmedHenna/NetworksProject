package devices.client.handlers.received;

import devices.Device;
import devices.client.Client;
import devices.client.ClientEventHandler;
import devices.client.ClientUtil;
import events.Event;
import events.tcp.TcpAckDataSegmentEvent;
import events.tcp.TcpSendDataSegmentEvent;
import model.TcpConnection;
import model.TcpCurrentReceivingState;
import model.packet.transport.TcpPayload;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class ClientReceivedTcpSendDataSegmentEventHandler extends ClientEventHandler {
    @Override
    public void processEvent(Client client, Event e) {
        TcpSendDataSegmentEvent event = (TcpSendDataSegmentEvent) e;

        TcpPayload tcpPayload = (TcpPayload) event.getPacket().getTransportPayload();

        TcpConnection currentConnection = ClientUtil.getReceivingTcpConnection(event.getPacket());
        TcpCurrentReceivingState currentReceivingState = ClientUtil.getCurrentReceivingState(client, currentConnection);
        if (currentReceivingState == null && ClientUtil.hasTcpConnection(client, currentConnection)) {
            currentReceivingState = new TcpCurrentReceivingState(currentConnection, new TreeMap<>());
            client.currentReceivingStates.add(currentReceivingState);
        }

        if(randomIsSegmentDropped()){
            client.log("Simulated segment dropped, not acknowledging");
            return;
        }

        if (currentReceivingState != null) {
            TreeMap<Integer, byte[]> currentReceivedData = currentReceivingState.getCurrentReceivedData();
            byte[] data = tcpPayload.getPayload();
            int sequenceNumber = event.getSequenceNumber();
            int ackNumber = sequenceNumber + data.length;


            if (isRandomNotValidChecksum(data, event.getChecksum())) {
                client.log("Invalid checksum detected");
                if(currentReceivedData.isEmpty()){
                    ackNumber = sequenceNumber;
                } else {
                    int lastSequenceNumber = currentReceivedData.lastKey();
                    byte[] lastData = currentReceivedData.lastEntry().getValue();
                    ackNumber = lastSequenceNumber + lastData.length;
                }
            } else {
                if(currentReceivedData.isEmpty()){
                    if(ackNumber == Device.MSS + 1){
                        currentReceivedData.put(sequenceNumber,data);
                    } else {
                        ackNumber = sequenceNumber;
                    }
                } else {
                    int lastSequenceNumber = currentReceivedData.lastKey();
                    byte[] lastData = currentReceivedData.lastEntry().getValue();
                    if(sequenceNumber - lastSequenceNumber == lastData.length){
                        currentReceivedData.put(sequenceNumber,data);
                    } else {
                        ackNumber = lastSequenceNumber + lastData.length;
                    }
                }
            }


            TcpAckDataSegmentEvent tcpAckDataSegmentEvent =
                    new TcpAckDataSegmentEvent(client, event.getSource(), tcpPayload.getDestinationPort(),
                            tcpPayload.getSourcePort(), ackNumber, "", randomWindowSize()
                    );

            client.sendEvent(tcpAckDataSegmentEvent);
        }
    }

    private boolean isRandomNotValidChecksum(byte[] data, String checksum) {
//        return new String(data).equals(checksum);
        Random random = new Random();
        return random.nextInt(15) == 1;
    }

    private boolean randomIsSegmentDropped() {
        Random random = new Random();
        return random.nextInt(15) == 1;
    }

    private int randomWindowSize() {
        Random random = new Random();
        return random.nextInt(4) + 1;
    }

}
