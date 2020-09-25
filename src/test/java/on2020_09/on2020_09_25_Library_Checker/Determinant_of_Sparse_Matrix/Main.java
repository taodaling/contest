package on2020_09.on2020_09_25_Library_Checker.Determinant_of_Sparse_Matrix;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_09/on2020_09_25_Library_Checker/Determinant_of_Sparse_Matrix/Determinant of Sparse Matrix.json"))
			Assert.fail();
	}
}
