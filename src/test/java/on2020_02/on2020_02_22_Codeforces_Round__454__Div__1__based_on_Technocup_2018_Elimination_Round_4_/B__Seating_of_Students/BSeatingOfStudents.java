package on2020_02.on2020_02_22_Codeforces_Round__454__Div__1__based_on_Technocup_2018_Elimination_Round_4_.B__Seating_of_Students;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class BSeatingOfStudents {
    int n;
    int m;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        int[][] ans = new int[n][m];

        if (n <= 3 && m <= 3) {
            int[][][] mat = new int[n][m][];
            List<int[]> vals = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    vals.add(new int[]{i, j});
                }
            }
            if (!bf(mat, vals.toArray(new int[0][]), n - 1, m - 1)) {
                out.println("NO");
                return;
            }
            out.println("YES");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    out.append(ceilId(mat[i][j][0], mat[i][j][1])).append(' ');
                }
                out.println();
            }

            return;
        }

        out.println("YES");
        if(n == 1){
            IntegerList l1 = new IntegerList(m);
            IntegerList l2 = new IntegerList(m);
            for(int i = 0; i < m; i++){
                if(i % 2 == 0){
                    l1.add(ceilId(0, i));
                }else{
                    l2.add(ceilId(0, i));
                }
            }
            l2.addAll(l1);
            for(int i = 0; i < m; i++){
                out.append(l2.get(i)).append(' ');
            }
            return;
        }
        if(m == 1){
            IntegerList l1 = new IntegerList(n);
            IntegerList l2 = new IntegerList(n);
            for(int i = 0; i < n; i++){
                if(i % 2 == 0){
                    l1.add(ceilId(i, 0));
                }else{
                    l2.add(ceilId(i, 0));
                }
            }
            l2.addAll(l1);
            for(int i = 0; i < n; i++){
                out.append(l2.get(i)).println();
            }
            return;
        }



        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans[i][j] = ceilId(i, j);
            }
        }
        if (m > 3) {
            for (int i = 1; i < m; i += 2) {
                vr(ans, i);
            }
            for (int i = 1; i < n; i += 2) {
                rr(ans, i);
                rr(ans, i);
            }
        } else {
            for (int i = 1; i < n; i += 2) {
                rr(ans, i);
            }
            for (int i = 1; i < m; i += 2) {
                vr(ans, i);
                vr(ans, i);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                out.append(ans[i][j]).append(' ');
            }
            out.println();
        }
    }

    int[][] dirs = new int[][]{
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
    };

    public boolean near(int[] a, int[] b) {
        return a[1] == b[1] && Math.abs(a[0] - b[0]) == 1 || a[0] == b[0] && Math.abs(a[1] - b[1]) == 1;
    }

    public boolean bf(int[][][] perm, int[][] vals, int r, int c) {
        if (c < 0) {
            return bf(perm, vals, r - 1, m - 1);
        }
        if (r < 0) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    for (int[] d : dirs) {
                        int nj = j + d[0];
                        int nk = k + d[1];
                        if (nj < 0 || nj >= n || nk < 0 || nk >= m) {
                            continue;
                        }
                        if (near(perm[j][k], perm[nj][nk])) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        for (int j = 0; j < vals.length; j++) {
            if (vals[j] == null) {
                continue;
            }
            perm[r][c] = vals[j];
            vals[j] = null;
            if (bf(perm, vals, r, c - 1)) {
                return true;
            }
            vals[j] = perm[r][c];
        }
        return false;
    }

    public void vr(int[][] mat, int c) {
        int n = mat.length;
        int m = mat[0].length;

        int head = mat[0][c];
        for (int j = 0; j < n - 1; j++) {
            mat[j][c] = mat[j + 1][c];
        }
        mat[n - 1][c] = head;
    }

    public void rr(int[][] mat, int r) {
        int n = mat.length;
        int m = mat[0].length;
        SequenceUtils.rotate(mat[r], 0, m - 1, 1);
    }

    int ceilId(int i, int j) {
        return i * m + j + 1;
    }
}
