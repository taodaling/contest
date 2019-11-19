package template;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SAISTest {

    @Test
    public void test1(){
        SAIS sais = new SAIS("123".toCharArray());
        Assert.assertEquals(0, sais.queryRank(0));
        Assert.assertEquals(1, sais.queryRank(1));
        Assert.assertEquals(2, sais.queryRank(2));
        Assert.assertEquals(0, sais.queryKth(0));
        Assert.assertEquals(1, sais.queryKth(1));
        Assert.assertEquals(2, sais.queryKth(2));
        Assert.assertEquals(0, sais.longestCommonPrefixBetween(0));
        Assert.assertEquals(0, sais.longestCommonPrefixBetween(1));
        Assert.assertEquals(0, sais.longestCommonPrefixBetween(2));
    }

    @Test
    public void test2(){
        SAIS sais = new SAIS("2131".toCharArray());
        Assert.assertEquals(2, sais.queryRank(0));
        Assert.assertEquals(1, sais.queryRank(1));
        Assert.assertEquals(3, sais.queryRank(2));
        Assert.assertEquals(0, sais.queryRank(3));

        Assert.assertEquals(3, sais.queryKth(0));
        Assert.assertEquals(1, sais.queryKth(1));
        Assert.assertEquals(0, sais.queryKth(2));
        Assert.assertEquals(2, sais.queryKth(3));


        Assert.assertEquals(0, sais.longestCommonPrefixBetween(0));
        Assert.assertEquals(1, sais.longestCommonPrefixBetween(1));
        Assert.assertEquals(0, sais.longestCommonPrefixBetween(2));
        Assert.assertEquals(0, sais.longestCommonPrefixBetween(3));
    }

    @Test
    public void test3(){
        SAIS sais = new SAIS("2111".toCharArray());
        Assert.assertEquals(3, sais.queryRank(0));
        Assert.assertEquals(2, sais.queryRank(1));
        Assert.assertEquals(1, sais.queryRank(2));
        Assert.assertEquals(0, sais.queryRank(3));

        Assert.assertEquals(3, sais.queryKth(0));
        Assert.assertEquals(2, sais.queryKth(1));
        Assert.assertEquals(1, sais.queryKth(2));
        Assert.assertEquals(0, sais.queryKth(3));


        Assert.assertEquals(0, sais.longestCommonPrefixBetween(0));
        Assert.assertEquals(1, sais.longestCommonPrefixBetween(1));
        Assert.assertEquals(2, sais.longestCommonPrefixBetween(2));
        Assert.assertEquals(0, sais.longestCommonPrefixBetween(3));
    }

}