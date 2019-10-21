package contest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import template.ArrayUtils;
import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.IntList;

public class TaskD {
    int[] perms;
    int[][] mergePerms;
    int[] permToIndex;
    List<Group> groups = new ArrayList<>();
    int m;
    Group[] groupArray;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        IntList list = new IntList(120);
        genAllPerm(new boolean[k], 0, 0, list);
        perms = list.toArray();


        m = perms.length;
        permToIndex = new int[100000];
        for (int i = 0; i < m; i++) {
            permToIndex[perms[i]] = i;
        }

        mergePerms = new int[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                mergePerms[i][j] = permToIndex[merge(perms[i], perms[j], k)];
            }
        }

        for (int i = 0; i < m; i++) {
            Group bs = new Group();
            bs.set(i, true);
            int j = i;
            while (true) {
                int next = mergePerms[i][j];
                if (bs.get(next)) {
                    break;
                }
                bs.set(next, true);
                j = next;
            }
            addGroup(bs);
        }

        for (int i = 0; i < groups.size(); i++) {
            for (int j = 0; j < i; j++) {
                Group g1 = groups.get(i);
                Group g2 = groups.get(j);
                addGroup(merge(g1, g2));
            }
        }

        groupArray = groups.toArray(new Group[0]);
        for (Group g : groupArray) {
            g.cardinality = Long.bitCount(g.b1) + Long.bitCount(g.b2);
        }
        Arrays.sort(groupArray, (a, b) -> a.cardinality() - b.cardinality());

        int[] tricks = new int[n];
        for (int i = 0; i < n; i++) {
            int t = 0;
            for (int j = 0; j < k; j++) {
                t = t * 10 + in.readInt() - 1;
            }
            tricks[i] = permToIndex[t];
        }

        int[][] next = new int[m][n];
        ArrayUtils.deepFill(next, -1);
        next[tricks[n - 1]][n - 1] = n - 1;
        for (int i = n - 2; i >= 0; i--) {
            for (int j = 0; j < m; j++) {
                next[j][i] = next[j][i + 1];
                if (tricks[i] == j) {
                    next[j][i] = i;
                }
            }
        }


        int nature = 0;
        for (int i = 1; i < k; i++) {
            nature = nature * 10 + i;
        }

        long ans = 0;
        Group empty = addElement(new Group(), permToIndex[nature]);
        for (int i = 0; i < n; i++) {

            Group state = empty;
            int trace = i;
            for (; trace < n;) {
                state = addElement(state, tricks[trace]);

                int jump = n;
                for (int j = 0; j < m; j++) {
                    if (next[j][i] != -1 && !state.get(j)) {
                        jump = Math.min(jump, next[j][i]);
                    }
                }

                ans += (jump - trace) * state.cardinality();
                trace = jump;
            }
        }

        out.println(ans);
    }

    IntList queue = new IntList(120);

    public Group merge(Group a, Group b) {
        queue.clear();
        a = new Group(a);
        for (int i = 0; i < m; i++) {
            if (b.get(i) && !a.get(i)) {
                queue.add(i);
                a.set(i, true);
            }
        }
        while (!queue.isEmpty()) {
            int last = queue.pop();
            for (int i = 0; i < m; i++) {
                if (!a.get(i)) {
                    continue;
                }
                if (!a.get(mergePerms[i][last])) {
                    a.set(mergePerms[i][last], true);
                    queue.add(mergePerms[i][last]);
                }
                if (!a.get(mergePerms[last][i])) {
                    a.set(mergePerms[last][i], true);
                    queue.add(mergePerms[last][i]);
                }
            }
        }
        return a;
    }

    public void addGroup(Group g) {
        if (groups.contains(g)) {
            return;
        }
        groups.add(g);
    }

    public Group addElement(Group x, int t) {
        for (Group g : groupArray) {
            if (x.subset(g) && g.get(t)) {
                return g;
            }
        }
        throw new RuntimeException();
    }

    int[] buf0 = new int[5];
    int[] buf1 = new int[5];

    int merge(int a, int b, int k) {
        deserialize(a, k, buf0);
        deserialize(b, k, buf1);
        int ans = 0;
        for (int i = 0; i < k; i++) {
            ans = ans * 10 + buf1[buf0[i]];
        }
        return ans;
    }

    public void deserialize(int x, int k, int[] buf) {
        for (int i = k - 1; i >= 0; i--) {
            buf[i] = x % 10;
            x /= 10;
        }
    }

    void genAllPerm(boolean[] used, int seq, int i, IntList perm) {
        if (i == used.length) {
            perm.add(seq);
            return;
        }
        for (int j = 0; j < used.length; j++) {
            if (used[j]) {
                continue;
            }
            used[j] = true;
            genAllPerm(used, seq * 10 + j, i + 1, perm);
            used[j] = false;
        }
    }
}


class Group {
    static DigitUtils.BitOperator bo = new DigitUtils.BitOperator();

    long b1;
    long b2;
    int cardinality;

    public Group() {}

    public Group(Group g) {
        this.b1 = g.b1;
        this.b2 = g.b2;
        this.cardinality = g.cardinality;
    }

    public void set(int i, boolean v) {
        if (i < 64) {
            b1 = bo.setBit(b1, i, v);
        } else {
            b2 = bo.setBit(b2, i - 64, v);
        }
    }

    public boolean get(int i) {
        if (i < 64) {
            return bo.bitAt(b1, i) == 1;
        } else {
            return bo.bitAt(b2, i - 64) == 1;
        }
    }

    public int cardinality() {
        return cardinality;
    }

    public boolean subset(Group g) {
        return bo.subset(b1, g.b1) && bo.subset(b2, g.b2);
    }

    @Override
    public boolean equals(Object obj) {
        Group g = (Group) obj;
        return g.b1 == b1 && g.b2 == b2;
    }
}
