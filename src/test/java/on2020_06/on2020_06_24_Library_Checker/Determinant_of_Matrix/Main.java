package on2020_06.on2020_06_24_Library_Checker.Determinant_of_Matrix;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_06/on2020_06_24_Library_Checker/Determinant_of_Matrix/Determinant of Matrix.json"))
			Assert.fail();
	}
}
