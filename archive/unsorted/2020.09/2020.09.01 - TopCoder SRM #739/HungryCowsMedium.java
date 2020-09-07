package contest;

import template.algo.LongBinarySearch;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class HungryCowsMedium {
    public long getWellFedTime(int[] cowAppetites, int[] barnPositions) {
        Arrays.sort(cowAppetites);
        Arrays.sort(barnPositions);
        SequenceUtils.reverse(cowAppetites);

        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                int curBarn = -1;
                long barnRemain = 0;
                for (int c : cowAppetites) {
                    if (barnRemain == 0) {
                        if (curBarn + 1 >= barnPositions.length) {
                            return false;
                        }
                        curBarn++;
                        barnRemain = mid - barnPositions[curBarn];
                    }
                    if (barnRemain < c) {
                        if (curBarn + 1 >= barnPositions.length) {
                            return false;
                        }
                        curBarn++;
                        long next = mid - barnPositions[curBarn];
                        if (next < c) {
                            return false;
                        }
                        barnRemain += next;
                    }
                    barnRemain -= c;
                }
                return true;
            }
        };

        long ans = lbs.binarySearch(0, (long) 1e18);
        return ans;
    }
}
