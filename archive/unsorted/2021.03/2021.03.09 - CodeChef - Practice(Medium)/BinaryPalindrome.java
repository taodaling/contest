package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.HashData;
import template.rand.RollingHash;

public class BinaryPalindrome {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] length = new int[n + 1];
        int[] data = new int[n + 1];
        int tail = 0;


        int limit = n;
        HashData[] hds = HashData.doubleHashData(limit);
        RollingHash r1 = new RollingHash(hds[0], hds[1], limit);
        RollingHash r2 = new RollingHash(hds[0], hds[1], limit);

        Machine pos = new Machine(data, 1, 0, r1);
        Machine rev = new Machine(data, 1, 0, r2);
        rev.rev = true;
        for (int i = 1; i <= n; i++) {
            int t = in.ri();
            if (t == 2) {
                tail--;
                if (pos.r > tail) {
                    pos.move(Math.min(pos.l, tail), tail);
                }
                if (rev.r > tail) {
                    rev.move(Math.min(rev.l, tail), tail);
                }
            } else {
                tail++;
                data[tail] = in.ri();
                length[tail] = length[tail - 1];

                int now = length[tail];

                //add 2
                if (now + 2 <= tail) {
                    int target = now + 2;
                    int l = tail - target + 1;
                    int r = tail;
                    pos.move(l, r);
                    rev.move(l, r);
                    if (pos.hash() == rev.hash()) {
                        length[tail] = target;
                    }
                }

                if (now + 1 <= tail && now + 1 > length[tail]) {
                    int target = now + 1;
                    int l = tail - target + 1;
                    int r = tail;
                    pos.move(l, r);
                    rev.move(l, r);
                    if (pos.hash() == rev.hash()) {
                        length[tail] = target;
                    }
                }
            }

            out.println(length[tail]);
        }


    }
}


class Machine {
    int[] data;
    int l;
    int r;
    RollingHash rh;
    boolean rev;

    public long hash() {
        return rh.hash();
    }

    public Machine(int[] data, int l, int r, RollingHash rh) {
        this.data = data;
        this.l = l;
        this.r = r;
        this.rh = rh;
    }

    public void move(int L, int R) {
        if (!rev) {
            while (L < l) {
                rh.addFirst(data[--l]);
            }
            while (r < R) {
                rh.addLast(data[++r]);
            }
            while (l < L) {
                l++;
                rh.removeFirst();
            }
            while (R < r) {
                r--;
                rh.removeLast();
            }
        } else {
            while (L < l) {
                rh.addLast(data[--l]);
            }
            while (r < R) {
                rh.addFirst(data[++r]);
            }
            while (l < L) {
                l++;
                rh.removeLast();
            }
            while (R < r) {
                r--;
                rh.removeFirst();
            }
        }
        assert r - l + 1 == rh.size();
    }
}