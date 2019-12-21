package contest;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import template.FastInput;
import template.FastOutput;
import template.IntegerList;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] scores = new int[n];
        for (int i = 0; i < n; i++) {
            scores[i] = in.readInt();
        }

        long[] pre = new long[n];
        for(int t = 1; t < n - 1; t++){
            for(int j = )

            int remain = n - 1 - a;
            factors.clear();
            for (int t = 1; t * t <= remain; t++) {
                if (remain % t != 0) {
                    continue;
                }
                factors.add(t);
                if (t * t != remain) {
                    factors.add(remain / t);
                }
            }

            if (a == n - 1) {
                factors.add(1);
            }
            for (int i = 0; i < factors.size(); i++) {
                int t = factors.get(i);
                int b = a - t;
                if (b >= a || b < 0) {
                    continue;
                }
                int k = remain / t;
                if (b % (a - b) == 0) {
                    int meet = b / (a - b) + 1;
                    if (meet <= k) {
                        continue;
                    }
                }

                Query left = new Query();
                left.step = a - b;
                left.until = k * left.step;
                leftQueries[left.step].add(left);

                Query right = new Query();
                right.step = a - b;
                right.until = a;
                rightQueries[right.step].add(right);

                Result result = new Result();
                result.left = left;
                result.right = right;
                results.add(result);
            }
        }

        for (int i = 0; i < n; i++) {
            leftQueries[i].sort(Query.sortByUntil);
            rightQueries[i].sort(Query.reverseSortByUntil);
        }

        for (int i = 1; i < n; i++) {
            int scan = 0;
            int m = leftQueries[i].size();
            long sum = 0;
            for (int j = i; scan < m; j += i) {
                while (scan < m && leftQueries[i].get(scan).until < j) {
                    leftQueries[i].get(scan).ans = sum;
                    scan++;
                }
                if (j >= n) {
                    break;
                }
                sum += scores[j];
            }
        }

        for (int i = 1; i < n; i++) {
            int scan = 0;
            int m = rightQueries[i].size();
            long sum = 0;
            for (int j = n - 1; scan < m; j -= i) {
                while (scan < m && rightQueries[i].get(scan).until > j) {
                    rightQueries[i].get(scan).ans = sum;
                    scan++;
                }
                if (j <= 0) {
                    break;
                }
                sum += scores[j];
            }
        }

        long ans = -(long) 1e18;
        for (Result r : results) {
            long sum = r.left.ans + r.right.ans;
            ans = Math.max(ans, sum);
        }

        out.println(ans);
    }
}
