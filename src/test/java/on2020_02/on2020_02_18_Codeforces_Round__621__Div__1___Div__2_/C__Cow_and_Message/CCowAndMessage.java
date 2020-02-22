package on2020_02.on2020_02_18_Codeforces_Round__621__Div__1___Div__2_.C__Cow_and_Message;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;

public class CCowAndMessage {
    int chars = 'z' - 'a' + 1;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] s = new int[(int) 1e5];
        int n = in.readString(s, 0);
        for (int i = 0; i < n; i++) {
            s[i] -= 'a';
        }
        int[][] translate = new int[chars][n];
        for (int i = 0; i < n; i++) {
            translate[s[i]][i] = 1;
        }
        IntegerPreSum[] ps = new IntegerPreSum[chars];
        for (int i = 0; i < chars; i++) {
            ps[i] = new IntegerPreSum(translate[i]);
        }
        long ans = 0;
        for (int i = 0; i < chars; i++) {
            ans = Math.max(ans, ps[i].prefix(n));
        }
        for (int i = 0; i < chars; i++) {
            for (int j = 0; j < chars; j++) {
                long local = 0;
                for (int k = 0; k < n; k++) {
                    local += translate[i][k] * ps[j].post(k + 1);
                }
                ans = Math.max(local, ans);
            }
        }

        out.println(ans);
    }
}
