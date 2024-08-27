package hr.tvz.popovic.chessengine.controller;

import hr.tvz.popovic.chessengine.model.Board;
import hr.tvz.popovic.chessengine.model.MakeMoveResponse;
import hr.tvz.popovic.chessengine.model.Piece;
import hr.tvz.popovic.chessengine.model.StartGameFromFenRequest;
import hr.tvz.popovic.chessengine.service.ChessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chess")
@CrossOrigin(origins = "http://localhost:3000")
public class ChessController {

    private final ChessService chessService;

    @PostMapping("/start")
    public ResponseEntity<List<Character>> start() {
        return ResponseEntity.ok(chessService.start());
    }

    @PostMapping("/start/fen")
    public ResponseEntity<List<Character>> startFromFen(@RequestBody StartGameFromFenRequest request) {
        return chessService.startFromFen(request.fen())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/move/{from}")
    public ResponseEntity<List<Byte>> moves(@PathVariable int from) {
        return ResponseEntity.ok(chessService.getMoves(from));
    }

    @PostMapping("/move/{from}/{to}/{thinkingTime}")
    public ResponseEntity<MakeMoveResponse> move(@PathVariable int from, @PathVariable int to, @PathVariable int thinkingTime) {
        return ResponseEntity.ok(chessService.makeMove(from, to, thinkingTime));
    }

}
