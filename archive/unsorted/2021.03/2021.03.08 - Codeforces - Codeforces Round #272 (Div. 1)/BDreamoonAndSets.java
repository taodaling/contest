package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class BDreamoonAndSets {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        out.println((6 * (n - 1) + 5) * k);
        for (int i = 0; i < n; i++) {
            out.append((6 * i + 1) * k).append(' ');
            out.append((6 * i + 2) * k).append(' ');
            out.append((6 * i + 3) * k).append(' ');
            out.append((6 * i + 5) * k).append(' ');
            out.println();
        }
    }
}

