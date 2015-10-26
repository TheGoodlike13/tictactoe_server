package eu.goodlike.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.MoreObjects;
import eu.goodlike.libraries.jackson.Json;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public final class MoveHistory {

    public boolean isOver() {
        return moves.size() >= 9;
    }

    public String toJson() {
        return "[" + moves.stream().map(Move::toJson).collect(joining(", ")) + "]";
    }

    public String toJsonWithoutArrays() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        return "{" + moves.stream().map(move -> move.toJsonWithoutArrays(atomicInteger)).collect(joining(", ")) + "}";
    }

    // CONSTRUCTORS

    public static MoveHistory fromJson(String moves) {
        try {
            return new MoveHistory(Json.from(moves).to(LIST_OF_MOVES));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MoveHistory fromJsonWithoutArrays(String moves) {
        try {
            LinkedHashMap<Integer, Move> map = Json.from(moves).to(MAP_OF_MOVES);
            return new MoveHistory(map.keySet().stream().map(map::get).collect(toList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MoveHistory() {
        this(Collections.emptyList());
    }

    public MoveHistory(List<Move> moves) {
        this.moves = moves;
    }

    // PRIVATE

    private final List<Move> moves;

    private static final TypeReference<List<Move>> LIST_OF_MOVES = new TypeReference<List<Move>>() {};
    private static final TypeReference<LinkedHashMap<Integer, Move>> MAP_OF_MOVES = new TypeReference<LinkedHashMap<Integer, Move>>() {};

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("moves", moves)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoveHistory)) return false;
        MoveHistory that = (MoveHistory) o;
        return Objects.equals(moves, that.moves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moves);
    }

}
