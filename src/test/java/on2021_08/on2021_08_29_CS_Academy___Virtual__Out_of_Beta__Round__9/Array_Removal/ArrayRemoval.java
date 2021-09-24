package on2021_08.on2021_08_29_CS_Academy___Virtual__Out_of_Beta__Round__9.Array_Removal;



import template.datastructure.BlockOnLine;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongArrayList;
import template.primitve.generated.datastructure.LongPreSum;

public class ArrayRemoval {
    LongPreSum ps;
    long best;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] a = in.rl(n);
        ps = new LongPreSum(i -> a[i], n);
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.ri() - 1;
        }
        BlockOnLine bol = new BlockOnLine(n, new BlockOnLine.CallBack() {
            @Override
            public void addBlock(int l, int r) {
                best = Math.max(best, ps.intervalSum(l, r));
            }

            @Override
            public void removeBlock(int l, int r) {

            }
        });
        LongArrayList list = new LongArrayList(n);
        for(int i = n - 1; i >= 0; i--){
            bol.add(p[i]);
            list.add(best);
        }
        list.reverse();
        for(long x : list.toArray()){
            out.println(x);
        }
    }
}

