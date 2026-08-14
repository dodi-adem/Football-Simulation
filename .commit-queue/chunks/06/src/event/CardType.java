package event;

public enum CardType {

    YELLOW("Yellow Card"),
    RED   ("Red Card");

    private final String label;

    CardType(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}