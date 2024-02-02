package game.pieces;

import game.Board;

import javax.swing.text.Position;
import java.awt.*;

public interface Tetramino {

    void rotate(int direction);
    void updatePos();
    void drawSmall(Graphics g, int queueNum);
    void drawGhostPiece(Graphics g);

    boolean checkDownCollision();
    Point getPos();

    void place();

    void setPos(Point pos);

    void setY(int y);
    void setX(int x);
    int calculateMaxDrop();
}
