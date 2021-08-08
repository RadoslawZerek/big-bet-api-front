package com.radoslawzerek.bigbetfrontend.views;

import com.radoslawzerek.bigbetfrontend.dto.UserDto;
import com.radoslawzerek.bigbetfrontend.pojo.LogInFeedback;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;


@UIScope
@PreserveOnRefresh
@SpringComponent
@Route(value = "user")
public class UserDataView extends VerticalLayout implements BeforeEnterObserver {

    private final MainView mainView;
    private final TextField userName = new TextField("Username");
    private final TextField email = new TextField("E-mail");



    @Autowired
    public UserDataView(MainView mainView) {
        this.mainView = mainView;

        Button logOut = new Button("Log Out");
        logOut.addClickListener(event -> {
            mainView.setUser(null);
            mainView.getBetsLayout().removeAll();
            mainView.getBetsLayout().removeAll();
            logOut.getUI().ifPresent(ui -> ui.navigate("login"));
        });

        Button mainPageButton = new Button("Go to Main Page");
        mainPageButton.addClickListener(event6 -> getUI().ifPresent(ui -> ui.navigate("")));

        Button changeUserName = new Button("Change username");
        changeUserName.addClickListener(event -> {
            Dialog changeNameDialog = new Dialog();
            TextField newUsernameField = new TextField("New username");
            newUsernameField.setValue(mainView.getUser().getUsername());
            PasswordField passwordField = new PasswordField("Confirm password");

            Button submitChangeName = new Button("Submit");
            submitChangeName.addClickListener(event1 -> {
                LogInFeedback logInFeedback = mainView.getBigBetFacade()
                        .logUserIn(mainView.getUser().getUsername(), passwordField.getValue());
                if(newUsernameField.getValue().length() < 3){
                    Notification.show("Please enter at least 3 character long new username");
                }else if (logInFeedback.getUser() == null) {
                    Notification.show(logInFeedback.getMessage());
                } else if(mainView.getBigBetFacade().checkIfUserExists(newUsernameField.getValue())){
                    Notification.show("User with pointed username already exists! Please choose other username");
                } else {
                    UserDto user = logInFeedback.getUser();
                    user.setUsername(newUsernameField.getValue());
                    mainView.getBigBetFacade().updateUser(user);
                    mainView.setUser(user);
                    mainView.updateUserBar();
                    userName.setValue(newUsernameField.getValue());
                    Notification.show("Username has been successfully changed!");
                    changeNameDialog.close();

                }
            });

            Button cancelChangeName = new Button("Cancel");
            cancelChangeName.addClickListener(event2 -> changeNameDialog.close());
            VerticalLayout changeNameDialogLayout = new VerticalLayout();
            changeNameDialogLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            HorizontalLayout buttonsLine = new HorizontalLayout();
            buttonsLine.add(submitChangeName, cancelChangeName);
            changeNameDialogLayout.add(newUsernameField, passwordField, buttonsLine);
            changeNameDialog.add(changeNameDialogLayout);
            changeNameDialog.open();
        });

        Button changeEmail = new Button("Change e-mail");
        changeEmail.addClickListener(event3 -> {
            Dialog changeEmailDialog = new Dialog();
            TextField newEmailField = new TextField("New e-mail");
            newEmailField.setWidthFull();
            newEmailField.setValue(mainView.getUser().getEmail());
            PasswordField passwordField = new PasswordField("Confirm password");

            Button submitChangeEmail = new Button("Submit");
            submitChangeEmail.addClickListener(event4 -> {
                LogInFeedback logInFeedback = mainView.getBigBetFacade()
                        .logUserIn(mainView.getUser().getUsername(), passwordField.getValue());
                if(newEmailField.getValue().length() < 5){
                    Notification.show("Please enter proper new e-mail");
                }else if (logInFeedback.getUser() == null) {
                    Notification.show(logInFeedback.getMessage());
                }else if(mainView.getBigBetFacade().checkIfUserExists(newEmailField.getValue())){
                    Notification.show("User with pointed email already exists! There can not be 2 users with the same e-mail!");
                }  else {
                    UserDto user = logInFeedback.getUser();
                    user.setEmail(newEmailField.getValue());
                    mainView.getBigBetFacade().updateUser(user);
                    mainView.setUser(user);
                    mainView.updateUserBar();
                    email.setValue(newEmailField.getValue());
                    Notification.show("E-mail has been successfully changed!");
                    changeEmailDialog.close();

                }
            });

            Button cancelChangeEmail = new Button("Cancel");
            cancelChangeEmail.addClickListener(event2 -> changeEmailDialog.close());
            VerticalLayout changeEmailDialogLayout = new VerticalLayout();
            changeEmailDialogLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            HorizontalLayout buttonsLine = new HorizontalLayout();
            buttonsLine.add(submitChangeEmail, cancelChangeEmail);
            changeEmailDialogLayout.add(newEmailField, passwordField, buttonsLine);
            changeEmailDialog.add(changeEmailDialogLayout);
            changeEmailDialog.open();
        });

        Button changeUserPassword = new Button("Change password");
        changeUserPassword.addClickListener(event6 -> {
            Dialog changePasswordDialog = new Dialog();
            PasswordField newPasswordField = new PasswordField("New password");
            PasswordField repeatNewPasswordField = new PasswordField("Repeat new password");
            PasswordField oldPasswordField = new PasswordField("Confirm old password");

            Button submitChangePassword = new Button("Submit");
            submitChangePassword.addClickListener(event7 -> {
                LogInFeedback logInFeedback = mainView.getBigBetFacade()
                        .logUserIn(mainView.getUser().getUsername(), oldPasswordField.getValue());
                if(newPasswordField.getValue().length() < 5){
                    Notification.show("Please enter at least 5 character long new password");
                }else if (!newPasswordField.getValue().equals(repeatNewPasswordField.getValue())){
                    Notification.show("New password and repeated new password have to be the same!");
                } else if (logInFeedback.getUser() == null) {
                    Notification.show(logInFeedback.getMessage());
                } else {
                    UserDto user = logInFeedback.getUser();
                    mainView.getBigBetFacade().updateUserPassword(user.getId(), newPasswordField.getValue());
                    Notification.show("Password has been successfully changed!");
                    changePasswordDialog.close();

                }
            });

            Button cancelChangePassword = new Button("Cancel");
            cancelChangePassword.addClickListener(event8 -> changePasswordDialog.close());
            VerticalLayout changePasswordDialogLayout = new VerticalLayout();
            changePasswordDialogLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            HorizontalLayout buttonsLine = new HorizontalLayout();
            buttonsLine.add(submitChangePassword, cancelChangePassword);
            changePasswordDialogLayout.add(newPasswordField, repeatNewPasswordField, oldPasswordField,
                    buttonsLine);
            changePasswordDialog.add(changePasswordDialogLayout);
            changePasswordDialog.open();
        });


        userName.setReadOnly(true);
        userName.getStyle().set("font-size", "22px");
        userName.setWidth("13.7em");
        HorizontalLayout userNameLine = new HorizontalLayout();
        userNameLine.setDefaultVerticalComponentAlignment(Alignment.END);
        userNameLine.add(userName, changeUserName);

        email.setReadOnly(true);
        email.setWidth("19em");
        changeEmail.setWidth("10em");
        HorizontalLayout emailLine = new HorizontalLayout();
        emailLine.setDefaultVerticalComponentAlignment(Alignment.END);
        emailLine.add(email, changeEmail);

        HorizontalLayout passwordLine = new HorizontalLayout();
        Label emptyLabel = new Label();
        emptyLabel.setWidth("19em");
        passwordLine.add(emptyLabel, changeUserPassword);
        passwordLine.getStyle().set("padding-top", "2em");

        setHorizontalComponentAlignment(Alignment.END, logOut);
        setHorizontalComponentAlignment(Alignment.END, mainPageButton);

        add(logOut, mainPageButton, userNameLine, emailLine, passwordLine);
        getStyle().set("padding-left", "15em");
        getStyle().set("padding-right", "5em");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (mainView.getUser() != null) {
            userName.setValue(mainView.getUser().getUsername());
            email.setValue(mainView.getUser().getEmail());
        } else {
            mainView.cleanUp();
            event.forwardTo(LoginView.class);
        }
    }
}
