package com.radoslawzerek.bigbetfrontend.views;

import com.radoslawzerek.bigbetfrontend.dto.UserDto;
import com.radoslawzerek.bigbetfrontend.facade.BigBetFacade;
import com.radoslawzerek.bigbetfrontend.pojo.SignUpFeedback;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@UIScope
@SpringComponent
@Route(value = "signup")
public class SignUpView extends VerticalLayout {

    @Autowired
    public SignUpView(MainView mainView, BigBetFacade bigBetFacade) {

        TextField username = new TextField("Username");
        TextField email = new TextField("E-mail");
        PasswordField password = new PasswordField("Password");
        PasswordField repeatPassword = new PasswordField("Repeat password");
        DatePicker birthDate = new DatePicker("Day of birth");
        birthDate.setInitialPosition(LocalDate.now());
        ChronoUnit years = ChronoUnit.YEARS;

        Button signInButton = new Button("Sign Up");
        signInButton.addClickListener(event -> {
            String usernameInput = username.getValue();
            String emailInput = email.getValue();
            String passwordInput = password.getValue();
            String repeatPasswordInput = repeatPassword.getValue();
            if (birthDate.getValue() == null || years.between(birthDate.getValue(), LocalDate.now()) < 18) {
                Notification.show("You must be at least 18 years old to sing up!");
            } else if (usernameInput == null || usernameInput.length() < 3) {
                Notification.show("Please enter at least 3 character long user login!");
                //TODO validate e-mail!
            } else if (emailInput == null || emailInput.length() < 5) {
                Notification.show("Please enter correct e-mail address!");
            } else if (passwordInput == null || passwordInput.length() < 5) {
                Notification.show("Please enter at least 5 character long password!");
            } else if (!passwordInput.equals(repeatPasswordInput)) {
                Notification.show("Password and repeated password need to be the same!");
            } else {
                UserDto user = new UserDto(usernameInput, emailInput, passwordInput);
                SignUpFeedback signUpFeedback = bigBetFacade.signUserUp(user);
                if (signUpFeedback.getUser() != null) {
                    username.setValue("");
                    email.setValue("");
                    password.setValue("");
                    repeatPassword.setValue("");
                    birthDate.setValue(LocalDate.now());
                    mainView.cleanUp();
                    mainView.setUser(signUpFeedback.getUser());
                    signInButton.getUI().ifPresent(ui -> ui.navigate(""));
                } else {
                    Notification.show(signUpFeedback.getMessage());
                }
            }
        });

        Button login = new Button("Log In");
        login.addClickListener(event -> login.getUI().ifPresent(ui -> ui.navigate("login")));

        setHorizontalComponentAlignment(Alignment.CENTER, username);
        setHorizontalComponentAlignment(Alignment.CENTER, email);
        setHorizontalComponentAlignment(Alignment.CENTER, password);
        setHorizontalComponentAlignment(Alignment.CENTER, repeatPassword);
        setHorizontalComponentAlignment(Alignment.CENTER, birthDate);
        setHorizontalComponentAlignment(Alignment.CENTER, signInButton);
        setHorizontalComponentAlignment(Alignment.CENTER, login);

        add(username, email, password, repeatPassword, birthDate, signInButton, login);

        System.out.println("From Sign Up: " + mainView);
    }
}
