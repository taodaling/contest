package on2021_03.on2021_03_27_Codeforces___Codeforces_Round__228__Div__1_.C__Fox_and_Card_Game;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class CFoxAndCardGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] piles = new int[n][];
        for (int i = 0; i < n; i++) {
            int m = in.ri();
            piles[i] = in.ri(m);
        }
        long first = 0;
        long second = 0;
        IntegerArrayList chance = new IntegerArrayList(n);
        for (int[] p : piles) {
            int l = 0;
            int r = p.length - 1;
            while (l < r) {
                first += p[l];
                second += p[r];
                l++;
                r--;
            }
            if (l == r) {
                chance.add(p[l]);
            }
        }
        chance.sort();
        chance.reverse();
        for (int i = 0; i < chance.size(); i++) {
            if (i % 2 == 0) {
                first += chance.get(i);
            } else {
                second += chance.get(i);
            }
        }
        out.println(first);
        out.println(second);
    }
}
