package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

import java.util.Arrays;

public class DTournamentConstruction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        int[] a = new int[m];
        for (int i = 0; i < m; i++) {
            a[i] = in.readInt();
        }
        Arrays.sort(a);
        boolean[][][] dp = new boolean[m + 1][1000][1000];
        int[][][] last = new int[m + 1][1000][1000];
        dp[0][0][0] = true;
        for (int i = 1; i <= m; i++) {
            int v = a[i - 1];
            for (int j = i; j < 1000; j++) {
                for (int k = 0; k < 1000; k++) {
                    //x + v - (j - 1) = k
                    int lastV = k + (j - 1) - v;
                    if (lastV < 0 || lastV >= 1000) {
                        continue;
                    }
                    if (dp[i - 1][j - 1][lastV]) {
                        dp[i][j][k] = true;
                        last[i][j][k] = j - 1;
                    }
                    if (dp[i][j - 1][lastV]) {
                        dp[i][j][k] = true;
                        last[i][j][k] = last[i][j - 1][lastV];
                    }
                }
            }
        }

        int n = -1;
        for (int i = 0; i < 1000; i++) {
            if (dp[m][i][0]) {
                n = i;
                break;
            }
        }

        if (n == -1) {
            out.println("=");
            return;
        }

        IntegerList list = new IntegerList(1000);
        {
            int j = n;
            int k = 0;
            for (int i = m; i >= 1; i--) {
                int v = a[i - 1];
                int l = last[i][j][k];
                while (j > l) {
                    list.add(v);
                    j--;
                    //x + v - (j - 1) =  k
                    k = k - v + j;
                }
            }
        }

        list.sort();
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                mat[i][j] = j < i ? 1 : 0;
            }
        }
        int[] seqA = list.toArray();
        int[] seqB = new int[n];
        for (int i = 0; i < n; i++) {
            seqB[i] = i;
        }

        int index = 0;
        int jIndex = 0;
        while (true) {
            while (index < n && seqA[index] <= seqB[index]) {
                index++;
            }
            if (index >= n) {
                break;
            }

            jIndex = Math.max(jIndex, index + 1);
            while (seqB[jIndex] <= seqA[jIndex]) {
                jIndex++;
            }

            for (int i = 0; i < n && seqB[index] < seqA[index] && seqB[jIndex] > seqA[jIndex]; i++) {
                if (mat[jIndex][i] == 1 && mat[i][index] == 1) {
                    inverse(mat, jIndex, i);
                    inverse(mat, index, i);
                    seqB[jIndex]--;
                    seqB[index]++;
                }
            }
        }


        out.println(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.append(mat[i][j]);
            }
            out.println();
        }
    }

    public void inverse(int[][] mat, int i, int j) {
        int tmp = mat[i][j];
        mat[i][j] = mat[j][i];
        mat[j][i] = tmp;
    }
}
