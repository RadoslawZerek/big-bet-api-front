package com.radoslawzerek.bigbetfrontend.views;

import com.radoslawzerek.bigbetfrontend.dto.UserDto;
import com.radoslawzerek.bigbetfrontend.enums.Role;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@UIScope
@PreserveOnRefresh
@SpringComponent
@Route(value = "admin")
public class AdminView extends VerticalLayout implements BeforeEnterObserver {

    private final MainView mainView;
    private final Grid<UserDto> grid = new Grid<>(UserDto.class);

    @Autowired
    public AdminView(MainView mainView) {
        this.mainView = mainView;

        Button logOut = new Button("Log Out");
        logOut.addClickListener(event -> {
            mainView.setUser(null);
            mainView.getBetsLayout().removeAll();
            mainView.getBetsLayout().removeAll();
            logOut.getUI().ifPresent(ui -> ui.navigate("login"));
        });

        setHorizontalComponentAlignment(Alignment.END, logOut);

        Button mainPageButton = new Button("Go to Main Page");
        mainPageButton.addClickListener(event6 -> getUI().ifPresent(ui -> ui.navigate("")));

        setHorizontalComponentAlignment(Alignment.END, mainPageButton);

        grid.getColumns()
                .forEach(column -> column.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> {
            UserDto user = grid.asSingleSelect().getValue();
            if (user != null) {
                Dialog changePasswordDialog = new Dialog();
                Label changePasswordLabel = new Label("Enter new password for chosen user");
                PasswordField newPassword = new PasswordField("New password");
                PasswordField repeatedNewPassword = new PasswordField("Repeat new password");
                Button submitButton = new Button("Submit");
                submitButton.addClickListener(event1 -> {
                    if (newPassword.getValue().length() < 5) {
                        Notification.show("Please enter ata least 5 character long new password");
                    } else if (!newPassword.getValue().equals(repeatedNewPassword.getValue())) {
                        Notification.show("New password and repeated new password have to be the same!");
                    } else {
                        mainView.getBigBetFacade().updateUserPassword(user.getId(), newPassword.getValue());
                        Notification.show("Password of " + user.getUsername() + " has been successfully changed");
                        changePasswordDialog.close();
                        grid.deselectAll();
                    }

                });
                Button cancelButton = new Button("Cancel");
                cancelButton.addClickListener(event2 -> {
                    changePasswordDialog.close();
                    grid.deselectAll();
                });

                VerticalLayout mainDialogLayout = new VerticalLayout();
                mainDialogLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
                HorizontalLayout buttonsLine = new HorizontalLayout();
                buttonsLine.add(submitButton, cancelButton);
                mainDialogLayout.add(changePasswordLabel, newPassword, repeatedNewPassword, buttonsLine);
                changePasswordDialog.add(mainDialogLayout);
                changePasswordDialog.open();
            }
        });

        grid.removeColumnByKey("password");
        grid.setColumns("id", "username", "email", "role", "created", "balance");
        grid.getColumns()
                .forEach(column -> column.setAutoWidth(true));
        VerticalLayout gridContainer = new VerticalLayout();
        gridContainer.add(grid);
        gridContainer.getStyle().set("padding-left", "10em");
        gridContainer.getStyle().set("padding-right", "10em");
        gridContainer.getStyle().set("padding-top", "5em");
        add(logOut, mainPageButton, gridContainer);
        getStyle().set("padding-left", "5em");
        getStyle().set("padding-right", "5em");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (mainView.getUser() != null && mainView.getUser().getRole().equals(Role.ADMIN)) {
            List<UserDto> users = mainView.getBigBetFacade().getAllUsers();
            grid.setItems(users);
            Notification.show("By clicking on user you can change his password");
        } else {
            mainView.cleanUp();
            event.forwardTo(LoginView.class);
        }
    }
}
