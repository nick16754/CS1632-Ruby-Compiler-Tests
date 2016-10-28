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

  @Test
  public void tokenizeSpace(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("a = 1");
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
    assertTrue(elementText.contains(":on_sp"));
  }

  @Test
  public void tokenizeNoSpace(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("a=1");
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
    assertFalse(elementText.contains(":on_sp"));
  }

  @Test
  public void testTokenizeIgnoredNewline(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts hello;\nx = 5;");
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
    assertFalse(elementText.contains(":on_nl"));
    assertTrue(elementText.contains(":on_ignored_nl"));
  }

  @Test
  public void testTokenizeNewline(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts hello\nx = 5;");
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
    assertTrue(elementText.contains(":on_nl"));
    assertFalse(elementText.contains(":on_ignored_nl"));
  }

  @Test
  public void testTokenizeOps(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("x = 5;\nx = x + 7\nx = x/3;");
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
    assertTrue(elementText.contains(":on_op, \"=\""));
    assertTrue(elementText.contains(":on_op, \"+\""));
    assertTrue(elementText.contains(":on_op, \"/\""));
  }

  @Test
  public void testTokenizeNoOps(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts hello");
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
    assertFalse(elementText.contains(":on_op"));
  }

}
