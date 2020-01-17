package template.datastructure;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
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
        SuffixTree st = new SuffixTree(a.length(), 'a', 'z');
        for(char c : a.toCharArray()){
            st.append(c);
        }
        int[] data = new int[b.length()];
        for(int i = 0; i < b.length(); i++){
            data[i] = b.charAt(i);
        }
        return st.contain(data, 0, data.length - 1);
    }
}