package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class ALittleArtem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        char[][] mat = new char[n][m];
        SequenceUtils.deepFill(mat, 'B');
        mat[0][0] = 'W';
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                out.append(mat[i][j]);
            }
            out.println();
        }
    }
}
