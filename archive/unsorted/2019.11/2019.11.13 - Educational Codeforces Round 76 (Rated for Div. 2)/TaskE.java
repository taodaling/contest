package contest;

import template.FastInput;
import template.FastOutput;
import template.Randomized;

import java.util.Arrays;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int c = in.readInt();
        int[] as = new int[a];
        int[] bs = new int[b];
        int[] cs = new int[c];
        for(int i = 0; i < a; i++){
            as[i] = in.readInt();
        }
        for(int i = 0; i < b; i++){
            bs[i] = in.readInt();
        }
        for(int i = 0; i < c; i++){
            cs[i] = in.readInt();
        }


        Randomized.randomizedArray(as, 0, a);
        Randomized.randomizedArray(bs, 0, b);
        Randomized.randomizedArray(cs, 0, c);
        Arrays.sort(as);
        Arrays.sort(bs);
        Arrays.sort(cs);

        int n = a + b + c;
        int[] ab = new int[n + 1];

        int la = 0;
        int lb = 0;
        int cost = a;

        for(int i = 1; i <= n; i++){
            while(la < a && as[la] < i){
                la++;
                cost--;
            }
            while(lb < b && bs[lb] < i){
                lb++;
                cost++;
            }
            ab[i] = cost;
        }

        int[] bc = new int[n + 1];
        int rb = b - 1;
        int rc = c - 1;
        cost = c;

        for(int i = n; i >= 1; i--){
            while(rb >= 0 && bs[rb] > i){
                rb--;
                cost++;
            }
            while(rc >= 0 && cs[rc] > i){
                rc--;
                cost--;
            }
            bc[i] = cost;
        }

        int[] sufMinBC = new int[n + 1];
        sufMinBC[n] = bc[n];
        for(int i = n - 1; i >= 1; i--){
            sufMinBC[i] = Math.min(sufMinBC[i + 1], bc[i]);
        }

        int ans = n;
        ans = Math.min(ans, b + c);
        for(int i = 1; i <= n; i++){
            if(i > 1) {
                ans = Math.min(ans, ab[i] + sufMinBC[i - 1]);
            }else{
                ans = Math.min(ans, ab[1] + b);
                ans = Math.min(ans, ab[1] + sufMinBC[1]);
            }
        }

        out.println(ans);
    }
}
