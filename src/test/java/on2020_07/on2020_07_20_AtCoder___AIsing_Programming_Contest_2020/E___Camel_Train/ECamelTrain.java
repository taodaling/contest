package on2020_07.on2020_07_20_AtCoder___AIsing_Programming_Contest_2020.E___Camel_Train;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class ECamelTrain {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Camel[] camels = new Camel[n];
        List<Camel> left = new ArrayList<>(n);
        List<Camel> right = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            camels[i] = new Camel();
            camels[i].k = in.readInt();
            camels[i].l = in.readInt();
            camels[i].r = in.readInt();

            if (camels[i].l >= camels[i].r) {
                left.add(camels[i]);
            } else {
                camels[i].k = n - camels[i].k;
                int tmp = camels[i].l;
                camels[i].l = camels[i].r;
                camels[i].r = tmp;
                right.add(camels[i]);
            }
            camels[i].punish = camels[i].l - camels[i].r;
        }

        left.sort((a, b) -> Integer.compare(a.k, b.k));
        right.sort((a, b) -> Integer.compare(a.k, b.k));

        long ans = maxProfit(left) + maxProfit(right);

        out.println(ans);
    }

    public long maxProfit(List<Camel> camels) {
        if (camels.isEmpty()) {
            return 0;
        }
        PriorityQueue<Camel> pq = new PriorityQueue<>(camels.size(), (a, b) -> Integer.compare(a.punish, b.punish));
        long sum = camels.stream().mapToLong(x -> x.l).sum();
        for (Camel c : camels) {
            if (pq.size() < c.k) {
                pq.add(c);
            } else {
                if (!pq.isEmpty() && pq.peek().punish < c.punish) {
                    sum -= pq.remove().punish;
                    pq.add(c);
                } else {
                    sum -= c.punish;
                }
            }
        }
        return sum;
    }
}

class Camel {
    int k;
    int l;
    int r;
    int punish;
}
