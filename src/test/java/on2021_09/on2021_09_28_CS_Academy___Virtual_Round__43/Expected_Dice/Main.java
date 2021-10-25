package on2021_09.on2021_09_28_CS_Academy___Virtual_Round__43.Expected_Dice;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2021_09/on2021_09_28_CS_Academy___Virtual_Round__43/Expected_Dice/Expected Dice.json"))
			Assert.fail();
	}
}
