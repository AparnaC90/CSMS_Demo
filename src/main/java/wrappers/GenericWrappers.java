package wrappers;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import keyword.Passfail;
import keyword.RunScripts;
import utils.Reporter;

public class GenericWrappers {

	protected static RemoteWebDriver driver;
	protected static Properties prop;
	public String sUrl,primaryWindowHandle,sHubUrl,sHubPort;
	int i=0;
	//public static String strExcelFileName="";
	String file = Passfail.pf.FileName;
	public GenericWrappers() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./config.properties")));
			sHubUrl = prop.getProperty("HUB");
			sHubPort = prop.getProperty("PORT");
			sUrl = prop.getProperty("URL");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will launch only firefox and maximise the browser and set the
	 * wait for 30 seconds and load the url
	 * @author -Karthikeyan
	 * @param url - The url with http or https
	 * 
	 */
	public boolean invokeApp(String browser) {
		boolean bReturn = false;
		try {

			/*	DesiredCapabilities dc = new DesiredCapabilities();
			dc.setBrowserName(browser);
			dc.setPlatform(Platform.WINDOWS);*/
			if(browser.equalsIgnoreCase("chrome")){
				System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
				driver = new ChromeDriver();
			}else if(browser.equalsIgnoreCase("ie")){
				System.setProperty("webdriver.ie.driver", "./drivers/IEDriverServer.exe");
				driver = new InternetExplorerDriver();
			}else if(browser.equalsIgnoreCase("firefox")){	
				System.setProperty("webdriver.gecko.driver", "./drivers/geckodriver.exe");
				driver = new FirefoxDriver();
			}
			else if(browser.equalsIgnoreCase("opera")){	
				System.setProperty("webdriver.opera.driver", "./drivers/operadriver.exe");
				driver = new OperaDriver();
			}


			//	driver = new RemoteWebDriver(new URL("http://"+sHubUrl+":"+sHubPort+"/wd/hub"), dc);

			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.get(sUrl);

			primaryWindowHandle = driver.getWindowHandle();		
			Reporter.reportStep("The browser:" + browser + " launched successfully", "PASS");
			bReturn = true;

		} catch (Exception e) {
			e.printStackTrace();
			Reporter.reportStep("The browser:" + browser + " could not be launched", "FAIL");
		}
		return bReturn;
	}

