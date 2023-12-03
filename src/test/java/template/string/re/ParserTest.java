package template.string.re;

import org.junit.Assert;
import org.junit.Test;
import template.rand.RandomWrapper;

public class ParserTest {
    private Matcher gen(String s) {
        return Parser.parse(s).newMatcher();
    }

    private boolean fastTest(String a, String b) {
        return gen(a).match(b, 0, b.length());
    }

    @Test
    public void test1() {
        Assert.assertTrue(fastTest("a", "a"));
    }

    @Test
    public void test3() {
        Assert.assertTrue(fastTest(".", "a"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test4() {
        Assert.assertTrue(fastTest("?", ""));
    }

    @Test
    public void test5() {
        Assert.assertTrue(fastTest("88?", "88"));
    }

    @Test
    public void test6() {
        Assert.assertTrue(fastTest("88?", "8"));
    }

    @Test
    public void test7() {
        Assert.assertTrue(fastTest("ab*\\(", "abbb("));
    }

    @Test
    public void test8() {
        Assert.assertTrue(fastTest("((ab|c)*1..)", "ccc135"));
    }

    @Test
    public void test9() {
        Assert.assertTrue(fastTest("((ab|c)*(1.).)", "ab123"));
    }

    @Test
    public void test10() {
        Assert.assertTrue(fastTest("a|b|c", "b"));
    }

    @Test
    public void test11() {
        Assert.assertFalse(fastTest("a|b|c", "bc"));
    }

    @Test
    public void test12() {
        Assert.assertTrue(fastTest("a|bc|c", "bc"));
    }

    @Test
    public void test13() {
        Assert.assertFalse(fastTest("a|bc|c", "cb"));
    }

    @Test
    public void test14() {
        Assert.assertFalse(fastTest("a|bc|c", "cb"));
    }

    @Test
    public void test16() {
        Assert.assertFalse(fastTest("\\.123", "1123"));
    }


    @Test
    public void test15() {
        Assert.assertArrayEquals(new int[]{0, 4}, gen("1111").find("111111", 0, 6).get());
    }

    @Test
    public void hugeTest() {
        String s = RandomWrapper.INSTANCE.nextString('a', 'z', 10000);
        String t = RandomWrapper.INSTANCE.nextString('a', 'z', 10000);
        fastTest(s, t);
    }
}