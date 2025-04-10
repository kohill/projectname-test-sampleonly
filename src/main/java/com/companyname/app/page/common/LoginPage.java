package com.companyname.app.page.common;

import com.companyname.common.data.Data;
import com.companyname.common.data.login.User;
import com.companyname.common.page.BasePage;

public class LoginPage extends BasePage {

//    public static final By loginFormLocator = By.id("login");

//    public final FixedElement loginForm = new FixedElement(loginFormLocator);
//    public final TextBox userName = new TextBox(By.id("username"));
//    public final ComboBox groups = new ComboBox(By.id("groups"));
//    public final ComboBox states = new ComboBox(By.id("states"));
//    public final ComboBox uwLevel = new ComboBox(By.id("uw_level"));
//    public final ComboBox billingLevel = new ComboBox(By.id("billing_level"));
//    public final TextBox password = new TextBox(By.id("password"));
//    public final Button login = new Button(By.id("submit"));
//
//    private final PasDialog logoutConfirmationDialog;

    public LoginPage() {
        super();

    }

   public void fill(Data rawData) {
        User data = (User) rawData;
        if (data != null) {
//            userName.setValue(data.getUsername());
//            groups.setValue(data.getGroups(), true);
//            states.setValue(BaseTest.getState(), true);
//            uwLevel.setValue(data.getUwAuthLevel());
//            billingLevel.setValue(data.getBillingAuthLevel());
//            password.setValue(data.getPassword());
        }
    }

    public void submit() {

    }

}
