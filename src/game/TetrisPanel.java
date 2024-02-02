package game;

import game.pieces.Mino;
import game.pieces.OPiece;
import game.pieces.TPiece;
import game.pieces.Tetramino;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.*;

public class TetrisPanel extends JPanel implements KeyListener, ActionListener
{
    private final int WIDTH = 550, HEIGHT = 750;
    int pieceNum, speed, timePassed, delay, sdf, arr, das, dcd;
    public static Board board;
    Tetramino[] queue, nextQueue;
    Timer timer, softDropTimer, rightMoveTimer, leftMoveTimer;
    private boolean isSoftDropping;
    Random rand;
    public TetrisPanel()
    {
        // Sets panel settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(new Color(40, 40, 40));
        this.setLayout(null);
        this.setFocusable(true);
        addKeyListener(this);

        isSoftDropping = false;
        board = new Board();
        timePassed = 0;
        pieceNum = 6;
        speed = 1;
        delay = 100;
        sdf = 1;
        arr = 50;
        das = 25;
        dcd = 1;
        timer = new Timer(delay, this);
        rand = new Random();
        startGame();
        timer.start();

        softDropTimer = new Timer(1, e -> {
            if (!isSoftDropping) {
                timer.setDelay(sdf);
                isSoftDropping = true;
            }
        });
        softDropTimer.setRepeats(false);

        leftMoveTimer = new Timer(arr, e -> queue[pieceNum].setX(queue[pieceNum].getPos().x - 1));
        leftMoveTimer.setInitialDelay(das);
        leftMoveTimer.setRepeats(true);

        rightMoveTimer = new Timer(arr, e -> queue[pieceNum].setX(queue[pieceNum].getPos().x + 1));
        rightMoveTimer.setInitialDelay(das);
        rightMoveTimer.setRepeats(true);

    }

    private void startGame() {
        queue = makeQueue();
        nextQueue = makeQueue();
    }
    private Tetramino[] makeQueue() {
        Tetramino[] queue = new Tetramino[7];
        Set<Integer> used = new HashSet<>();
        int r = rand.nextInt(7);
        for (int i = 0; i < 7; i++) {
            while (!used.add(r)) {
                r = rand.nextInt(7);
            }
            switch (r) {
                case 0:
//                    queue[i] = new IPiece();
//                    break;
                case 1:
//                    queue[i] = new JPiece();
//                    break;
                case 2:
                    queue[i] = new OPiece();
                    break;
                case 3:
//                    queue[i] = new SPiece();
//                    break;
                case 4:
//                    queue[i] = new OPiece();
//                    break;
                case 5:
//                    queue[i] = new ZPiece();
//                    break;
                case 6:
                    queue[i] = new TPiece();
                    break;
                default:
                    System.err.println("Error Setting up Queue");
            }
        }
        return queue;
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawScreen(g);
        drawPiece(g);
        checkLineClear();
    }

    private void drawScreen(Graphics g) {
        // Draw Grid
        board.draw(g);

        // Draw Queue
        g.setColor(Color.BLACK);
        g.fillRect(430, 80, 100, 360);
        for(int queueNum = 0; queueNum < 6 - pieceNum; queueNum++)
        {
            queue[pieceNum + queueNum + 1].drawSmall(g, queueNum);
        }
        for(int i = 0, queueNum = 6 - pieceNum; queueNum < 6; queueNum++)
        {
            nextQueue[i++].drawSmall(g, queueNum);
        }
    }

    private void drawPiece(Graphics g) {
        Tetramino piece = queue[pieceNum];

        piece.drawGhostPiece(g);
        if(timePassed > speed)
        {
            if(piece.checkDownCollision())
            {
                piece.place();
                pieceNum++;
                if(pieceNum == 7) newQueue();
            }
            else {
                piece.setY(piece.getPos().y + 1);
            }
            timePassed = 0;
        }
    }

    private void checkLineClear() {
        int rowsCleared = 0;
        boolean fullRow = true;
        for (int row = board.board.length - 1; row >= 0; row--) {
            for (int col = 0; col < board.board[row].length; col++) {
                if (!board.board[row][col].isOccupied()) {
                    fullRow = false;
                    break;
                }
            }
            if(fullRow) {
                clearRow(row);
                rowsCleared++;
                row++;
            }
        }

    }

    private void clearRow(int rowToClear) {
        for (int row = rowToClear; row > 0; row--) {
            for (int col = 0; col < board.board[row].length; col++) {
                board.board[row][col].setColor(board.board[row - 1][col].getColor());
                board.board[row][col].setOccupied(board.board[row - 1][col].isOccupied());
            }
        }

        for (int col = 0; col < board.board[0].length; col++) {
            board.board[0][col].setColor(Color.black);
            board.board[0][col].setOccupied(false);
        }
    }

    private void newQueue() {
        pieceNum = 0;
        queue = nextQueue;
        nextQueue = makeQueue();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timePassed++;
        validate();
        repaint();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                if (!isSoftDropping) {
                    softDropTimer.restart();
                }
                break;
            case KeyEvent.VK_LEFT:
                leftMoveTimer.start();
                break;
            case KeyEvent.VK_RIGHT:
                rightMoveTimer.start();
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        Tetramino piece = queue[pieceNum];
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_LEFT:
                leftMoveTimer.stop();
                break;
            case KeyEvent.VK_RIGHT:
                rightMoveTimer.stop();
                break;
            case KeyEvent.VK_X, KeyEvent.VK_UP:
                piece.rotate(1);
                break;
            case KeyEvent.VK_Z:
                piece.rotate(3);
                break;
            case KeyEvent.VK_A:
                piece.rotate(2);
                break;
            case KeyEvent.VK_SPACE:
                piece.place();
                repaint();
                pieceNum++;
                if(pieceNum == 7)
                    newQueue();
                break;
            case KeyEvent.VK_DOWN:
                if (isSoftDropping) {
                    timer.setDelay(delay);
                    isSoftDropping = false;
                }
                softDropTimer.stop();
                break;
        }
    }
}

