package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;

public class FriendsOfFriends {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int x = in.ri() - 1;
        BitSet[] adj = new BitSet[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new BitSet(n);
            int m = in.ri();
            for (int j = 0; j < m; j++) {
                adj[i].set(in.ri() - 1);
            }
        }
        BitSet merge = new BitSet(n);
        merge.copy(adj[x]);
        for (int i = 0; i < n; i++) {
            if (adj[x].get(i)) {
                merge.or(adj[i]);
            }
        }
        merge.xor(adj[x]);
        merge.clear(x);
        out.println(merge.size());
        for (int i = merge.nextSetBit(0); i < merge.capacity(); i = merge.nextSetBit(i + 1)) {
            out.println(i + 1);
        }
    }
}
