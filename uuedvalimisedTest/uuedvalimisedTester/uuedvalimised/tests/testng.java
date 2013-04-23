package uuedvalimised.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class testng {
    int testInt;
    WebDriver webdriver;
 
    @BeforeMethod
    public void setUp() {
    	System.setProperty("webdriver.chrome.driver","chromedriver.exe");
    	webdriver = new ChromeDriver(); 
    	webdriver.get("http://uuedvalimised.appspot.com/index");
    	
    	
    }
 
    @Test
    public void addTest() {
        System.out.println("add test");
        try {
			Thread.sleep(10000);
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
 

    @AfterMethod
    public void afterMethod(){
        webdriver.quit();
    } 
}