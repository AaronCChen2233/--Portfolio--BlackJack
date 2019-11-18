package Model;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> HoldCards;
    private int leftToken;
    private int betToken;

    public int getBetToken() {
        return betToken;
    }

    public void setBetToken(int betToken) {
        this.betToken = betToken;
    }

    public int getLeftToken() {
        return leftToken;
    }

    public void setLeftToken(int leftToken) {
        this.leftToken = leftToken;
    }

    public ArrayList<Card> getHoldCards() {
        return HoldCards;
    }

    public Player(){
        HoldCards = new ArrayList<Card>();
        /*default have 3 token*/
        leftToken = 3;
    }

    public void getCard(Card card){
        HoldCards.add(card);
    }

    public void removeAllHoldCard(){
        HoldCards.clear();
    }
}
