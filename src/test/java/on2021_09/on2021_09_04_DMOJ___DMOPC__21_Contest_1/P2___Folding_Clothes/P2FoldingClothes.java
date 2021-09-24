package on2021_09.on2021_09_04_DMOJ___DMOPC__21_Contest_1.P2___Folding_Clothes;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SortUtils;

public class P2FoldingClothes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] w = in.ri(n);
        Stack a = new Stack(n);
        Stack b = new Stack(n);
        for (int i = n - 1; i >= 0; i--) {
            a.add(w[i]);
        }
        move(a, b, n, 1);
        for (int i = 0; i < n; i++) {
            int maxIndex = b.maxIndex();
            int moved = b.size - maxIndex;
            move(b, a, moved, -1);
            move(a, b, moved - 1, 1);
        }

        assert SortUtils.notStrictDescending(a.data, 0, n - 1);

        out.println(seq.size());
        for (int x : seq.toArray()) {
            out.println(x);
        }
    }

    IntegerArrayList seq = new IntegerArrayList();

    public void move(Stack a, Stack b, int k, int sign) {
        if (k == 0) {
            return;
        }
        for (int i = a.size - k; i < a.size; i++) {
            b.add(a.data[i]);
        }
        a.size -= k;
        seq.add(sign * k);
    }
}

class Stack {
    int[] data;
    int size;

    public Stack(int cap) {
        data = new int[cap];
    }

    void add(int x) {
        data[size++] = x;
    }

    int maxIndex() {
        int ans = 0;
        for (int i = 0; i < size; i++) {
            if (data[i] > data[ans]) {
                ans = i;
            }
        }
        return ans;
    }
}