	/**
	 * This method will enter the value to the text field using id attribute to locate
	 * 
	 * @param idValue - id of the webelement
	 * @param data - The data to be sent to the webelement
	 * @author -Karthikeyan
	 * @throws IOException 
	 * @throws COSVisitorException 
	 */
	public boolean enterById(String idValue, String data) {
		boolean bReturn = false;

		try {
			Thread.sleep(1500);
			driver.findElement(By.id(idValue)).clear();
			driver.findElement(By.id(idValue)).sendKeys(data);	
			Reporter.reportStep("The data: "+data+" entered successfully in field :"+idValue, "PASS");

			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The data: "+data+" could not be entered in the field :"+idValue, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	public boolean enterByName(String Name, String data) {
		boolean bReturn = false;

		try {
			Thread.sleep(6000);
			driver.findElement(By.id(Name)).clear();
			driver.findElement(By.id(Name)).sendKeys(data);	
			Reporter.reportStep("The data: "+data+" entered successfully in field :"+Name, "PASS");

			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The data: "+data+" could not be entered in the field :"+Name, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	public boolean enterByXpath(String Xpathvalue, String data) {
		boolean bReturn = false;
		try {
			driver.findElement(By.xpath(Xpathvalue)).clear();
			driver.findElement(By.xpath(Xpathvalue)).sendKeys(data);	
			Reporter.reportStep("The data: "+data+" entered successfully in field :"+Xpathvalue, "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The data: "+data+" could not be entered in the field :"+Xpathvalue, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	public boolean searchById(String idValue, String data) {
		boolean bReturn = false;

		try {
			Thread.sleep(3000);
			//driver.findElement(By.id(idValue)).clear();
			driver.findElement(By.id(idValue)).sendKeys(data);	
			Reporter.reportStep("The data: "+data+" entered successfully in field :"+idValue, "PASS");

			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The data: "+data+" could not be entered in the field :"+idValue, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}



	public boolean  switchToLastWindow() {
		boolean bReturn = false;
		try {
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				driver.switchTo().window(winHandle);
			}
			Reporter.reportStep("Switch to Last window was successful","PASS");
			passfail(file, "Pass");
			bReturn = true;
		} catch (WebDriverException e) {
			passfail(file, "Fail");
			System.out.println("The Browser could not be found");
		} return bReturn;

	}

	public boolean acceptAlert() {
		boolean bReturn = false;
		try {
			driver.switchTo().alert().accept();
			Reporter.reportStep("The alert found successful","PASS");
			passfail(file, "Pass");
			bReturn = true;
		} catch (NoAlertPresentException e) {
			Reporter.reportStep("The alert could not be found","Fail");
			passfail(file, "Fail");
		}
		return bReturn;
	}


	public boolean  switchToParentWindow() {
		boolean bReturn = false;
		try {
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				driver.switchTo().window(winHandle);
				break;
			}
			Reporter.reportStep("Switch to primary window was successful","PASS");
			passfail(file, "Pass");
			bReturn = true;
		} catch (WebDriverException e) {
			passfail(file, "Fail");
			System.out.println("The Browser could not be found");
		} return bReturn;

	}

	/**
	 * This method will verify the title of the browser 
	 * @param title - The expected title of the browser
	 * @author -Karthikeyan
	 */
	public boolean verifyTitle(String title){
		boolean bReturn = false;
		try{
			if (driver.getTitle().equalsIgnoreCase(title)){
				Reporter.reportStep("The title of the page matches with the value :"+title, "PASS");
				passfail(file, "Pass");
				bReturn = true;
			}else
				Reporter.reportStep("The title of the page:"+driver.getTitle()+" did not match with the value :"+title, "SUCCESS");
			passfail(file, "Pass");

		}catch (Exception e) {
			Reporter.reportStep("The title did not match", "FAIL");
			passfail(file, "Fail");
		}

		return bReturn;
	}

	/**
	 * This method will verify the given text
	 * @param xpath - The locator of the object in xpath
	 * @param text  - The text to be verified
	 * @author -Karthikeyan
	 * @throws InterruptedException 
	 */
	public boolean verifyTextByXpath(String xpath, String text) throws InterruptedException{
		boolean bReturn = false;
		Thread.sleep(2000);
		String sText = driver.findElementByXPath(xpath).getText();
		if (driver.findElementByXPath(xpath).getText().trim().equalsIgnoreCase(text)){
			Reporter.reportStep("The text: "+sText+" matches with the value :"+text, "PASS");
			passfail(file, "Pass");
			bReturn = true;
		}else{
			Reporter.reportStep("The text: "+sText+" did not match with the value :"+text, "FAIL");
			passfail(file, "Fail");
		}


		return bReturn;
	}

	public boolean verifyTextByXpathResult(String xpath, String result) throws InterruptedException{
		boolean bReturn = false;
		Thread.sleep(2000);
		String sText = driver.findElementByXPath(xpath).getText();
		if (driver.findElementByXPath(xpath).getText().trim().equalsIgnoreCase(result)){
			Reporter.reportStep("The text: "+sText+" matches with the value :"+result, "PASS");
			passfail(file, "Pass");
			bReturn = true;
		}else{
			Reporter.reportStep("The text: "+sText+" did not match with the value :"+result, "FAIL");
			passfail(file, "Fail");
		}


		return bReturn;
	}

	/**
	 * This method will verify the given text
	 * @param xpath - The locator of the object in xpath
	 * @param text  - The text to be verified
	 * @author -Karthikeyan
	 * @throws Throwable 
	 */
	public boolean verifyTextContainsByXpath(String xpath, String text) {
		boolean bReturn = false;

		String sText = driver.findElementByXPath(xpath).getText();
		if (driver.findElementByXPath(xpath).getText().trim().contains(text)){
			Reporter.reportStep("The text: "+sText+" contains the value :"+text, "PASS");
			passfail(file, "Pass");
			bReturn = true;
		}else{
			Reporter.reportStep("The text: "+sText+" did not contain the value :"+text, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	public boolean verifyTextContainsByXpathwithwait(String xpath, String text)  {
		boolean bReturn = false;
		WebDriverWait wait=new WebDriverWait(driver, 25);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		String sText = driver.findElementByXPath(xpath).getText();
		if (driver.findElementByXPath(xpath).getText().trim().contains(text)){
			Reporter.reportStep("The text: "+sText+" contains the value :"+text, "PASS");
			passfail(file, "Pass");
			bReturn = true;
		}else{
			Reporter.reportStep("The text: "+sText+" did not contain the value :"+text, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	public boolean verifyTextContainsById(String idvalue, String text){
		boolean bReturn = false;
		String sText = driver.findElementById(idvalue).getText();
		if (driver.findElementById(idvalue).getText().trim().contains(text)){
			Reporter.reportStep("The text: "+sText+" contains the value :"+text, "PASS");
			passfail(file, "Pass");
			bReturn = true;
		}else{
			Reporter.reportStep("The text: "+sText+" did not contain the value :"+text, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	// Verify the validation message
	public boolean verifyTextContainsByIdResult(String idvalue, String result){
		boolean bReturn = false;
		String sText = driver.findElementById(idvalue).getText();
		if (driver.findElementById(idvalue).getText().trim().contains(result)){
			Reporter.reportStep("The text: "+sText+" contains the value :"+result, "PASS");
			passfail(file, "Pass");
			bReturn = true;
		}else{
			Reporter.reportStep("The text: "+sText+" did not contain the value :"+result, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}


	/**
	 * This method will close all the browsers
	 * @author -Karthikeyan
	 */
	public void quitBrowser() {
		try {
			driver.quit();
		} catch (Exception e) {
			Reporter.reportStep("The browser:"+driver.getCapabilities().getBrowserName()+" could not be closed.", "FAIL");
		}

	}

	/**
	 * This method will click the element using id as locator
	 * @param id  The id (locator) of the element to be clicked
	 * @author -Karthikeyan
	 */
	public boolean clickById(String id) {
		boolean bReturn = false;
		try{
			Thread.sleep(500);
			driver.findElement(By.id(id)).click();
			Reporter.reportStep("The element with id: "+id+" is clicked.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The element with id: "+id+" could not be clicked.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	/**
	 * This method will click the element using id as locator
	 * @param id  The id (locator) of the element to be clicked
	 * @author -Karthikeyan
	 */
	public boolean clickByClassName(String classVal) {
		boolean bReturn = false;
		try{
			driver.findElement(By.className(classVal)).click();
			Reporter.reportStep("The element with class Name: "+classVal+" is clicked.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The element with class Name: "+classVal+" could not be clicked.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}
	/**
	 * This method will click the element using name as locator
	 * @param name  The name (locator) of the element to be clicked
	 * @author -Karthikeyan
	 */
	public boolean clickByName(String name) {
		boolean bReturn = false;
		try{
			driver.findElement(By.name(name)).click();
			Reporter.reportStep("The element with name: "+name+" is clicked.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The element with name: "+name+" could not be clicked.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	/**
	 * This method will click the element using link name as locator
	 * @param name  The link name (locator) of the element to be clicked
	 * @author -Karthikeyan
	 */
	public boolean clickByLink(String name) {
		boolean bReturn = false;
		try{
			driver.findElement(By.linkText(name)).click();
			Reporter.reportStep("The element with link name: "+name+" is clicked.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The element with link name: "+name+" could not be clicked.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	/**
	 * This method will click the element using xpath as locator
	 * @param xpathVal  The xpath (locator) of the element to be clicked
	 * @author -Karthikeyan
	 */
	public boolean clickByXpath(String xpathVal) {
		boolean bReturn = false;
		try{
			driver.findElement(By.xpath(xpathVal)).click();
			Reporter.reportStep("The element : "+xpathVal+" is clicked.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The element with xpath: "+xpathVal+" could not be clicked.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	public boolean clickByXpathwithwait(String xpathVal) {
		boolean bReturn = false;
		try{
			Thread.sleep(2000);
			driver.findElement(By.xpath(xpathVal)).click();
			Reporter.reportStep("The element : "+xpathVal+" is clicked.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The element with xpath: "+xpathVal+" could not be clicked.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}


	/**
	 * This method will mouse over on the element using xpath as locator
	 * @param xpathVal  The xpath (locator) of the element to be moused over
	 * @author -Karthikeyan
	 */
	public boolean mouseOverByXpath(String xpathVal) {
		boolean bReturn = false;
		try{
			new Actions(driver).moveToElement(driver.findElement(By.xpath(xpathVal))).build().perform();
			Reporter.reportStep("The mouse over by xpath : "+xpathVal+" is performed.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The mouse over by xpath : "+xpathVal+" could not be performed.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	/**
	 * This method will mouse over on the element using link name as locator
	 * @param xpathVal  The link name (locator) of the element to be moused over
	 * @author -Karthikeyan
	 */
	public boolean mouseOverByLinkText(String linkName) {
		boolean bReturn = false;
		try{
			new Actions(driver).moveToElement(driver.findElement(By.linkText(linkName))).build().perform();
			Reporter.reportStep("The mouse over by link : "+linkName+" is performed.", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The mouse over by link : "+linkName+" could not be performed.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	public String getTextByXpath(String xpathVal){
		String bReturn = "";
		try{
			return driver.findElement(By.xpath(xpathVal)).getText();

		} catch (Exception e) {
			Reporter.reportStep("The element with xpath: "+xpathVal+" could not be found.", "FAIL");
		}
		return bReturn; 
	}

	/**
	 * This method will select the drop down value using id as locator
	 * @param id The id (locator) of the drop down element
	 * @param value The value to be selected (visibletext) from the dropdown 
	 * @author -Karthikeyan
	 */
	public boolean selectById(String id, String value) {
		boolean bReturn = false;
		try{
			new Select(driver.findElement(By.id(id))).selectByVisibleText(value);
			Reporter.reportStep("The element with xpath: "+id+" is selected with value :"+value, "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The value: "+value+" could not be selected.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	public boolean selectByXpath(String xpath, String value) {
		boolean bReturn = false;
		try{
			new Select(driver.findElement(By.xpath(xpath))).selectByVisibleText(value);
			Reporter.reportStep("The element with xpath: "+xpath+" is selected with value :"+value, "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The value: "+value+" could not be selected.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	public boolean clickbyCssSelector(String selector) {
		boolean bReturn = false;
		try{
			Thread.sleep(1500);
			driver.findElement(By.cssSelector(selector)).click();
			Reporter.reportStep("The element with Css selector: "+selector+" is clicked with value :", "PASS");
			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The value: "+selector+" could not be clicked.", "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}


	//Method used for auto suggest(Search box) by using Id.
	public boolean SearchById(String idValue, String data) {
		boolean bReturn = false;

		try {
			driver.findElement(By.id(idValue)).clear();
			driver.findElement(By.id(idValue)).sendKeys(data);	
			driver.findElement(By.id(idValue)).sendKeys("",Keys.DOWN,Keys.DOWN);
			Reporter.reportStep("The data: "+data+" entered successfully in field :"+idValue, "PASS");

			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The data: "+data+" could not be entered in the field :"+idValue, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	//Method used for auto suggest(Search box) by using xpath.
	public boolean SearchByXpath(String xpath, String data) {
		boolean bReturn = false;

		try {
			driver.findElement(By.xpath(xpath)).clear();
			driver.findElement(By.xpath(xpath)).sendKeys(data);	
			driver.findElement(By.xpath(xpath)).sendKeys("",Keys.DOWN,Keys.DOWN);
			Reporter.reportStep("The data: "+data+" entered successfully in field :"+xpath, "PASS");

			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The data: "+data+" could not be entered in the field :"+xpath, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	//clear the value in the search box by using Id
	public boolean clearById(String idValue, String data) {
		boolean bReturn = false;

		try {
			Thread.sleep(1500);
			driver.findElement(By.id(idValue)).sendKeys("",Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,
					Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE);
			Reporter.reportStep("The data: "+data+" cleared successfully in field :"+idValue, "PASS");

			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The data: "+data+" could not be cleared in the field :"+idValue, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	//clear the value in the search box by using xpath
	public boolean clearByXpath(String xpath, String data) {
		boolean bReturn = false;

		try {
			Thread.sleep(1500);
			driver.findElement(By.xpath(xpath)).sendKeys("",Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,
					Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE);
			Reporter.reportStep("The data: "+data+" cleared successfully in field :"+xpath, "PASS");

			passfail(file, "Pass");
			bReturn = true;

		} catch (Exception e) {
			Reporter.reportStep("The data: "+data+" could not be cleared in the field :"+xpath, "FAIL");
			passfail(file, "Fail");
		}
		return bReturn;
	}

	//Method for select value in listbox by using Id
	public boolean selectlistbyId(String idValue){
		boolean bReturn = false;

		try{
			WebElement ele = driver.findElement(By.id(idValue));
			Actions builder = new Actions(driver);
			builder.clickAndHold(ele).release().build().perform();
			Reporter.reportStep("selected successfully :"+idValue, "PASS");
			passfail(file, "Pass");

		}catch(Exception e){
			Reporter.reportStep("Could not be selected:"+idValue, "FAIL");
			passfail(file, "Fail");

		}
		return bReturn;	
	}

	//Method for select value in listbox by using xpath
	public boolean selectlistbyXpath(String xpath){
		boolean bReturn = false;

		try{
			WebElement ele = driver.findElement(By.xpath(xpath));
			Actions builder = new Actions(driver);
			builder.clickAndHold(ele).release().build().perform();
			Reporter.reportStep("selected successfully :"+xpath, "PASS");
			passfail(file, "Pass");

		}catch(Exception e){
			Reporter.reportStep("Could not be selected:"+xpath, "FAIL");
			passfail(file, "Fail");

		}
		return bReturn;	
	}

	//Method created Performing operation for Assign user/Group in different versions.
	public void AssignUserorGroup() throws InterruptedException{
		try{
			String groupbtn =	driver.findElement(By.xpath("//*[@id='divUserRoleList']/div/div/div/div[2]/label")).getText();
			System.out.println(groupbtn);

			String userbtn =	driver.findElement(By.xpath("//*[@id='divUserRoleList']/div/div/div/div[1]/label")).getText();
			System.out.println(userbtn);

			if(userbtn.contains("Users")){
				Thread.sleep(1000);
				passfail(file, "Pass");
				driver.findElement(By.xpath("//*[@id='divUserRoleList']/div/div/div/div[1]/label")).click();
				try{
					Thread.sleep(1000);
					driver.findElement(By.id("chkSelectAll")).click();
					Thread.sleep(1000);
					driver.findElement(By.id("btnDeleteSelected")).click();
					Thread.sleep(1000);
					driver.findElement(By.xpath("//*[@class='bootstrap-dialog-footer-buttons']/button[1]")).click();
				}catch(Exception e){
				}
			}else{}
			if(groupbtn.contains("Groups")){
				Thread.sleep(1500);
				driver.findElement(By.xpath("//*[@id='divUserRoleList']/div/div/div/div[2]/label")).click();
				try{
					Thread.sleep(1000);
					driver.findElement(By.id("chkSelectAll")).click();;
					driver.findElement(By.id("btnDeleteSelected")).click();
					Thread.sleep(1000);
					driver.findElement(By.xpath("//*[@class='bootstrap-dialog-footer-buttons']/button[1]")).click();
					//click Dashboard
					Thread.sleep(1000);
					driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
				}catch(Exception e){
					Thread.sleep(1000);
					driver.findElement(By.id("btnAssignRole")).click();
					Thread.sleep(2000);
					driver.findElement(By.id("chkSelectAll")).click();
					Thread.sleep(2500);
					driver.findElement(By.id("btnSave2")).click();
					//click Dashboard
					Thread.sleep(1000);
					driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();

				}
			}else{
				System.out.println("Else loop");

			}
		}catch(Exception e){
			passfail(file, "Pass");
			Thread.sleep(1000);
			driver.findElement(By.id("chkSelectAll")).click();
			Thread.sleep(1500);
			driver.findElement(By.id("btnDeleteSelected")).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//*[@class='bootstrap-dialog-footer-buttons']/button[1]")).click();
			Thread.sleep(1500);
			driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
		}
	}

	//Method create for Users/User and User Group for performing in different version

	public void UserandUsergroup() throws InterruptedException{

		try{
			String Usergroup = driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div/div[2]/div[2]/div[2]/div/div[2]/span")).getText();
			String user = driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div/div[2]/div[2]/div[2]/div/div[1]/span")).getText();

			if(Usergroup.contains("User Group")){
				passfail(file, "Pass");
				//click Usergroup
				driver.findElementByCssSelector("i[class='fa fa-3x fa-users color-warningyellow']").click();
				//click Edit
				Thread.sleep(1000);
				driver.findElementByCssSelector("i[class='fa fa-pencil fa-lg']").click();
				//click save
				driver.findElement(By.id("btnSave")).click();
				//click dashboard
				Thread.sleep(1500);
				driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
				Thread.sleep(1500);
				//click user
				driver.findElementByCssSelector("i[class='fa fa-3x fa-user color-yellowgreen']").click();
				//click refresh
				Thread.sleep(1000);
				//	driver.findElementByCssSelector("button[class='btn btn-primary btn-add-item pull-right']").click();

				driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div[2]/div/div[2]/button/b")).click();
				//click Dashboard
				Thread.sleep(1500);
				driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
			}
			else{
				passfail(file, "Pass");
				driver.findElementByCssSelector("i[class='fa fa-3x fa-users color-yellowgreen']").click();
				//click add user
				driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div[2]/div/div/button/b")).click();
				//click Add
				driver.findElement(By.id("btnSave")).click();
				//click cancel
				driver.findElement(By.xpath("//*[@id='frmUser']/div/div[3]/div/div[2]/input[2]")).click();
				//click Dashboard
				Thread.sleep(1500);
				driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
			}
		}catch(Exception e){

			driver.findElementByCssSelector("i[class='fa fa-3x fa-users color-yellowgreen']").click();
			//click add user
			Thread.sleep(1500);
			driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div[2]/div/div/button/b")).click();
			//click Add
			Thread.sleep(1000);
			driver.findElement(By.id("btnSave")).click();
			//click cancel
			driver.findElement(By.xpath("//*[@id='frmUser']/div/div[3]/div/div[2]/input[2]")).click();
			//click Dashboard
			Thread.sleep(1500);
			driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();

		}
	}

	//Method created for Data Management, NOtification and settings for automating different versions

	public void DataManagement() throws InterruptedException{

		try{
			//String STM = driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div/div[3]/div[2]/div[2]/div/div[1]/span")).getText();
			//System.out.println(STM);

			String du = driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div/div[3]/div[2]/div[2]/div/div[3]/span")).getText();
			System.out.println(du);
			//	if(STM.contains("Source Target Mapping"))
			if (du.contains("Data Uploads")){
				//click source target mapping
				Thread.sleep(3000);
				driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div/div[3]/div[2]/div[2]/div/div[1]/a/i")).click();
				//click remap
				Thread.sleep(3000);
				driver.findElement(By.xpath("//*[@id='tblTargetMappingList']/tbody/tr[1]/td[4]/span/a/span")).click();
				//click No
				Thread.sleep(3000);
				driver.findElement(By.xpath("//*[@class='bootstrap-dialog-footer-buttons']/button[2]")).click();
				//Click upload
				Thread.sleep(4000);
				driver.findElement(By.xpath("//*[@id='dropUpload']/div/div/img")).click();

				//File upload
				Thread.sleep(6000);
				StringSelection ss = new StringSelection("D:\\DTE Test data\\Text\\ApplicationActions.txt");
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

				driver.findElement(By.id("txtFileDescription")).sendKeys("Application Action");

				WebElement ele = driver.findElement(By.id("selSplitColumns"));
				Select s = new Select(ele);
				s.getOptions();
				s.selectByIndex(34);

				driver.findElement(By.xpath("//*[@id='demo-step-wz']/form/div[2]/div/button[3]")).click();
				//click next
				Thread.sleep(2000);
				driver.findElement(By.xpath("//*[@id='demo-step-wz']/form/div[2]/div/button[3]")).click();
				//click Next
				Thread.sleep(2000);
				driver.findElement(By.xpath("//*[@id='demo-step-wz']/form/div[2]/div/button[3]")).click();
				//click finish
				Thread.sleep(2000);
				driver.findElement(By.xpath("//*[@id='demo-step-wz']/form/div[2]/div/a")).click();
				//click dashboard
				Thread.sleep(2000);
				driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
				//click preview target
				Thread.sleep(3000);
				driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div/div[3]/div[2]/div[2]/div/div[2]/a/i")).click();
				//select value in dropdown
				WebElement TT = driver.findElement(By.id("SelTargetTableInformationDetailsList"));
				Select t = new Select(TT);
				t.getOptions();
				t.selectByVisibleText("Applications");
				//click dashboard
				Thread.sleep(3000);
				driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
				//click upload
				Thread.sleep(2000);
				driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div/div[3]/div[2]/div[2]/div/div[3]/a/i")).click();
				//Click Add upload file
				Thread.sleep(6000);
				driver.findElement(By.xpath("//*[@id='divUploadDataList']/div[1]/div/div/button/b")).click();
				//click upload
				Thread.sleep(4000);
				driver.findElement(By.cssSelector("i[class='fa fa-upload fa-lg']")).click();

				//click upload in popup
				Thread.sleep(5000);
				driver.findElement(By.id("FileName")).click();

				//File upload
				Thread.sleep(6000);
				StringSelection ss1 = new StringSelection("D:\\DTE Test data\\Text\\ApplicationActions.txt");
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss1, null);

				Robot robot1 = new Robot();
				robot1.keyPress(KeyEvent.VK_ENTER);
				robot1.keyRelease(KeyEvent.VK_ENTER);
				robot1.keyPress(KeyEvent.VK_CONTROL);
				robot1.keyPress(KeyEvent.VK_V);
				robot1.keyRelease(KeyEvent.VK_V);
				robot1.keyRelease(KeyEvent.VK_CONTROL);
				robot1.keyPress(KeyEvent.VK_ENTER);
				robot1.keyRelease(KeyEvent.VK_ENTER);

				//click upload
				Thread.sleep(2000);
				driver.findElement(By.cssSelector("button[class='btn btn-success']")).click();
				//click dashboard
				Thread.sleep(3000);
				driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
				//preview mapping
				Thread.sleep(3000);
				driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div/div[3]/div[2]/div[2]/div/div[4]/a/i")).click();
				//click dashboard
				Thread.sleep(3000);
				driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
				//click Notification
				Thread.sleep(2000);
				driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div/div[3]/div[1]/div[2]/div/div[2]/a/i")).click();
				//click dashboard
				Thread.sleep(3000);
				driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
				//click setting
				Thread.sleep(2000);
				driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div/div[3]/div[1]/div[2]/div/div[3]/a/i")).click();

				driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/table/tbody/tr[2]/td[2]/div[1]/span/small")).click();
				//click dashboard
				Thread.sleep(3000);
				driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
			}
		}catch(Exception e){
			//version 1 and 2
			//click Notification
			//	driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div/div[3]/div/div[2]/div/div[2]/a/i")).click();
			driver.findElementByCssSelector("i[class='fa fa-3x fa-bell-o color-darkorange']").click();
			//click dashboard
			Thread.sleep(3000);
			driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
			//click setting
			Thread.sleep(1000);
			//	driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/div/div[3]/div/div[2]/div/div[3]/a/i")).click();
			driver.findElementByCssSelector("i[class='fa fa-3x fa-cog color-chocolate']").click();
			try{
				//Version 1
				driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/table/tbody/tr[3]/td[2]/div[1]/label/div")).click();
				//click dashboard
				driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
			}catch(Exception e1){
				//version 2
				driver.findElement(By.xpath("//*[@id='page-content']/div/div/div[1]/table/tbody/tr[3]/td[2]/div[1]/span")).click();
				//click dashboard
				driver.findElement(By.xpath("//*[@id='mainnav-menu']/li[1]/a/i")).click();
			}

		}

	}


	//Method for write the Pass Fail Status in to Excel

	public int passfail(String fileName,String Value){

		try {
			i++;	
			FileInputStream file = new FileInputStream(new File(fileName)); 
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Cell Text = sheet.getRow(i).getCell(6);
			Text.setCellValue(Value);
			file.close();
			FileOutputStream outFile =new FileOutputStream(new File(fileName));
			workbook.write(outFile);
			outFile.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return i;
	}
}

/*	public void loadObjects() throws FileNotFoundException, IOException{
		prop = new Properties();
		prop.load(new FileInputStream(new File("./object.properties")));

	}*/




