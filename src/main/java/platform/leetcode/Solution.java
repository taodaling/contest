package platform.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Solution {
    public void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public void shuffle(int[] data) {
        Random random = new Random();
        for (int i = data.length - 1; i >= 0; i--) {
            swap(data, i, random.nextInt(i + 1));
        }
    }
}
