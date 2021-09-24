package on2021_09.on2021_09_12_Codeforces___Codeforces_Global_Round_16.D2__Seating_Arrangements__hard_version_;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;

public class D2SeatingArrangementsHardVersion {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        IntegerBIT[] rows = new IntegerBIT[n];
        for (int i = 0; i < n; i++) {
            rows[i] = new IntegerBIT(m);
        }
        Person[] people = new Person[n * m];
        for (int i = 0; i < n * m; i++) {
            people[i] = new Person();
            people[i].a = in.ri();
            people[i].order = i;
        }
        Person[] sorted = people.clone();
        Arrays.sort(sorted, Comparator.comparingInt(x -> x.a));
        int nm = n * m;
        for (int i = 0; i < nm; i++) {
            int l = i;
            int r = i;
            while (r + 1 < nm && sorted[r + 1].a == sorted[i].a) {
                r++;
            }
            i = r;
            Person[] consider = Arrays.copyOfRange(sorted, l, r + 1);
            int iter = 0;
            for (int j = l; j <= r; j++) {
                int L = j;
                int R = j;
                while (R + 1 <= r && (R + 1) / m == j / m) {
                    R++;
                }
                j = R;
                for (int t = R; t >= L; t--) {
                    consider[iter++].assign = t;
                }
            }
        }
        boolean[] used = new boolean[nm];
        for (Person p : people) {
            used[p.assign] = true;
        }
        for (int i = 0; i < nm; i++) {
            assert used[i];
        }
        long cost = 0;
        for (Person p : people) {
            int r = p.assign / m;
            int c = p.assign % m;
            cost += rows[r].query(c + 1);
            rows[r].update(c + 1, 1);
        }
        out.println(cost);
        debug.debug("people", people);
    }
    Debug debug = new Debug(false);
}

class Person {
    int order;
    int a;
    int assign;

    @Override
    public String toString() {
        return "Person{" +
                "order=" + order +
                ", assign=" + assign +
                '}';
    }
}
