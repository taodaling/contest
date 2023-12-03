package template.math;

import org.junit.Assert;
import org.junit.Test;
import template.rand.RandomWrapper;

import java.math.BigInteger;
import java.text.MessageFormat;

import static org.junit.Assert.*;

public class BigInt10Test {
//    public static void main(String[] args) {
//        BigInt10Test ob = new BigInt10Test();
////        ob.randomAddTest();
////        ob.randomSubTest();
////        ob.randomMulTest();
//        ob.randomDivTest();
//    }

    private String randomNum(int len) {
        String ans = RandomWrapper.INSTANCE.nextString('0', '9', len);
        if (RandomWrapper.INSTANCE.nextInt(0, 1) == 0) {
            ans = "-" + ans;
        }
//        boolean allZero = true;
//        for (char c : ans.toCharArray()) {
//            if (Character.isDigit(c) && c != '0') {
//                allZero = false;
//            }
//        }
//        if (allZero) {
//            return randomNum(len);
//        }
        return ans;
    }

    int n = 100;
    int len = 10;

    private void randomAddTest() {
        for (int i = 0; i < n; i++) {
            String a = randomNum(RandomWrapper.INSTANCE.nextInt(1, len));
            String b = randomNum(RandomWrapper.INSTANCE.nextInt(1, len));

            String template = "@Test\n" +
                    "    public void test{2}_Plus_{3}()'{'\n" +
                    "        String a = \"{0}\";\n" +
                    "        String b = \"{1}\";\n" +
                    "        BigInteger bi = new BigInteger(a).add(new BigInteger(b));\n" +
                    "        BigInt10 bi10 = new BigInt10(a).add(new BigInt10(b));\n" +
                    "        Assert.assertEquals(bi.toString(), bi10.toString());\n" +
                    "    '}'";

            System.out.println(MessageFormat.format(template, a, b,
                    a.replaceAll("-", "_"), b.replaceAll("-", "_")));
        }
    }

    private void randomSubTest() {
        for (int i = 0; i < n; i++) {
            String a = randomNum(RandomWrapper.INSTANCE.nextInt(1, len));
            String b = randomNum(RandomWrapper.INSTANCE.nextInt(1, len));

            String template = "@Test\n" +
                    "    public void test{2}_Sub_{3}()'{'\n" +
                    "        String a = \"{0}\";\n" +
                    "        String b = \"{1}\";\n" +
                    "        BigInteger bi = new BigInteger(a).subtract(new BigInteger(b));\n" +
                    "        BigInt10 bi10 = new BigInt10(a).sub(new BigInt10(b));\n" +
                    "        Assert.assertEquals(bi.toString(), bi10.toString());\n" +
                    "    '}'";

            System.out.println(MessageFormat.format(template, a, b,
                    a.replaceAll("-", "_"), b.replaceAll("-", "_")));
        }
    }

    private void randomMulTest() {
        for (int i = 0; i < n; i++) {
            String a = randomNum(RandomWrapper.INSTANCE.nextInt(1, len));
            String b = randomNum(RandomWrapper.INSTANCE.nextInt(1, len));

            String template = "@Test\n" +
                    "    public void test{2}_Mul_{3}()'{'\n" +
                    "        String a = \"{0}\";\n" +
                    "        String b = \"{1}\";\n" +
                    "        BigInteger bi = new BigInteger(a).multiply(new BigInteger(b));\n" +
                    "        BigInt10 bi10 = new BigInt10(a).mul(new BigInt10(b));\n" +
                    "        Assert.assertEquals(bi.toString(), bi10.toString());\n" +
                    "    '}'";

            System.out.println(MessageFormat.format(template, a, b,
                    a.replaceAll("-", "_"), b.replaceAll("-", "_")));
        }
    }

    private void randomDivTest() {
        for (int i = 0; i < n; i++) {
            String a = randomNum(RandomWrapper.INSTANCE.nextInt(1, len));
            String b = randomNum(RandomWrapper.INSTANCE.nextInt(1, len));

            String template = "@Test\n" +
                    "    public void test{2}_div_{3}()'{'\n" +
                    "        String a = \"{0}\";\n" +
                    "        String b = \"{1}\";\n" +
                    "        BigInteger bi = new BigInteger(a).divide(new BigInteger(b));\n" +
                    "        BigInt10 bi10 = new BigInt10(a).div(new BigInt10(b));\n" +
                    "        Assert.assertEquals(bi.toString(), bi10.toString());\n" +
                    "    '}'";

            System.out.println(MessageFormat.format(template, a, b,
                    a.replaceAll("-", "_"), b.replaceAll("-", "_")));
        }
    }

}