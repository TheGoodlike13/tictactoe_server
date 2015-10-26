package eu.goodlike.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class TicTacToeController {

    @RequestMapping(value = "/game/{matchName}/player/{playerId:1|2}", method = GET)
    @ResponseBody
    public String getMoves(@PathVariable String matchName, @PathVariable int playerId,
                                @RequestHeader("Accept") String accept) throws InterruptedException {
        boolean useSimpleJson = !accept.equals("application/json+map");
        MoveHistory moves = ticTacToeService.getMoveHistoryForGame(matchName, playerId);
        return useSimpleJson ? moves.toJson() : moves.toJsonWithoutArrays();
    }

    @RequestMapping(value = "/game/{matchName}/player/{playerId:1|2}", method = POST)
    @ResponseBody
    public void nextMove(@PathVariable String matchName, @PathVariable int playerId,
                         @RequestBody String moves, @RequestHeader("Content-Type") String type) {
        boolean useSimpleJson = !type.equals("application/json+map");
        ticTacToeService.setMoveHistoryForGame(matchName, playerId, useSimpleJson ? MoveHistory.fromJson(moves) : MoveHistory.fromJsonWithoutArrays(moves));
    }

    // CONSTRUCTORS

    @Autowired
    public TicTacToeController(TicTacToeService ticTacToeService) {
        this.ticTacToeService = ticTacToeService;
    }

    // PRIVATE

    private final TicTacToeService ticTacToeService;

}
