package yola.form.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import yola.form.pageobjects.Pages;


public class FormTest {

    @BeforeMethod
    public void setUp() {
        Configuration.browser = "chrome";
        Selenide.open(Given.TEST_FORM_URL);
    }

    @AfterMethod
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    // Main cases

    @Test(description = "The form is submitted when all required fields are filled with valid values")
    public void testFormSubmittedWithAllValidRequiredValues() {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, Given.VALID_YYYY)
                .enterName(Given.VALID_NAME)
                .clickExcellentCheckbox()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageShown();
    }

    @Test(description = "The form is not submitted when the all input fields are empty")
    public void testFormNotSubmittedWhenAllRequiredFieldsEmpty() {
        Pages.formPage()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageNotShown()
                .assertInvalidEmailErrorShown()
                .assertInvalidDateErrorShown()
                .assertInvalidNameErrorShown()
                .assertCheckboxErrorShown();
    }

    @Test(description = "The form is not submitted when Email address input field is empty")
    public void testFormNotSubmittedWhenOnlyEmailFieldEmpty() {
        Pages.formPage()
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, Given.VALID_YYYY)
                .enterName(Given.VALID_NAME)
                .clickExcellentCheckbox()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageNotShown()
                .assertInvalidEmailErrorShown();
    }

    @Test(description = "The form is not submitted when month input field is empty")
    public void testFormNotSubmittedWhenOnlyMonthValueEmpty() {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate("", Given.VALID_SS, Given.VALID_YYYY)
                .enterName(Given.VALID_NAME)
                .clickExcellentCheckbox()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageNotShown()
                .assertInvalidDateErrorShown();
    }

    @Test(description = "The form is not submitted when day input field is empty")
    public void testFormNotSubmittedWhenOnlyDayValueEmpty() {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(Given.VALID_MM, "", Given.VALID_YYYY)
                .enterName(Given.VALID_NAME)
                .clickExcellentCheckbox()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageNotShown()
                .assertInvalidDateErrorShown();
    }

    @Test(description = "The form is not submitted when year input field is empty")
    public void testFormNotSubmittedWhenOnlyYearValueEmpty() {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, "")
                .enterName(Given.VALID_NAME)
                .clickExcellentCheckbox()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageNotShown()
                .assertInvalidDateErrorShown();
    }

    @Test(description = "The form is not submitted when \"Your name\" input field is empty")
    public void testFormNotSubmittedWhenOnlyNameFieldEmpty() {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, Given.VALID_YYYY)
                .clickExcellentCheckbox()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageNotShown()
                .assertInvalidNameErrorShown();
    }

    @Test(description = "The form is not submitted when none of check boxes is marked")
    public void testFormNotSubmittedWhenNoneOfCheckboxesMarked() {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, Given.VALID_YYYY)
                .enterName(Given.VALID_NAME)
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageNotShown()
                .assertCheckboxErrorShown();
    }

    // "Email address" block checking

    @DataProvider(name = "valid_emails")
    public Object[] validEmails() {
        return new Object[] {
                "firstname.lastname@domain.com", // Email contains dot in the address field
                "email@subdomain.domain.com", // Email contains dot with subdomain
                "firstname+lastname@domain.com", // Plus sign is considered valid character
                "email@123.123.123.123", // Domain is valid IP address
                "'email'@domain.com", // Quotes around email is considered valid
                "1234567890@domain.com", // Digits in address are valid
                "email@domain-one.com", // Dash in domain name is valid
                "_______@domain.com", // Underscore in the address field is valid
                "firstname-lastname@domain.com", // Dash in address field is valid
                "~@example.com", // ~ in the address field is valid
                "_Yosemite.Sam@example.com", // Underscore before name is valid
                "!def!xyz%abc@example.com" // Valid email example
        };
    }

    @Test(description = "The form is submitted when Email address input field contains valid mailboxes", dataProvider = "valid_emails")
    public void testFormSubmittedWhenEmailFieldContainsValidMailboxes(String validEmail) {
        Pages.formPage()
                .enterEmail(validEmail)
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, Given.VALID_YYYY)
                .enterName(Given.VALID_NAME)
                .clickGoodEnoughCheckbox()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageShown();
    }

    @DataProvider(name = "invalid_emails")
    public Object[] invalidEmails() {
        return new Object[] {
                "plainaddress", // Missing @ sign and domain
                "#@%^%#$@#$@#.com", // Garbage
                "@domain.com", // Missing username
                "email.domain.com", // Missing @
                "email@domain@domain.com", // Two @ signs
                ".email@domain.com", // Leading dot in address is not allowed
                "email.@domain.com", // Trailing dot in address is not allowed
                "email..email@domain.com", // Multiple dots
                "あいうえお@domain.com", // Unicode char as address
                "email@domain.com (Joe Smith)", //  Text followed email is not allowed
                "email@domain", // Missing top level domain (.com/.net/.org/etc)
                "email@-domain.com", // Leading dash in front of domain is invalid
                "email@111.222.333.44444444", // Invalid IP format
                "email@domain..com", // Multiple dot in the domain portion is invalid
                " ", // Space symbol
                ".@example.com", // Dot before @
                "Ima Fool@example.com" // Space in username
        };
    }

    @Test(description = "The form is not submitted when Email address input field contains invalid mailboxes", dataProvider = "invalid_emails")
    public void testFormNotSubmittedWhenEmailFieldContainsInvalidMailboxes(String invalidEmail) {
        Pages.formPage()
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, Given.VALID_YYYY)
                .enterName(Given.VALID_NAME)
                .clickExcellentCheckbox()
                .enterEmail(invalidEmail)
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageNotShown()
                .assertInvalidEmailErrorShown();
    }

    // 'Your age:' block checking

        // Month validation

    @DataProvider(name = "month_valid_values")
    public static Object[] monthValidValues() {
        return new Object[] {
                "01",
                "12",
        };
    }

    @Test(description = "The form is submitted when month contains >0 and <=12 integers" ,dataProvider = "month_valid_values")
    public void testFormSubmittedWithValidMonthValues(String monthValidValue) {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(monthValidValue, Given.VALID_SS, Given.VALID_YYYY)
                .enterName(Given.VALID_NAME)
                .clickCouldBeBetterCheckbox()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageShown();
    }

    @Test(description = "Month value with > 12 integers is not accepted")
    public void testMoreThanMaxMonthValueIsNotAccepted() {
        Pages.formPage()
                .enterBirthDate("13", Given.VALID_SS, Given.VALID_YYYY)
                .pressTabInTheDateInputField()
                .assertDateFieldContains(Given.MAX_MONTH_VALUE, Given.VALID_SS, Given.VALID_YYYY);
    }

    @Test(description = "Month value with < 1 integer is not accepted")
    public void testLessThanMinMonthValueIsNotAccepted() {
        Pages.formPage()
                .enterBirthDate("00", Given.VALID_SS, Given.VALID_YYYY)
                .pressTabInTheDateInputField()
                .assertDateFieldContains(Given.MIN_MONTH_VALUE, Given.VALID_SS, Given.VALID_YYYY);
    }

        // Day validation

    @DataProvider(name = "day_valid_values")
    public static Object[] dayValidValues() {
        return new Object[] {
                "01",
                "31",
        };
    }

    @Test(description = "The form is submitted when day contains >0 and <=31 integers", dataProvider = "day_valid_values")
    public void testFormSubmittedWithValidDayValues(String dayValidValue) {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(Given.VALID_MM, dayValidValue, Given.VALID_YYYY)
                .enterName(Given.VALID_NAME)
                .clickCouldBeBetterCheckbox()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageShown();

    }

    @Test(description = "Day value with > 12 integers is not accepted")
    public void testMoreThanMaxDayValueIsNotAccepted() {
        Pages.formPage()
                .enterBirthDate(Given.VALID_MM, "32", Given.VALID_YYYY)
                .pressTabInTheDateInputField()
                .assertDateFieldContains(Given.VALID_MM, Given.MAX_DAY_VALUE, Given.VALID_YYYY);
    }

    @Test(description = "Day value with < 1 integer is not accepted")
    public void testLessThanMinDayValueIsNotAccepted() {
        Pages.formPage()
                .enterBirthDate(Given.VALID_MM, "00", Given.VALID_YYYY)
                .pressTabInTheDateInputField()
                .assertDateFieldContains(Given.VALID_MM, Given.MIN_DAY_VALUE, Given.VALID_YYYY);
    }

        // Year validation

    @DataProvider(name = "year_valid_values")
    public static Object[] yearValidValues() {
        return new Object[] {
                "0001",
                "9999",
        };
    }

    @Test(description = "The form is submitted when year contains >0 and <=9999 integers", dataProvider = "year_valid_values")
    public void testFormSubmittedWithValidYearValues(String yearValidValue) {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(Given.VALID_MM, Given.VALID_YYYY, yearValidValue)
                .enterName(Given.VALID_NAME)
                .clickCouldBeBetterCheckbox()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageShown();
    }

    @Test(description = "Year value with > 4 integers is not accepted")
    public void testMoreThanMaxYearValueIsNotAccepted() {
        Pages.formPage()
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, "10000")
                .pressTabInTheDateInputField()
                .assertDateFieldContains(Given.VALID_MM, Given.VALID_SS, Given.MAX_YEAR_VALUE);
    }

    @Test(description = "Year value with < 1 integer is not accepted")
    public void testLessThanMinYearValueIsNotAccepted() {
        Pages.formPage()
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, "0000")
                .pressTabInTheDateInputField()
                .assertDateFieldContains(Given.VALID_MM, Given.VALID_SS, Given.MIN_YEAR_VALUE);
    }

    // Special dates validation

    @Test(description = "February 29th of a leap year is accepted by the date input field")
    public void testLastFebruaryDayOfLeapYearAccepted() {
        Pages.formPage()
                .enterBirthDate(Given.FEBRUARY_MONTH_VALUE, Given.LAST_FEBRUARY_DAY_VALUE_OF_LEAP_YEAR, Given.LEAP_YEAR_VALUE)
                .pressTabInTheDateInputField()
                .assertDateFieldContains(Given.FEBRUARY_MONTH_VALUE, Given.LAST_FEBRUARY_DAY_VALUE_OF_LEAP_YEAR, Given.LEAP_YEAR_VALUE);
    }

    @Test(description = "February 29th of a not leap year is not accepted by the date input field")
    public void testLastFebruaryDayOfNotLeapYearIsNotAccepted() {
        Pages.formPage()
                .enterBirthDate(Given.FEBRUARY_MONTH_VALUE, Given.LAST_FEBRUARY_DAY_VALUE_OF_LEAP_YEAR, Given.VALID_YYYY)
                .pressTabInTheDateInputField()
                .assertInvalidDateErrorShown();
    }

    @DataProvider(name = "special_months_values")
    public static Object[] specialMonthsValues() {
        return new Object[] {
                "09",
                "11",
        };
    }

    @Test(description = "31th days of special months are not accepted by the date input field while input", dataProvider = "special_months_values")
    public void testLastDaysOfSpecialMonthsAreNotAcceptedWhileInput(String specialMonth) {
        Pages.formPage()
                .enterBirthDate(specialMonth, Given.MAX_DAY_VALUE, Given.VALID_YYYY)
                .pressTabInTheDateInputField()
                .assertInvalidDateErrorShown();
    }

    @DataProvider(name = "invalid_date_values")
    public static Object[][] invalidDateValues() {
        return new Object[][] {
                {" ", "", ""},
                {"Aa`~!@@#$%^&*()_-+=:;,./?\\|дД", "", ""},
                {"", " ", ""},
                {"", "Aa`~!@@#$%^&*()_-+=:;,./?\\|дД", ""},
                {"", "", " "},
                {"", "", "Aa`~!@@#$%^&*()_-+=:;,./?\\|дД"},
        };
    }

    @Test(description = "Values with special symbols and letters are not accepted by date input field", dataProvider = "invalid_date_values")
    public void testInvalidDateValuesAreNotAcceptedWhileInput(String invalidMonthValue, String invalidDayValue, String invalidYearValue) {
        Pages.formPage()
                .enterBirthDate(invalidMonthValue, invalidDayValue, invalidYearValue)
                .pressTabInTheDateInputField()
                .assertDateFieldContains("", "", "");
    }

    @Test(description = "Zero is automatically added before a single number inputted in the month, day and year of the date input field")
    public void testZeroAddedBeforeSingleIntegerInput() {
        Pages.formPage()
                .enterBirthDate(Given.SINGLE_NUMBER_VALUE, Given.SINGLE_NUMBER_VALUE, Given.SINGLE_NUMBER_VALUE)
                .pressTabInTheDateInputField()
                .assertDateFieldContains("0" + Given.SINGLE_NUMBER_VALUE, "0" + Given.SINGLE_NUMBER_VALUE, "000" + Given.SINGLE_NUMBER_VALUE);
    }

    // "Your name" block checking

    @DataProvider(name = "names_with_valid_lengths")
    public static Object[] namesWithValidLengths() {
        return new Object[] {
                "T",
                "Test-With 19symbols",
                "Test-With 20-symbols"
        };
    }

    @Test(description = "Your name input field accepts value with  >0 and <=20 symbols", dataProvider = "names_with_valid_lengths")
    public void testFormSubmittedWithValidNameLength(String nameWithValidLengths) {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, Given.VALID_YYYY)
                .enterName(nameWithValidLengths)
                .clickExcellentCheckbox()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageShown();
    }

    @Test(description = "Your name input field does not accept > 20 symbols value")
    public void testFormNotSubmittedWithInvalidNameLength() {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, Given.VALID_YYYY)
                .clickExcellentCheckbox()
                .enterName(Given.NAME_WITH_INVALID_LENGTH)
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageNotShown()
                .assertInvalidNameErrorShown();
    }

    // "How is your mood?" block checking

    @Test(description = "Form is submitted when 'Other:' check box is marked and related input field contains symbols")
    public void testFormSubmittedWhenOtherCheckboxMarkedWithFilledField() {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, Given.VALID_YYYY)
                .enterName(Given.VALID_NAME)
                .clickOtherCheckbox()
                .enterTextInTheFieldOfOtherCheckbox(Given.TEXT_WITH_200_SYMBOLS)
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageShown();
    }

    @Test(description = "The form is not submitted when 'Other:' check box is marked and depended input field is empty")
    public void testFormNotSubmittedWhenOtherCheckboxRelatedFieldEmpty() {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, Given.VALID_YYYY)
                .enterName(Given.VALID_NAME)
                .clickOtherCheckbox()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageNotShown()
                .assertCheckboxErrorShown();
    }

    @Test(description = "Only one check box of the 'How is your mood?' block can be marked at one time")
    public void testOneCheckboxCanBeMarkedAtOneTime() {
        Pages.formPage()
                .clickExcellentCheckbox()
                .clickGoodEnoughCheckbox()
                .clickCouldBeBetterCheckbox()
                .clickVeryBadCheckbox()
                .clickOtherCheckbox();
        Pages.formPage()
                .assertExcellentCheckboxNotMarked()
                .assertGoodEnoughCheckboxNotMarked()
                .assertCouldBeBetterCheckboxNotMarked()
                .assertVeryBadCheckboxNotMarked();
    }

    // Other cases checking

    @Test(description = "'Submit another response' link reopens new form")
    public void testSubmitAnotherResponseLinkOpensNewForm() {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, Given.VALID_YYYY)
                .enterName(Given.VALID_NAME)
                .clickExcellentCheckbox()
                .clickSubmitButton()
                .assertResponseHasBeenRecordedMessageShown()
                .clickSubmitAnotherResponseLink()
                .assetPageWithTestFormOpened();
    }

    @Test(description = "All inputted data is cleared after refreshing the page")
    public void testAllInputtedDataClearedAfterPageRefresh() {
        Pages.formPage()
                .enterEmail(Given.VALID_EMAIL)
                .enterBirthDate(Given.VALID_MM, Given.VALID_SS, Given.VALID_YYYY)
                .enterName(Given.VALID_NAME)
                .clickExcellentCheckbox()
                .clickGoodEnoughCheckbox()
                .clickCouldBeBetterCheckbox()
                .clickVeryBadCheckbox()
                .clickOtherCheckbox()
                .enterTextInTheFieldOfOtherCheckbox(Given.TEXT_WITH_200_SYMBOLS);
        Selenide.refresh();
        Selenide.switchTo().alert().accept();
        Pages.formPage()
                .assertEmailInputFieldEmpty()
                .assertDateInputFieldEmpty()
                .assertNameInputFieldEmpty()
                .assertExcellentCheckboxNotMarked()
                .assertGoodEnoughCheckboxNotMarked()
                .assertCouldBeBetterCheckboxNotMarked()
                .assertVeryBadCheckboxNotMarked()
                .assertOtherCheckboxNotMarked()
                .assertFieldOfOtherCheckboxEmpty();
    }

}
