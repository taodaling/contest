package on2021_06.on2021_06_21_Library_Checker.Inverse_Matrix;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_06/on2021_06_21_Library_Checker/Inverse_Matrix/Inverse Matrix.json"))
			Assert.fail();
	}
}
