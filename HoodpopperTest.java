import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HoodpopperTest {

	static WebDriver driver = new HtmlUnitDriver();

	// Start at the home page for Hoodpopper for each test
	@Before
	public void setUp() throws Exception {
		driver.get("http://lit-bayou-7912.herokuapp.com/");
	}

  // Given that I am on the main page
  // When I view the title
  // Then I see that it contains the word "Hoodpopper"
  @Test
	public void testShowsCorrectTitle() {
		String title = driver.getTitle();
		assertTrue(title.contains("Hoodpopper"));
	}

  // Given that I am on the main page
  // When I click on the "Tokenize" button
  // Then I should be redirected to the page entitled
  // "Hood Popped - Tokenize Operation" which contains the word "Tokenize"
  @Test
  public void testTokenizeButton() {
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("h1"));
    String elementText = e.getText();
    assertTrue(elementText.contains("Tokenize"));
  }

  // Given that I am on the main page
  // When I click on the "Parse" button
  // Then I should be redirected to the page entitled
  // "Hood Popped - Parse Operation" which contains the word "Parse"
  @Test
  public void testParseButton() {
    driver.findElement(By.xpath("(//input[@name='commit'])[2]")).click();
    WebElement e = driver.findElement(By.cssSelector("h1"));
    String elementText = e.getText();
    assertTrue(elementText.contains("Parse"));
  }

  // Given that I am on the main page
  // When I click on the "Compile" button
  // Then I should be redirected to the page entitled
  // "Hood Popped - Compile Operation" which contains the word "Compile"
  @Test
  public void testCompileButton() {
    driver.findElement(By.xpath("(//input[@name='commit'])[3]")).click();
    WebElement e = driver.findElement(By.cssSelector("h1"));
    String elementText = e.getText();
    assertTrue(elementText.contains("Compile"));
  }

  // Given that I am on the main page
  // When I click on the "Tokenize" button
  // Then I should see that the "Tokenize" page contains the "Back" link
  @Test
  public void testTokenizeHasBackLink() {
	driver.findElement(By.name("commit")).click();
	try {
		driver.findElement(By.linkText("Back"));
	} catch (NoSuchElementException nseex) {
		fail();
	}
  }

  // Given that I am on the main page
  // When I click on the "Parse" button
  // Then I should see that the "Parse" page contains the "Back" link
  @Test
  public void testParseHasBackLink() {
	driver.findElement(By.xpath("(//input[@name='commit'])[2]")).click();
	try {
		driver.findElement(By.linkText("Back"));
	} catch (NoSuchElementException nseex) {
		fail();
	}
  }

  // Given that I am on the main page
  // When I click on the "Compile" button
  // Then I should see that the "Compile" page contains the "Back" link
  @Test
  public void testCompileHasBackLink() {
	driver.findElement(By.xpath("(//input[@name='commit'])[3]")).click();
	try {
		driver.findElement(By.linkText("Back"));
	} catch (NoSuchElementException nseex) {
		fail();
	}
  }
  
  // Given that I am on the main page
  // When I click on the "Tokenize" button and then the "Back" link
  // Then I should be redirected from the "Tokenize" page to the main page
  @Test
  public void testBackLink() {
    driver.findElement(By.name("commit")).click();
    driver.findElement(By.linkText("Back")).click();
    String title = driver.getTitle();
	assertTrue(title.contains("Hoodpopper"));
  }
  
  // Given that I am on the main page
  // When I type function "puts" into the Code box and click "Tokenize"
  // Then "puts" should be tokenized as an identifier
  @Test
  public void testFunctionPutsIsIdentifier() {
	driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts");
    driver.findElement(By.name("commit")).click();
	WebElement e = driver.findElement(By.cssSelector("p"));
	String tokenized = e.getText();
	assertTrue(tokenized.contains(":on_ident, \"puts\""));
  }

  // Given that I am on the main page
  // When I type variable "a" into the Code box and click "Tokenize"
  // Then "a" should be tokenized as an identifier
  @Test
  public void testVariableAIsIdentifier() {
	driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("a");
    driver.findElement(By.name("commit")).click();
	WebElement e = driver.findElement(By.cssSelector("p"));
	String tokenized = e.getText();
	assertTrue(tokenized.contains(":on_ident, \"a\""));
  }

  // Edge case: combination of function and string as identifier
  // Given that I am on the main page
  // When I type variable "putsa" into the Code box and click "Tokenize"
  // Then "putsa" should be tokenized as an identifier
  @Test
  public void testVariablePutsaIsIdentifier() {
	driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("putsa");
    driver.findElement(By.name("commit")).click();
	WebElement e = driver.findElement(By.cssSelector("p"));
	String tokenized = e.getText();
	assertTrue(tokenized.contains(":on_ident, \"putsa\""));
  }

  //Check whitespace token
  @Test
  public void tokenizeSpace(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("a = 1");
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
    assertTrue(elementText.contains(":on_sp"));
  }

  //Check that no whitespace token is created with no spaces
  @Test
  public void tokenizeNoSpace(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("a=1");
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
    assertFalse(elementText.contains(":on_sp"));
  }

  //When a newline is put after a semicolon there should be no on_nl token created
  //but there should be an ignored_nl token
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

  //When a newline is put in the code, newline should be tokenized
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

  //Check that +, /, and = operations all are tokenized
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

  //When no operations are in the code there should be no :on_op token
  @Test
  public void testTokenizeNoOps(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts hello");
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
    assertFalse(elementText.contains(":on_op"));
  }

  // Defect in Hoodpopper: this test will fail
  // Given that I am on the main page
  // When I type operator "+" into the Code box and click "Parse"
  // Then I should see "+" shown in the AST
  @Test
  public void testParseOnlyOperator(){
	driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("+");
	try {
	driver.findElement(By.xpath("(//input[@name='commit'])[2]")).click();
	WebElement e = driver.findElement(By.xpath("//p[2]"));
	String AST = e.getText();
	assertTrue(AST.contains("+"));
	} catch(NoSuchElementException nseex) {
		fail();
	}
  }

  // Edge case: input special character
  // Defect in Hoodpopper: this test will fail
  // Given that I am on the main page
  // When I type special character "!" into the Code box and click "Parse"
  // Then I should see "!" shown in the AST
  @Test
  public void testParseOnlySpecialChar() {
	driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("!");
	try {
		driver.findElement(By.xpath("(//input[@name='commit'])[2]")).click();
		WebElement e = driver.findElement(By.xpath("//p[2]"));
		String AST = e.getText();
		assertTrue(AST.contains("!"));
	} catch(NoSuchElementException nseex) {
		fail();
	}
  }

  // Given that I am on the main page
  // When I type the sequence of a function "puts", an operator "+",
  // And a float "3.14159" into the Code box and click "Parse"
  // Then I should see all three types shown in the AST
  @Test
  public void testParseShowsCorrectTokensInAST() {
	driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts + 3.14159");
	try {
		driver.findElement(By.xpath("(//input[@name='commit'])[2]")).click();
		WebElement e = driver.findElement(By.xpath("//p[2]"));
		String AST = e.getText();
		assertTrue(AST.contains("puts"));	
		assertTrue(AST.contains("+"));
		assertTrue(AST.contains("3.14159"));
	} catch(NoSuchElementException nseex) {
		fail();
	}
  }


