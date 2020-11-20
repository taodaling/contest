package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class A2BinaryTableHardVersion {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        debug.debug("t", testNumber);
        int n = in.readInt();
        int m = in.readInt();
        mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readChar() - '0';
            }
        }
        seq = new ArrayList<>(m * n);
        IntegerArrayList list = new IntegerArrayList(6);
        if (n % 2 == 1) {
            for (int i = 0; i < m; i++) {
                if (mat[n - 1][i] == 1) {
                    apply(n - 1, i, n - 2, i, n - 2, i + 1 == m ? i - 1 : i + 1);
                }
            }
            n--;
        }

        if (m % 2 == 1) {
            for (int i = 0; i < n; i++) {
                if (mat[i][m - 1] == 1) {
                    apply(i, m - 1, i, m - 2, i + 1 == n ? i - 1 : i + 1, m - 2);
                }
            }
            m--;
        }

        for (int i = 0; i < n; i += 2) {
            for (int j = 0; j < m; j += 2) {
                int low = i;
                int high = i + 1;
                int left = j;
                int right = j + 1;
                debug.debug("low", low);
                debug.debug("left", left);
                int sum;
                while ((sum = mat[low][left] + mat[low][right] + mat[high][left] + mat[high][right]) > 0) {
                    list.clear();
                    int greedy = sum == 2 ? 0 : 1;
                    for (int a = low; a <= high; a++) {
                        for (int b = left; b <= right; b++) {
                            if (mat[a][b] == greedy && list.size() < 6) {
                                list.add(a);
                                list.add(b);
                            }
                        }
                    }
                    for (int a = low; a <= high; a++) {
                        for (int b = left; b <= right; b++) {
                            if (mat[a][b] != greedy && list.size() < 6) {
                                list.add(a);
                                list.add(b);
                            }
                        }
                    }
                    assert list.size() == 6;
                    apply(list.toArray());
                }
            }
        }

        for(int[] row : mat){
            for(int cell : row){
                assert cell == 0;
            }
        }

        out.println(seq.size());
        for(int[] xy : seq){
            for(int t : xy){
                out.append(t + 1).append(' ');
            }
            out.println();
        }
    }

    int[][] mat;
    List<int[]> seq;

    public void apply(int[] xy) {
        seq.add(xy);
        for (int i = 0; i < 6; i += 2) {
            mat[xy[i]][xy[i + 1]] ^= 1;
        }
    }

    public void apply(int x1, int y1, int x2, int y2, int x3, int y3) {
        apply(new int[]{x1, y1, x2, y2, x3, y3});
    }
}
