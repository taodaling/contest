package on2020_05.on2020_05_29_Codeforces___Playrix_Codescapes_Cup__Codeforces_Round__413__rated__Div__1___Div__2_.E__Aquarium_decoration2;





import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.LongPreSum;

import java.util.PriorityQueue;

public class EAquariumDecoration {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        int[] cost = new int[n];
        int[] type = new int[n];
        in.populate(cost);
        for (int i = 0; i < 2; i++) {
            int t = in.readInt();
            while (t-- > 0) {
                int j = in.readInt() - 1;
                type[j] |= 1 << i;
            }
        }
        IntegerList[] classify = new IntegerList[4];
        for (int i = 0; i < 4; i++) {
            classify[i] = new IntegerList(n);
        }
        for (int i = 0; i < n; i++) {
            classify[type[i]].add(cost[i]);
        }
        for (int i = 0; i < 4; i++) {
            classify[i].sort();
        }

        LongPreSum[] lps = new LongPreSum[4];
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            lps[i] = new LongPreSum(j -> classify[finalI].get(j), classify[finalI].size());
        }

        long limit = (long) 1e18;
        long ans = limit;
        Machine mac = new Machine(n);
        for (int i = 0; i < classify[0].size(); i++) {
            mac.addFree(classify[0].get(i));
        }
        for (int i = 0; i <= classify[3].size() && i <= m; i++) {
            int req = Math.max(0, k - i);
            if (req > classify[1].size() || req > classify[2].size()) {
                continue;
            }

            while (classify[1].size() > req) {
                mac.addFree(classify[1].pop());
            }
            while (classify[2].size() > req) {
                mac.addFree(classify[2].pop());
            }

            if (m - i - req * 2 >= 0 && m - i - req * 2 <= mac.size()) {
                long local = lps[3].prefix(i - 1) +
                        lps[1].prefix(req - 1) + lps[2].prefix(req - 1) + mac.presum(m - i - req * 2);
                ans = Math.min(ans, local);
            }
        }

        out.println(ans == limit ? -1 : ans);
    }
}

class Machine {
    PriorityQueue<Integer> cur;
    PriorityQueue<Integer> cand;
    long sum = 0;

    public Machine(int n) {
        cand = new PriorityQueue<>(n);
        cur = new PriorityQueue<>(n, (a, b) -> -a.compareTo(b));
    }

    public void addFree(int x) {
        cand.add(x);
    }

    private void add(int x) {
        cur.add(x);
        sum += x;
    }

    private int remove() {
        int ans = cur.remove();
        sum -= ans;
        return ans;
    }

    public int size() {
        return cur.size() + cand.size();
    }

    public long presum(int n) {
        while (cur.size() < n) {
            add(cand.remove());
        }
        while (cur.size() > n) {
            cand.add(remove());
        }
        while (!cur.isEmpty() && !cand.isEmpty() && cur.peek() > cand.peek()) {
            cand.add(remove());
            add(cand.remove());
        }
        return sum;
    }
}