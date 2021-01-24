package template.rand;

import template.utils.Pair;

public class HashSeed {
    private static int[] seeds = new int[]{11, 13, 17, 29, 31, 41, 43, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103};

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
