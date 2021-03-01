package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongHashSet;
import template.rand.HashData;
import template.rand.ModifiableHash;
import template.utils.SequenceUtils;

public class TeamName {
    HashData[] hd = HashData.doubleHashData(100);
    ModifiableHash mh = new ModifiableHash(hd[0], hd[1], 100);
    LongHashSet set = new LongHashSet((int) 1e5, false);

    public void hash(char[] s, long[] res) {
        mh.init(s.length);
        for (int i = 1; i < s.length; i++) {
            mh.set(i, s[i]);
        }
        for (int i = 'a'; i <= 'z'; i++) {
            mh.set(0, i);
            res[i - 'a'] = mh.hashV();
        }
    }

    int charset = 'z' - 'a' + 1;
    long[][] edges = new long[charset][charset];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[][] s = new char[n][];
        set.clear();
        SequenceUtils.deepFill(edges, 0L);
        long[][] hash = new long[n][charset];
        for (int i = 0; i < n; i++) {
            s[i] = in.rs().toCharArray();
            hash(s[i], hash[i]);
            set.add(hash[i][s[i][0] - 'a']);
        }
        long ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < charset; j++) {
                if (set.contain(hash[i][j])) {
                    continue;
                }
                ans += edges[j][s[i][0] - 'a'];
            }
            for (int j = 0; j < charset; j++) {
                if (set.contain(hash[i][j])) {
                    continue;
                }
                edges[s[i][0] - 'a'][j]++;
            }
        }
        out.println(ans * 2);
    }
}
