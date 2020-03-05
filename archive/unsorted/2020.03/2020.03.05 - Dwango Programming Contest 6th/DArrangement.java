package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerList;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.*;

public class DArrangement {
    Debug debug = new Debug(false);
    int[] next;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        next = new int[n + 1];
        int[] forbidden = new int[n + 1];
        boolean[] used = new boolean[n + 1];
        for (int i = 1; i <= n; i++) {
            next[i] = in.readInt();
            forbidden[next[i]]++;
        }

        int first = 0;
        for (int i = 1; i <= n; i++) {
            if (forbidden[next[i]] != n - 1) {
                first = i;
                break;
            }
        }

        if (first == 0) {
            out.println(-1);
            return;
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>(n);
        for (int i = 1; i <= n; i++) {
            if (i == first) {
                continue;
            }
            pq.add(i);
        }

        int last = first;
        IntegerList seq = new IntegerList(n);
        seq.add(last);
        forbidden[next[last]]--;
        used[last] = true;

        List<Integer> backup = new ArrayList<>();
        while (pq.size() > 10) {
            int remain = pq.size();
            debug.debug("seq", seq);
            debug.debug("remain", remain);
            int head = pq.remove();
            if (head == next[last] || !used[next[head]] && forbidden[next[head]] == remain - 1) {
                backup.add(head);
                head = pq.remove();
            }
            last = head;
            seq.add(last);
            forbidden[next[last]]--;
            used[last] = true;
            pq.addAll(backup);
            //debug.debug("forbidden", forbidden);
            //debug.debug("backup", backup);
            backup.clear();
        }
        int[] remain = pq.stream().mapToInt(Integer::intValue).toArray();
        debug.debug("remain", remain);
        Arrays.sort(remain);
        int[] perm = new int[pq.size()];
        if (!dfs(perm, remain, new boolean[pq.size()], 0, last)) {
            out.println(-1);
            return;
        }
        seq.addAll(perm);
        for (int i = 0; i < seq.size(); i++) {
            out.append(seq.get(i)).append(' ');
        }
    }

    public boolean dfs(int[] perm, int[] val, boolean[] used, int i, int last) {
        if (i == perm.length) {
            for (int j = 0; j < perm.length; j++) {
                if (next[last] == perm[j]) {
                    return false;
                }
                last = perm[j];
            }
            return true;
        }
        for (int j = 0; j < val.length; j++) {
            if (used[j]) {
                continue;
            }
            used[j] = true;
            perm[i] = val[j];
            if (dfs(perm, val, used, i + 1, last)) {
                return true;
            }
            used[j] = false;
        }
        return false;
    }
}
