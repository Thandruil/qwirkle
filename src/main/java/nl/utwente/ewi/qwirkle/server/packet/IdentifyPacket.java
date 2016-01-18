package nl.utwente.ewi.qwirkle.server.packet;

import nl.utwente.ewi.qwirkle.net.IProtocol;
import nl.utwente.ewi.qwirkle.net.IllegalStateException;
import nl.utwente.ewi.qwirkle.server.*;

import java.util.ArrayList;
import java.util.List;

public class IdentifyPacket implements IPacket {

    String name;
    List<IProtocol.Feature> features;

    public IdentifyPacket(String name, List<IProtocol.Feature> features) {
        this.name = name;
        this.features = features == null ? new ArrayList<>() : features;
    }

    public void process(ClientHandler client) throws QwirkleException, IllegalStateException {
        client.identify(name, features);
    }
}
