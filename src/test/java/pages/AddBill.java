package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import groovyjarjarantlr4.v4.parse.ANTLRParser.finallyClause_return;

import java.time.Duration;

public class AddBill {
	private WebDriver driver;
	private WebDriverWait wait;

	private final By billingsMenu = By.xpath("//span[normalize-space()='Billings']");
	private final By billingsOption = By.id("billings");
	private final By tripDetailsInput = By.id("inputTripDetails");
	private final By billNumberInput = By.id("inputBillNumber");
	private final By termSelect = By.xpath("(//select[@id='term'])[2]");
	private final By sequenceInput = By.id("inputSequence");
	private final By shipperIdInput = By.id("inputShipperId");
	private final By consigneeIdInput = By.id("inputConsigneeId");
	private final By thirdPartyInput = By.id("inputThirdparty");
	// private final By termChangedPopup = By.id("termOverRide");
	// private final By termPopupSelect = By.xpath("(//select[@id='term'])[2]");
	// private final By termOKbutton = By.xpath("//button[normalize-space()='OK']");
	private final By fullLoad = By.id("stampCheckbox6");
	private final By qst = By.id("stampCheckbox7");
	private final By aptCharge = By.id("stampCheckbox10");
	private final By dangersGood = By.id("stampCheckbox3");
	private final By heatedChrge = By.id("stampCheckbox4");
	private final By usd = By.id("stampCheckbox8");
	private final By quantityBy = By.xpath("//input[contains(@id,'inputqty_0')]");
	private final By piecesInput = By.xpath("(//input[@id='inputpiece_0'])[2]");
	private final By cuftInput = By.xpath("//input[@formcontrolname='cuFt']");
	private final By descriptionInput = By.xpath("(//input[@id='inputdescription_0'])[1]");
	private final By weightInput = By.id("inputWeight");
	private final By scaledWeightInput = By.id("inputscaledweight");
	private final By qstPortionInput = By.id("inputqstportion");
	private final By declaredValueInput = By.id("labeldeclaredvalue");
	private final By calcButton = By.id("calcButton");
	private final By yesButton = By.xpath("//span[normalize-space()='Yes']");
	private final By saveToDraft = By.xpath("//button[@id='btnDraft']");
	private final By publishedButton = By.xpath("//button[@id='btnPublish']");
	private final By loader = By.id("mainLoader");
	private final By confirmButton = By.xpath("//span[normalize-space()='Yes']");
	private final By toastMessageBy = By.id("toastmessagewarn");
	private final By errorMessage= By.id("toastmessageerror");
	private final By searchYesPopup = By.xpath("(//span[normalize-space()='Yes'])[1]");

	// Constructor
	public AddBill(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	// Helper methods
	private void waitForLoaderToDisappear() {
		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
		} catch (TimeoutException e) {
			System.out.println("Loader timeout: " + e.getMessage());
		}
	}

	private void waitForElementToBeClickable(By locator) {
		wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	private void waitForElementToBeVisible(By locator) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	private void scrollIntoView(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	private void safeClick(WebElement element) {
		try {
			scrollIntoView(element);
			element.click();
		} catch (ElementClickInterceptedException e) {
			System.out.println("Click intercepted, using JavaScript click.");
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		}
	}

	// Navigation methods
	public void navigateToBillings() {
		waitForLoaderToDisappear();
		waitForElementToBeClickable(billingsMenu);
		safeClick(driver.findElement(billingsMenu));
	}

	public void selectBillingOption() {
		waitForLoaderToDisappear();
		waitForElementToBeClickable(billingsOption);
		safeClick(driver.findElement(billingsOption));
	}

	// Input methods
	public void enterTripDetails(String tripDetails) {
		WebElement element = driver.findElement(tripDetailsInput);
		element.sendKeys(tripDetails, Keys.TAB);
	}

	public void enterBillNumber(String billNumber) {
		WebElement element = driver.findElement(billNumberInput);
		element.sendKeys(billNumber, Keys.TAB);
	}
	


	public void selectTerm(String term) {
		new Select(driver.findElement(termSelect)).selectByVisibleText(term);
	}

	public void enterSequence(String sequence) {
		driver.findElement(sequenceInput).sendKeys(sequence);
	}

	public void enterShipmentDetails(String shipperId) throws InterruptedException {
		// Locate the input field for the shipper ID
		WebElement eleShipper = driver.findElement(shipperIdInput);

		// Focus on the element
		((JavascriptExecutor) driver).executeScript("arguments[0].focus();", eleShipper);

		// Clear the input field and type the shipper ID
		eleShipper.clear();
		for (char c : shipperId.toCharArray()) {
			eleShipper.sendKeys(String.valueOf(c));

			// Scroll to keep the element in the center while typing
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", eleShipper);

			// Adding a small delay between keystrokes for visual effect
			Thread.sleep(100);
		}

		// Wait until the dropdown options are visible
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(
				ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'scroll-container')]")));

		// Locate all dropdown options
		java.util.List<WebElement> dropdownOptions = driver
				.findElements(By.xpath("//div[contains(@class, 'scroll-container')]//div"));

		if (!dropdownOptions.isEmpty()) {
			// Select the first option from the list
			WebElement firstOption = dropdownOptions.get(0);

			// Scroll the first dropdown option into view
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstOption);

			// Click the first option
			firstOption.click();

			// Adding a small wait to ensure the option is selected before proceeding
			Thread.sleep(2000);
		} else {
			throw new RuntimeException("No dropdown options available to select.");
		}
		// Adding a small wait to ensure the option is selected before proceeding
		Thread.sleep(2000);
	}

