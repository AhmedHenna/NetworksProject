package model.devices;

import events.Event;
import events.arp.ArpRequestEvent;
import events.arp.ArpResponseEvent;
import model.IpAddress;
import model.Link;
import model.packet.IpPayload;
import model.packet.Packet;

import java.util.ArrayList;

// TODO: Refactor how events  are sent/received
// Switch as the event broker? Switch needs to recieve before forwarding based on macs
// Packets don't go directly between device to device
// FLAWED SYSTEM
public class Client extends Device{

    public Client(String name, String macAddress, IpAddress ipAddress, IpAddress subnetMask, IpAddress defaultGateway, ArrayList<Link> linkedDevices) {
        super(name, macAddress, ipAddress, subnetMask, defaultGateway, linkedDevices);
    }

    @Override
    protected void processSentEvent(Event event) {

    }

    @Override
    protected void processReceivedEvent(Event event) {
        if(event instanceof ArpRequestEvent){
            handleArpRequestEvent((ArpRequestEvent) event);
        } else if(event instanceof ArpResponseEvent){

        }
    }

    private void handleArpRequestEvent(ArpRequestEvent event){
        Packet packet = event.getPacket();
        IpPayload ipPayload = packet.getIpPayload();
        if(ipPayload.getDestinationIp().equals(getIpAddress())){
            ArpResponseEvent response = new ArpResponseEvent(this, event.getSource());
            sendEvent(response);
        }
    }

    private void handleArpResponseEvent(ArpResponseEvent event){
        Packet packet = event.getPacket();
        IpPayload ipPayload = packet.getIpPayload();
    }


}
