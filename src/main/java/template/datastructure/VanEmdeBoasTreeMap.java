package template.datastructure;

public class VanEmdeBoasTreeMap<T> {
    private VanEmdeBoasTreeBeta tree;
    private Object[] data;
    private int u;

    public VanEmdeBoasTreeMap(int n) {
        this.u = n;
        data = new Object[n];
        tree = VanEmdeBoasTreeBeta.newInstance(n);
    }

    private boolean tooSmall(int i) {
        return i < 0;
    }

    private boolean tooLarge(int i) {
        return i >= u;
    }

    public void put(int key, T val) {
        assert !tooSmall(key) && !tooLarge(key);
        if (!tree.member(key)) {
            tree.insert(key);
        }
        data[key] = val;
    }

    public T get(int key) {
        if (tooLarge(key) || tooSmall(key)) {
            return null;
        }
        return tree.member(key) ? (T) data[key] : null;
    }

    public T remove(int key) {
        if (tooLarge(key) || tooSmall(key)) {
            return null;
        }
        if (tree.member(key)) {
            tree.delete(key);
            return (T) data[key];
        }
        return null;
    }

    public T floorValue(int key) {
        key = floorKey(key);
        if (key == -1) {
            return null;
        }
        return (T) data[key];
    }

    public int floorKey(int key) {
        if (tooSmall(key)) {
            return -1;
        }
        if (tooLarge(key)) {
            key = u - 1;
        }
        if (tree.member(key)) {
            return key;
        }
        return tree.predecessor(key);
    }

    public T ceilValue(int key) {
        key = ceilKey(key);
        if (key == -1) {
            return null;
        }
        return (T) data[key];
    }

    public int ceilKey(int key) {
        if (tooLarge(key)) {
            return -1;
        }
        if (tooSmall(key)) {
            key = 0;
        }
        if (tree.member(key)) {
            return key;
        }
        return tree.successor(key);
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder("{");
        tree.visit(i -> {
            ans.append(i).append(':').append(data[i]).append(',');
        });
        if (ans.length() > 1) {
            ans.setLength(ans.length() - 1);
        }
        ans.append('}');
        return ans.toString();
    }
}
