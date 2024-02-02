package game.pieces;

import game.Board;
import game.TetrisPanel;

import javax.swing.border.StrokeBorder;
import javax.swing.text.Position;
import java.awt.*;
import java.util.HashMap;

public class OPiece implements Tetramino{
    private Color color;
    private Point pos;
    private int phase, width, height;
    private int[][] matrix;
    HashMap<RotationKey, int[][]> rotationsMapNormal = Rotations.rotationsMapNormal;
    Mino[][] board = TetrisPanel.board.getBoard();
    public OPiece() {
        color = Color.YELLOW;
        phase = 0;
        matrix = new int[][]{{1, 1},
                             {1, 1}};
        width = 2;
        height = 2;
        pos = new Point(4,-height);
    }

    public Point getPos() {
        return pos;
    }

    public void clearPiece() {
        for(int i = 0; i < matrix.length; i++) {
            if(pos.y + i < 0) continue;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) continue;
                board[pos.y + i][pos.x + j].setColor(Color.BLACK);
            }
        }
    }
    @Override
    public void setPos(Point newPos) {
        clearPiece();

        this.pos = new Point(newPos.x, newPos.y);
        for(int i = 0; i < matrix.length; i++) {
            if(pos.y + i < 0) continue;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) continue;
                board[pos.y + i][pos.x + j].setColor(color);
            }
        }
    }

    @Override
    public void setY(int y) {
        if(y < -2 || y > 20 - height)
            return;
        setPos(new Point(pos.x, y));
    }

    @Override
    public void setX(int x) {
        if(x < 0 || x > 10 - width)
            return;
        for(int i = 0; i < matrix.length; i++)
            for(int j = 0; j < matrix[i].length; j++)
                if (pos.y >= 0 && board[pos.y + i][x + j].isOccupied())
                    return;
        setPos(new Point(x, pos.y));
    }

    @Override
    public void rotate(int direction) {

    }
    public int calculateMaxDrop() {
        int maxDrop = board.length; // Maximum rows the piece can drop

        // Calculate how far the piece can drop
        for (int y = matrix.length - 1; y >= 0; y--) { // Iterate from the bottom row of the piece upwards
            for (int x = 0; x < matrix[0].length; x++) { // Iterate over the piece's width
                if (matrix[x][y] == 0) { // Check only parts of the piece (matrix value 1 indicates part of the piece)
                    continue;
                }
                int currentDrop = 0;
                while (true) {
                    int boardY = pos.y + y + currentDrop + 1; // Calculate the Y position on the board for the current part of the piece
                    boardY = boardY > 0 ? boardY : 0;
                    if (boardY >= board.length || board[boardY][pos.x + x].isOccupied()) {
                        // Stop dropping if we've reached the bottom or found a non-empty cell
                        break;
                    }
                    currentDrop++;
                }
                maxDrop = Math.min(maxDrop, currentDrop); // Update maxDrop to the smallest drop distance found
            }
        }
        return maxDrop;
    }
    @Override
    public void place() {

        setY(pos.y + calculateMaxDrop());// Update the piece's Y position
        for(int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0 || pos.y + j < 0)
                    continue;
                board[pos.y + i][pos.x + j].setOccupied(true);
            }
        }
    }

    @Override
    public void updatePos() {
        setPos(pos);

    }

    @Override
    public void drawSmall(Graphics g, int queueNum) {
        g.setColor(color);
        g.fillRect(460, 90 + queueNum * 60, 40, 40);
    }

    @Override
    public void drawGhostPiece(Graphics g) {
        int newY = pos.y + calculateMaxDrop();
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                if(matrix[i][j] == 0)
                    continue;
                g.setColor(color);
                g.drawRect((pos.x + i)*30+100, (newY + j)*30+100, 30, 30);
            }
        }
    }

    @Override
    public boolean checkDownCollision() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    int boardX = pos.x + j;
                    int boardY = pos.y + i + 1;
                    if(boardY < 0) continue;
                    if (boardY >= board.length) {
                        return true;
                    }

                    if (board[boardY][boardX].isOccupied()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
