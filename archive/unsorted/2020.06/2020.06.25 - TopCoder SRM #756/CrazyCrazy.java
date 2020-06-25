package contest;

import template.binary.Bits;
import template.math.DigitUtils;
import template.math.Modular;
import template.primitve.generated.datastructure.*;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class CrazyCrazy {
    public String possible(String song) {
        LongHashSet leftSet = new LongHashSet(1 << 20, true);
        LongHashSet rightSet = new LongHashSet(1 << 20, true);
        char[] s = song.toCharArray();
        int n = s.length;
        char[] leftHalf = Arrays.copyOfRange(s, 0, n / 2);
        char[] rightHalf = Arrays.copyOfRange(s, n / 2, n);
        SequenceUtils.reverse(rightHalf);
        IntegerDeque dq = new IntegerDequeImpl(20);
        IntegerList list = new IntegerList(20);


        int x1 = RandomWrapper.INSTANCE.nextInt(1, mod.getMod() - 1);
        int x2 = RandomWrapper.INSTANCE.nextInt(1, mod.getMod() - 1);
        int halfN = n / 2;
        for (int i = 0; i < 1 << halfN; i++) {
            if (!solve(dq, leftHalf, i)) {
                continue;
            }
            list.clear();
            list.addAll(dq.iterator());
            long h = DigitUtils.asLong(hash(list, x1),
                    hash(list, x2));
            leftSet.add(h);
        }

        for (int i = 0; i < 1 << halfN; i++) {
            if (!solve(dq, rightHalf, i)) {
                continue;
            }
            list.clear();
            list.addAll(dq.iterator());
            list.reverse();
            long h = DigitUtils.asLong(hash(list, x1),
                    hash(list, x2));
            rightSet.add(h);
        }


        for(LongIterator iterator = leftSet.iterator(); iterator.hasNext(); ){
            long key = iterator.next();
            if(rightSet.contain(key)){
                return "possible";
            }
        }

        return "impossible";
    }

    Modular mod = new Modular(1e9 + 7);

    public int hash(IntegerList val, int x) {
        int ans = 1;
        for (int i = 0; i < val.size(); i++) {
            ans = mod.mul(ans, x);
            ans = mod.plus(ans, val.get(i));
        }
        return ans;
    }

    public boolean solve(IntegerDeque dq, char[] c, int mask) {
        dq.clear();
        int build = 0;
        for (int i = 0; i < c.length; i++) {
            if (Bits.get(mask, i) == build) {
                dq.addLast(c[i]);
            } else {
                if (dq.isEmpty()) {
                    build = 1 - build;
                    dq.addLast(c[i]);
                } else {
                    if (dq.removeFirst() != c[i]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
