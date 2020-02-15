package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.EratosthenesSieve;
import template.math.ILongModular;

import java.util.function.IntConsumer;

public class FTheNeutralZone {
    int a;
    int b;
    int c;
    int d;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        a = in.readInt();
        b = in.readInt();
        c = in.readInt();
        d = in.readInt();

        Consumer consumer = new Consumer(n);
        EratosthenesSieve.sieve(consumer, n);
        int ans = consumer.sum;
        out.println(DigitUtils.asLong(0, ans));
    }

    class Consumer implements IntConsumer {
        int n;
        int sum;

        public Consumer(int n) {
            this.n = n;
        }

        @Override
        public void accept(int value) {
            sum += log(value, n) * f(value);
        }
    }

    public int log(int x, int n){
        return n < x ? 0 : (n / x + log(x, n / x));
    }


    public int f(int x){
        int x1 = x;
        int x2 = x1 * x;
        int x3 = x2 * x;
        return x3 * a + x2 * b + x1 * c + d;
    }
}

