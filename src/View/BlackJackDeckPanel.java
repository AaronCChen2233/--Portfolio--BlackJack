package View;

import Model.BlackJackResult;
import Model.blackJackDeck;
import Model.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class BlackJackDeckPanel extends JPanel {
    private Model.blackJackDeck blackJackDeck;
    private JPanel mainPanel;
    private JButton hitButton;
    private JButton standButton;
    private JPanel dealerCardPanel;
    private JLabel dealerPointsLable;
    private JLabel resultLable;
    private JButton playAgainButton;
    private JPanel resultPanel;
    private JPanel playersPanel;
    private PlayerPanel playerPanel;

    public BlackJackDeckPanel() throws IOException {
        super();
        blackJackDeck = new blackJackDeck();
        /*Now only for one player, multiple coming soon~*/
        blackJackDeck.playersJoinGame(1);

        /*just for test*/
//        mainPanel.setLayout(new GridLayout(4, 13));
//        for(Card card : blackJackDeck.getCards()){
//            mainPanel.add(new JCard(card));
//        }

        playerPanel = new PlayerPanel(blackJackDeck.getPlayers().get(0)) {
            @Override
            public void betConfirmNewGameStart() throws IOException {
                blackJackDeck.nextRound();
                newGameStart();
            }
        };

        playersPanel.add(playerPanel);
        playersPanel.updateUI();

        standButton.setVisible(false);
        hitButton.setVisible(false);

        /*Now playAgainButton might not use because when player bet confirm new game will start automatically*/
        playAgainButton.setVisible(false);

        setBackground(new Color(2, 74, 40));
        mainPanel.setBackground(new Color(2, 74, 40));
        dealerCardPanel.setBackground(new Color(2, 74, 40));
        playerPanel.setBackground(new Color(2, 74, 40));
        add(mainPanel);

        standButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allPlayerAreStand();
            }
        });

        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    playerPanel.addPlayerHoldCard(blackJackDeck.playerHit(0));
                    String playerPointsString = blackJackDeck.checkPlayerIsBlackJack(0) ? "Black Jack" : String.valueOf(blackJackDeck.getPlayerHandCardsValue(0));
                    playerPanel.setPlayerCardValue(playerPointsString);

                    /*if player got BlackJack or 21 point stand automatically*/
                    if (checkPlayerHasBlackJackOr21Point(0)) {
                        allPlayerAreStand();
                    }

                    if (blackJackDeck.checkPlayerBust(0)) {
                        /*Bust*/
                        showResult("You bust you lose~");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blackJackDeck.nextRound();
                try {
                    newGameStart();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void allPlayerAreStand() {
        /*Use another thread for the effect dealer hit card one by one per second*/
        dealerPointsLable.setVisible(true);
        standButton.setEnabled(false);
        hitButton.setEnabled(false);
        TimeSetter timeSetter = new TimeSetter();
        Thread t = new Thread(timeSetter);
        t.start();
    }

    private void dealerShowCards() {
        blackJackDeck.dealerShowCards();
        String dealerPointsString = blackJackDeck.checkDealerIsBlackJack() ? "Black Jack" : String.valueOf(blackJackDeck.getDealerHandCardsValue());
        dealerPointsLable.setText(dealerPointsString);
        for (Component jcard : dealerCardPanel.getComponents()) {
            ((JCard) jcard).faceUp();
        }
        dealerCardPanel.updateUI();
    }

    private void showResult(String s) {
        /*different color for different result*/
        BlackJackResult result =blackJackDeck.getResult(0);
        switch (result) {
            case PlayerWin:
                resultLable.setForeground(Color.yellow);
                break;
            case DealerWin:
                resultLable.setForeground(Color.red);
                break;
            case Push:
                resultLable.setForeground(Color.blue);
                break;
        }
        playerPanel.setResult(result);
        resultPanel.setVisible(true);
        standButton.setVisible(false);
        hitButton.setVisible(false);
        resultLable.setText(s);
    }

    private void newGameStart() throws IOException {
        standButton.setVisible(true);
        hitButton.setVisible(true);
        standButton.setEnabled(true);
        hitButton.setEnabled(true);
        resultPanel.setVisible(false);
        dealerPointsLable.setVisible(false);
        playerPanel.removeAllCard();
        dealerCardPanel.removeAll();

        for (Card c : blackJackDeck.getPlayers().get(0).getHoldCards()) {
            playerPanel.addPlayerHoldCard(c);
        }

        String playerPointsString = blackJackDeck.checkPlayerIsBlackJack(0) ? "Black Jack" : String.valueOf(blackJackDeck.getPlayerHandCardsValue(0));
        playerPanel.setPlayerCardValue(playerPointsString);

        for (Card c : blackJackDeck.getDealer().getHoldCards()) {
            dealerCardPanel.add(new JCard(c));
        }

        dealerCardPanel.updateUI();

        /*if player got BlackJack or 21 point stand automatically*/
        if (checkPlayerHasBlackJackOr21Point(0)) {
            allPlayerAreStand();
        }
    }

    private boolean checkPlayerHasBlackJackOr21Point(int playerIndex) {
        return blackJackDeck.getPlayerHandCardsValue(playerIndex) == 21;
    }

    class TimeSetter implements Runnable {
        @Override
        public void run() {
            dealerHit();
        }
    }

    private void dealerHit() {
        try {
            /*dealer show card*/
            dealerShowCards();
            Thread.sleep(1000);
            /*If Dealer hand cards value smaller than 17 dealer will keep hit*/
            while (blackJackDeck.getDealerHandCardsValue() < 17) {
                dealerCardPanel.add(new JCard(blackJackDeck.dealerHit()));
                dealerCardPanel.updateUI();
                String dealerPointsString = blackJackDeck.checkDealerIsBlackJack() ? "Black Jack" : String.valueOf(blackJackDeck.getDealerHandCardsValue());
                dealerPointsLable.setText(dealerPointsString);
                if (blackJackDeck.checkDealerBust()) {
                    /*Bust*/
                    showResult("Dealer bust you win!");
                    return;
                }
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

        /*if hadn't bust and dealer points is bigger than 17 check*/
        String dealerPointsString = blackJackDeck.checkDealerIsBlackJack() ? "Black Jack" : String.valueOf(blackJackDeck.getDealerHandCardsValue()) + " points";
        switch (blackJackDeck.getResult(0)) {
            case PlayerWin:
                showResult("Dealer has " + dealerPointsString + " you win!");
                break;
            case DealerWin:
                showResult("Dealer has " + dealerPointsString + " you lose~");
                break;
            case Push:
                showResult("Dealer also has " + dealerPointsString + " that push");
                break;
        }
    }
}
