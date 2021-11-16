package template.rand;

import template.math.EratosthenesSieve;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Pair;

public class HashSeed {
    private static int[] seeds;

    static {
        IntegerArrayList list = new IntegerArrayList(1000);
        EratosthenesSieve.sieve(10000, list::add);
        seeds = list.toArray();
    }

    public static int getSeed() {
        return seeds[RandomWrapper.INSTANCE.nextInt(seeds.length)];
    }


    public static Pair<Integer, Integer> getSeed2() {
        int a = seeds[RandomWrapper.INSTANCE.nextInt(seeds.length)];
        int b = a;
        while (b == a) {
            b = seeds[RandomWrapper.INSTANCE.nextInt(seeds.length)];
        }
        return new Pair<>(a, b);
    }
}
