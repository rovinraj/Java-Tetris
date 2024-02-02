package game.pieces;

import game.Board;
import game.TetrisPanel;

import javax.swing.border.StrokeBorder;
import javax.swing.text.Position;
import java.awt.*;
import java.util.HashMap;

public class TPiece implements Tetramino{
    private Color color;
    private Point pos;
    private int phase, width, height;
    private int[][] matrix;
    HashMap<RotationKey, int[][]> rotationsMapNormal = Rotations.rotationsMapNormal;
    Mino[][] board = TetrisPanel.board.getBoard();
    public TPiece() {
        color = Color.MAGENTA;
        phase = 0;
        matrix = new int[][]{{0, 1, 0},
                             {1, 1, 1},
                             {0, 0, 0}};
        width = 3;
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
        if(y < -2 || y > board.length - height)
            return;
        setPos(new Point(pos.x, y));
    }

    @Override
    public void setX(int x) {
        if(x < 0 || x > 10 - width)
            return;
        for(int i = 0; i < matrix.length; i++)
            for(int j = 0; j < matrix[i].length; j++) {
                if(matrix[i][j] == 0) continue;
                if (pos.y >= 0 && board[pos.y + i][x + j].isOccupied())
                    return;
            }
        setPos(new Point(x, pos.y));
    }

    @Override
    public void rotate(int direction) {
        clearPiece();
        int N = matrix.length;

        // Rotate the matrix 'direction' times
        for (int r = 0; r < direction; r++) {
            // Transpose the matrix
            for (int i = 0; i < N; i++) {
                for (int j = i; j < N; j++) {
                    int temp = matrix[i][j];
                    matrix[i][j] = matrix[j][i];
                    matrix[j][i] = temp;
                }
            }

            // Reverse each row to complete the rotation
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N / 2; j++) {
                    int temp = matrix[i][j];
                    matrix[i][j] = matrix[i][N - 1 - j];
                    matrix[i][N - 1 - j] = temp;
                }
            }
        }

        // Recalculate the width and height after rotation
        int firstRow = N, lastRow = -1, firstCol = N, lastCol = -1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (matrix[i][j] == 1) {
                    if (i < firstRow) firstRow = i;
                    if (i > lastRow) lastRow = i;
                    if (j < firstCol) firstCol = j;
                    if (j > lastCol) lastCol = j;
                }
            }
        }

        // Update width and height based on the occupied cells
        width = lastCol - firstCol + 1;
        height = lastRow - firstRow + 1;
    }

    public int calculateMaxDrop() {
        int maxDrop = Integer.MAX_VALUE; // Start with the maximum possible drop

        for (int x = 0; x < matrix[0].length; x++) {
            int columnDrop = 0; // Drop distance for the current column
            boolean columnHasBlock = false; // Does this column have a block in it?

            for (int y = matrix.length - 1; y >= 0; y--) { // Start from the bottom of the piece
                if (matrix[y][x] != 0) {
                    columnHasBlock = true; // Found a block in this column
                    int boardY = pos.y + y + columnDrop; // Position on the board where the block would be
                    boardY = boardY > 0 ? boardY : 0;
                    // Check drop distance for this column
                    while (boardY < board.length && !board[boardY][pos.x + x].isOccupied()) {
                        columnDrop++;
                        boardY = pos.y + y + columnDrop; // Update boardY for the next iteration
                    }
                    break; // Only need to check until the first (bottom-most) block in the column
                }
            }

            if (columnHasBlock) {
                // Update the maxDrop based on the lowest drop distance found among all columns
                maxDrop = Math.min(maxDrop, columnDrop - 1); // Subtract 1 because the loop increments columnDrop one extra time
            }
        }

        return maxDrop == Integer.MAX_VALUE ? 0 : maxDrop; // If maxDrop wasn't updated, no blocks could move down
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
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                if(matrix[i][j] == 0)
                    continue;
                g.setColor(color);
                g.fillRect((pos.x + j)*20+372, 130 + queueNum * 60 + (pos.y + i)*20, 20, 20);
            }
        }
    }

    @Override
    public void drawGhostPiece(Graphics g) {
        int newY = pos.y + calculateMaxDrop();
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                if(matrix[i][j] == 0)
                    continue;
                g.setColor(color);
                g.drawRect((pos.x + j)*30+100, (newY + i)*30+100, 30, 30);
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
