package on2021_06.on2021_06_22_.Brief_Statements_Union;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_06/on2021_06_22_/Brief_Statements_Union/Brief Statements Union.json"))
			Assert.fail();
	}
}
