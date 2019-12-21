package contest;

import template.graph.UndirectedEulerTrace;
import template.io.FastInput;
import template.io.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int c = in.readInt();
        int d = in.readInt();
        if (a > b) {
            if (c == 0 && d == 0 && a - b == 1) {
                out.println("YES");
                for (int i = 0; i < b; i++) {
                    out.append("0 1 ");
                }
                out.append("0");
                return;
            }
            out.println("NO");
            return;
        }
        if (c < d) {
            if (a == 0 && b == 0 && d - c == 1) {
                out.println("YES");
                for (int i = 0; i < c; i++) {
                    out.append("3 2 ");
                }
                out.append("3");
                return;
            }
            out.println("NO");
            return;
        }

        IntDeque leftDeque = new IntegerDequeImpl(a + b);
        IntDeque rightDeque = new IntegerDequeImpl(c + d);
        for(int i = 0; i < a; i++){
            leftDeque.addLast(0);
            leftDeque.addLast(1);
        }
        for(int i = 0; i < d; i++){
            rightDeque.addFirst(3);
            rightDeque.addFirst(2);
        }
        int left = b - a;
        int right = c - d;
        if (left < right) {
            right--;
            rightDeque.addLast(2);
        }
        if(left > right){
            left--;
            leftDeque.addFirst(1);
        }
        if(left != right){
            out.println("NO");
            return;
        }
        out.println("YES");
        while(!leftDeque.isEmpty()){
            out.append(leftDeque.removeFirst()).append(' ');
        }
        for(int i = 0; i < left; i++){
            out.append(2).append(' ').append(1).append(' ');
        }
        while(!rightDeque.isEmpty()){
            out.append(rightDeque.removeFirst()).append(' ');
        }
    }
}
