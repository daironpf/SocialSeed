package com.social.seed;

import com.social.seed.repository.HashTagRepositoryTest;
import com.social.seed.repository.PostRepositoryTest;
import com.social.seed.repository.SocialUserRepositoryTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@SpringBootTest
public class SocialSeedApplicationTests {
	Calculator underTest = new Calculator();

	@Test
	void itShouldAddTwoNumbers(){
		// given
		int numberOne = 20;
		int numberTwo = 30;

		// when
		int result = underTest.add(numberOne, numberTwo);

		// then
		int expected = 50;
		assertThat(result).isEqualTo(expected);
	}

	class Calculator{
		int add(int a, int b) {
			return a + b;
		}
	}

//	public static void main(String[] args) {
//		Result result = JUnitCore.runClasses(
//				HashTagRepositoryTest.class,
//				SocialUserRepositoryTest.class,
//				PostRepositoryTest.class
//				/* Otras clases de prueba si las tienes */
//		);
//
//		if (result.wasSuccessful()) {
//			System.out.println("All tests have passed.");
//		} else {
//			System.out.println("Some tests have failed.");
//
//			for (Failure failure : result.getFailures()) {
//				System.out.println(failure.toString());
//			}
//		}
//	}
}
