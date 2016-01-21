package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;

public class TilesNotOwnedException extends QwirkleException {
    public TilesNotOwnedException() {
        super(IProtocol.Error.MOVE_TILES_UNOWNED);
    }
}
