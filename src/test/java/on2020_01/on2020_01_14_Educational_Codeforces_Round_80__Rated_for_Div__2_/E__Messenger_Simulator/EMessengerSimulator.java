package on2020_01.on2020_01_14_Educational_Codeforces_Round_80__Rated_for_Div__2_.E__Messenger_Simulator;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerBIT;
import template.primitve.generated.IntegerIterator;
import template.primitve.generated.MultiWayIntegerDeque;
import template.primitve.generated.MultiWayIntegerStack;

import java.util.*;

public class EMessengerSimulator {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[m];
        for (int i = 0; i < m; i++) {
            a[i] = in.readInt() - 1;
        }

        MultiWayIntegerDeque deque = new MultiWayIntegerDeque(n, m);
        for (int i = 0; i < m; i++) {
            deque.addLast(a[i], i);
        }

        List<Query> queries = new ArrayList<>();
        int[] first = new int[n];
        int[] last = new int[n];

        for (int i = 0; i < n; i++) {
            if (deque.isEmpty(i)) {
                first[i] = i;
            } else {
                first[i] = 0;
                IntegerIterator iterator = deque.iterator(i);
                int pre = iterator.next();
                while (iterator.hasNext()) {
                    int next = iterator.next();
                    queries.add(new Query(pre + 1, next - 1, i));
                    pre = next;
                }
                if(pre + 1 <= m - 1) {
                    queries.add(new Query(pre + 1, m - 1, i));
                }
            }
        }

        IntegerBIT appearBIT = new IntegerBIT(n);
        boolean[] visited = new boolean[n];
        for (int i = 0; i < m; i++) {
            if (visited[a[i]]) {
                continue;
            }
            visited[a[i]] = true;
            appearBIT.update(a[i] + 1, 1);
            last[a[i]] = Math.max(last[a[i]], a[i] + appearBIT.query(n) - appearBIT.query(a[i] + 1));
        }

        for(int i = 0; i < n; i++){
            if(visited[i]){
                continue;
            }
            visited[i] = true;
            last[i] = Math.max(last[i], i + appearBIT.query(n) - appearBIT.query(i + 1));
        }

        int[] registries = new int[n];
        Arrays.fill(registries, -1);
        IntegerBIT countBit = new IntegerBIT(m);
        queries.sort((x, y) -> x.r - y.r);
        Deque<Query> queue = new ArrayDeque<>(queries);

        for (int i = 0; i < m; i++) {
            if (registries[a[i]] != -1) {
                countBit.update(1 + registries[a[i]], -1);
            }
            registries[a[i]] = i;
            countBit.update(1 + registries[a[i]], 1);
            while (!queue.isEmpty() && queue.peekFirst().r == i) {
                Query q = queue.removeFirst();
                int ans = countBit.query(q.r + 1) - countBit.query(q.l);
                last[q.person] = Math.max(last[q.person], ans);
            }
        }

        for(int i = 0; i < n; i++){
            out.append(first[i] + 1).append(' ').append(last[i] + 1).println();
        }
    }
}

class Query {
    int l;
    int r;
    int person;

    public Query(int l, int r, int person) {
        this.l = l;
        this.r = r;
        this.person = person;
    }
}