package on2021_05.on2021_05_30_AtCoder___NOMURA_Programming_Contest_2021_AtCoder_Regular_Contest_121_.B___RGB_Matching;



import template.datastructure.FixedMinHeap;
import template.datastructure.RangeTree;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class BRGBMatching {
    int eval(char c) {
        return c == 'R' ? 0 : c == 'G' ? 1 : 2;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri() * 2;
        Dog[] dogs = new Dog[n];
        for (int i = 0; i < n; i++) {
            dogs[i] = new Dog(in.rl(), eval(in.rc()));
        }
        int[] cnts = new int[3];
        for (Dog dog : dogs) {
            cnts[dog.c]++;
        }
        List<Integer> odd = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (cnts[i] % 2 == 1) {
                odd.add(i);
            }
        }
        if (odd.size() == 0) {
            out.println(0);
            return;
        }
        assert odd.size() == 2;
        int c1 = odd.get(0);
        int c2 = odd.get(1);
        int c3 = 0 ^ 1 ^ 2 ^ c1 ^ c2;
        //c1 and c2 match
        Arrays.sort(dogs, Comparator.comparingLong(x -> x.a));
        TreeSet<Long> set1 = new TreeSet<>();
        TreeSet<Long> set2 = new TreeSet<>();
        for (Dog dog : dogs) {
            if (dog.c == c1) {
                set1.add(dog.a);
            } else if (dog.c == c2) {
                set2.add(dog.a);
            }
        }
        for (Dog dog : dogs) {
            dog.x = inf;
            dog.y = 0;
            if (dog.c == c1) {
                dog.x = Math.min(dog.x, dist(set2.ceiling(dog.a), dog.a));
                dog.x = Math.min(dog.x, dist(set2.floor(dog.a), dog.a));
            }
        }

        long best = calc(dogs);
        for (Dog dog : dogs) {
            dog.x = inf;
            dog.y = inf;
            if (dog.c == c3) {
                dog.x = Math.min(dog.x, dist(set2.ceiling(dog.a), dog.a));
                dog.x = Math.min(dog.x, dist(set2.floor(dog.a), dog.a));

                dog.y = Math.min(dog.y, dist(set1.ceiling(dog.a), dog.a));
                dog.y = Math.min(dog.y, dist(set1.floor(dog.a), dog.a));
            }
        }

        best = Math.min(best, calc(dogs));

        out.println(best);
    }

    public long calc(Dog[] dogs) {
        FixedMinHeap<Dog> mx = new FixedMinHeap<>(2, Comparator.comparingLong(x -> x.x));
        FixedMinHeap<Dog> my = new FixedMinHeap<>(2, Comparator.comparingLong(x -> x.y));
        for (Dog dog : dogs) {
            mx.add(dog);
            my.add(dog);
        }
        long best = inf;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (mx.get(i) == my.get(j)) {
                    continue;
                }
                best = Math.min(mx.get(i).x + my.get(j).y, best);
            }
        }
        return best;
    }

    long inf = (long) 1e18;

    public long eval(Long v) {
        return v == null ? inf : v;
    }

    public long dist(Long v, long x) {
        return Math.abs(eval(v) - x);
    }
}

class Dog {
    long a;
    int c;

    public Dog(long a, int c) {
        this.a = a;
        this.c = c;
    }

    long x;
    long y;
}