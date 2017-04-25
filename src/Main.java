/*Quelle:
 * https://www.tutorials.de/threads/java-memory-spiel.155182/
 * Thomas Darimont
 */


import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
 
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
 
public class Main extends JFrame {
 
    private ImageIcon backIcon;
 
    private CardGame game;
 
    public Main() {
        super("Main");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container c = getContentPane();
        game = new CardGame();
        //backIcon = new ImageIcon(game.loadImage("e:/imgs/back.jpg"));
        game.init();
 
        CardPanel cp = new CardPanel();
        cp.populate(game.getCards());
 
        c.add(cp);
 
        pack();
        setVisible(true);
    }
 
    public static void main(String[] args) {
        new Main();
    }
 
    class Card extends JLabel {
 
        private ImageIcon icon;
 
        protected int id;
 
        protected boolean stillInGame = true;
 
        public Card(ImageIcon icon, int id) {
            this.icon = icon;
            this.id = id;
            setIcon(backIcon);
            setBorder(BorderFactory.createEtchedBorder());
 
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent evt) {
                    game.selectCard(Card.this);
                    game.checkCards();
                }
            });
 
        }
 
        public void showFront() {
            setIcon(this.icon);
        }
 
        public void showBack() {
            setIcon(backIcon);
        }
    }
 
    class CardPanel extends JPanel {
 
        final int MAX_ROWS = 4;
 
        final int MAX_COLUMNS = 4;
 
        public CardPanel() {
            setLayout(new GridLayout(MAX_ROWS, MAX_COLUMNS));
        }
 
        public void populate(List list) {
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                add((Card) iter.next());
            }
        }
    }
 
    class CardGame {
        private List cards;
 
        private int selectionCount = 0;
 
        private Card current;
 
        private Card previous;
 
        public CardGame() {
            cards = new ArrayList();
        }
 
        public void init() {
            cards.clear();
            loadCards();
            Collections.shuffle(cards);
        }
 
        public Image loadImage(String imgPath) {
 
            final int MAX_WIDTH = 60;
            final int MAX_HEIGHT = 120;
            try {
            	System.out.println(imgPath);
                Image img = ImageIO.read(new File(imgPath)).getScaledInstance(
                        MAX_WIDTH, MAX_HEIGHT, BufferedImage.SCALE_SMOOTH);
                return img;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
 
        private void loadCards() {
 
            for (int i = 0; i < 8; i++) {
                ImageIcon icon = new ImageIcon(loadImage("c:/imgs/" + i
                        + ".png"));
 
                Card c = new Card(icon, i);
                cards.add(c);
                c = new Card(icon, i);
                cards.add(c);
            }
        }
 
        public void selectCard(Card c) {
        	
            if (current == c)
                return;
            if (!c.stillInGame)
                return;
            c.showFront();
            if (selectionCount++ % 2 == 0) {
                if (current != null && previous != null) {
                    current.showBack();
                    previous.showBack();
                }
                current = null;
                previous = null;
            }
 
            previous = current;
            current = c;
        }
 
        public void checkCards() {
            if (current != null && previous != null)
                if (current.id == previous.id) {
                    current.stillInGame = false;
                    previous.stillInGame = false;
                    current = null;
                    previous = null;
                    System.out.println("match");
                }
        }
 
        public List getCards() {
            return cards;
        }
    }
}