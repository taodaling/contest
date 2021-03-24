package on2021_03.on2021_03_17_Codeforces___Codeforces_Round__254__Div__1_.D__DZY_Loves_Strings;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongObjectHashMap;
import template.rand.HashData;
import template.rand.Hasher;
import template.rand.RollingHash;
import template.rand.SparseMultiSetHasher;

import java.util.HashMap;
import java.util.Map;

public class DDZYLovesStrings {
    HashData[] hds = HashData.doubleHashData(10);
    RollingHash rh = new RollingHash(hds[0], hds[1], 10);

    public long hashV(String s) {
        rh.clear();
        for (char c : s.toCharArray()) {
            rh.addLast(c);
        }
        return rh.hashV();
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = new char[(int) 1e5];
        int n = in.rs(s);
        LongObjectHashMap<IntegerArrayList> map = new LongObjectHashMap<>(n * 4, false);

        for (int i = 1; i <= 4; i++) {
            rh.clear();
            for (int j = 0; j < n; j++) {
                rh.addLast(s[j]);
                if (rh.size() > i) {
                    rh.removeFirst();
                }
                if (rh.size() == i) {
                    long h = rh.hashV();
                    IntegerArrayList list = map.get(h);
                    if (list == null) {
                        list = new IntegerArrayList();
                        map.put(h, list);
                    }
                    list.add(j - i + 1);
                }
            }
        }

        int q = in.ri();
        LongHashMap cache = new LongHashMap(q, false);
        int inf = (int)1e9;
        for (int i = 0; i < q; i++) {
            String a = in.rs();
            String b = in.rs();
            if (a.length() < b.length()) {
                String tmp = a;
                a = b;
                b = tmp;
            }
            if (a.contains(b)) {
                if (map.containKey(hashV(a))) {
                    out.println(a.length());
                } else {
                    out.println(-1);
                }
                continue;
            }
            long ha = hashV(a);
            long hb = hashV(b);
            long merged = ha * 31 + hb;
            if (cache.containKey(merged)) {
                out.println(cache.get(merged));
                continue;
            }
            IntegerArrayList la = map.get(ha);
            IntegerArrayList lb = map.get(hb);
            long ans = inf;
            if (la == null || lb == null) {

            } else {
                if (la.size() > lb.size()) {
                    {
                        IntegerArrayList tmp = la;
                        la = lb;
                        lb = tmp;
                    }
                    {
                        String tmp = a;
                        a = b;
                        b = tmp;
                    }
                }

                for (int j = 0; j < la.size(); j++) {
                    int index = la.get(j);
                    int ceil = lb.upperBound(index);
                    if (ceil < lb.size()) {
                        int cand = lb.get(ceil) + b.length() - 1 -
                                index + 1;
                        ans = Math.min(ans, cand);
                    }
                    ceil--;
                    if(ceil >= 0){
                        int cand = index + a.length() - 1 -
                                lb.get(ceil) + 1;
                        ans = Math.min(ans, cand);
                    }
                }
            }

            if(ans == inf){
                ans = -1;
            }
            out.println(ans);
            cache.put(merged, ans);
        }


    }
}
