package game;

import game.pieces.Mino;

import java.awt.*;

public class Board {
    public Mino[][] board;

    public Board() {
        board = new Mino[20][10];
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                board[i][j] = new Mino(Color.BLACK, new Point(j, i));
            }
        }
    }

    public Mino[][] getBoard() {
        return board;
    }


    public void draw(Graphics g) {

        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                board[i][j].draw(g);
            }
        }
    }

    public void printBoard() {
        System.out.println("    1 2 3 4 5 6 7 8 9 10");
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(j == 0)
                    System.out.printf("%02d: ", (i + 1));
                System.out.print(board[i][j].isOccupied() ? "1 " : "0 ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
