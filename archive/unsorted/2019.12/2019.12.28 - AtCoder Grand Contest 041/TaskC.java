package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[][] mat = new char[n][n];
        SequenceUtils.deepFill(mat, '.');
        if (n <= 2) {
            out.println(-1);
            return;
        }
        char[][] ans = draw(n);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                out.append(ans[i][j]);
            }
            out.println();
        }
    }

    public char[][] duplicate(char[][] template, int n) {
        int m = template.length;
        char[][] ans = new char[n][n];
        for (int i = 0; i < n; i += m) {
            for (int j = 0; j < n; j += m) {
                draw(ans, template, i, j);
            }
        }
        return ans;
    }

    public void draw(char[][] data, char[][] temp, int i, int j) {
        int m = temp.length;
        for (int a = 0; a < m; a++) {
            for (int b = 0; b < m; b++) {
                data[a + i][b + j] = temp[a][b];
            }
        }
    }

    char[][] temp4 = new char[][]{
            {'o', 'o', 'q', 'r'},
            {'p', 'p', 'q', 'r'},
            {'s', 't', 'u', 'u'},
            {'s', 't', 'v', 'v'},
    };
    char[][] temp31 = new char[][]{
            {'e', 'e', '.'},
            {'.', '.', 'f'},
            {'.', '.', 'f'},
    };
    char[][] temp32 = new char[][]{
            {'g', 'g', 'h'},
            {'j', '.', 'h'},
            {'j', 'i', 'i'},
    };

    public char[][] draw(int n) {
        if (n % 4 == 0) {
            return duplicate(temp4, n);
        }
        if (n % 2 == 0) {
            int m = n;
            while (m % 2 == 0) {
                m /= 2;
            }
            return duplicate(draw(m), n);
        }
        if (n % 3 == 0) {
            return duplicate(temp31, n);
        }
        if (n % 3 == 1) {
            char[][] ans = new char[n][n];
            SequenceUtils.deepFill(ans, '.');
            char last = 'k';
            for (int i = 0; i + 1 < n; i += 2) {
                ans[i][n - 1] = ans[i + 1][n - 1] = ans[n - 1][i] = ans[n - 1][i + 1] = last;
                last = last == 'k' ? 'l' : 'k';
            }
            draw(ans, drawSpecial(n - 1, (n - 1) / 2 - 1), 0, 0);
            return ans;
        }


        char[][] ans = new char[n][n];
        SequenceUtils.deepFill(ans, '.');
        char lastA = 'k';
        char lastB = 'm';
        char lastC = 'a';
        char lastD = 'c';
        for (int i = 0; i + 1 < n; i += 2) {
            ans[0][i] = ans[0][i + 1] = lastC;
            ans[n - 1][i + 1] = ans[n - 1][i + 2] = lastA;
            ans[i + 1][0] = ans[i + 2][0] = lastD;
            ans[i][n - 1] = ans[i + 1][n - 1] = lastB;
            lastA = lastA == 'k' ? 'l' : 'k';
            lastB = lastB == 'm' ? 'n' : 'm';
            lastC = lastC == 'a' ? 'b' : 'a';
            lastD = lastD == 'c' ? 'd' : 'c';
        }
        draw(ans, drawSpecial(n - 2, (n + 1) / 2 - 2), 1, 1);
        return ans;
    }

    public char[][] drawSpecial(int n, int k) {
        char[][] ans = duplicate(temp31, n);
        int m = n / 3;
        k -= m;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < k; j++) {
                draw(ans, temp32, i * 3, ((i + j) % m) * 3);
            }
        }
        return ans;
    }
}
