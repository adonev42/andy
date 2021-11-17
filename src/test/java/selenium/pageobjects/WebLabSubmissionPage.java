package selenium.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Pattern;

public class WebLabSubmissionPage {
    private final WebDriver driver;
    private final String url;

    private static final String SOLUTION_DIV_XPATH = "/html/body/div[3]/div[5]/div/div/div[3]/div[1]/div";

    public WebLabSubmissionPage(WebDriver driver, String url) {
        this.driver = driver;
        this.url = url;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = SOLUTION_DIV_XPATH)
    private WebElement solutionDiv;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/span")
    private WebElement saveButton;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/div/a[2]")
    private WebElement runOnlyTestsBtn;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/div/a[3]")
    private WebElement runWithCoverageBtn;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/div/a[4]")
    private WebElement assessWithoutHintsBtn;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/div/a[5]")
    private WebElement assessWithHintsBtn;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/div/div/pre")
    private WebElement output;

    public void navigate() {
        this.driver.navigate().to(url);
    }

    public void runOnlyTests() {
        this.runOnlyTestsBtn.click();
    }

    public void runWithCoverage() {
        this.runWithCoverageBtn.click();
    }

    public void assessWithoutHints() {
        this.assessWithoutHintsBtn.click();
    }

    public void assessWithHints() {
        this.assessWithHintsBtn.click();
    }

    public void enterSolution(String solution) {
        // Wait until solution div is visible to prevent flakiness
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(SOLUTION_DIV_XPATH)));

        this.solutionDiv.click();
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL);
        actions.sendKeys("a");
        actions.keyUp(Keys.CONTROL);
        actions.sendKeys(Keys.BACK_SPACE);
        actions.sendKeys(solution);
        actions.perform();
        this.saveButton.click();
    }

    public String getOutput() {
        // Wait until WebLab finishes processing submission
        WebDriverWait wait = new WebDriverWait(driver, 90);
        wait.until(ExpectedConditions.textMatches(By.id(output.getAttribute("id")),
                Pattern.compile("Status: Done")));
        return this.output.getText();
    }
}
