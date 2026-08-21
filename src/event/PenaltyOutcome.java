package event;

public enum PenaltyOutcome {

    SCORED("Scored"),
    MISSED("Missed");

    private final String label;

    PenaltyOutcome(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}