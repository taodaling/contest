package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.Random;

public class CRiversideCurio {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = in.readInt();
        }

        IntegerDeque dq = new IntegerDequeImpl(n);
        int k = 0;
        long p = 0;
        for (int i = 0; i < n; i++) {
            while (k < m[i]) {
                k++;
                int index = dq.removeLast();
                p += n - index;
            }
            if (k == m[i]) {
                k++;
                p += n - i;
            } else {
                dq.addLast(i);
            }
        }

        p -= n;
        for(int i = 0; i < n; i++){
            p -= m[i];
        }

        out.println(p);
    }

}

