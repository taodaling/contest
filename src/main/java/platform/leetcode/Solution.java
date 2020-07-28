package platform.leetcode;

import java.util.Arrays;
import java.util.Random;

public class Solution {
    public static void main(String[] args) {
        int[] input = new int[]{};
        int[] output = new int[]{2, 3};
    }


    public static void sort(int[] data) {
        Random random = new Random();
        for (int i = data.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = data[j];
            data[j] = data[i];
            data[i] = tmp;
        }
        Arrays.sort(data);
    }
}
