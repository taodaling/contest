package contest;

import template.graph.DirectedEdge;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntRadix;
import template.math.Permutation;
import template.math.PermutationUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class SwapGame {
    IntRadix radix = new IntRadix(10);

    public int calc(int[] x) {
        int ans = 0;
        for (int i = x.length - 1; i >= 0; i--) {
            ans = ans * 10 + x[i];
        }
        return ans;
    }

    public int getId(int r, int c) {
        return r * 3 + c;
    }

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] all = IntStream.range(1, 10).toArray();
        IntegerArrayList list = new IntegerArrayList((int) 1e6);
        for (int i = 0; i < 9; i++) {
            all[i] = i + 1;
        }
        do {
            list.add(calc(all));
        } while (PermutationUtils.nextPermutation(all));
        int m = list.size();
        debug.elapse("all perm");
        IntegerHashMap map = new IntegerHashMap(list.size(), false);
        for (int i = 0; i < m; i++) {
            map.put(list.get(i), i);
        }
        debug.debug("list.size()", list.size());
        debug.elapse("put into map");

        IntegerDequeImpl dq1 = new IntegerDequeImpl(m);
        IntegerDequeImpl dq2 = new IntegerDequeImpl(m);

        int[] target = IntStream.range(1, 10).toArray();
        int[] start = new int[9];
        in.populate(start);
        int bid = map.get(calc(start));
        int end = map.get(calc(target));
        int[] dist1 = new int[m];
        int[] dist2 = new int[m];
        int inf = (int) 1e9;
        SequenceUtils.deepFill(dist1, inf);
        SequenceUtils.deepFill(dist2, inf);
        dist1[bid] = 0;
        dist2[end] = 0;
        dq1.addLast(bid);
        dq2.addLast(end);
        while (!dq1.isEmpty() && !dq2.isEmpty()) {
            int head;
            int[] dist;
            IntegerDequeImpl dq;
            if (dist1[dq1.peekFirst()] <= dist2[dq2.peekFirst()]) {
                head = dq1.removeFirst();
                dist = dist1;
                dq = dq1;
            } else {
                head = dq2.removeFirst();
                dist = dist2;
                dq = dq2;
            }
            if(dist1[head] + dist2[head] < inf){
                out.println(dist1[head] + dist2[head]);
                return;
            }

            //up down
            int v = list.get(head);
            for (int r = 0; r < 2; r++) {
                for (int c = 0; c < 3; c++) {
                    int to = radix.swap(v, getId(r, c), getId(r + 1, c));
                    int node = map.get(to);
                    if (dist[head] + 1 < dist[node]) {
                        dist[node] = dist[head] + 1;
                        dq.addLast(node);
                    }
                }
            }
            //left right
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 2; c++) {
                    int to = radix.swap(v, getId(r, c), getId(r, c + 1));
                    int node = map.get(to);
                    if (dist[head] + 1 < dist[node]) {
                        dist[node] = dist[head] + 1;
                        dq.addLast(node);
                    }
                }
            }

        }


        debug.elapse("bfs");

    }
}

