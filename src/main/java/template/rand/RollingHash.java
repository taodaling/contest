package template.rand;

import template.datastructure.IntDeque;
import template.math.Modular;
import template.math.Power;

public class RollingHash {
    public static final Modular MOD = new Modular((int) (1e9 + 7));
    private int inverse;
    private int[] xs;
    private IntDeque deque;
    private int hash;

    public RollingHash(RollingHash model) {
        inverse = model.inverse;
        deque = new IntDeque(model.deque.getCapacity());
        xs = model.xs;
    }

    public RollingHash(int size, int x) {
        deque = new IntDeque(size);
        xs = new int[size + 1];
        inverse = new Power(MOD).inverse(x);
        xs[0] = 1;
        for (int i = 1; i <= size; i++) {
            xs[i] = MOD.mul(xs[i - 1], x);
        }
    }

    public void clear() {
        hash = 0;
        deque.clear();
    }

    public int hash() {
        return hash;
    }

    public int hashVerbose() {
        return MOD.plus(hash, xs[deque.size()]);
    }

    public void addLast(int v) {
        hash = MOD.plus(hash, MOD.mul(v, xs[deque.size()]));
        deque.addLast(v);
    }

    public boolean isEmpty() {
        return deque.isEmpty();
    }

    public int size() {
        return deque.size();
    }

    public void removeFirst() {
        hash = MOD.mul(MOD.subtract(hash, deque.removeFirst()), inverse);
    }

    @Override
    public String toString() {
        return hash + "=rand(" + deque + ")";
    }
}
