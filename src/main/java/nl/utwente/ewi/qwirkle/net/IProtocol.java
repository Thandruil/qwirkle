package nl.utwente.ewi.qwirkle.net;

/**
 * IProtocol is the interface for the Qwirkle Protocol implementation. This interface
 * contains definitions for all commands used in the protocol. An implementation has
 * to be made by the groups themselves.
 *
 * @author Erik Gaal
 * @version 0.1
 * @since 0.1-w01
 */
public interface IProtocol {

    /**
     * <p>Enumeration of the features supported by the protocol.</p>
     */
    enum Feature {
        CHAT, CHALLENGE, LEADERBOARD, LOBBY
    }

    /**
     * <p>Sent by the client when connecting to a server to identify itself.</p>
     * <p>The player name must match regex <code>^[a-zA-Z0-9-_]$</code></p>
     * <p>The player name must be unique.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>name</code> - name of the player</dd>
     *     <dd><code>features</code> - list of features supported by the client <em>(see {@link nl.utwente.ewi.qwirkle.net.IProtocol.Feature})</em></dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>CONNECT</strong> PlayerA CHAT,LEADERBOARD</code></dd>
     * </dl>
     */
    String CLIENT_IDENTIFY = "IDENTIFY";

    /**
     * <p>Sent by the client when gracefully quitting a game.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>QUIT</strong></code></dd>
     * </dl>
     */
    String CLIENT_QUIT = "QUIT";

    /**
     * <p>Sent by the client to enter a queue for a n-player game.</p>
     * <p>The player can queue for 2, 3 or 4 player games.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>numbers</code> - a list of numbers of players</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>QUEUE</strong> 2,4</code></dd>
     * </dl>
     */
    String CLIENT_QUEUE = "QUEUE";

    /**
     * <p>Sent by the client to put tiles on the board as a move.</p>
     * <p>The player can play 1 to 6 tiles in one turn.</p>
     * <p>The player must own the tiles.</p>
     * <p>The move must be valid.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>tile@x,y</code> - list of tilecode at coordinate</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>MOVE_PUT</strong> 0@0,0 6@0,1 12@0,2</code></dd>
     * </dl>
     */
    String CLIENT_MOVE_PUT = "MOVE_PUT";

    /**
     * <p>Sent by the client to trade tiles as a move.</p>
     * <p>A player can trade 1 to 6 tiles in one turn.</p>
     * <p>The player must own the tiles.</p>
     * <p>The deck contain at least as many tiles as traded.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>tiles</code> - list of tiles</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>MOVE_TRADE</strong> 23 18 7</code></dd>
     * </dl>
     */
    String CLIENT_MOVE_TRADE = "MOVE_TRADE";

    /**
     * <p>Sent by the server to confirm a player connecting.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>features</code> - list of features</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>CONNECTOK</strong> CHAT,LOBBY</code></dd>
     * </dl>
     */
    String SERVER_IDENITFY = "IDENTIFYOK";

    /**
     * <p>Sent by the server to announce a game starting.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>players</code> - list of players in the game</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>GAMESTART</strong> ALICE BOB CAROL</code></dd>
     * </dl>
     */
    String SERVER_GAMESTART = "GAMESTART";

    /**
     * <p>Sent by the server to announce a game ending.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>result</code> - WIN or ERROR</dd>
     *     <dd><code>player</code> - list of player with their score</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>GAMEEND</strong> WIN 11,Alice 13,Bob 17,Carol</code></dd>
     * </dl>
     */
    String SERVER_GAMEEND = "GAMEEND";

    /**
     * <p>Sent by the server to announce the current turn.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>player</code> - the player</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>TURN</strong> Alice</code></dd>
     * </dl>
     */
    String SERVER_TURN = "TURN";

    /**
     * <p>Sent by the server to draw a player a new tile.</p>
     * <p>The server must send this to one player.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>tiles</code> - list of tiles</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>DRAWTILE</strong> 23 7 19</code></dd>
     * </dl>
     */
    String SERVER_DRAWTILE = "DRAWTILE";

    /**
     * <p>Sent by the server to confirm putting tiles as a move.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>tile@x,y</code> - list of tile at coordinate</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>MOVEOK_PUT</strong> 0@0,0 6@0,1 12@0,2</code></dd>
     * </dl>
     */
    String SERVER_MOVE_PUT = "MOVEOK_PUT";

    /**
     * <p>Sent y the server to confirm trading tiles as a move.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>amount</code> - amount of tiles traded</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>MOVEOK_TRADE</strong> 3</code></dd>
     * </dl>
     */
    String SERVER_MOVE_TRADE = "MOVEOK_TRADE";

    /**
     * <p>Sent by the server to indicate an error.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>code</code> - the error</dd>
     *     <dd><code>[message]</code> - a human readable message</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>ERROR</strong> 1 Invalid move</code></dd>
     * </dl>
     */
    String SERVER_ERROR = "ERROR";

    /**
     * <p>Sent by the client when chatting.</p>
     * <p>The channel must be <code>global</code> or <code>@playername</code>.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>channel</code> - the channel</dd>
     *     <dd><code>message</code> - the message</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>CHAT</strong> global Hello world!</code></dd>
     *     <dd><code><strong>CHAT</strong> @Bob Hello Bob!</code></dd>
     * </dl>
     */
    String CLIENT_CHAT = "CHAT";

    /**
     * <p>Sent by the server to confirm a chat message.</p>
     *
     * <dl>
     *     <dt>Parameters:</dt>
     *     <dd><code>channel</code> - the channel</dd>
     *     <dd><code>sender</code> - the sender</dd>
     *     <dd><code>message</code> - the message</dd>
     * </dl>
     * <dl>
     *     <dt>Example:</dt>
     *     <dd><code><strong>CHATOK</strong> global Alice Hello world!</code></dd>
     *     <dd><code><strong>CHATOK</strong> @Bob Alice Hello Bob!</code></dd>
     * </dl>
     */
    String SERVER_CHAT = "CHATOK";

    /* Challenge */
    String CLIENT_CHALLENGE = "CHALLENGE";
    String CLIENT_CHALLENGE_ACCEPT = "CHALLENGE_ACCEPT";
    String CLIENT_CHALLENGE_DECLINE = "CHALLENGE_DECLINE";
    String SERVER_CHALLENGE = "CHALLENGEOK";

    /* Leaderboard */
    String CLIENT_LEADERBOARD = "LEADERBOARD";
    String SERVER_LEADERBOARD = "LEADERBOARDOK";

    /* Lobby */
    String CLIENT_LOBBY = "LOBBY";
    String SERVER_LOBBY = "LOBBYOK";
}
