package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class BitSetTestTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        System.out.println("build  testcase " + testNum);
        int n = random.nextInt(1, 100);
        int m = random.nextInt(1, 200);
        int[][] ops = new int[n][];
        for (int i = 0; i < n; i++) {
            int[] op = null;
            int t = random.nextInt(0, 11);
            if (t == 0) {
                //set one
                int l = random.nextInt(0, m - 1);
                op = SequenceUtils.wrapArray(t, l);
            } else if (t == 1) {
                //set bulk
                int l = random.nextInt(0, m - 1);
                int r = random.nextInt(0, m - 1);
                if (l > r) {
                    int tmp = l;
                    l = r;
                    r = tmp;
                }
                op = SequenceUtils.wrapArray(t, l, r);
            } else if (t == 2) {
                //clear
                int l = random.nextInt(0, m - 1);
                op = SequenceUtils.wrapArray(t, l);
            } else if (t == 3) {
                //clear bulk
                int l = random.nextInt(0, m - 1);
                int r = random.nextInt(0, m - 1);
                if (l > r) {
                    int tmp = l;
                    l = r;
                    r = tmp;
                }
                op = SequenceUtils.wrapArray(t, l, r);
            } else if (t == 4) {
                //flip
                int l = random.nextInt(0, m - 1);
                op = SequenceUtils.wrapArray(t, l);
            } else if (t == 5) {
                //flip bulk
                int l = random.nextInt(0, m - 1);
                int r = random.nextInt(0, m - 1);
                if (l > r) {
                    int tmp = l;
                    l = r;
                    r = tmp;
                }
                op = SequenceUtils.wrapArray(t, l, r);
            } else if (t == 6) {
                //and
                int prev = random.nextInt(0, i);
                op = SequenceUtils.wrapArray(t, prev);
            } else if (t == 7) {
                //or
                int prev = random.nextInt(0, i);
                op = SequenceUtils.wrapArray(t, prev);
            } else if (t == 8) {
                //xor
                int prev = random.nextInt(0, i);
                op = SequenceUtils.wrapArray(t, prev);
            } else if (t == 9) {
                //left shift
                int step = random.nextInt(0, m);
                op = SequenceUtils.wrapArray(t, step);
            } else if (t == 10) {
                //right shift
                int step = random.nextInt(0, m);
                op = SequenceUtils.wrapArray(t, step);
            } else if (t == 11) {
                int l = random.nextInt(0, m - 1);
                int r = random.nextInt(0, m - 1);
                if (l > r) {
                    int tmp = l;
                    l = r;
                    r = tmp;
                }
                op = SequenceUtils.wrapArray(t, l, r);
            }
            ops[i] = op;
        }

        String ans = solve(ops, m);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for (int[] op : ops) {
            for (int x : op) {
                in.append(x).append(' ');
            }
            in.append('\n');
        }
        return new Test(in.toString(), ans);
    }

    public String solve(int[][] ops, int m) {
        int n = ops.length;
        StringBuilder builder = new StringBuilder();
        boolean[][] data = new boolean[n + 1][m];
        for (int i = 1; i <= n; i++) {
            int[] op = ops[i - 1];
            data[i] = data[i - 1].clone();
            int t = op[0];
            if (t == 0) {
                //set one
                int l = op[1];
                data[i][l] = true;
            } else if (t == 1) {
                //set bulk
                int l = op[1];
                int r = op[2];
                for (int j = l; j <= r; j++) {
                    data[i][j] = true;
                }
            } else if (t == 2) {
                //clear
                int l = op[1];
                data[i][l] = false;
            } else if (t == 3) {
                //clear bulk
                int l = op[1];
                int r = op[2];
                for (int j = l; j <= r; j++) {
                    data[i][j] = false;
                }
            } else if (t == 4) {
                //flip
                int l = op[1];
                data[i][l] = !data[i][l];
            } else if (t == 5) {
                //flip bulk
                int l = op[1];
                int r = op[2];
                for (int j = l; j <= r; j++) {
                    data[i][j] = !data[i][j];
                }
            } else if (t == 6) {
                //and
                int prev = op[1];
                for (int j = 0; j < m; j++) {
                    data[i][j] = data[i][j] && data[prev][j];
                }
            } else if (t == 7) {
                //or
                int prev = op[1];
                for (int j = 0; j < m; j++) {
                    data[i][j] = data[i][j] || data[prev][j];
                }
            } else if (t == 8) {
                //xor
                int prev = op[1];
                for (int j = 0; j < m; j++) {
                    data[i][j] = data[i][j] != data[prev][j];
                }
            } else if (t == 9) {
                //left shift
                int step = op[1];
                for (int j = 0; j < m; j++) {
                    if (j + step < m) {
                        data[i][j] = data[i][j + step];
                    } else {
                        data[i][j] = false;
                    }
                }
            } else if (t == 10) {
                //right shift
                int step = op[1];
                for (int j = m - 1; j >= 0; j--) {
                    if (j - step >= 0) {
                        data[i][j] = data[i][j - step];
                    } else {
                        data[i][j] = false;
                    }
                }
            } else if (t == 11) {
                int l = op[1];
                int r = op[2];
                output(builder, Arrays.copyOfRange(data[i], l, r + 1));
            }
            output(builder, data[i]);
        }
        return builder.toString();
    }

    private void output(StringBuilder builder, boolean[] data) {
        int sum = 0;
        int m = data.length;
        for (int j = 0; j < m; j++) {
            sum += data[j] ? 1 : 0;
        }
        builder.append(sum).append(' ');
        for (int j = 0; j < m; j++) {
            if (data[j]) {
                builder.append(j).append(' ');
            }
        }
        builder.append('\n');
    }
}
