package hr.tvz.popovic.chessengine.service;

import chariot.ClientAuth;
import chariot.model.Enums;
import chariot.model.Event;
import chariot.model.GameStateEvent;
import hr.tvz.popovic.chessengine.config.LichessProperties;
import hr.tvz.popovic.chessengine.evaluation.AlphaBetaPruning;
import hr.tvz.popovic.chessengine.generator.Generators;
import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.LichessStartRequest;
import org.springframework.stereotype.Service;

@Service
public class LichessService {

    public static final String INITIAL_BOARD_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private final ClientAuth client;
    private final LichessProperties properties;

    LichessService(ClientAuth client, LichessProperties properties) {
        this.client = client;
        this.properties = properties;
        startListening();
    }

    public void startGame(LichessStartRequest request) {
        client.bot()
                .challengeAI(builder -> builder.clockBlitz5m0s()
                        .color(Enums.ColorPref.random)
                        .level(request.level())
                        .variant(Enums.VariantName.standard)
                        .fen(INITIAL_BOARD_FEN)
                );
    }

    private void startListening() {
        new Thread(() ->
                client.bot()
                        .connect()
                        .stream()
                        .forEach(event -> {
                            if (Event.Type.gameStart.equals(event.type())) {
                                playGame(event);
                            }
                        })
        ).start();
    }

    private void playGame(Event event) {
        if(event instanceof Event.GameStartEvent gameStart) {
            var gameId = gameStart.gameId();
            var board = Board.createInitialBoard();
            client.bot()
                    .connectToGame(gameId)
                    .stream()
                    .forEach(gameEvent -> {
                        switch (gameEvent.type()) {
                            case gameFull -> startPlaying(gameId, board, gameEvent);
                            case gameState -> playMove(gameId, board, gameEvent);
                        }
                    });
        }

    }

    private void startPlaying(String gameId, Board board, GameStateEvent gameEvent) {
        if(gameEvent instanceof GameStateEvent.Full event) {
            if(event.white().name().equals(properties.getName())) {
                board.setPlayer(true);
                AlphaBetaPruning.getBestMove(board, properties.getThinkingTime())
                        .ifPresent(move -> client.bot().move(gameId, move.toString()));
            }
        }
    }

    private void playMove(String gameId, Board board, GameStateEvent gameEvent) {
        if(gameEvent instanceof GameStateEvent.State event) {
            var move = event.moveList().getLast();
            Generators.generateAllMoves(board)
                    .stream()
                    .filter(m -> m.toString().equals(move))
                    .findFirst()
                    .ifPresent(board::makeMove);

            if(board.isPlayer()) {
                AlphaBetaPruning.getBestMove(board, properties.getThinkingTime())
                        .ifPresent(m -> client.bot().move(gameId, m.toString()));
            }
        }
    }

}
