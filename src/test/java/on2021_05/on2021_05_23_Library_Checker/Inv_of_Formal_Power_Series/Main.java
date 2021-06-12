package on2021_05.on2021_05_23_Library_Checker.Inv_of_Formal_Power_Series;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_05/on2021_05_23_Library_Checker/Inv_of_Formal_Power_Series/Inv of Formal Power Series.json"))
			Assert.fail();
	}
}
