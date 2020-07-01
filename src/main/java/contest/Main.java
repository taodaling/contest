package contest;

public class Main {
    public static void main(String[] args) {
        int ans = 0;
        for (int i = 0; i < 500000; i++) {
            if(i > 100000 && i < 101000 && i % 100 == 0)
            System.out.println(i);
            ans += i;
        }
        System.out.println(ans);
    }


}
