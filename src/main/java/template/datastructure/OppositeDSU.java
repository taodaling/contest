package template.datastructure;

public class OppositeDSU {
    int[] ps;
    int[] ranks;
    int n;
    public OppositeDSU(int n) {
        this.n = n;
        ps = new int[n * 2];
        ranks = new int[n * 2];
        reset();
    }

    public void reset(){
        for (int i = 0; i < ps.length; i++) {
            ps[i] = i;
        }
    }

    private int opposite(int i){
        return i < n ? i + n : i - n;
    }

    public int find(int i) {
        return ps[i] == ps[ps[i]] ? ps[i] : (ps[i] = find(ps[i]));
    }

    /**
     * Return 1 if a is definitely different with b, or 0 if a is definitely same with b, or -1 for unknown case.
     */
    public int xor(int a, int b) {
        if (find(a) == find(b)) {
            return 0;
        }
        if (find(a) == find(opposite(b))) {
            return 1;
        }
        return -1;
    }

    public void different(int a, int b) {
        merge0(a, opposite(b));
        merge0(opposite(a), b);
    }

    public void same(int a, int b) {
        merge0(a, b);
        merge0(opposite(a), opposite(b));
    }

    private void merge0(int a, int b) {
        a = find(a);
        b = find(b);
        if (a == b) {
            return;
        }
        if (ranks[a] == ranks[b]) {
            ranks[a]++;
        }
        if (ranks[a] > ranks[b]) {
            ps[b] = a;
        } else {
            ps[a] = b;
        }
    }
}
