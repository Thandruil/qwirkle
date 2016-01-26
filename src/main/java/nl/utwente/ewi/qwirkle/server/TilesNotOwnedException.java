package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;

/**
 * Thrown when one or more of the given tiles in the move are not in the hand of the Player.
 */
public class TilesNotOwnedException extends QwirkleException {
    public TilesNotOwnedException() {
        super(IProtocol.Error.MOVE_TILES_UNOWNED);
    }
}
