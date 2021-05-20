package on2021_05.on2021_05_19_Library_Checker.Set_Xor_Min;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_05/on2021_05_19_Library_Checker/Set_Xor_Min/Set Xor-Min.json"))
			Assert.fail();
	}
}
