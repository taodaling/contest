package template.rand;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerDequeImpl;

public class RollingHash {
    HashData hd1;
    HashData hd2;
    IntegerDequeImpl dq;
    long h1;
    long h2;

    public RollingHash(HashData hd1, HashData hd2, int n) {
        this.hd1 = hd1;
        this.hd2 = hd2;
        dq = new IntegerDequeImpl(n + 10);
    }

    public void clear() {
        h1 = h2 = 0;
        dq.clear();
    }

    public int size(){
        return dq.size();
    }

    public void addLast(int x) {
        h1 = (h1 + (long) hd1.pow[dq.size()] * x) % hd1.mod;
        h2 = (h2 + (long) hd2.pow[dq.size()] * x) % hd2.mod;
        dq.addLast(x);
    }

    public void addFirst(int x){
        h1 = (h1 * hd1.pow[1] + x) % hd1.mod;
        h2 = (h2 * hd2.pow[1] + x) % hd2.mod;
        dq.addFirst(x);
    }

    public int removeLast(){
        int x = dq.removeLast();
        h1 = (h1 + (long) hd1.pow[dq.size()] * (hd1.mod - x)) % hd1.mod;
        h2 = (h2 + (long) hd2.pow[dq.size()] * (hd2.mod - x)) % hd2.mod;
        return x;
    }

    public int removeFirst() {
        int x = dq.removeFirst();
        h1 = (h1 - x + hd1.mod) * hd1.inv[1] % hd1.mod;
        h2 = (h2 - x + hd2.mod) * hd2.inv[1] % hd2.mod;
        return x;
    }

    public int peekFirst(){
        return dq.peekFirst();
    }

    public int peekLast(){
        return dq.peekLast();
    }

    public long hash() {
        return DigitUtils.asLong(h1, h2);
    }

    public long hashV() {
        long ans1 = (h1 + hd1.pow[dq.size() + 1]) % hd1.mod;
        long ans2 = (h2 + hd2.pow[dq.size() + 1]) % hd2.mod;
        return DigitUtils.asLong(ans1, ans2);
    }
}
