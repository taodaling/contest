package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

public class FRandomTournament {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        BitSet[] bs = new BitSet[n];
        for (int i = 0; i < n; i++) {
            bs[i] = new BitSet(n);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int val = in.readInt();
                if (val == 0) {
                    bs[j].set(i);
                } else {
                    bs[i].set(j);
                }
            }
        }

        IntegerDeque dq = new IntegerDequeImpl(n);
        BitSet state = new BitSet(n);
        BitSet xor = new BitSet(n);
        int ans = 0;
        for (int i = 0; i < n; i++) {
            dq.clear();
            state.fill(false);
            state.set(i);
            //state.toString();
            dq.addLast(i);
            while (!dq.isEmpty()) {
                int next = dq.removeFirst();
                xor.copy(state);
                xor.xor(bs[next]);
                for (int j = xor.nextSetBit(0); j < n; j = xor.nextSetBit(j + 1)) {
                    if (state.get(j)) {
                        continue;
                    }
                    dq.addLast(j);
                }
                state.or(bs[next]);
            }
            if (state.size() == n) {
                ans++;
            }
        }

        out.println(ans);
    }
}
