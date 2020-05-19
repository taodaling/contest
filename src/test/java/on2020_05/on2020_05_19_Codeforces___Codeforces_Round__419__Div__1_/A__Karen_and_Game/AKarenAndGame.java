package on2020_05.on2020_05_19_Codeforces___Codeforces_Round__419__Div__1_.A__Karen_and_Game;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

public class AKarenAndGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.readInt();
            }
        }
        int[] a = new int[n];
        int[] b = new int[m];

        int inf = (int) 1e9;
        int ans = inf;

        int[] ansA = null;
        int[] ansB = null;
        for (int i = 0; i <= 500; i++) {
            a[0] = i;
            for (int j = 0; j < m; j++) {
                b[j] = mat[0][j] - a[0];
            }
            for (int j = 1; j < n; j++) {
                a[j] = mat[j][0] - b[0];
            }

            boolean valid = true;
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    if (a[j] + b[k] != mat[j][k]) {
                        valid = false;
                    }
                }
            }

            int sum = 0;

            for (int x : a) {
                if (x < 0) {
                    valid = false;
                }
                sum += x;
            }
            for (int x : b) {
                if (x < 0) {
                    valid = false;
                }
                sum += x;
            }

            if (valid && sum < ans) {
                ans = sum;
                ansA = a.clone();
                ansB = b.clone();
            }
        }

        if (ansA == null) {
            out.println(-1);
            return;
        }

        int sum = 0;
        for (int x : ansA) {
            sum += x;
        }
        for (int x : ansB) {
            sum += x;
        }
        out.println(sum);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < ansA[i]; j++) {
                out.append("row ").println(i + 1);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < ansB[i]; j++) {
                out.append("col ").println(i + 1);
            }
        }
    }

}
