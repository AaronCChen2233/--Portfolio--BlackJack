package Model;

public enum CardValue {
    Joker("JOKER"),
    Ace("A"),
    Two("2"),
    Three("3"),
    Four("4"),
    Five("5"),
    Six("6"),
    Seven("7"),
    Eight("8"),
    Nine("9"),
    Ten("10"),
    Jack("J"),
    Queen("Q"),
    King("K");

    private final String LABLE;

    public String getLabel() {
        return LABLE;
    }

    private CardValue(String label) {
        this.LABLE = label;
    }
}
