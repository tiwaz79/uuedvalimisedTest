package uuedvalimised.tests;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class UuedValimisedTest {
	
	private boolean runFromProduction = true;
	
	
    WebDriver driver;
    String winHandleBefore;
    
    private String voteArea;
    private int candNr;
    private int candidancyArea;
    private int appliedPartyInt;
    private String searchName;
    private String fbLoginName;
    private String fbPassword;
    
 
    @BeforeMethod
    public void setUp() throws InterruptedException{
    	
    	Properties prop = new Properties();
    	
    	try{
    		if(runFromProduction){
    			prop.load(new FileInputStream("test/testParameters.properties"));
    		}else{
    			prop.load(new FileInputStream("parameters.properties"));
    		}
    		
    		voteArea=prop.getProperty("voteArea");
    		candNr=Integer.parseInt(prop.getProperty("candNr"));;
    		candidancyArea=Integer.parseInt(prop.getProperty("candidancyArea"));
    		appliedPartyInt=Integer.parseInt(prop.getProperty("appliedPartyInt"));
    		searchName=prop.getProperty("searchName");
    		fbLoginName=prop.getProperty("fbLoginName");
    		fbPassword=prop.getProperty("fbPassword");
    		
    		
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    	if(runFromProduction){
    		System.setProperty("webdriver.chrome.driver","test/chromedriver.exe");
    	}else{
    		System.setProperty("webdriver.chrome.driver","chromedriver.exe");
    	}
    	driver = new ChromeDriver(); 
    	driver.get("http://uuedvalimised.appspot.com");
    	
    	Thread.sleep(3000);
    	login();
    	
    	
    }
 
    @Test
    public void addVoteTest() throws InterruptedException{
    	Thread.sleep(1000);
    	driver.findElement(By.id("tab2")).click();
    	JavascriptExecutor executor = (JavascriptExecutor)driver;
    	executor.executeScript("alert = function() {}");
    	Thread.sleep(1000);
    	
    	
    	int valimisPiirkondLength = Integer.parseInt(voteArea) < 10 ? 22 : 23;
    	
    	WebElement select2 = driver.findElement(By.id("selection"));
    	String votedCandidateRegion = "";
    	List <WebElement> options2 = select2.findElements(By.tagName("option"));
    	for(WebElement option2 : options2){
    		if(option2.getAttribute("value").equals("Images/Valimisringkonnad/Valimisringkond_nr_"+voteArea+".png")){
    			votedCandidateRegion = option2.getText().substring(valimisPiirkondLength);
    			option2.click();
    			break;
    			}
    		}
    	
    	Thread.sleep(1500);
    	
    	driver.findElement(By.id("candidateSelect"+candNr)).click();
    	String candName = driver.findElement(By.id("candidateName"+candNr)).getText();
    	candName = candName.replaceAll(" ", "");
    	String candParty = driver.findElement(By.id("candidateParty"+candNr)).getText();
    	
    	driver.findElement(By.id("voteBtn")).click();
    	Thread.sleep(2000);
    	
    	driver.findElement(By.id("unVoteBtn")).click();
    	
       	String divText = driver.findElement(By.id("votedFor")).getText();
       	
       	if(divText.contains(candName) && divText.contains(candParty) && divText.contains(votedCandidateRegion)){
       		Assert.assertTrue(true);
       	}else{
       		Assert.assertTrue(false, "Vote test failed: either area, candidate name or party doesn't match");
       	}
    	
    	
    }
    
    @Test
    public void addCandidancyTest() throws InterruptedException{
    	Thread.sleep(1000);
    	driver.findElement(By.id("tab5")).click();
    	
    	JavascriptExecutor executor = (JavascriptExecutor)driver;
    	executor.executeScript("alert = function() {}");
    	
    	Thread.sleep(3000);
    	
    	WebElement select = driver.findElement(By.id("applicationArea"));
    	String appliedArea = "";
    	
    	
    	int valimisPiirkondLength = candidancyArea < 10 ? 22 : 23;

    	
    	List <WebElement> options = select.findElements(By.tagName("option"));
    	for(WebElement option : options){
    		if(option.getAttribute("value").equals(String.valueOf(candidancyArea))){
    			appliedArea = option.getText().substring(valimisPiirkondLength);
    			option.click();
    			break;
    			}
    		}
    	Thread.sleep(500);
    	WebElement select2 = driver.findElement(By.id("applicationParty"));
    	String appliedParty = "";
    	List <WebElement> options2 = select2.findElements(By.tagName("option"));
    	for(WebElement option2 : options2){
    		if(option2.getAttribute("value").equals(String.valueOf(this.appliedPartyInt))){
    			appliedParty = option2.getText();
    			option2.click();
    			break;
    			}
    		}
    	Thread.sleep(500);
       	driver.findElement(By.id("kandideeri")).click();
       	Thread.sleep(100);
       	
       	Thread.sleep(500);
       	
       	String divText = driver.findElement(By.id("candidateAs")).getText();
       	
       	if(divText.contains(appliedArea) && divText.contains(appliedParty)){
       		Assert.assertTrue(true);
       	}else{
       		driver.findElement(By.id("uncandidate")).click();
       		Assert.assertTrue(false, "Add candidacy test failed: either party or area doesn't match");
       	}
       	driver.findElement(By.id("uncandidate")).click();
       	
       	Thread.sleep(2000);
    }
    
    @Test
    public void addFindCandidateTest() throws InterruptedException{
    	
    	
    	Thread.sleep(1000);
    	driver.findElement(By.id("tab6")).click();
    	Thread.sleep(2000);
    	driver.findElement(By.name("candidateSearchByName")).sendKeys(searchName);
    	Thread.sleep(1000);
    	
    	driver.findElement(By.id("otsiNupp")).click();
    	
    	Thread.sleep(1000);
    	
    	List<WebElement> elementsOdd = driver.findElements(By.className("odd"));
    	List<WebElement> elementsEven = driver.findElements(By.className("even"));
    	
    	boolean matchFound = false;
    	
    	searchName = searchName.toUpperCase();
    	
    	for(WebElement element : elementsEven){
    		if(!matchFound){
    			String elementText = element.getText().toUpperCase();
    			if(elementText.contains(searchName)){
    				matchFound = true;
    				break;
    			}
    		}
    		
    	}
    	for(WebElement element : elementsOdd){
    		if(!matchFound){
    			String elementText = element.getText().toUpperCase();
    			if(elementText.contains(searchName)){
    				matchFound = true;
    				break;
    			}
    		}else{
    			break;
    		}
    		
    	}
    	
    	Assert.assertTrue(matchFound, "Testing searching name failed. Didn't find matching name");
    	
    	Thread.sleep(1000);
    	
    }
    
 

    @AfterMethod
    public void afterMethod(){
        driver.quit();
    } 
    
    private void login() throws InterruptedException{
    	((JavascriptExecutor)driver).executeScript("window.alert = function(msg){};");
    	 
    	driver.findElement(By.id("fbloginBtn")).click();
    	Thread.sleep(1000);
    	 winHandleBefore = driver.getWindowHandle();

    	 for(String winHandle : driver.getWindowHandles()){
    	     driver.switchTo().window(winHandle);
    	 }

    	Thread.sleep(1000);
    	
    	driver.findElement(By.id("email")).sendKeys(fbLoginName);
    	driver.findElement(By.id("pass")).sendKeys(fbPassword);
    	
    	
    	driver.findElement(By.id("u_0_1")).click();
    	Thread.sleep(2000);
    	
    	driver.switchTo().window(winHandleBefore);
    	
    }
    
}