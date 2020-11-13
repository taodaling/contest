package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.BinaryTreeTraversalProblem;

import java.util.function.Consumer;

public class TreeTraversals {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] preOrder = new int[n];
        int[] inOrder = new int[n];
        in.populate(preOrder);
        in.populate(inOrder);
        for(int i = 0; i < n; i++){
            preOrder[i]--;
            inOrder[i]--;
        }
        BinaryTreeTraversalProblem.Node root = BinaryTreeTraversalProblem.travelByPreAndIn(preOrder, inOrder);
        postOrder(root, x -> out.append(x.id + 1).append(' '));
    }

    public static void postOrder(BinaryTreeTraversalProblem.Node root, Consumer<BinaryTreeTraversalProblem.Node> consumer) {
        if (root == null) {
            return;
        }
        postOrder(root.left, consumer);
        postOrder(root.right, consumer);
        consumer.accept(root);
    }
}
