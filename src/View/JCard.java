package View;

import Model.Card;
import Model.CardDirection;
import Model.Tools;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JCard extends JPanel {
    private Card card;
    private JPanel mainPanel;
    private JPanel faceUpPanel;
    private JPanel faceDownPanel;
    private JLabel cardValueLabel1;
    private JLabel cardValueLabel2;
    private JLabel suiteImageLabel;

    public JCard(Card _card) throws IOException {
        super();
        card = _card;

        setFaceDirection();

        initialFaceUp();
        initialFaceDown();

        Border cardBorder = new LineBorder(Color.BLACK, 3, true);
        setBorder(cardBorder);
        add(mainPanel);
    }

    private void setFaceDirection() {
        faceUpPanel.setVisible(card.getCardDirection().equals(CardDirection.FaceUp));
        faceDownPanel.setVisible(card.getCardDirection().equals(CardDirection.FaceDown));
    }

    private void initialFaceUp() {
        String suiteImagePath = String.format("src/images/%s.png", card.getCardSuite().toString());
        /*If can't find the image put word*/
        try {
            BufferedImage suiteImage = ImageIO.read(new File(suiteImagePath));
            suiteImage = Tools.resize(suiteImage, Card.CARDWIDTH - 20, Card.CARDWIDTH - 20);
            suiteImageLabel.setIcon(new ImageIcon(suiteImage));
        } catch (IOException ex) {
            suiteImageLabel.setText(card.getCardSuite().toString());
        }
        cardValueLabel1.setText(card.getCardValue().getLabel());
        cardValueLabel2.setText(card.getCardValue().getLabel());

        /*put color*/
        Color valueColor = new Color(255, 255, 255);
        switch (card.getCardSuite()) {
            case Clubs:
            case Spades: {
                valueColor = new Color(44, 47, 56);
                break;
            }
            case Hearts:
            case Diamonds: {
                valueColor = new Color(230, 76, 60);
                break;
            }
        }
        cardValueLabel1.setForeground(valueColor);
        cardValueLabel2.setForeground(valueColor);
        suiteImageLabel.setForeground(valueColor);
    }

    private void initialFaceDown() {
        faceDownPanel.setLayout(new GridLayout(11, 9));

        try {
            BufferedImage suiteImage = ImageIO.read(new File("src/images/AaronChenGitHub.png"));
            suiteImage = Tools.resize(suiteImage, 20, 20);

            for (int i = 0; i < 99; i++) {
                JLabel pic = new JLabel();
                pic.setIcon(new ImageIcon(suiteImage));
                faceDownPanel.add(pic);
            }
        } catch (IOException ex) {
            for (int i = 0; i < 99; i++) {
                JPanel pic = new JPanel();
                pic.setMaximumSize(new Dimension(20,20));
                pic.setSize(new Dimension(20,20));
                pic.setBackground(i % 2 == 0 ? Color.blue : Color.white);
                faceDownPanel.add(pic);
            }
        }
    }

    public void faceDown() {
        card.setCardDirection(CardDirection.FaceDown);
        setFaceDirection();
    }

    public void faceUp() {
        card.setCardDirection(CardDirection.FaceUp);
        setFaceDirection();
    }

    public JCard() {

    }
}
