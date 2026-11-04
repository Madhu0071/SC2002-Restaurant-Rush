package restaurantrush;

import java.util.OptionalDouble;

public class TurnReport {
    private int turn;
    private int servedCount;
    private int unhappyDepartures;
    private int revenueCents;
    private OptionalDouble avgSatisfaction;

    public TurnReport(int turn, int servedCount, int unhappyDepartures, int revenueCents, OptionalDouble avgSatisfaction) {
        this.turn = turn;
        this.servedCount = servedCount;
        this.unhappyDepartures = unhappyDepartures;
        this.revenueCents = revenueCents;
        this.avgSatisfaction = avgSatisfaction;
    }

    public int getTurn() {
        return turn;
    }

    public int getServedCount() {
        return servedCount;
    }

    public int getUnhappyDepartures() {
        return unhappyDepartures;
    }

    public int getRevenueCents() {
        return revenueCents;
    }

    public OptionalDouble getAvgSatisfaction() {
        return avgSatisfaction;
    }
}
