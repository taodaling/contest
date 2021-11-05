package on2021_10.on2021_10_30_AtCoder___AtCoder_Beginner_Contest_223.H___Xor_Query;



import template.datastructure.LinearBasisExpiry;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;

public class HXorQuery {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        long[] a = in.rl(n);
        LinearBasisExpiry lb = new LinearBasisExpiry();
        Query[] qs = new Query[q];
        for (int i = 0; i < q; i++) {
            qs[i] = new Query();
            qs[i].l = in.ri() - 1;
            qs[i].r = in.ri() - 1;
            qs[i].x = in.rl();
        }
        Query[] original = qs.clone();
        Arrays.sort(qs, Comparator.comparingInt(x -> x.r));
        int iter = 0;
        for (int i = 0; i < n; i++) {
            lb.add(a[i], i);
            while (iter < q && qs[iter].r == i) {
                Query query = qs[iter++];
                long set = lb.representationOriginal(query.x);
                if(set == -1 || lb.minExpiry(set) < query.l){
                    query.ans = false;
                }else{
                    query.ans = true;
                }
            }
        }
        for(Query query : original){
            out.println(query.ans ? "Yes" : "No");
        }
    }


    Debug debug = new Debug(false);
}

class Query {
    int l;
    int r;
    long x;
    boolean ans;
}
