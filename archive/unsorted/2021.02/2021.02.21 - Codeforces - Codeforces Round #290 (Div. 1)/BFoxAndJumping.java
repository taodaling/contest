package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.PollardRho;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;

public class BFoxAndJumping {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item();
            items[i].l = in.ri();
        }
        for (int i = 0; i < n; i++) {
            items[i].c = in.ri();
        }
        int inf = (int) 1e9;
        int best = inf;
        for (Item pick : items) {
            IntegerArrayList factor = Factorization.factorizeNumberPrime(pick.l);
            for (Item item : items) {
                item.bit = 0;
            }
            for (int i = 0; i < factor.size(); i++) {
                int x = factor.get(i);
                for (Item item : items) {
                    if (item.l % x != 0) {
                        item.bit |= 1 << i;
                    }
                }
            }
            int m = factor.size();
            int[] dp = new int[1 << m];
            Arrays.fill(dp, inf);
            dp[0] = pick.c;
            for (int i = 0; i < 1 << m; i++) {
                for (Item item : items) {
                    dp[i | item.bit] = Math.min(dp[i | item.bit], dp[i] + item.c);
                }
            }
            best = Math.min(best, dp[dp.length - 1]);
        }
        if(best >= inf){
            out.println(-1);
        }else{
            out.println(best);
        }
    }
}

class Item {
    int l;
    int c;
    int bit;
}
