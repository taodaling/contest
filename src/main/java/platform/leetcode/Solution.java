package platform.leetcode;

import java.util.Arrays;
import java.util.Random;

public class Solution {
    public static void main(String[] args) {
        new Solution().test();
    }

    public void test() {
        int time = 10000;
        while(time-- > 0){
            testOne();
        }
    }


    Random random = new Random(0);

    public int nextInt(int l, int r) {
        return random.nextInt(r - l + 1) + l;
    }

    public void testOne() {

    }



}
