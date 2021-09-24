package template.utils;

public class GridUtils {
    public static final int[][] DIR4 = new int[][]{
            {1, 0},
            {0, -1},
            {-1, 0},
            {0, 1}
    };
    public static final int[][] DIR8 = new int[][]{
            {1, 0},
            {1, -1},
            {0, -1},
            {-1, -1},
            {-1, 0},
            {-1, 1},
            {0, 1},
            {1, 1}
    };

    /**
     * 轮廓线DP
     */
    public static final int[][] OUTLINE_TRANSFORM = new int[][]{
            //lr
            {0, 1, 0, 1},
            //lt
            {1, 1, 0, 0},
            //rt
            {1, 0, 0, 1},
            //lb
            {0, 1, 1, 0},
            //rb
            {0, 0, 1, 1},
            //tb
            {1, 0, 1, 0}
    };
}
