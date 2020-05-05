package on2020_05.on2020_05_03_Codeforces___MemSQL_Start_c_UP_3_0___Round_2_and_Codeforces_Round__437__Div__1_.B__Ordering_Pizza;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;

public class BOrderingPizza {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int s = in.readInt();
        Person[] people = new Person[n];
        for (int i = 0; i < n; i++) {
            people[i] = new Person();
            people[i].s = in.readInt();
            people[i].a = in.readInt();
            people[i].b = in.readInt();
        }
        Arrays.sort(people, (a, b) -> -Integer.compare(a.a - a.b, b.a - b.b));

        long sum = 0;
        long prefA = 0;
        for (Person p : people) {
            sum += p.s;
            if (p.a >= p.b) {
                prefA += p.s;
            }
        }

        long req = DigitUtils.ceilDiv(sum, s);
        long t1 = DigitUtils.floorDiv(prefA, s);

        long ans = 0;
        for (long i = t1 - 1; i <= t1 + 1; i++) {
            if (i < 0 || i > req) {
                continue;
            }
            ans = Math.max(ans, profit(people, i * s, (req - i) * s));
        }

        out.println(ans);
    }

    public long profit(Person[] people, long a, long b) {
        long ans = 0;
        int l = 0;
        int r = people.length - 1;
        while (l <= r && people[l].a > people[l].b && a > 0) {
            long forA = Math.min(people[l].s, a);
            long forB = people[l].s - forA;
            a -= forA;
            b -= forB;
            ans += forA * people[l].a + forB * people[l].b;
            l++;
        }
        while(l <= r && people[r].a < people[r].b && b > 0){
            long forB = Math.min(people[r].s, b);
            long forA = people[r].s - forB;
            a -= forA;
            b -= forB;
            ans += forA * people[r].a + forB * people[r].b;
            r--;
        }

        while(l <= r){
            long forA = Math.min(people[l].s, a);
            long forB = people[l].s - forA;
            a -= forA;
            b -= forB;
            ans += forA * people[l].a + forB * people[l].b;
            l++;
        }

        return ans;
    }
}

class Person {
    int s;
    int a;
    int b;
}