//Check that the tree is the same for two different inputs when the only
//difference between the imputs its added whitespace
  @Test
  public void testParseWhitespace(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts helloworld;");
    driver.findElement(By.xpath("(//input[@name='commit'])[2]")).click();
    WebElement e = driver.findElement(By.xpath("//p[2]"));
    String noWhitespaceText = e.getText();

    WebDriver newDriver = new HtmlUnitDriver();
    newDriver.get("http://lit-bayou-7912.herokuapp.com/");
    newDriver.findElement(By.id("code_code")).clear();
    newDriver.findElement(By.id("code_code")).sendKeys("puts helloworld;\n  \n");
    newDriver.findElement(By.xpath("(//input[@name='commit'])[2]")).click();
    e = newDriver.findElement(By.xpath("//p[2]"));
    String whitespaceText = e.getText();
    assertEquals(noWhitespaceText,whitespaceText);
  }

  //checks that the use of puts in the code compiles putstring in YARV
  @Test
  public void testCompilePuts(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts \"whats up\"");
    driver.findElement(By.xpath("(//input[@name='commit'])[3]")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
    assertTrue(elementText.contains("putstring"));
  }

  //checks that the use of plus/multiply in the code compiles with opt_plus/opt_mult
  @Test
  public void testCompileOps(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("x = 5 + 3 * 100");
    driver.findElement(By.xpath("(//input[@name='commit'])[3]")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
    assertTrue(elementText.contains("opt_plus"));
    assertTrue(elementText.contains("opt_mult"));
  }

  //code without operations or puts should not cotain opt or putstring instructions
  @Test
  public void testCompileEmpty(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("x;\n");
    driver.findElement(By.xpath("(//input[@name='commit'])[3]")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
    assertFalse(elementText.contains("putstring"));
    assertFalse(elementText.contains("opt_plus"));
    assertFalse(elementText.contains("opt_mult"));
  }

}
