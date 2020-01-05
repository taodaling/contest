package on2020_01.on2020_01_04_Hello_2020.A__New_Year_and_Naming;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_01/on2020_01_04_Hello_2020/A__New_Year_and_Naming/A. New Year and Naming.json"))
			Assert.fail();
	}
}
