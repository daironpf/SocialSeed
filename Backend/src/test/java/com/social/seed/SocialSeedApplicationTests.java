package com.social.seed;

import com.social.seed.repository.HashTagRepositoryTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SocialSeedApplicationTests {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(HashTagRepositoryTest.class /* Otras clases de prueba si las tienes */);

		if (result.wasSuccessful()) {
			System.out.println("All tests have passed.");
		} else {
			System.out.println("Some tests have failed.");

			for (Failure failure : result.getFailures()) {
				System.out.println(failure.toString());
			}
		}
	}
}
