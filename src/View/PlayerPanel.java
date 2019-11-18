package View;

import Model.BlackJackResult;
import Model.Card;
import Model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public abstract class PlayerPanel extends JPanel {
    private JPanel playerCardPanel;
    private JLabel playerPointsLable;
    private JPanel playerMainPanel;
    private JPanel tokenPanel;
    private JLabel leftTokenLabel;
    private JSpinner betTokenSpinner;
    private JButton confirmButton;
    private Player player;

    public PlayerPanel(Player _player) {
        super();
        player = _player;
        betTokenSpinner.setModel(new SpinnerNumberModel(1, 1, player.getLeftToken(), 1));
        leftTokenLabel.setText(String.valueOf(player.getLeftToken()));
        add(playerMainPanel);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    player.setBetToken(Integer.parseInt(betTokenSpinner.getValue() + ""));
                    betConfirmNewGameStart();
                    betConfirm();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void betConfirm(){
        betTokenSpinner.setEnabled(false);
        confirmButton.setEnabled(false);
    }

    public void nextRound(){
        betTokenSpinner.setEnabled(true);
        confirmButton.setEnabled(true);
        if(player.getLeftToken()>0){
            betTokenSpinner.setModel(new SpinnerNumberModel(1, 1, player.getLeftToken(), 1));
        }else {
            /*you lose all Token*/
            betConfirm();
            leftTokenLabel.setText("You are bankrupt");
        }
    }

    public void setPlayerCardValue(String text) {
        playerPointsLable.setText(text);
    }

    public void addPlayerHoldCard(Card card) throws IOException {
        playerCardPanel.add(new JCard(card));
        playerCardPanel.updateUI();
    }

    public void removeAllCard() {
        playerCardPanel.removeAll();
        playerCardPanel.updateUI();
    }

    /*result*/
    public void setResult(BlackJackResult blackJackResult) {
        switch (blackJackResult) {
            case PlayerWin:
                player.setLeftToken(player.getLeftToken()+player.getBetToken());
                break;
            case DealerWin:
                player.setLeftToken(player.getLeftToken()-player.getBetToken());
                break;
            case Push:
                break;
        }

        leftTokenLabel.setText(String.valueOf(player.getLeftToken()));

        /*After lose or get then new game start*/
        nextRound();
    }

    public abstract void betConfirmNewGameStart() throws IOException;
}
