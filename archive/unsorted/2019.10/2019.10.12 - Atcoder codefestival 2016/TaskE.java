package contest;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import template.FastInput;

public class TaskE {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int m = in.readInt();
        int n = in.readInt();
        int q = in.readInt();
        int[] seq = new int[q + 1];
        int[] last = new int[q + 1];
        int[] reg = new int[n + 1];
        for (int i = 1; i <= q; i++) {
            seq[i] = in.readInt();
            last[i] = reg[seq[i]];
            reg[seq[i]] = i;
        }

        ArrayList<Integer> perms = new ArrayList<>();
        List<Integer> cnts = new ArrayList<>();
        Constraint[] constraints = new Constraint[q + 1];
        List<Constraint> wait = new ArrayList<>(q);
        boolean[] used = new boolean[n + 1];
        boolean flag = true;
        int nextValue = 1;

        for (int i = q; i >= 1; i--) {
            int v = seq[i];
            if (used[v]) {
                continue;
            }
            used[v] = true;
            flag = flag && nextValue == v;
            nextValue++;
        }

        Arrays.fill(used, false);
        if (flag) {
            out.println("Yes");
            return;
        }

        for (int i = q; i >= 1; i--) {
            int v = seq[i];
            if (used[v]) {
                continue;
            }
            for (int j = 0, until = wait.size(); j < until; j++) {
                Constraint c = wait.get(j);
                c.num = v;
                c.require = m - j;
                constraints[c.index] = c;
            }

            used[v] = true;
            wait.clear();
            for (int j = i; j >= 1; j = last[j]) {
                Constraint c = new Constraint();
                c.index = j;
                wait.add(c);
            }
            perms.add(v);
            cnts.add(wait.size());
        }

        if (cnts.get(0) < m) {
            out.println("No");
            return;
        }

        int[] cnt = new int[n + 1];

        boolean valid = true;
        List<Integer> sortedPerm = (List<Integer>) perms.clone();
        sortedPerm.sort(Comparator.naturalOrder());
        for (int i = 0; i < sortedPerm.size(); i++) {
            if (i + 1 != sortedPerm.get(i)) {
                valid = false;
                break;
            }
        }

        if (valid) {
            for (int i = perms.size()  - 1; i >= 0; i--) {
                if (i < perms.size() - 1 && perms.get(i + 1) - 1 != perms.get(i)) {
                    break;
                }
                cnt[perms.get(i)] = m;
            }
        }

        for (int i = 1; i <= q; i++) {
            cnt[seq[i]]++;
            if (constraints[i] != null) {
                if (cnt[constraints[i].num] < constraints[i].require) {
                    out.println("No");
                    return;
                }
            }
        }

        out.println("Yes");
        return;
    }
}


class Constraint {
    int index;
    int num;
    int require;
}
