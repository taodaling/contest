package on2020_05.on2020_05_18_Codeforces___Codeforces_Round__423__Div__1__rated__based_on_VK_Cup_Finals_.C__DNA_Evolution;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;

public class CDNAEvolution {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[100000];
        int n = in.readString(s, 0);
        int[] data = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            data[i] = cast(s[i - 1]);
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 1; j <= 10; j++) {
                for (int k = 0; k < j; k++) {
                    bits[i][j][k] = new IntegerBIT(n);
                }
            }
        }

        for (int i = 1; i <= n; i++) {
            addAffect(i, data[i], 1);
        }

        char[] buf = new char[10];
        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 1) {
                int x = in.readInt();
                int c = cast(in.readChar());
                addAffect(x, data[x], -1);
                data[x] = c;
                addAffect(x, data[x], 1);
            } else {
                int l = in.readInt();
                int r = in.readInt();
                int m = in.readString(buf, 0);

                int cnt = 0;
                for (int j = 0; j < m; j++) {
                    int c = cast(buf[j]);
                    int mod = (l + j) % m;
                    cnt += bits[c][m][mod].query(l, r);
                }
                out.println(cnt);
            }
        }
    }

    IntegerBIT[][][] bits = new IntegerBIT[4][11][10];

    public void addAffect(int index, int c, int add) {
        for (int j = 1; j <= 10; j++) {
            bits[c][j][index % j].update(index, add);
        }
    }

    public int cast(char c) {
        switch (c) {
            case 'A':
                return 0;
            case 'T':
                return 1;
            case 'G':
                return 2;
            case 'C':
                return 3;
        }
        return -1;
    }
}
