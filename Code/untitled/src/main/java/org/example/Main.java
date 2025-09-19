package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DisplayGUI::new);
    }
}

class Card {
    private final int cardNumber;
    private ImageIcon icon;

    // Constructor: Creates a new card and automatically loads its image
    public Card(int cardNumber) {
        this.cardNumber = cardNumber;
        loadImage();
    }

    // Loads the card image from resources folder and scales it to fit the display
    private void loadImage() {
        // Calculate suit: divide by 13 and throw away remainder (integer division)
        // Card 0-12 = suit 0 (spades), Card 13-25 = suit 1 (hearts), etc.
        // Examples: card 0 ÷ 13 = 0 remainder 0, so suit = 0
        //           card 15 ÷ 13 = 1 remainder 2, so suit = 1
        //           card 39 ÷ 13 = 3 remainder 0, so suit = 3
        int suit = cardNumber / 13;

        // Calculate card value: get the remainder when divided by 13
        // This "wraps around" every 13 cards: 0,1,2...12,0,1,2...12
        // Examples: card 0 ÷ 13 = remainder 0 (ace)
        //           card 15 ÷ 13 = remainder 2 (3)
        //           card 25 ÷ 13 = remainder 12 (king)
        int value = cardNumber % 13;

        // Create filename with automatic zero-padding
        // %02d means "2 digits, pad with zeros if needed"
        // Examples: value 5 becomes "05", value 11 stays "11"
        //           Final: "0_05.png", "2_11.png", "3_00.png"
        String filename = String.format("%d_%02d.png", suit, value);

        try {
            // Load the original image from resources folder
            ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(
                getClass().getResource("/" + filename)));

            // Scale image to 60x84 pixels to fit nicely in the grid
            Image scaledImage = originalIcon.getImage().getScaledInstance(60, 84, Image.SCALE_SMOOTH);
            this.icon = new ImageIcon(scaledImage);
            System.out.println("Loaded: " + filename);
        } catch (Exception e) {
            System.out.println("Failed to load: " + filename);
            this.icon = null;
        }
    }

    // Returns the card's image for displaying in the GUI
    public ImageIcon getIcon() {
        return icon;
    }

    public int getCardNumber() {
        return cardNumber;
    }
}

class DisplayGUI extends JFrame {
    private JPanel cardPanel;
    private final int CARD_COUNT = 52;
    private Card[] cards;
    private final Shuffler shuffler;

    // Constructor: Sets up the main window and displays all cards
    public DisplayGUI() {
        setTitle("Poker Card Display - Lab 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        shuffler = new Shuffler(); // Create shuffler instance
        loadCards();           // Create all 52 Card objects
        initializeComponents(); // Set up the GUI layout
        displayCards(false);   // Show the cards in order (0,1,2...51)

        setVisible(true);      // Make the window visible
    }

    // Sets up the GUI layout: title, card grid, and shuffle button
    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Title at the top
        JLabel titleLabel = new JLabel("Poker Card Display", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Card grid panel: 4 rows x 13 columns
        cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(4, 13, 5, 5));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cardPanel.setBackground(new Color(0, 128, 0)); // Green like a poker table

        // Scroll pane: allows scrolling if cards don't fit on screen
        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setPreferredSize(new Dimension(950, 500));
        add(scrollPane, BorderLayout.CENTER);

        // Shuffle button at the bottom
        JButton shuffleButton = new JButton("Shuffle Cards");
        shuffleButton.setFont(new Font("Arial", Font.BOLD, 16));
        shuffleButton.setPreferredSize(new Dimension(200, 40));
        shuffleButton.addActionListener(e -> shuffleAndDisplay()); // Connect to shuffle method

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(shuffleButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Creates all 52 Card objects (each Card automatically loads its own image)
    private void loadCards() {
        cards = new Card[CARD_COUNT];
        for (int i = 0; i < CARD_COUNT; i++) {
            cards[i] = new Card(i); // Card constructor handles image loading
        }
    }

    // Displays cards with option to shuffle
    private void displayCards(boolean shuffle) {
        cardPanel.removeAll(); // Clear any existing cards

        // Get card order: either 0,1,2...51 or shuffled
        ArrayList<Integer> cardOrder = new ArrayList<>();
        if (shuffle) {
            cardOrder = shuffler.GetShuffledArray();
            System.out.println("Shuffler returned array of size: " + cardOrder.size());
        } else {
            for (int i = 0; i < CARD_COUNT; i++) {
                cardOrder.add(i); // Normal order: 0,1,2,3...51
            }
        }

        for (int i = 0; i < CARD_COUNT; i++) {
            // Create a label to hold each card image
            JLabel cardLabel = new JLabel();
            cardLabel.setPreferredSize(new Dimension(65, 90));
            cardLabel.setHorizontalAlignment(JLabel.CENTER);
            cardLabel.setVerticalAlignment(JLabel.CENTER);

            // Get which card to show at this position
            int cardIndex = cardOrder.get(i);

            // Show the card image, or a placeholder if image failed to load
            if (cards[cardIndex].getIcon() != null) {
                cardLabel.setIcon(cards[cardIndex].getIcon());
            } else {
                // Fallback: show white rectangle with card number
                cardLabel.setOpaque(true);
                cardLabel.setBackground(Color.WHITE);
                cardLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                cardLabel.setText("Card " + cardIndex);
                cardLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            }

            cardPanel.add(cardLabel);
        }

        // Refresh the display
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    // Uses Shuffler class to rearrange card display order
    private void shuffleAndDisplay() {
        System.out.println("Shuffle button clicked!");
        displayCards(true); // Reuse displayCards method with shuffle=true
        System.out.println("Shuffle complete!");
    }

}