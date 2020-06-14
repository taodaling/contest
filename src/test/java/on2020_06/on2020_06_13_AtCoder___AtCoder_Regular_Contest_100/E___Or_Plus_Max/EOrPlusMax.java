package on2020_06.on2020_06_13_AtCoder___AtCoder_Regular_Contest_100.E___Or_Plus_Max;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerFixedMinHeap;

public class EOrPlusMax {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[1 << n];
        in.populate(a);

        IntegerFixedMinHeap[] heaps = new IntegerFixedMinHeap[1 << n];
        for (int i = 0; i < 1 << n; i++) {
            heaps[i] = new IntegerFixedMinHeap(2);
            heaps[i].add(-a[i]);
        }
        for (int j = n - 1; j >= 0; j--) {
            for (int i = (1 << n) - 1; i >= 0; i--) {
                if (Bits.get(i, j) == 0) {
                } else {
                    heaps[i].addAll(heaps[i - (1 << j)]);
                }
            }
        }

        int max = 0;
        for (int i = 1; i < 1 << n; i++) {
            max = Math.max(max, -(heaps[i].get(0) + heaps[i].get(1)));
            out.println(max);
        }
    }

}
