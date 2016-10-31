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
		//Check the title as soon at the site loads
		String title = driver.getTitle();
		assertTrue(title.contains("Hoodpopper"));
	}

  // Given that I am on the main page
  // When I click on the "Tokenize" button
  // Then I should be redirected to the page entitled
  // "Hood Popped - Tokenize Operation" which contains the word "Tokenize"
  @Test
  public void testTokenizeButton() {
    // Click on Tokenize button
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("h1"));
    String elementText = e.getText();
    // The heading of the page should be Tokenize
    assertTrue(elementText.contains("Tokenize"));
  }

  // Given that I am on the main page
  // When I click on the "Parse" button
  // Then I should be redirected to the page entitled
  // "Hood Popped - Parse Operation" which contains the word "Parse"
  @Test
  public void testParseButton() {
    // Click on Parse button
    driver.findElement(By.xpath("(//input[@name='commit'])[2]")).click();
    WebElement e = driver.findElement(By.cssSelector("h1"));
    String elementText = e.getText();
    // Page heading should be Parse
    assertTrue(elementText.contains("Parse"));
  }

  // Given that I am on the main page
  // When I click on the "Compile" button
  // Then I should be redirected to the page entitled
  // "Hood Popped - Compile Operation" which contains the word "Compile"
  @Test
  public void testCompileButton() {
    // Click on Compile
    driver.findElement(By.xpath("(//input[@name='commit'])[3]")).click();
    WebElement e = driver.findElement(By.cssSelector("h1"));
    String elementText = e.getText();
    // Page heading should be Compile
    assertTrue(elementText.contains("Compile"));
  }

  // Given that I am on the main page
  // When I click on the "Tokenize" button
  // Then I should see that the "Tokenize" page contains the "Back" link
  @Test
  public void testTokenizeHasBackLink() {
	// Click on Tokenize button
	driver.findElement(By.name("commit")).click();
	// Test fails if there is no back link on the page
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
	// Test fails if there is no back link on the page	  
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
	// Test fails if there is no back link on the page	  
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
	// Click on Tokenize button	  
    driver.findElement(By.name("commit")).click();
	// Checks that there is no parse link (confirming we left the main page)
	try {
		driver.findElement(By.name("commit"));
		fail();
	} catch (NoSuchElementException nseex) {
		// Expected result because there should be no link on this apge
	}  
    driver.findElement(By.linkText("Back")).click();
	// Checks that there is a parse link (confirming we have returned to the main page)
	try {
		driver.findElement(By.name("commit"));
	} catch (NoSuchElementException nseex) {
	}
  }
  
  // Given that I am on the main page
  // When I type function "puts" into the Code box and click "Tokenize"
  // Then "puts" should be tokenized as an identifier
  @Test
  public void testFunctionPutsIsIdentifier() {
	driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts");
	// Click on Tokenize button	  
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
	// Click on Tokenize button	  
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
	// Click on Tokenize button	  
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String tokenized = e.getText();
    assertTrue(tokenized.contains(":on_ident, \"putsa\""));
  }

  // Given that I am on the main page
  // When I submit code that contains a space character and click "Tokenize"
  // It should create a token called :on_sp
  @Test
  public void tokenizeSpace(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("a = 1");
	// Click on Tokenize button	  
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
	// Confirm that is an on_sp token created
    assertTrue(elementText.contains(":on_sp"));
  }

  // Given that I am on the main page
  // When I submit code that contains a space character and click "Tokenize"
  // It should not create a token called :on_sp
  @Test
  public void tokenizeNoSpace(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("a=1");
	// Click on Tokenize button	  
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
	// There should be no on_sp token
    assertFalse(elementText.contains(":on_sp"));
  }
	
  // Edge Case:  Even when a newline is typed, if its after a semicolon :on_nl token is not created	
  // Given that I am on the main page and type a newline directly after a semicolon	
  // When a newline is put after a semicolon in the textbox and "Tokenize" is clicked 
  // There should be no on_nl token created but there should be an ignored_nl token
  @Test
  public void testTokenizeIgnoredNewline(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts hello;\nx = 5;");
	// Click on Tokenize button	  
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
	// Only the on_ignored_nl token is created
    assertFalse(elementText.contains(":on_nl"));
    assertTrue(elementText.contains(":on_ignored_nl"));
  }

  // Given that I am on the main page and type a newline directly after a semicolon	
  // When a newline is entered and I click "Tokenize" 
  // There should be an on_nl token created but there should not be an any ignored_nl tokens
  @Test
  public void testTokenizeNewline(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts hello\nx = 5;");
	// Click on Tokenize button	  
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
	// Only the on_nl token is created
    assertTrue(elementText.contains(":on_nl"));
    assertFalse(elementText.contains(":on_ignored_nl"));
  }

  // Given that I type in any code that conatins operators (+,-,*,/,=)
  // When I clicked tokenize on the main page 
  // The page should display an :on_op token with that opertor in quotes
  @Test
  public void testTokenizeOps(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("x = 5;\nx = x + 7\nx = x/3;");
	// Click on Tokenize button	  
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
	// Text should contain each specific operator token
    assertTrue(elementText.contains(":on_op, \"=\""));
    assertTrue(elementText.contains(":on_op, \"+\""));
    assertTrue(elementText.contains(":on_op, \"/\""));
  }

  // Given that I type in any code that does not contain operators (+,-,*,/,=)
  // When I clicked tokenize on the main page 
  // The page should not display any :on_op tokens
  @Test
  public void testTokenizeNoOps(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts hello");
	// Click on Tokenize button	  
    driver.findElement(By.name("commit")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
	// Text should not contain operator token
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
	// Test fails if there is no parse element	  
	try {
		// Click on Parse button
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
	// Test fails if there is no parse element	  
	try {
		// Click on Parse button		
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
	// Test fails if there is no parse element
    try {
		// Click on Parse button		
		driver.findElement(By.xpath("(//input[@name='commit'])[2]")).click();
		WebElement e = driver.findElement(By.xpath("//p[2]"));
		String AST = e.getText();
		// Confirm operator, value and command are all in the tree
		assertTrue(AST.contains("puts"));	
		assertTrue(AST.contains("+"));
		assertTrue(AST.contains("3.14159"));
    } catch(NoSuchElementException nseex) {
		fail();
    }
  }

  // Given that I am on the main page
  // When I enter two different inputs where the only difference between inputs
  // Is added whitespace tokens and click on "Parse"
  // Then whitespace should be ignored and both should return the same tree
  @Test
  public void testParseWhitespace(){
	// Save the result from parsing the first string
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts helloworld;");
	// Click on Parse button	  
    driver.findElement(By.xpath("(//input[@name='commit'])[2]")).click();
    WebElement e = driver.findElement(By.xpath("//p[2]"));
    String noWhitespaceText = e.getText();

	// Create a new driver at the same location as driver
	// Save the result from parsing thee second string
    WebDriver newDriver = new HtmlUnitDriver();
    newDriver.get("http://lit-bayou-7912.herokuapp.com/");
    newDriver.findElement(By.id("code_code")).clear();
    newDriver.findElement(By.id("code_code")).sendKeys("puts helloworld;\n  \n");
    newDriver.findElement(By.xpath("(//input[@name='commit'])[2]")).click();
    e = newDriver.findElement(By.xpath("//p[2]"));
    String whitespaceText = e.getText();
	  
	// Check the strings are the same both times  
    assertEquals(noWhitespaceText,whitespaceText);
  }

  // Given that I am on that main page of Hoodpopper
  // When I enter the puts command in the textbox and click "Compile"
  // Then the instruction putstring should be part of the in YARV code
  @Test
  public void testCompilePuts(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("puts \"whats up\"");
	//Click on Compile button  
    driver.findElement(By.xpath("(//input[@name='commit'])[3]")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
	// Confirm the compiled code contains putstring
    assertTrue(elementText.contains("putstring"));
  }

  // Given that I am on the main page of Hoodpopper
  // When I enter text that contains an operator and click "Compile"
  // The corresponding opt instruction should appear in the YARV code 
  @Test
  public void testCompileOps(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("x = 5 + 3 * 100");
	// Click on Compile button 	  
    driver.findElement(By.xpath("(//input[@name='commit'])[3]")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
	// Confirm the compiled code does contain operations
    assertTrue(elementText.contains("opt_plus"));
    assertTrue(elementText.contains("opt_mult"));
  }

  // Given that I am on the main page of Hoodpopper
  // When I enter text that contains no operators or puts commands and click "Compile"
  // Then no opt or putstring instructions should appear in the YARV code
  @Test
  public void testCompileEmpty(){
    driver.findElement(By.id("code_code")).clear();
    driver.findElement(By.id("code_code")).sendKeys("x;\n");
	// Click on Compile button 	  
    driver.findElement(By.xpath("(//input[@name='commit'])[3]")).click();
    WebElement e = driver.findElement(By.cssSelector("p"));
    String elementText = e.getText();
	// Check that the compiled code does not contain putstring or operations
    assertFalse(elementText.contains("putstring"));
    assertFalse(elementText.contains("opt_plus"));
    assertFalse(elementText.contains("opt_mult"));
  }

}
