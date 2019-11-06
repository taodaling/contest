package contest;

import template.*;

import java.util.*;

public class TaskC {

    SubsetGenerator sg = new SubsetGenerator();
    Node[] nodes;
    int n;
    Map<Long, Node> map;
    long notExist;
    long[] mask2Key;
    Map<Long, LongList> sequence;
    DigitUtils.BitOperator bo = new DigitUtils.BitOperator();
    boolean[] dp;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        map = new LinkedHashMap<>(200000);
        for (int i = 0; i < n; i++) {
            int k = in.readInt();
            for (int j = 0; j < k; j++) {
                long x = in.readInt();
                map.put(x, nodes[i]);
                nodes[i].sum += x;
            }
        }

        long total = 0;
        for (Node node : nodes) {
            total += node.sum;
        }

        if (total % n != 0) {
            out.println("No");
            return;
        }
        long avg = total / n;

        notExist = (long) 1e18;
        mask2Key = new long[1 << n];
        Arrays.fill(mask2Key, notExist);
        sequence = new HashMap<>(200000);


        for (Map.Entry<Long, Node> kv : map.entrySet()) {
            for (Node node : nodes) {
                node.handled = false;
            }

            long key = kv.getKey();

            Node node = kv.getValue();
            node.handled = true;
            int mask = bo.setBit(0, node.id, true);

            LongList list = new LongList(15);
            list.add(key);

            long req = avg - (node.sum - key);
            boolean valid = true;

            while (true) {
                if (req == key) {
                    break;
                }
                Node next = map.get(req);
                if (next == null || next.handled) {
                    valid = false;
                    break;
                }
                next.handled = true;
                list.add(req);
                req = avg - (next.sum - req);
                mask = bo.setBit(mask, next.id, true);
            }

            if (!valid) {
                continue;
            }

            mask2Key[mask] = key;
            sequence.put(key, list);
        }


        dp = new boolean[1 << n];
        for (int i = 0; i < (1 << n); i++) {
            dp[i] = mask2Key[i] != notExist;
            sg.setSet(i);
            while (!dp[i] && sg.hasNext()) {
                int next = sg.next();
                if (next == 0 || next == i) {
                    continue;
                }
                dp[i] = dp[i] || (dp[next] && dp[i - next]);
            }
        }

        if (!dp[(1 << n) - 1]) {
            out.println("No");
            return;
        }

        populate((1 << n) - 1);
        out.println("Yes");
        for (Node node : nodes) {
            out.append(node.out).append(' ').append(node.to + 1).append('\n');
        }
    }

    public void populate(int mask) {
        if (mask2Key[mask] != notExist) {
            LongList list = sequence.get(mask2Key[mask]);
            int m = list.size();
            for (int i = 0; i < m; i++) {
                long v = list.get(i);
                long last = list.get(DigitUtils.mod(i - 1, m));
                Node which = map.get(v);
                Node to = map.get(last);

                which.out = v;
                which.to = to.id;
            }
            return;
        }

        sg.setSet(mask);
        while (sg.hasNext()) {
            int next = sg.next();
            if (next == 0 || next == mask) {
                continue;
            }
            if (dp[next] && dp[mask - next]) {
                populate(next);
                populate(mask - next);
                return;
            }
        }
    }

}

class Node {
    int id;
    long sum;
    boolean handled;

    long out;
    long to;
}
