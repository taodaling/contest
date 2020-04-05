package on2020_04.on2020_04_03_Codeforces_Round__631__Div__1____Thanks__Denis_aramis_Shitov_.C__Drazil_Likes_Heap;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class CDrazilLikesHeap {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();
        g = in.readInt();
        int n = (1 << h) - 1;
        int m = (1 << g) - 1;
        heap = new int[2 * (n + 1)];
        Arrays.fill(heap, -1);
        long sum = 0;
        for (int i = 1; i <= n; i++) {
            heap[i] = in.readInt();
            sum += heap[i];
        }

        subtract = 0;
        seq.clear();

        for(int i = 1; i <= m; i++) {
            dfs(i);
        }
        for (int i = n; i >= 1; i--) {
            if (i > m && heap[i] != -1) {
                subtract += heap[i];
                heap[i] = -1;
                seq.add(i);
            }
        }
        sum -= subtract;
        out.println(sum);
        for (int i = 0, size = seq.size(); i < size; i++) {
            out.append(seq.get(i)).append(' ');
        }
        out.println();
    }

    public void dfs(int i) {
        int h = Log2.floorLog(i) + 1;
        if (h > g) {
            return;
        }
        while (test(i)) {
            subtract += heap[i];
            seq.add(i);
            replace(i);
        }
    }

    public void replace(int i) {
        int h = Log2.floorLog(i) + 1;
        int left = i * 2;
        int right = i * 2 + 1;
        if (heap[left] == -1 && heap[right] == -1) {
            heap[i] = -1;
            return;
        }
        if (heap[left] < heap[right]) {
            SequenceUtils.swap(heap, i, right);
            replace(right);
            return;
        }
        SequenceUtils.swap(heap, i, left);
        replace(left);
    }

    public boolean test(int i) {
        int h = Log2.floorLog(i) + 1;
        if (h > g) {
            return heap[i] != -1;
        }
        if (heap[i] == -1) {
            return false;
        }
        int left = i * 2;
        int right = i * 2 + 1;
        if (heap[left] < heap[right]) {
            return test(right);
        }
        return test(left);
    }

    int[] heap;
    int g;
    public IntegerList seq = new IntegerList(1000000);
    public long subtract;

}

