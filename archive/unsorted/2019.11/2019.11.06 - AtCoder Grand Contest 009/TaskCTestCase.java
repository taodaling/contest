package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.RandomWrapper;

public class TaskCTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));
    public Test create(int testNum){
        int n = random.nextInt(1, 5);
        int a = random.nextInt(1, 10);
        int b = random.nextInt(1, 10);

        TreeSet<Integer> set = new TreeSet<>();
        while(set.size() < n){
            int v = random.nextInt(0, 50);
            set.add(v);
        }

        int[] data = set.stream().mapToInt(Integer::intValue).toArray();
        int ans = solve(data, a, b, -(int)1e8, -(int)1e8, 0);

        StringBuilder builder = new StringBuilder();
        builder.append(n).append(' ').append(a).append(' ').append(b).append('\n');
        for(int v : data){
            builder.append(v).append(' ');
        }

        return new Test(builder.toString(), Integer.toString(ans));
    }

    public int solve(int[] data, int a, int b, int lastA, int lastB, int i){
        if(i == data.length){
            return 1;
        }
        int ans = 0;
        if(lastA + a <= data[i]){
            ans += solve(data, a, b, data[i], lastB, i + 1);
        }
        if(lastB + b <= data[i]){
            ans += solve(data, a, b, lastA, data[i], i + 1);
        }
        return ans;
    }
}
