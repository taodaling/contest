package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Bits;
import template.math.Modular;
import template.math.Power;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

import java.util.Arrays;

public class ECowAndTreats {
    private int[] left;
    private int[] right;
    Machine machine;
    int[] f;
    int[] h;
    int[][] status;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        Modular mod = new Modular(1e9 + 7);

        int[] s = new int[n];
        for (int i = 0; i < n; i++) {
            s[i] = in.readInt();
        }
        f = new int[m];
        h = new int[m];
        left = new int[m];
        right = new int[m];
        status = new int[m][4];
        IntegerHashMap map = new IntegerHashMap(m, false);
        for (int i = 0; i < m; i++) {
            f[i] = in.readInt();
            h[i] = in.readInt();
            left[i] = right[i] = -1;
            map.put(get(f[i], h[i]), i);
        }

        int[] cnts = new int[n + 1];
        for (int i = 0; i < n; i++) {
            cnts[s[i]]++;
            int key = get(s[i], cnts[s[i]]);
            if(map.containKey(key)) {
                left[map.get(key)] = i;
            }
        }
        Arrays.fill(cnts, 0);
        for (int i = n - 1; i >= 0; i--) {
            cnts[s[i]]++;
            int key = get(s[i], cnts[s[i]]);
            if(map.containKey(key)) {
                right[map.get(key)] = i;
            }
        }

        int[] inverseLeft = new int[n];
        int[] inverseRight = new int[n];
        Arrays.fill(inverseLeft, -1);
        Arrays.fill(inverseRight, -1);
        for (int i = 0; i < m; i++) {
            if(left[i] != -1) {
                inverseLeft[left[i]] = i;
            }
            if(right[i] != -1) {
                inverseRight[right[i]] = i;
            }
        }

        machine = new Machine(n + 1);
        for (int i = 0; i < m; i++) {
            calc(i, -1);
        }

        int max = machine.max;
        int maxCnt = machine.cnt;
        for (int i = 0; i < n; i++) {
            clear(inverseRight[i]);
            calc(inverseRight[i], i);
            clear(inverseLeft[i]);
            if (inverseLeft[i] != -1) {
                int cow = inverseLeft[i];
                int recordLeft = machine.left[f[cow]];
                int recordBoth = machine.both[f[cow]];
                machine.update(f[cow], -recordLeft, recordBoth, -recordBoth);
                if (max < machine.max + 1) {
                    max = machine.max + 1;
                    maxCnt = 0;
                }
                if (max == machine.max + 1) {
                    maxCnt = mod.plus(maxCnt, machine.cnt);
                }
                machine.update(f[cow], recordLeft, -recordBoth, recordBoth);
            }
            calc(inverseLeft[i], i);
        }

        out.append(max).append(' ').append(maxCnt);
    }

    public void clear(int cow) {
        if (cow == -1) {
            return;
        }
        machine.update(f[cow], -status[cow][1], -status[cow][2], -status[cow][3]);
        Arrays.fill(status[cow], 0);
    }

    public void calc(int cow, int leftBound) {
        if (cow == -1) {
            return;
        }
        int mask = 0;
        mask = Bits.setBit(mask, 0, left[cow] != -1 && left[cow] <= leftBound);
        mask = Bits.setBit(mask, 1, right[cow] != -1 && right[cow] > leftBound);
        status[cow][mask] = 1;
        machine.update(f[cow], status[cow][1], status[cow][2], status[cow][3]);
    }

    public int get(int f, int h) {
        return f * 10000 + h;
    }
}

class Machine {
    int[] left;
    int[] right;
    int[] both;

    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);

    int max;
    int cnt = 1;

    public Machine(int n) {
        left = new int[n];
        right = new int[n];
        both = new int[n];
    }

    public void takeEffect(int i) {
        if (left[i] + right[i] + both[i] == 0) {
        } else if (both[i] + left[i] + right[i] == 1 ||
                left[i] + both[i] == 0 || right[i] + both[i] == 0) {
            max += 1;
            int cnt1 = left[i] + right[i] + 2 * both[i];
            cnt = mod.mul(cnt, cnt1);
        } else {
            max += 2;
            int cnt1 = mod.mul(left[i], right[i] + both[i]);
            int cnt2 = mod.mul(both[i], right[i] + both[i] - 1);
            int cnt3 = mod.plus(cnt1, cnt2);
            cnt = mod.mul(cnt, cnt3);
        }
    }

    public void clearEffect(int i) {
        if (left[i] + right[i] + both[i] == 0) {
        } else if (both[i] + left[i] + right[i] == 1 ||
                left[i] + both[i] == 0 || right[i] + both[i] == 0) {
            max -= 1;
            int cnt1 = left[i] + right[i] + 2 * both[i];
            cnt = mod.mul(cnt, power.inverseByFermat(cnt1));
        } else {
            max -= 2;
            int cnt1 = mod.mul(left[i], right[i] + both[i]);
            int cnt2 = mod.mul(both[i], right[i] + both[i] - 1);
            int cnt3 = mod.plus(cnt1, cnt2);
            cnt = mod.mul(cnt, power.inverseByFermat(cnt3));
        }
    }

    public void update(int i, int l, int r, int b) {
        clearEffect(i);
        left[i] += l;
        right[i] += r;
        both[i] += b;
        takeEffect(i);
    }
}