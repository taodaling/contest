package on2020_06.on2020_06_12_Virtual_Judge___be_greater_contest_1.A___Or_Plus_Max;



import template.io.FastInput;
import template.io.FastOutput;

public class AOrPlusMax {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[1 << n];
        in.populate(a);
        int[][] heaps = new int[1 << n][2];
        for (int j = 0; j < 1 << n; j++) {
            int next = j + 1;
            while (next != 0) {
                next = (next - 1) & j;
                add(heaps[j], a[next]);
            }
        }

        int max = 0;
        for(int i = 1; i < 1 << n; i++){
            max = Math.max(max, heaps[i][0] + heaps[i][1]);
            out.println(max);
        }
    }

    public void add(int[] heap, int x) {
        if (heap[0] >= x) {
            return;
        }
        if (heap[1] >= x) {
            heap[0] = x;
            return;
        }
        heap[0] = heap[1];
        heap[1] = x;
    }
}
