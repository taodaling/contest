package on2021_11.on2021_11_16_Library_Checker.Associative_Array;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_11/on2021_11_16_Library_Checker/Associative_Array/Associative Array.json"))
			Assert.fail();
	}
}
