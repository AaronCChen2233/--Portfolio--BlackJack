package View;

import javax.swing.*;
import java.io.IOException;

public class CardGameWindow extends JFrame {


    public CardGameWindow() throws IOException {
        setTitle("Aaron Card Game");
        setSize(1900, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new BlackJackDeckPanel());
    }
}
