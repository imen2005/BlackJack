import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {
    private class Card {
    String value;
    String type;

    Card(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public String toString() {
        return value + "-" + type;
    }

    public int getValue() {
        if ("AJQK".contains(value)) { //A J Q K
            if (value == "A") {
                return 11;
            }
            return 10;
        }
        return Integer.parseInt(value); //2-10
    }

    public boolean isAce() {
        return value == "A";
    }
        
    }

    ArrayList<Card> deck;
    Random random = new Random();

    //dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerScore;
    int dealerAceCount;

    //player
    ArrayList<Card> playerHand;
    int playerScore;
    int playerAceCount;

    //window
    int boardWidth = 600;
    int boardHeight = boardWidth;

    int cardWidth = 110; //The ratio btw the w and h should be 1/1.4
    int cardHeight = 154;

    //Image x
    int cardX = 20;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            //draw hidden card
            Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
            g.drawImage(hiddenCardImg, cardX, 20, cardWidth, cardHeight, null);
            //draw dealer hand
            for (int i = 0; i < dealerHand.size(); i++) {
                Card card = dealerHand.get(i);
                Image cardImage = new ImageIcon(getClass().getResource("./cards/" + card.toString() + ".png")).getImage();
                g.drawImage(cardImage, cardX + 120*(i+1), 20, cardWidth, cardHeight, null);
            }
            //draw the player hand
            for (int i = 0; i < playerHand.size(); i++) {
                Card card = playerHand.get(i);
                Image cardImage = new ImageIcon(getClass().getResource("./cards/" + card.toString() + ".png")).getImage();
                g.drawImage(cardImage, cardX + 120*i,360 , cardWidth, cardHeight, null);
            }

            //WON or LOST
            if (st) {
                //draw hidden card 
                Image hiddenCarImg = new ImageIcon(getClass().getResource("./cards/" + hiddenCard.toString() + ".png")).getImage();
                g.drawImage(hiddenCarImg, cardX, 20, cardWidth, cardHeight, null);
                if ((playerScore > 21 && dealerScore <= 21) || (playerScore > 21 && dealerScore > 21 && playerScore > dealerScore) || (playerScore <= 21 && dealerScore <= 21 && playerScore < dealerScore)) {
                    g.drawString("LOST", 280, 280);
                } else if ((playerScore <= 21 && dealerScore > 21) || (playerScore > 21 && dealerScore > 21 && playerScore <= dealerScore) || (playerScore <= 21 && dealerScore <= 21 && playerScore >= dealerScore)) {
                    g.drawString("WON", 280, 280);
                }
            }
            st = false;
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
    Boolean st = false;

    BlackJack() {
        startGame();
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); //this is going to open up our window in the middle of the screen instead of the top left
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        hitButton.addActionListener(e -> addACard(playerHand));
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        stayButton.addActionListener(e -> stay());
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void startGame() {  
        //deck
        buildDeck();
        shuffleDeck();

        //dealer
        dealerHand = new ArrayList<Card>();
        dealerScore = 0;
        dealerAceCount = 0;


        hiddenCard = deck.remove(deck.size()-1); //remove card at last index
        dealerScore += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;
        Card card = deck.remove(deck.size()-1);
        dealerHand.add(card);
        dealerScore += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;

        System.out.println("DEALER:");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerScore);
        System.out.println(dealerAceCount);

        //player
        playerHand = new ArrayList<Card>();
        playerScore = 0;
        playerAceCount = 0;

        for (int i = 0; i < 2; i++) {
            card = deck.remove(deck.size()-1);
            playerScore += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }
        System.out.println("PLAYER:");
        System.out.println(playerHand);
        System.out.println(playerScore);
        System.out.println(playerAceCount);
    }

    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < values.length; j++) {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }

        System.out.println("BUILD DECK:");
        System.out.println(deck);
    }

    public void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }

        System.out.println("AFTER SHUFFLE: ");
        System.out.println(deck);
    }

    public void stay() {
        while (dealerScore < 17) {
            addACard(dealerHand);
        }
        if (playerScore >= 17 && dealerScore >= 17) {
            if (((playerScore >= dealerScore) && (playerScore <= 21)) || (dealerScore > 21)) {
                System.out.println("YOU WIN");
            }
            else{
                System.out.println("YOU LOSE");
            }
            st = true;
            gamePanel.repaint();
        }
        else if (playerScore < 17) {
            addACard(playerHand);
        }
        while (playerScore > 21 && playerAceCount > 0) {
            playerScore -= 10;
            playerAceCount--;
            System.out.println("PlayerScoreAce:" + playerScore);
        }
        while (dealerScore > 21 && dealerAceCount > 0) {
            dealerScore -= 10;
            dealerAceCount--;
            System.out.println("DealerScoreAce:" + dealerScore);
        }
    }


    public void addACard(ArrayList<Card> hand) {
        Card card = deck.remove(deck.size()-1);
        hand.add(card);
        if (hand == playerHand) {
            playerScore += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            System.out.println(playerScore);
        } else {
            dealerScore += card.getValue();
            dealerAceCount += card.isAce() ? 1 : 0;
            System.out.println(dealerScore);
        }
        gamePanel.repaint();
    }
}
