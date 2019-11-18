package Model;

public class Card {
    public static final int CARDWIDTH = 120;
    public static final int CARDHEIGHT = 270;
    private CardSuite cardSuite;
    private CardValue cardValue;
    private CardDirection cardDirection;
    private int cardValueInt = 0;

    public int getCardValueInt() {
        return cardValueInt;
    }

    public void setCardValueInt(int cardValueInt) {
        this.cardValueInt = cardValueInt;
    }

    public CardSuite getCardSuite() {
        return cardSuite;
    }

    public void setCardSuite(CardSuite cardSuite) {
        this.cardSuite = cardSuite;
    }

    public CardValue getCardValue() {
        return cardValue;
    }

    public void setCardValue(CardValue cardValue) {
        this.cardValue = cardValue;
    }

    public CardDirection getCardDirection() {
        return cardDirection;
    }

    public void setCardDirection(CardDirection cardDirection) {
        this.cardDirection = cardDirection;
    }

    public Card(CardSuite cardSuite, CardValue cardValue, CardDirection cardDirection, int cardValueInt) {
        this.cardSuite = cardSuite;
        this.cardValue = cardValue;
        this.cardDirection = cardDirection;
        this.cardValueInt = cardValueInt;
    }
}
