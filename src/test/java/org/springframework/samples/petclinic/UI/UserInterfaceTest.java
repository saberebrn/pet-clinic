package org.springframework.samples.petclinic.UI;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class UserInterfaceTest {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	JavascriptExecutor js;
	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
		driver = new ChromeDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		js = (JavascriptExecutor) driver;
	}

	@Test
	public void testOwnerPetVisit() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.id("main-navbar")).click();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Error'])[1]/following::div[1]")).click();
		driver.findElement(By.linkText("Add Owner")).click();
		driver.findElement(By.id("firstName")).click();
		driver.findElement(By.id("firstName")).clear();
		driver.findElement(By.id("firstName")).sendKeys("Jake");
		driver.findElement(By.id("lastName")).click();
		driver.findElement(By.id("lastName")).clear();
		driver.findElement(By.id("lastName")).sendKeys("Robertson");
		driver.findElement(By.id("address")).click();
		driver.findElement(By.id("address")).clear();
		driver.findElement(By.id("address")).sendKeys("321 Elm Street");
		driver.findElement(By.id("city")).click();
		driver.findElement(By.id("city")).clear();
		driver.findElement(By.id("city")).sendKeys("Chicago");
		driver.findElement(By.id("telephone")).click();
		driver.findElement(By.id("telephone")).clear();
		driver.findElement(By.id("telephone")).sendKeys("1234567");
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Error'])[1]/following::div[1]")).click();
		driver.findElement(By.xpath("//form[@id='add-owner-form']/div[2]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Error'])[1]/following::div[2]")).click();
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Error'])[1]/following::div[2]")).click();
		driver.findElement(By.xpath("//a[contains(text(),'Add\n      New Pet')]")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Walter");
		driver.findElement(By.id("birthDate")).click();
		driver.findElement(By.id("birthDate")).clear();
		driver.findElement(By.id("birthDate")).sendKeys("2018-09-17");
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Error'])[1]/following::div[3]")).click();
		driver.findElement(By.id("type")).click();
		new Select(driver.findElement(By.id("type"))).selectByVisibleText("dog");
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Type'])[1]/following::div[2]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.xpath("//a[contains(text(),'Add\n                  Visit')]")).click();
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Eye Surgery");
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Description'])[1]/following::div[3]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Error'])[1]/following::div[1]")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
