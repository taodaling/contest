package on2021_04.on2021_04_17_Library_Checker.Matrix_Product;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_04/on2021_04_17_Library_Checker/Matrix_Product/Matrix Product.json"))
			Assert.fail();
	}
}
