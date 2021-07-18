package on2021_07.on2021_07_14_DMOJ___DMOPC__20_Contest_2.P1___Laugh_Graphs;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class P1LaughGraphs {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[n];
        in.rs(s);
        char[][] mat = new char[2 * n + 20][n];
        SequenceUtils.deepFill(mat, '.');
        int row = n + 10;
        int col = 0;
        char[] map = new char[128];
        map['^'] = '/';
        map['v'] = '\\';
        map['>'] = '_';
        for (char c : s) {
            if (c == 'v') {
                row++;
            }
            mat[row][col] = map[c];
            if (c == '^') {
                row--;
            } else if (c == 'v') {
            }
            col++;
        }
        for (int i = 0; i < mat.length; i++) {
            boolean exist = false;
            for (int j = 0; j < n; j++) {
                if (mat[i][j] != '.') {
                    exist = true;
                }
            }
            if (exist) {
                out.println(mat[i]);
            }
        }
    }
}
