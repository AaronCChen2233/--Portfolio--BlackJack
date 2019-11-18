package Model;

import java.util.ArrayList;
import java.util.Collections;
/*
 * rule change list
 * V1.blackJack is bigger than 21 point
 * 2.if player got black jack and dealer's face up card isn't 10 point cards or ace is player win
 * (because that mean dealer won't get black jack)
 *
 * V3.Player got BlackJack or 21 should stand automatically(for sure)
 * 4.dealer keep hit until have 17 point if is soft 17 will keep hit(soft 17 means have Ace for Example have Ace and 6)
 * V5.Allow players have tokens for bet
 * 6.not shuffle ever round only when cards not enough
 *
 * About Multiple players rules
 * 1.if is face up game(players can see each other cards)use 8 decks of cards
 * if is face down game(players can't see each other cards, but got 21 or BlackJack or bust shout show cards)
 * use 2 decks of cards
 *
 * 2.player bust already lose dealer will take the token.
 * after that if dealer fight with other player bust or not doesn't matter
 *
 * 3.Multiple players can up to 8 people
 * */

public class blackJackDeck {
    private ArrayList<Card> cards = new ArrayList<Card>();
    private Player dealer;

    /*Because if multiple players so use list*/
    private ArrayList<Player> players;

    private int cardIndex = 0;

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Player getDealer() {
        return dealer;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    private int getCardsValue(ArrayList<Card> cards) {
        int total = 0;
        boolean hasAce = false;
        for (Card c : cards) {
            total += c.getCardValueInt();
            if (c.getCardValue().equals(CardValue.Ace)) {
                hasAce = true;
            }
        }

        /*Rule for Ace Because if haven't bust more point is better*/
        /*so if haven't bust say Ace is 11 points*/
        /*already add 1 point in foreach loop so only +10 points*/
        /*allow player say Ace have difference values*/
        /*if player have more than one Ace player can say one is 11 point another is 1 point*/
        if ((total + 10) <= 21 && hasAce) {
            total += 10;
        }
        return total;
    }

    public int getDealerHandCardsValue() {
        return getCardsValue(dealer.getHoldCards());
    }

    public int getPlayerHandCardsValue(int i) {
        return getCardsValue(players.get(i).getHoldCards());
    }

    private boolean checkIsBlackJack(ArrayList<Card> cards) {
        /*If cards value is 21 and only have 2 cards is black jack*/
        return getCardsValue(cards) == 21 && cards.size() == 2;
    }

    public boolean checkDealerIsBlackJack() {
        return checkIsBlackJack(dealer.getHoldCards());
    }

    public boolean checkPlayerIsBlackJack(int i) {
        return checkIsBlackJack(players.get(i).getHoldCards());
    }

    public blackJackDeck() {
        for (CardSuite cardSuite : CardSuite.values()) {
            for (CardValue cardValue : CardValue.values()) {
                /*black jack doesn't use Joker*/
                if (!cardValue.equals(CardValue.Joker)) {
                    cards.add(new Card(cardSuite, cardValue, CardDirection.FaceUp, getCardValueInt(cardValue)));
                }
            }
        }
        dealer = new Player();
    }

    public void playersJoinGame(int playersCount) {
        players = new ArrayList<Player>();
        for (int i = 0; i < playersCount; i++) {
            Player player = new Player();
            players.add(player);
        }
    }

    public void nextRound() {
        shuffle();
        dealer.removeAllHoldCard();
        for (Player player : players) {
            player.removeAllHoldCard();
            player.getCard(cards.get(cardIndex));
            cardIndex++;
            player.getCard(cards.get(cardIndex));
            cardIndex++;
        }

        dealer.getCard(cards.get(cardIndex));
        cardIndex++;
        dealer.getCard(cards.get(cardIndex));
        cardIndex++;

        /*Dealer first card is face down*/
        dealer.getHoldCards().get(0).setCardDirection(CardDirection.FaceDown);
    }

    public Card playerHit(int playerIndex) {
        cardIndex++;
        players.get(playerIndex).getCard(cards.get(cardIndex));
        return cards.get(cardIndex);
    }

    public Card dealerHit() {
        cardIndex++;
        dealer.getCard(cards.get(cardIndex));
        return cards.get(cardIndex);
    }

    public void dealerShowCards() {
        /*Set every cards are face up*/
        for (Card c : dealer.getHoldCards()) {
            c.setCardDirection(CardDirection.FaceUp);
        }
    }

    public BlackJackResult getResult(int playerIndex) {
        int playerCardValue = getPlayerHandCardsValue(playerIndex);
        int dealerCardValue = getDealerHandCardsValue();
        if (playerCardValue > 21) {
            /*Player Bust*/
            return BlackJackResult.DealerWin;
        }

        if (dealerCardValue > 21) {
            /*Dealer Bust*/
            return BlackJackResult.PlayerWin;
        }

        if (dealerCardValue > playerCardValue) {
            /*Dealer card value bigger than player DealerWin*/
            return BlackJackResult.DealerWin;
        } else if (dealerCardValue == playerCardValue) {
            if (dealerCardValue == 21 || playerCardValue == 21) {
                /*Because BlackJack and 21 points in this function will get same value, but rule say BlackJack is bigger than 21 point*/
                /*Only have two card that BlackJack*/
                if (dealer.getHoldCards().size() == 2 && players.get(playerIndex).getHoldCards().size() == 2) {
                    /*Both are BlackJack*/
                    return BlackJackResult.Push;
                } else if (dealer.getHoldCards().size() == 2 && players.get(playerIndex).getHoldCards().size() != 2) {
                    /*Dealer have BlackJack*/
                    return BlackJackResult.DealerWin;
                } else if (dealer.getHoldCards().size() != 2 && players.get(playerIndex).getHoldCards().size() == 2) {
                    /*Player have BlackJack*/
                    return BlackJackResult.PlayerWin;
                }
            }
            /*Player card value equal dealer Push*/
            return BlackJackResult.Push;
        } else {
            /*Player card value bigger than dealer*/
            return BlackJackResult.PlayerWin;
        }
    }

    /*true is Bust*/
    public boolean checkPlayerBust(int playerIndex) {
        return getPlayerHandCardsValue(playerIndex) > 21;
    }

    /*true is Bust*/
    public boolean checkDealerBust() {
        return getCardsValue(dealer.getHoldCards()) > 21;
    }

    /*Because different card value for different card game so put card value when initial the card game not in the Card class*/
    private int getCardValueInt(CardValue cardValue) {
        switch (cardValue) {
            case Ace:
                return 1;
            case Two:
                return 2;
            case Three:
                return 3;
            case Four:
                return 4;
            case Five:
                return 5;
            case Six:
                return 6;
            case Seven:
                return 7;
            case Eight:
                return 8;
            case Nine:
                return 9;
            case Ten:
            case Jack:
            case Queen:
            case King:
                return 10;
            default:
                return 0;
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
        for (Card card : cards) {
            card.setCardDirection(CardDirection.FaceUp);
        }
        cardIndex = 0;
    }
}
