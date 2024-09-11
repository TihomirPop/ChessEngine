package hr.tvz.popovic.chessengine.controller;

import hr.tvz.popovic.chessengine.model.LichessStartRequest;
import hr.tvz.popovic.chessengine.service.LichessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lichess")
@CrossOrigin()
public class LichessController {

    private final LichessService lichessService;

    @PostMapping("/start")
    public void startGame(@RequestBody LichessStartRequest request) {
        lichessService.startGame(request);
    }

}
