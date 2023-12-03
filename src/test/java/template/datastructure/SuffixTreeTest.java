package template.datastructure;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class SuffixTreeTest {
    @Test
    public void test1(){
        Assert.assertTrue(testContain("abbac", "bba"));
    }

    @Test
    public void test2(){
        Assert.assertTrue(testContain("ababac", "bac"));
    }

    @Test
    public void test3(){
        Assert.assertFalse(testContain("ababac", "babc"));
    }

    @Test
    public void test4(){
        Assert.assertTrue(testContain("ababac", "aba"));
    }

    public boolean testContain(String a, String b){
        SuffixTree st = new SuffixTree(i -> a.charAt(i), a.length(), 'a', 'z');
        return st.contain(IntStream.range(0, b.length()).map(i -> b.charAt(i)).toArray(), 0, b.length() - 1);
    }
}