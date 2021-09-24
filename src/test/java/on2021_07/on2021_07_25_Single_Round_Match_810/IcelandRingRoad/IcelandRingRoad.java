package on2021_07.on2021_07_25_Single_Round_Match_810.IcelandRingRoad;



import template.datastructure.summary.ActiveSum;
import template.datastructure.summary.ActiveUpdate;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.primitve.generated.datastructure.LongEntryIterator;
import template.primitve.generated.datastructure.LongHashMap;
import template.primitve.generated.datastructure.LongHashSet;
import template.rand.RandomWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class IcelandRingRoad {
//    Debug debug = new Debug(true);

    public int solve(int N, int P, int M, long state) {
        int[] C = new int[N];
        for (int i = 0; i <= N - 1; i++) {
            state = (state * 1103515245 + 12345) % (1L << 31);
            C[i] = (int) (1 + ((state / 10) % M));
        }
        int[] L = new int[P];
        int[] W = new int[P];
        for (int j = 0; j <= P - 1; j++) {
            state = (state * 1103515245 + 12345) % (1L << 31);
            L[j] = (int) ((state / 10) % N);
            state = (state * 1103515245 + 12345) % (1L << 31);
            W[j] = (int) ((state / 10) % N);
        }

        long[] xor = new long[N];
        for(int i = 0; i < P; i++){
            int l = L[i];
            int r = W[i];
            if(l == r){
                continue;
            }
            if(l > r){
                int tmp = l;
                l = r;
                r = tmp;
            }
            long id = RandomWrapper.INSTANCE.nextLong();
            xor[l] ^= id;
            xor[r] ^= id;
        }

        for(int i = 1; i < N; i++){
            xor[i] ^= xor[i - 1];
        }
        LongHashMap map = new LongHashMap(N, false);
        for(int i = 0; i < N; i++){
            map.modify(xor[i], C[i]);
        }
        long maxV = 0;
        for(LongEntryIterator iterator = map.iterator(); iterator.hasNext(); ){
            iterator.next();
            maxV = Math.max(maxV, iterator.getEntryValue());
        }
//        debug.debug("C", C);
//        debug.debug("L", L);
//        debug.debug("W", W);
        int sum = Arrays.stream(C).sum();
        return (int) (sum - maxV);
    }
}

class Event {
    int l;
    int r;

    public Event(int l, int r) {
        this.l = l;
        this.r = r;
    }

    @Override
    public String toString() {
        return "Event{" +
                "l=" + l +
                ", r=" + r +
                '}';
    }
}


