package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.math.GCDs;
import template.math.LCMs;
import template.math.MultiplicativeFunctionSieve;
import template.primitve.generated.IntegerDeque;
import template.primitve.generated.IntegerDequeImpl;
import template.primitve.generated.IntegerIterator;
import template.primitve.generated.IntegerList;
import template.primitve.generated.IntegerVersionArray;
import template.primitve.generated.MultiWayIntegerDeque;
import template.primitve.generated.MultiWayIntegerStack;

public class FClassical {
    int limit = 100000;
    MultiWayIntegerStack divisors = Factorization.factorizeRange(limit);
    MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(limit, true, false, false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        IntegerList list = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            list.add(in.readInt());
        }
        list.unique();
        MultiWayIntegerDeque multiple = new MultiWayIntegerDeque(limit + 1, 20 * limit);
        for (int i = 0; i < list.size(); i++) {
            int x = list.get(i);
            for (IntegerIterator iterator = divisors.iterator(x); iterator.hasNext(); ) {
                int divisor = iterator.next();
                multiple.addLast(divisor, x / divisor);
            }
        }

        IntegerVersionArray iva = new IntegerVersionArray(limit + 1);
        IntegerDeque stack = new IntegerDequeImpl(n);
        long ans = list.tail();
        for (int i = 1; i <= limit; i++) {
            iva.clear();
            while (!multiple.isEmpty(i)) {
                int last = multiple.removeLast(i);
                int cop = countCoPrime(iva, last);
                int pop = 0;
                while (cop > 0) {
                    pop = stack.removeLast();
                    remove(iva, pop);
                    if (GCDs.gcd(pop, last) == 1) {
                        cop--;
                    }
                }
                ans = Math.max(ans, LCMs.lcm(pop, last) * i);
                add(iva, last);
                stack.addLast(last);
            }
        }

        out.println(ans);
    }

    public int countCoPrime(IntegerVersionArray iva, int x) {
        int ans = 0;
        for (IntegerIterator iterator = divisors.iterator(x); iterator.hasNext(); ) {
            int divisor = iterator.next();
            ans += iva.get(divisor) * sieve.mobius[divisor];
        }
        return ans;
    }

    public void add(IntegerVersionArray iva, int x) {
        for (IntegerIterator iterator = divisors.iterator(x); iterator.hasNext(); ) {
            int divisor = iterator.next();
            iva.inc(divisor);
        }
    }

    public void remove(IntegerVersionArray iva, int x) {
        for (IntegerIterator iterator = divisors.iterator(x); iterator.hasNext(); ) {
            int divisor = iterator.next();
            iva.dec(divisor);
        }
    }
}
