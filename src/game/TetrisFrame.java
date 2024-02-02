package game;

import game.pieces.Rotations;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class TetrisFrame extends JFrame {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new TetrisFrame().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TetrisFrame() {
        this.setTitle("Tetris");
        ImageIcon icon = new ImageIcon("src/images/brain.png");
        setIconImage(icon.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        Rotations hashmapsetup = new Rotations();
        setContentPane(new TetrisPanel());
        pack();
        this.setLocationRelativeTo(null);
    }

}
