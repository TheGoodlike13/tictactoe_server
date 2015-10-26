package eu.goodlike.api;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.MoreObjects;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TicTacToeService {

    public MoveHistory getMoveHistoryForGame(String gameName, int playerId) throws InterruptedException {
        MoveHistory current = fetchCurrentGame(gameName);
        while (current.equals(playerId == 1 ? lastTime1 : lastTime2)) {
            Thread.sleep(1000);
            current = fetchCurrentGame(gameName);
        }
        save(current, playerId);
        return current;
    }

    public synchronized void setMoveHistoryForGame(String gameName, int playerId, MoveHistory history) {
        gameCache.put(gameName, history);
        save(history, playerId);
    }

    // CONSTRUCTORS

    public TicTacToeService() {
        gameCache = Caffeine.newBuilder()
                .softValues()
                .build(name -> new MoveHistory());
    }

    // PRIVATE

    private final LoadingCache<String, MoveHistory> gameCache;

    private MoveHistory lastTime1, lastTime2;

    private synchronized MoveHistory fetchCurrentGame(String gameName) {
        MoveHistory game = gameCache.get(gameName);
        if (game.isOver())
            throw new IllegalArgumentException("Game over!");

        return game;
    }

    private synchronized void save(MoveHistory moves, int playerId) {
        if (playerId == 1)
            lastTime1 = moves;
        else
            lastTime2 = moves;
    }

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("gameCache", gameCache)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicTacToeService)) return false;
        TicTacToeService that = (TicTacToeService) o;
        return Objects.equals(gameCache, that.gameCache);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameCache);
    }

}
