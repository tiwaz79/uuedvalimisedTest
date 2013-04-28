package uuedvalimised.tests;

import org.testng.TestListenerAdapter;

import org.testng.TestNG;

public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		testng.setTestClasses(new Class[] { UuedValimisedTest.class });
		testng.addListener(tla);
		testng.run(); 

	}

}
