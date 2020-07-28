package on2020_07.on2020_07_25_hackercup_2020_qualification_round.Alchemy;

import net.egork.chelper.tester.NewTester;

import org.junit.Assert;
import org.junit.Test;

public class Main {
	@Test
	public void test() throws Exception {
		if (!NewTester.test("src/test/java/on2020_07/on2020_07_25_hackercup_2020_qualification_round/Alchemy/Alchemy.json"))
			Assert.fail();
	}
}
