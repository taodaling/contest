package on2021_03.on2021_03_25_Library_Checker.Exp_of_Formal_Power_Series;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_03/on2021_03_25_Library_Checker/Exp_of_Formal_Power_Series/Exp of Formal Power Series.json"))
			Assert.fail();
	}
}
