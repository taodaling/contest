package on2021_10.on2021_10_13_TopCoder_SRM__815.MaximizingLCM;



import template.datastructure.BitSet;
import template.math.GCDs;
import template.math.LCMs;
import template.math.LongMillerRabin;
import template.primitve.generated.datastructure.LongArrayList;

public class MaximizingLCM {
    public long maximize(int N, long M) {
        if (N >= M) {
            long ans = 1;
            for (int i = 1; i <= M; i++) {
                ans = LCMs.lcm(ans, i);
            }
            return ans;
        }
        LongArrayList candList = new LongArrayList();
        int prime = 0;
        for (long i = M; i >= 1 && prime < N; i--) {
            candList.add(i);
            if (LongMillerRabin.mr(i, 10)) {
                prime++;
            }
        }

        cand = candList.toArray();


        if (prime < N) {
            LongArrayList filteredCand = new LongArrayList();
            for (long c : cand) {
                boolean ok = true;
                for (long t : cand) {
                    if (t > c && t % c == 0) {
                        ok = false;
                    }
                }
                if (ok) {
                    filteredCand.add(c);
                }
            }
            cand = filteredCand.toArray();
            return bf(N, 0, 1);
        }

        int k = cand.length;
        allow = new BitSet[N + 1];
        conflict = new BitSet[k];
        for (int i = 0; i <= N; i++) {
            allow[i] = new BitSet(k);
        }
        tmp = new BitSet(k);
        for (int i = 0; i < k; i++) {
            conflict[i] = new BitSet(k);
            conflict[i].fill(true);
            for (int j = 0; j < k; j++) {
                if (GCDs.gcd(cand[i], cand[j]) != 1) {
                    conflict[i].clear(j);
                }
            }
        }
        allow[N].fill(true);
        long ans = smart(N, 0, 1);
//        long bfRes = bf(N, 0, 1);
//        if(bfRes != ans){
//            throw new IllegalStateException();
//        }
        return ans;
    }

    long[] cand;
    BitSet[] allow;
    BitSet[] conflict;
    BitSet tmp;

    LongArrayList path = new LongArrayList(100);

    public long bf(int remain, int startIndex, long val) {
        if (remain == 0 || startIndex == cand.length) {
//           if(val == 999982000106999790L){
//               System.err.println(path);
//           }
            return val;
        }
        long ans = bf(remain, startIndex + 1, val);
//        path.add(cand[startIndex]);
        ans = Math.max(ans,
                bf(remain - 1, startIndex + 1, LCMs.lcm(val, cand[startIndex])));
//        path.pop();
        return ans;
    }


    public long smart(int remain, int startIndex, long val) {
        long ans = val;
        if (remain == 0) {
            return ans;
        }
        for (int bit = allow[remain].nextSetBit(startIndex); bit < allow[remain].capacity(); bit = allow[remain].nextSetBit(bit + 1)) {
            allow[remain - 1].copy(allow[remain]);
            allow[remain - 1].and(conflict[bit]);
            long best = smart(remain - 1, bit + 1, val * cand[bit]);
            ans = Math.max(ans, best);
        }
        return ans;
    }
}


