package platform.niuke;

public class Solution {
    public void solve () {
        try{
            Thread  t = new Thread(null, () -> {
            }, "", 1 << 29);
            t.start();
            t.join();
        }catch(Exception e){}
    }

}

