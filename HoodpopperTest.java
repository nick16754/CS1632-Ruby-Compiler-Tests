import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HoodpopperTest {

	static WebDriver driver = new HtmlUnitDriver();

	@Before
	public void setUp() throws Exception {
		driver.get("http://lit-bayou-7912.herokuapp.com/");
	}


  @Test
  public void testTokenizeButton() {
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("h1"));
    String elementText = e.getText();
    assertTrue(elementText.contains("Tokenize"));
  }

  @Test
  public void testParseButton() {
    driver.findElement(By.xpath("(//input[@name='commit'])[2]")).click();
    WebElement e = driver.findElement(By.cssSelector("h1"));
    String elementText = e.getText();
    assertTrue(elementText.contains("Parse"));
  }

  @Test
  public void testCompileButton() {
    driver.findElement(By.xpath("(//input[@name='commit'])[3]")).click();
    WebElement e = driver.findElement(By.cssSelector("h1"));
    String elementText = e.getText();
    assertTrue(elementText.contains("Compile"));
  }
  
  @Test
  public void testTokenizeHasBackLink() {
	driver.findElement(By.name("commit")).click();
	try {
		driver.findElement(By.linkText("Back"));
	} catch (NoSuchElementException nseex) {
		fail();
	}
  }
  
  @Test
  public void testParseHasBackLink() {
	driver.findElement(By.xpath("(//input[@name='commit'])[2]")).click();
	try {
		driver.findElement(By.linkText("Back"));
	} catch (NoSuchElementException nseex) {
		fail();
	}
  }
  
  @Test
  public void testCompileHasBackLink() {
	driver.findElement(By.xpath("(//input[@name='commit'])[3]")).click();
	try {
		driver.findElement(By.linkText("Back"));
	} catch (NoSuchElementException nseex) {
		fail();
	}
  }
  
  @Test
  public void testFunctionPutsIsIdentifier() {
	driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts");
    driver.findElement(By.name("commit")).click();
	WebElement e = driver.findElement(By.cssSelector("p"));
	String tokenized = e.getText();
	assertTrue(tokenized.contains(":on_ident, \"puts\""));
  }
  
  @Test
  public void testVariableAIsIdentifier() {
	driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("a");
    driver.findElement(By.name("commit")).click();
	WebElement e = driver.findElement(By.cssSelector("p"));
	String tokenized = e.getText();
	assertTrue(tokenized.contains(":on_ident, \"a\""));
  }
  
  //edge case: combination of function and string is identifier
  @Test
  public void testVariablePutsaIsIdentifier() {
	driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("putsa");
    driver.findElement(By.name("commit")).click();
	WebElement e = driver.findElement(By.cssSelector("p"));
	String tokenized = e.getText();
	assertTrue(tokenized.contains(":on_ident, \"putsa\""));
  }

}
