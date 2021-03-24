package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;

import java.util.Arrays;

public class MedianOfMedians {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[][] mat = new int[3][3];
        int[] row = new int[3];
        for(int i = 0; i < 3; i++){
            in.populate(mat[i]);
            Arrays.sort(mat[i]);
            row[i] = mat[i][1];
        }
        Arrays.sort(row);
        out.println(row[1]);
    }
}
