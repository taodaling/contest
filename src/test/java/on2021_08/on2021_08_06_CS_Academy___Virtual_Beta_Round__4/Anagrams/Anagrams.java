package on2021_08.on2021_08_06_CS_Academy___Virtual_Beta_Round__4.Anagrams;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.DenseMultiSetHasher;
import template.rand.MultiSetHasher;

import java.util.HashMap;
import java.util.Map;

public class Anagrams {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[(int)1e5];
        MultiSetHasher msh  = new DenseMultiSetHasher('a', 'z');
        Map<Long, Integer> map = new HashMap<>(n);
        for(int i = 0; i < n; i++){
            int m = in.rs(s);
            long hash = 0;
            for(int j = 0; j < m; j++){
                hash = msh.merge(hash, msh.hash(s[j]));
            }
            map.put(hash, map.getOrDefault(hash, 0) + 1);
        }
        long max = map.values().stream().mapToLong(Integer::longValue).max().orElse(-1);
        out.println(max);
    }
}
