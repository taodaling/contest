package on2020_10.on2020_10_26_Library_Checker.Enumerate_Primes;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.EratosthenesSieve;
import template.math.EulerSieve;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerIterator;

import java.util.function.IntConsumer;

public class EnumeratePrimes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int a = in.readInt();
        int b = in.readInt();
        Consumer consumer = new Consumer(a, b);
        EratosthenesSieve.sieve(consumer, n);
        out.println(consumer.pi);
        //ax+b>=pi
        //x>=(pi-b)/a
        out.println(consumer.list.size());
        for (IntegerIterator iterator = consumer.list.iterator();
        iterator.hasNext(); ) {
            out.println(iterator.next());
        }
    }
}

class Consumer implements IntConsumer {
    int pi;
    int a;
    int next;
    IntegerArrayList list = new IntegerArrayList();

    public Consumer(int a, int next) {
        this.a = a;
        this.next = next;
    }

    @Override
    public void accept(int value) {
        pi++;
        if (pi - 1 == next) {
            list.add(value);
            next += a;
        }
    }
}