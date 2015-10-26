package eu.goodlike.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicInteger;

public final class Move {

    public String toJson() {
        return "{\"x\": " + x + ", \"y\": " + y + ", \"v\": \"" + v + "\"}";
    }

    public String toJsonWithoutArrays(AtomicInteger atomicInteger) {
        int index = atomicInteger.getAndIncrement();
        return "\"" + index + "\": " + toJson();
    }

    @JsonCreator
    public Move(@JsonProperty(value = "x") int x,
                @JsonProperty(value = "y") int y,
                @JsonProperty(value = "v") String v) {
        this.x = x;
        this.y = y;
        this.v = v;
    }

    // PRIVATE

    private final int x, y;
    private final String v;

}