	public void enterConsigneeDetails(String consigneeId) {
		WebElement consigneeElement = driver.findElement(consigneeIdInput);
		consigneeElement.clear();
		consigneeElement.sendKeys(consigneeId);
		waitForElementToBeVisible(By.xpath("//div[contains(@class, 'scroll-container')]//div[1]"));
		driver.findElement(By.xpath("//div[contains(@class, 'scroll-container')]//div[1]")).click();
	}

	public void enterThirdPartyDetails(String thirdParty) {
		WebElement element = driver.findElement(thirdPartyInput);
		if (element.getAttribute("value").isEmpty()) {
			element.sendKeys(thirdParty, Keys.ENTER, Keys.TAB);
		} else {
			// element.sendKeys(thirdParty, Keys.ENTER, Keys.TAB);
			System.out.println("Third-party details already exist. Skipping input.");
		}
	}

	// Checkbox interaction
	public void selectCheckbox(String checkboxName) {
		By checkboxLocator = getCheckboxLocator(checkboxName);
		if (checkboxLocator != null) {
			WebElement checkbox = driver.findElement(checkboxLocator);
			if (!checkbox.isSelected()) {
				safeClick(checkbox);
			} else {
				System.out.println("Checkbox '" + checkboxName + "' is already selected.");
			}
		} else {
			System.err.println("Checkbox locator not found or Stamps not available for this bill: " + checkboxName);
		}
	}

	private By getCheckboxLocator(String checkboxName) {
		switch (checkboxName) {
		case "QST":
			return qst;
		case "Full Load":
			return fullLoad;
		case "USD":
			return usd;
		case "Heated Freight":
			return heatedChrge;
		case "APPOINTMENT CHARGE":
			return aptCharge;
		case "Dangerous Goods":
			return dangersGood;
		default:
			return null;
		}
	}

	public void enterQuantity(String quantity) {
		try {
			WebElement element = driver.findElement(quantityBy);
			element.sendKeys(quantity);
			waitForLoaderToDisappear();
		} catch (Exception e) {
			System.out.println("Error entering quantity: " + e.getMessage());
		}
	}

	public void enterDescription(String description) throws InterruptedException {
		driver.findElement(descriptionInput).sendKeys(description);
		Thread.sleep(2000); // Wait for 2 seconds if necessary
	}

	public void enterPieces(String pieces) throws InterruptedException {
		driver.findElement(piecesInput).sendKeys(pieces);
		Thread.sleep(5000); // Wait for 5 seconds if necessary
	}

	public void enterCubicFeet(String cuft) throws InterruptedException {
		WebElement cuf = driver.findElement(cuftInput);
		cuf.clear(); // Clear the existing value before entering the new one
		cuf.sendKeys(cuft);
		Thread.sleep(5000); // Wait for 5 seconds if necessary
	}

	public void enterWeight(String weight) throws InterruptedException {
		WebElement element = driver.findElement(weightInput);
		element.sendKeys(weight);
		element.sendKeys(Keys.TAB); // Move to the next field
		Thread.sleep(2000); // Wait for 2 seconds if necessary
	}

	public void navigateThroughFields() throws InterruptedException {
		driver.findElement(scaledWeightInput).sendKeys(Keys.TAB); // Move to QST portion
		driver.findElement(qstPortionInput).sendKeys(Keys.TAB); // Move to declared value
		driver.findElement(declaredValueInput).sendKeys(Keys.TAB); // Move to the next field if necessary
	}

	public void calculateAndConfirm() throws InterruptedException {
		driver.findElement(calcButton).click();
		Thread.sleep(2000);

	}

	public void clickPublished() {
		try {
			// Wait for loader to disappear
			waitForLoaderToDisappear();

			// Locate and click the "Published" button
			WebElement publishedButtonElement = driver.findElement(publishedButton);
			wait.until(ExpectedConditions.elementToBeClickable(publishedButton));
			publishedButtonElement.click();

			// Wait for loader to disappear after clicking
			waitForLoaderToDisappear();

			// Check for the toast message
			By toastMessageLocator = By.xpath("//div[contains(@class,'toast-message')]"); // Update with the correct
																							// toast locator if
																							// different
			WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(toastMessageLocator));

			String toastMessage = toastElement.getText();
			if (toastMessage.equalsIgnoreCase("All reference required")) {
				System.out.println("Test Case Failed: " + toastMessage);
			} else {
				System.out.println("Test Case Passed: Published successfully.");
			}

		} catch (Exception e) {
			System.out.println("Error in clickPublished: " + e.getMessage());
		}
	}

	public void saveToDraft() throws InterruptedException {
		wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
		WebElement saveToDraftElement = driver.findElement(saveToDraft);
		wait.until(ExpectedConditions.elementToBeClickable(saveToDraftElement));
		saveToDraftElement.click();

	}

	/*
	 * public void clickThreeDot() throws InterruptedException {
	 * wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
	 * WebElement threeDotElement = driver.findElement(clickThreeDot);
	 * wait.until(ExpectedConditions.elementToBeClickable(threeDotElement));
	 * threeDotElement.click(); }
	 * 
	 * public void clickMoveToAutorate() throws InterruptedException {
	 * driver.findElement(clickMoveToAutorate).click(); Thread.sleep(2000); }
	 */
	public void clickYesButton() {
		driver.findElement(confirmButton).click();
		waitForLoaderToDisappear();
	}

	public void testToastMessageAfterPublish() {
        // Locate and click the Publish button
        WebElement publishButton = driver.findElement(publishedButton); 
        publishButton.click();

        // Wait for the toast message to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@id='toastmessageerror']")));

        // Validate the presence of the toast message
        if(toastMessage.isDisplayed()) {
        	System.out.println("Test passed "  + toastMessage.getText());
        }
        else {
        	System.out.println("Test Passed no toast message appered");
        }
	}
}
