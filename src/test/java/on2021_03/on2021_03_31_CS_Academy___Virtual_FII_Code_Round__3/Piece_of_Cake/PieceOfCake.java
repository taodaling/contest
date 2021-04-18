package on2021_03.on2021_03_31_CS_Academy___Virtual_FII_Code_Round__3.Piece_of_Cake;



import template.datastructure.MultiSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.FractionComparator;
import template.math.GCDs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PieceOfCake {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        long total = 0;
        for (int x : a) {
            total += x;
        }
        Fraction[] fractions = new Fraction[n];
        for (int i = 0; i < n; i++) {
            fractions[i] = new Fraction(a[i], 1);
        }
        Arrays.sort(fractions);
        PriorityQueue<Fraction> pq = new PriorityQueue<>(n, Comparator.reverseOrder());
        pq.addAll(Arrays.asList(fractions));
        Fraction sum = new Fraction(0, 1);
        boolean alice = true;
        boolean ok = false;
        while (!ok || !alice) {
            Fraction largest = pq.remove();
            if (largest == fractions[0]) {
                ok = true;
            }
            largest.half();
            if (alice) {
                sum.add(largest);
            }
            alice = !alice;
            pq.add(largest);
        }

        Arrays.sort(fractions);
        Fraction even = new Fraction(0, 1);
        Fraction odd = new Fraction(0, 1);
        for (int i = 0; i < n; i++) {
            if (i % 2 == 1) {
                odd.add(fractions[i]);
            } else {
                even.add(fractions[i]);
            }
        }
        if (n % 2 == 0) {
            sum.add(odd);
        } else {
            even.mul(new Fraction(2, 3));
            odd.mul(new Fraction(1, 3));
            sum.add(even);
            sum.add(odd);
        }

        sum.norm();
        out.append(sum.a).append('/').append(sum.b).println();
        out.append(sum.b * total - sum.a).append('/').append(sum.b).println();
    }
}

class Fraction implements Comparable<Fraction> {
    long a;
    long b;

    void norm() {
        long g = GCDs.gcd(a, b);
        a /= g;
        b /= g;
    }

    void half() {
        if (a % 2 == 0) {
            a /= 2;
        } else {
            b *= 2;
        }
    }

    void add(Fraction x) {
        long largest = Math.max(b, x.b);
        a = a * (largest / b) + x.a * (largest / x.b);
        b = largest;
    }

    void mul(Fraction x) {
        a *= x.a;
        b *= x.b;
    }

    public Fraction(long a, long b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public int compareTo(Fraction o) {
        long largest = Math.max(o.b, b);
        return Long.compare(a * (largest / b), o.a * (largest / o.b));
    }

    @Override
    public String toString() {
        return "Fraction{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}