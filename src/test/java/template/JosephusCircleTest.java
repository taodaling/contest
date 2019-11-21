package template;

import org.junit.Assert;
import org.junit.Test;
import template.algo.JosephusCircle;

public class JosephusCircleTest {
    @Test
    public void test1(){
        Assert.assertEquals(1, JosephusCircle.dieTime(5, 1, 0));
        Assert.assertEquals(2, JosephusCircle.dieTime(5, 1, 1));
        Assert.assertEquals(3, JosephusCircle.dieTime(5, 1, 2));
        Assert.assertEquals(4, JosephusCircle.dieTime(5, 1, 3));
        Assert.assertEquals(5, JosephusCircle.dieTime(5, 1, 4));
    }

    @Test
    public void test2(){
        Assert.assertEquals(2, JosephusCircle.dieTime(5, 3, 0));
        Assert.assertEquals(4, JosephusCircle.dieTime(5, 3, 1));
        Assert.assertEquals(1, JosephusCircle.dieTime(5, 3, 2));
        Assert.assertEquals(5, JosephusCircle.dieTime(5, 3, 3));
        Assert.assertEquals(3, JosephusCircle.dieTime(5, 3, 4));
    }

    @Test
    public void test3(){
        JosephusCircle.dieTime(1000000000, 1000, 1);
    }

    @Test
    public void test4(){
        Assert.assertEquals(8, JosephusCircle.dieTime(10, 2, 0));
        Assert.assertEquals(3, JosephusCircle.dieTime(4, 2, 2));
        Assert.assertEquals(8, JosephusCircle.dieTime(11, 3, 10));
    }

    @Test
    public void test5(){
        for(int i = 1; i <= 100; i++){
            for(int j = 1; j <= 100; j++){
                for(int k = 0; k < i; k++){
                    if(JosephusCircle.dieTimeBF(i, j, k) !=
                            JosephusCircle.dieTime(i, j, k)){
                        throw new RuntimeException(String.format("fail on (%d, %d, %d)", i, j, k));
                    }
                }
            }
        }
    }

    @Test
    public void test6(){
        for(int i = 1; i <= 100; i++){
            for(int j = 1; j <= 100; j++){
                for(int k = 0; k < i; k++){
                    if(JosephusCircle.dieAtRound(i, j, JosephusCircle.dieTime(i, j, k)) != k){
                        throw new RuntimeException(String.format("fail on (%d, %d, %d)", i, j, k));
                    }
                }
            }
        }
    }

    @Test
    public void test7(){
        Assert.assertEquals(0, JosephusCircle.dieAtRoundBF(5, 3, 2));
        Assert.assertEquals(1, JosephusCircle.dieAtRoundBF(5, 3, 4));
        Assert.assertEquals(2, JosephusCircle.dieAtRoundBF(5, 3, 1));
        Assert.assertEquals(3, JosephusCircle.dieAtRoundBF(5, 3, 5));
        Assert.assertEquals(4, JosephusCircle.dieAtRoundBF(5, 3, 3));
    }

    @Test
    public void test8(){
        Assert.assertEquals(0, JosephusCircle.dieAtRound(5, 3, 2));
        Assert.assertEquals(1, JosephusCircle.dieAtRound(5, 3, 4));
        Assert.assertEquals(2, JosephusCircle.dieAtRound(5, 3, 1));
        Assert.assertEquals(3, JosephusCircle.dieAtRound(5, 3, 5));
        Assert.assertEquals(4, JosephusCircle.dieAtRound(5, 3, 3));
    }

    @Test
    public void test9(){
        Assert.assertEquals(0, JosephusCircle.dieAtRound(1, 1, 1));
        Assert.assertEquals(0, JosephusCircle.dieAtRound(2, 1, 1));
    }
}