package yola.form.pageobjects;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FormPage {

    private static By emailInputFieldLocator = By.cssSelector(".quantumWizTextinputPaperinputInput");
    private static By invalidEmailErrorLocator = By.cssSelector("#i2");
    private static By invalidDateErrorLocator = By.cssSelector("#i\\.err\\.86059121");
    private static By invalidNameErrorLocator = By.cssSelector("#i\\.err\\.404367803");
    private static By unmarkedCheckboxErrorLocator = By.cssSelector("#i\\.err\\.1001784558");
    private static By dateFieldLocator = By.cssSelector(".quantumWizTextinputPaperinputInput");
    private static By yourNameInputFieldLocator = By.cssSelector(".quantumWizTextinputPaperinputInput");
    private static By checkboxLocator = By.cssSelector(".quantumWizTogglePapercheckboxInnerBox");
    private static By otherCheckboxInputFieldLocator = By.cssSelector(".quantumWizTextinputSimpleinputInput");
    private static By submitButtonLocator = By.cssSelector(".appsMaterialWizButtonPaperbuttonLabel");
    private static By responseHasBeenRecordedMessageLocator = By.cssSelector(".freebirdFormviewerViewResponseConfirmationMessage");
    private static By submitAnotherResponseLinkLocator = By.cssSelector(".freebirdFormviewerViewResponseLinksContainer > a");


    public FormPage enterEmail(String email) {
        $$(emailInputFieldLocator).get(0).setValue(email);
        return this;
    }

    public FormPage enterBirthDate(String mm, String ss, String yyyy) {
        if (mm.equals("")) {
            $$(dateFieldLocator).get(1).pressTab().setValue(ss + yyyy);
        }
        else if (ss.equals("")) {
            $$(dateFieldLocator).get(1).setValue(mm).pressTab().setValue(yyyy);
        }
        else if (yyyy.equals("")) {
            $$(dateFieldLocator).get(1).setValue(mm + ss);
        }
        else $$(dateFieldLocator).get(1).setValue(mm + ss + yyyy);
        return this;
    }

    public FormPage enterName(String name) {
        $$(yourNameInputFieldLocator).get(2).setValue(name);
        return this;
    }

    public FormPage clickExcellentCheckbox() {
        $$(checkboxLocator).get(0).click();
        return this;
    }

    public FormPage assertExcellentCheckboxNotMarked() {
        $$(checkboxLocator).get(0).shouldNotBe(Condition.checked);
        return this;
    }

    public FormPage clickGoodEnoughCheckbox() {
        $$(checkboxLocator).get(1).click();
        return this;
    }

    public FormPage assertGoodEnoughCheckboxNotMarked() {
        $$(checkboxLocator).get(1).shouldNotBe(Condition.checked);
        return this;
    }

    public FormPage clickCouldBeBetterCheckbox() {
        $$(checkboxLocator).get(2).click();
        return this;
    }

    public FormPage assertCouldBeBetterCheckboxNotMarked() {
        $$(checkboxLocator).get(2).shouldNotBe(Condition.checked);
        return this;
    }

    public FormPage clickVeryBadCheckbox() {
        $$(checkboxLocator).get(3).click();
        return this;
    }

    public FormPage assertVeryBadCheckboxNotMarked() {
        $$(checkboxLocator).get(3).shouldNotBe(Condition.checked);
        return this;
    }

    public FormPage assertOtherCheckboxNotMarked() {
        $$(checkboxLocator).get(4).shouldNotBe(Condition.checked);
        return this;
    }

    public FormPage clickOtherCheckbox() {
        $$(checkboxLocator).get(4).click();
        return this;
    }

    public FormPage enterTextInTheFieldOfOtherCheckbox(String text) {
        $$(otherCheckboxInputFieldLocator).get(0).sendKeys(text);
        return this;
    }

    public FormPage clickSubmitButton() {
        $(submitButtonLocator).click();
        return this;
    }

    public FormPage assertResponseHasBeenRecordedMessageShown() {
        $(responseHasBeenRecordedMessageLocator).shouldBe(Condition.visible);
        return this;
    }

    public FormPage assertResponseHasBeenRecordedMessageNotShown() {
        $(responseHasBeenRecordedMessageLocator).shouldNotBe(Condition.visible);
        return this;
    }

    public FormPage clickSubmitAnotherResponseLink() {
        $$(submitAnotherResponseLinkLocator).get(0).click();
        return this;
    }

    public FormPage assertInvalidEmailErrorShown() {
        $(invalidEmailErrorLocator).shouldBe(Condition.visible);
        return this;
    }

    public FormPage assertInvalidDateErrorShown() {
        $(invalidDateErrorLocator).shouldBe(Condition.visible);
        return this;
    }

    public FormPage assertInvalidNameErrorShown() {
        $(invalidNameErrorLocator).shouldBe(Condition.visible);
        return this;
    }

    public FormPage assertCheckboxErrorShown() {
        $(unmarkedCheckboxErrorLocator).shouldBe(Condition.visible);
        return this;
    }

    public FormPage assetPageWithTestFormOpened() {
        $(submitButtonLocator).shouldBe(Condition.visible);
        return this;
    }

    public FormPage pressTabInTheDateInputField() {
        $$(dateFieldLocator).get(1).pressTab();
        return this;
    }

    public FormPage assertDateFieldContains(String mm, String ss, String yyyy) {
        String inputtedDate;
        if ((mm + ss + yyyy).equals("")) {
            inputtedDate = yyyy + mm + ss;
        } else {
            inputtedDate = yyyy + "-" + mm + "-" + ss;
        }
        String currentDate = $$(dateFieldLocator).get(1).getValue();
        assert currentDate.equals(inputtedDate);
        return this;
    }

    public FormPage assertEmailInputFieldEmpty() {
        $$(emailInputFieldLocator).get(0).shouldBe(Condition.empty);
        return this;
    }

    public FormPage assertDateInputFieldEmpty() {
        $$(dateFieldLocator).get(1).shouldBe(Condition.empty);
        return this;
    }

    public FormPage assertNameInputFieldEmpty() {
        $$(yourNameInputFieldLocator).get(2).shouldBe(Condition.empty);
        return this;
    }

    public FormPage assertFieldOfOtherCheckboxEmpty() {
        $$(otherCheckboxInputFieldLocator).get(0).shouldBe(Condition.empty);
        return this;
    }

}
