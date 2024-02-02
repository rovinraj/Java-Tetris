package game.pieces;

import java.awt.*;

public class Mino {
    Color color;
    Point pos;
    boolean occupied;
    public Mino(Color color, Point pos) {
        this.color = color;
        this.pos = pos;
        occupied = false;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(pos.x*30+100, pos.y*30+100, 30, 30);
        g.setColor(new Color(80, 80, 80));
        g.drawRect(pos.x*30+100, pos.y*30+100, 30, 30);
    }

}
