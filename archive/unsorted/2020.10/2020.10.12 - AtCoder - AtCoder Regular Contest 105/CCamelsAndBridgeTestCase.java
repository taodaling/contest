package contest;

import java.util.*;
import java.util.stream.IntStream;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.algo.IntBinarySearch;
import template.math.PermutationUtils;
import template.rand.RandomWrapper;

public class CCamelsAndBridgeTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int... vals) {
        for (int val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long... vals) {
        for (long val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 2);
        int[] w = new int[n];
        for (int i = 0; i < n; i++) {
            w[i] = random.nextInt(1, 10);
        }
        int m = random.nextInt(1, 2);
        int[][] bridge = new int[m][2];
        for (int j = 0; j < m; j++) {
            bridge[j][0] = random.nextInt(1, 10);
            bridge[j][1] = random.nextInt(1, 10);
        }

        int ans = solve(w, bridge);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        printLine(in, w);
        for(int i = 0; i < m; i++){
            printLine(in, bridge[i]);
        }
        return new Test(in.toString(), "" + ans);
    }

    public int solve(int[] w, int[][] bridges) {
        int[] perm = IntStream.range(0, w.length).toArray();
        int n = w.length;
        int m = bridges.length;
        for (int i = 0; i < m; i++) {
            for (int x : w) {
                if (x > bridges[i][1]) {
                    return -1;
                }
            }
        }
        int res = Integer.MAX_VALUE;
        do {
            int[] permW = new int[w.length];
            for (int i = 0; i < w.length; i++) {
                permW[i] = w[perm[i]];
            }

            int[] d = new int[n];
            for (int i = 1; i < n; i++) {
                int finalI = i;
                IntBinarySearch ibs = new IntBinarySearch() {
                    @Override
                    public boolean check(int mid) {
                        for (int j = 0; j < m; j++) {
                            int len = bridges[j][0];
                            int cap = bridges[j][1];
                            d[finalI] = mid;
                            int sum = 0;
                            int r = -1;
                            int l = 0;
                            int lenSum = 0;
                            for (int k = 0; k <= finalI; k++) {
                                while (l < k) {
                                    sum -= permW[l];
                                    lenSum -= permW[l + 1];
                                    l++;
                                }
                                while (r + 1 <= finalI && lenSum + d[r + 1] < len) {
                                    r++;
                                    lenSum += d[r];
                                    sum += permW[r];
                                }
                                if (sum > cap) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    }
                };

                d[i] = ibs.binarySearch(0, (int) 1e8);
            }

            int ans = Arrays.stream(d).sum();
            res = Math.min(res, ans);
        } while (PermutationUtils.nextPermutation(perm));
        return res;
    }
}
