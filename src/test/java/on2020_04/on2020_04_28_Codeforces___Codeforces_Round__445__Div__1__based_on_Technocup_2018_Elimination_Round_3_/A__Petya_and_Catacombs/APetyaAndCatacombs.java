package on2020_04.on2020_04_28_Codeforces___Codeforces_Round__445__Div__1__based_on_Technocup_2018_Elimination_Round_3_.A__Petya_and_Catacombs;



import template.io.FastInput;
import template.io.FastOutput;

public class APetyaAndCatacombs {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        boolean[] tag = new boolean[n + 1];
        int cost = 0;
        for (int i = 1; i <= n; i++) {
            int x = in.readInt();
            if (tag[x]) {
                tag[x] = false;
                tag[i] = true;
            } else {
                tag[i] = true;
                cost++;
            }
        }
        out.println(cost);
    }
}
