package template;

public class Hash {
    private static final NumberTheory.Modular MOD = new NumberTheory.Modular((int) (1e9 + 7));
    private int[] inverse;
    private int[] xs;
    private int[] hash;
    private int n;

    public static interface ToHash<T> {
        int hash(T obj);
    }

    public Hash(Hash model) {
        inverse = model.inverse;
        hash = new int[model.hash.length];
        n = model.n;
        xs = model.xs;
    }

    public Hash(int size, int x) {
        inverse = new int[size];
        hash = new int[size];
        xs = new int[size];
        int invX = new NumberTheory.Power(MOD).inverse(x);
        inverse[0] = 1;
        xs[0] = 1;
        for (int i = 1; i < size; i++) {
            this.inverse[i] = MOD.mul(this.inverse[i - 1], invX);
            xs[i] = MOD.mul(xs[i - 1], x);
        }
    }

    public <T> void populate(T[] data, int n, ToHash<T> toHash) {
        this.n = n;
        hash[0] = toHash.hash(data[0]);
        for (int i = 1; i < n; i++) {
            hash[i] = MOD.plus(hash[i - 1], MOD.mul(toHash.hash(data[i]), xs[i]));
        }
    }

    public void populate(Object[] data, int n) {
        this.n = n;
        hash[0] = data[0].hashCode();
        for (int i = 1; i < n; i++) {
            hash[i] = MOD.plus(hash[i - 1], MOD.mul(data[i].hashCode(), xs[i]));
        }
    }

    public void populate(int[] data, int n) {
        this.n = n;
        hash[0] = data[0];
        for (int i = 1; i < n; i++) {
            hash[i] = MOD.plus(hash[i - 1], MOD.mul(data[i], xs[i]));
        }
    }

    public void populate(char[] data, int n) {
        this.n = n;
        hash[0] = data[0];
        for (int i = 1; i < n; i++) {
            hash[i] = MOD.plus(hash[i - 1], MOD.mul(data[i], xs[i]));
        }
    }

    public int partial(int l, int r) {
        int h = hash[r];
        if (l > 0) {
            h = MOD.plus(h, -hash[l - 1]);
            h = MOD.mul(h, inverse[l]);
        }
        return h;
    }
}
