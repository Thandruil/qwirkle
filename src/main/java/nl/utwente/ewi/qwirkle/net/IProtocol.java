package nl.utwente.ewi.qwirkle.net;

public interface IProtocol {
    /* Core */
    String CLIENT_CONNECT = "CONNECT";
    String CLIENT_QUIT = "QUIT";
    String CLIENT_QUEUE = "QUEUE";
    String CLIENT_MOVE_PUT = "MOVE PUT";
    String CLIENT_MOVE_TRADE = "MOVE TRADE";

    String SERVER_CONNECT = "CONNECTOK";
    String SERVER_GAMESTART = "GAMESTART";
    String SERVER_GAMEEND = "GAMEEND";
    String SERVER_TURN = "TURN";
    String SERVER_DRAWTILE = "DRAWTILE";
    String SERVER_MOVE_PUT = "MOVEOK PUT";
    String SERVER_MOVE_TRADE = "MOVEOK TRADE";
    String SERVER_ERROR = "ERROR";

    /* Chat */
    String CLIENT_CHAT = "CHAT";
    String SERVER_CHAT = "CHATOK";

    /* Challenge */
    String CLIENT_CHALLENGE = "CHALLENGE";
    String CLIENT_CHALLENGE_ACCEPT = "CACCEPT";
    String CLIENT_CHALLENGE_DECLINE = "CDECLINE";
    String SERVER_CHALLENGE = "CHALLENGEOK";

    /* Leaderboard */
    String CLIENT_LEADERBOARD = "LEADERBOARD";
    String SERVER_LEADERBOARD = "LEADERBOARDOK";

    /* Lobby */
    String CLIENT_LOBBY = "LOBBY";
    String SERVER_LOBBY = "LOBBYOK";
}
