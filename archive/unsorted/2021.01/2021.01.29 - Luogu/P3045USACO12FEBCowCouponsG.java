package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.CouponProblem;

import java.util.Arrays;
import java.util.PriorityQueue;

public class P3045USACO12FEBCowCouponsG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        long m = in.rl();
        long[] p = new long[n];
        long[] d = new long[n];
        for(int i = 0; i < n; i++){
            p[i] = in.ri();
            d[i] = in.ri();
        }
        int[] ans = new CouponProblem().solve(p, d, k, m);
        int cnt = 0;
        for(int x : ans){
            if(x > 0){
                cnt++;
            }
        }
        out.println(cnt);
    }
}

class Item{
    int p;
    int c;
}