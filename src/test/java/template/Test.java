package template;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {

        int time = 10;
        Test obj = new Test((int) 1e8);
        for (int i = 0; i < time; i++) {
            System.out.println("round " + i + "...");
            test("empty", obj::empty);
            test("plus", obj::plus);
            test("plusMultiway", obj::plusMultiway);
            test("longPlus", obj::longPlus);
            test("subtract", obj::subtract);
            test("mul", obj::mul);
            test("longMul", obj::longMul);
            test("div", obj::div);
            test("mod", obj::mod);
            test("invoke", obj::invoke);
            test("and", obj::and);
            test("choose", obj::choose);
            test("doublePlus", obj::doublePlus);
            test("doubleMul", obj::doubleMul);
            test("doubleDiv", obj::doubleDiv);
            test("mulModByInt", obj::mulModByInt);
            test("mulModByLong", obj::mulModByLong);
        }

        for (String key : elapse.keySet()) {
            System.out.println(key + " finished in " + Math.round(elapse.get(key) / (double) time) + "ms");
        }
    }

    private static Map<String, Long> elapse = new HashMap<>();

    public static void test(String name, Runnable task) {
        long now = System.currentTimeMillis();
        task.run();
        long end = System.currentTimeMillis();
        elapse.put(name, elapse.getOrDefault(name, 0L) + end - now);
    }

    int round;

    public Test(int round) {
        this.round = round;
    }

    public Test() {
        this((int) 1e9);
    }


    public void plus() {
        int sum = 0;
        for (int i = 1; i <= round; i++) {
            sum += i;
        }
    }

    public void plusMul() {
        int sum = 0;
        for (int i = 1; i <= round; i++) {
            sum += i;
            sum *= i;
        }
    }

    public void choose() {
        int sum = 0;
        for (int i = 1; i <= round; i++) {
            if (sum < 0) {
                sum += i;
            } else {
                sum -= i;
            }
        }
    }

    public void plusMultiway() {
        int sum = 0;
        for (int i = 1; i <= round; i += 2) {
            sum += i;
            sum += i + 1;
        }
    }

    public void mulModByInt() {
        int sum = 1;
        int mod = (int) (1e9 + 7);
        for (int i = 1; i <= round; i++) {
            sum = (int) ((long) sum * i % mod);
        }
    }

    public void mulModByLong() {
        long sum = 1;
        int mod = (int) (1e9 + 7);
        for (int i = 1; i <= round; i++) {
            sum = (sum * i % mod);
        }
    }


    public void subtract() {
        int sum = 0;
        for (int i = 1; i <= round; i++) {
            sum -= i;
        }
    }


    public void mul() {
        int sum = 1;
        for (int i = 1; i <= round; i++) {
            sum *= i;
        }
    }


    public void longMul() {
        long sum = 1;
        for (int i = 1; i <= round; i++) {
            sum *= i;
        }
    }

    public void longPlus() {
        long sum = 1;
        for (int i = 1; i <= round; i++) {
            sum += i;
        }
    }


    public void div() {
        int sum = 1;
        for (int i = 1; i <= round; i++) {
            sum /= i;
        }
    }


    public void mod() {
        int sum = 1;
        for (int i = 1; i <= round; i++) {
            sum %= i;
        }
    }


    public void empty() {
        int sum = 1;
        for (int i = 1; i <= round; i++) {
        }
    }

    private int simpleInvoke(int x, int y) {
        return local += x + y;
    }

    private void doublePlus() {
        double sum = 1;
        for (int i = 1; i <= round; i++) {
            sum += i;
        }
    }

    private void doubleMul() {
        double sum = 1;
        for (int i = 1; i <= round; i++) {
            sum *= i;
        }
    }

    private void doubleDiv() {
        double sum = 1;
        for (int i = 1; i <= round; i++) {
            sum /= i;
        }
    }


    int local = 0;


    public void invoke() {
        int sum = 1;
        for (int i = 1; i <= round; i++) {
            sum = simpleInvoke(sum, i);
        }
    }


    public void and() {
        int sum = 1;
        for (int i = 1; i <= round; i++) {
            sum &= i;
        }
    }
}

