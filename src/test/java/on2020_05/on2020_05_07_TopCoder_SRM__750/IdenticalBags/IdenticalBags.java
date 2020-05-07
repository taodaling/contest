package on2020_05.on2020_05_07_TopCoder_SRM__750.IdenticalBags;



import template.algo.LongBinarySearch;

public class IdenticalBags {
    public long makeBags(long[] candy, long bagSize) {
        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                long sum = 0;
                for (long c : candy) {
                    sum += c / mid;
                }
                return sum < bagSize;
            }
        };
        long bag = lbs.binarySearch(1, (long) 1e18);
        if (lbs.check(bag)) {
            bag--;
        }
        return bag;
    }
}
