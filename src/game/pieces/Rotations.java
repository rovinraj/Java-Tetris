package game.pieces;

import java.util.HashMap;
import java.util.Objects;

public class Rotations {
    public final static int[][][] WALLKICK_NORMAL= {
    /*       Case 1   Case 2   Case 3    Case 4   Case 5       */
            // 0->1
            {{0, 0},  {-1, 0}, {-1, 1},  {0, -2}, {-1, -2}},
            // 1->0
            {{0, 0},  {1, 0},  {1, -1},  {0, 2},  {1, 2}},
            // 1->2
            {{0, 0},  {1, 0},  {1, -1},  {0, 2},  {1, 2}},
            // 2->1
            {{0, 0},  {-1, 0}, {-1, 1},  {0, -2}, {-1, -2}},
            // 2->3
            {{0, 0},  {1, 0},  {1, 1},   {0, -2}, {1, -2}},
            // 3->2
            {{0, 0},  {-1, 0}, {-1, -1}, {0, 2},  {-1, 2}},
            // 3->0
            {{0, 0},  {-1, 0}, {-1, -1}, {0, 2},  {-1, 2}},
            // 0->3
            {{0, 0},  {1, 0},  {1, 1},   {0, -2}, {1, -2}},
            // 0->2                                        Case 6   Case 7   Case 8   Case 9   Case 10  Case 11  Case 12
            {{0, 0},  {1, 0},  {2, 0},   {1, 1},  {2, 1},  {-1, 0}, {-2, 0}, {-1, 1}, {-2, 1}, {0,-1},  {3, 0},  {-3, 0}},
            // 1->3
            {{0, 0},  {0, 1},  {0, 2},   {-1, 1}, {-1, 2}, {0,-1},  {0,-2},  {-1,-1}, {-1,-2}, {1, 0},  {0, 3},  {0,-3}},
            // 2->0
            {{0, 0},  {-1, 0}, {-2, 0},  {-1,-1}, {-2,-1}, {1, 0},  {2, 0},  {1,-1},  {2,-1},  {0, 1},  {-3, 0}, {3, 0}},
            // 3->1
            {{0, 0},  {0, 1},  {0, 2},   {1, 1},  {1, 2},  {0,-1},  {0,-2},  {1,-1},  {1,-2},  {-1, 0}, {0, 3},  {0,-3}}
    };

    public final static int[][][] WALLKICK_IPIECE = {
            // 0->1
            {{0, 0},  {-2, 0}, {1, 0}, {-2, -1}, {1, 2}},
            // 1->0
            {{0, 0},  {2, 0}, {-1, 0}, {2, 1}, {-1, -2}},
            // 1->2
            {{0, 0},  {-1, 0}, {2, 0}, {-1, 2}, {2, -1}},
            // 2->1
            {{0, 0},  {1, 0}, {-2, 0}, {1, -2}, {-2, 1}},
            // 2->3
            {{0, 0},  {2, 0}, {-1, 0}, {2, 1}, {-1, -2}},
            // 3->2
            {{0, 0},  {-2, 0}, {1, 0}, {-2, -1}, {1, 2}},
            // 3->0
            {{0, 0},  {1, 0}, {-2, 0}, {1, -2}, {-2, 1}},
            // 0->3
            {{0, 0},  {-1, 0}, {2, 0}, {-1, 2}, {2, -1}},
            // 0->2
            {{-1, 0}, {-2, 0},{1, 0},{2, 0},{0, 1}},
            // 1->3
            {{0, 1},  {0, 2},{0,-1},{0,-2},{-1, 0}},
            // 2->0
            {{1, 0},  {2, 0},{-1, 0},{-2, 0},{0,-1}},
            // 3->1
            {{0, 1},  {0, 2},{0,-1},{0,-2},{1, 0}}
    };

    public static HashMap<RotationKey, int[][]> rotationsMapNormal;
    public static HashMap<RotationKey, int[][]> rotationsMapIPiece;

    public Rotations() {
        rotationsMapNormal = new HashMap<>();
        rotationsMapIPiece = new HashMap<>();
        //init normal map
        for(int i = 0, j = 1, k = 0; i < 4; i++, j++) {
            if (j == 4) j = 0; // 4 rotations = original

            // single rotations
            rotationsMapIPiece.put(new RotationKey(i, j), WALLKICK_IPIECE[k]);
            rotationsMapNormal.put(new RotationKey(i, j), WALLKICK_NORMAL[k++]);

            rotationsMapIPiece.put(new RotationKey(j, i), WALLKICK_IPIECE[k]);
            rotationsMapNormal.put(new RotationKey(j, i), WALLKICK_NORMAL[k++]);


            // double rotations
            rotationsMapIPiece.put(new RotationKey(i, (i + 2) % 4), WALLKICK_IPIECE[i + 8]);
            rotationsMapNormal.put(new RotationKey(i, (i + 2) % 4), WALLKICK_NORMAL[i + 8]);

        }
    }

}

class RotationKey {
    private final int from;
    private final int to;

    public RotationKey(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RotationKey key = (RotationKey) o;
        return from == key.from && to == key.to;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
