package com.radoslawzerek.bigbetfrontend.views;

import com.radoslawzerek.bigbetfrontend.dto.BetDto;
import com.radoslawzerek.bigbetfrontend.dto.UserDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@UIScope
@PreserveOnRefresh
@SpringComponent
@Route(value = "bets")
public class UserBetsView extends VerticalLayout implements BeforeEnterObserver{

    private final MainView mainView;
    private final TextField userName = new TextField("User");
    private List<BetDto> bets;
    private final Grid<BetDto> grid = new Grid<>();
    private final Checkbox onlyPending = new Checkbox();
    private final ChronoUnit seconds = ChronoUnit.SECONDS;


    @Autowired
    public UserBetsView(MainView mainView) {
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
        mainPageButton.addClickListener(event6 -> mainPageButton.getUI().ifPresent(ui -> ui.navigate("")));

        setHorizontalComponentAlignment(Alignment.END, mainPageButton);

        HorizontalLayout userBar = new HorizontalLayout();
        userBar.getStyle().set("border-bottom-style", "solid");
        userBar.getStyle().set("border-width", "1px");
        userBar.getStyle().set("padding-bottom", "15px");


        userName.setReadOnly(true);
        userName.getStyle().set("font-size", "22px");

        onlyPending.setLabel("Show only pending bets");
        onlyPending.addValueChangeListener(event -> {
            bets = mainView.getBigBetFacade().getBetsOfUser(mainView.getUser().getId(),
                    onlyPending.getValue());
            grid.setItems(bets);
        });


        userBar.add(userName, onlyPending);
        userBar.setDefaultVerticalComponentAlignment(Alignment.END);


        grid.addColumn(betDto -> betDto.getBetProspect().getTeams().get(0))
                .setHeader("Home team");
        grid.addColumn(betDto -> betDto.getBetProspect().getTeams().get(1))
                .setHeader("Away team");
        grid.addColumn(betDto -> betDto.getBetProspect().getCommence_time().toLocalDateTime()
                .plusHours(1).toString().replace('T', ' '))
                .setHeader("Match start");
        grid.addColumn(BetDto::getTippedWinner)
                .setHeader("Bet on");
        grid.addColumn(BetDto::getOdd)
                .setHeader("Odd");
        grid.addColumn(BetDto::getWinner)
                .setHeader("Actual winner");
        grid.addColumn(betDto -> betDto.getStake() + " €")
                .setHeader("Stake");
        grid.addColumn(betDto -> betDto.getOdd().multiply(betDto.getStake())
                .setScale(2, RoundingMode.HALF_UP) + " €")
                .setHeader("Potential win");
        grid.addColumn(betDto -> betDto.isFinalized() ? "yes" : "no")
                .setHeader("Finalized");
        grid.addColumn(betDto -> betDto.getCashWin() == null ? "" : betDto.getCashWin() + " €")
                .setHeader("Cash win");

        grid.getColumns()
                .forEach(column -> column.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> {
            BetDto bet = grid.asSingleSelect().getValue();
            if (bet != null){
                if (bet.getBetProspect().getCommence_time().isBefore(ZonedDateTime.now())){
                    Dialog deleteBetDialog = new Dialog();
                    Label deleteBetLabel = new Label("Do you want to delete bet?");
                    Button confirmButton = new Button("Delete");
                    confirmButton.addClickListener(event1 -> {
                        if (bet.getBetProspect().getCommence_time().isBefore(ZonedDateTime.now())){
                            UserDto user = mainView.getBigBetFacade().getUserById(bet.getUser().getId());
                            if(user != null){
                                BigDecimal newBalance = user.getBalance().add(bet.getStake())
                                        .setScale(2, RoundingMode.HALF_UP);
                                user.setBalance(newBalance);
                                mainView.getBigBetFacade().updateUser(user);
                                mainView.setUser(user);
                                mainView.updateUserBar();
                                mainView.getBigBetFacade().deleteBet(bet.getId());
                                bets = mainView.getBigBetFacade().getBetsOfUser(mainView.getUser().getId(),
                                        onlyPending.getValue());
                                grid.setItems(bets);
                                Notification.show("Bet has been successfully deleted\n" +
                                        "Stake from this bets was added to your balance.");
                                grid.deselectAll();
                                deleteBetDialog.close();
                            }
                        }else{
                            Notification.show("Too late, you can not delete bet after match starts!");
                            deleteBetDialog.close();
                            grid.deselectAll();
                        }
                    });
                    Button cancelButton = new Button("Cancel");
                    cancelButton.addClickListener(event2 -> {
                        deleteBetDialog.close();
                        grid.deselectAll();
                    });

                    VerticalLayout mainDialogLayout = new VerticalLayout();
                    mainDialogLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
                    HorizontalLayout buttonsLayout = new HorizontalLayout();
                    buttonsLayout.add(confirmButton, cancelButton);
                    mainDialogLayout.add(deleteBetLabel, buttonsLayout);
                    deleteBetDialog.add(mainDialogLayout);
                    deleteBetDialog.open();
                } else {
                    Notification.show("By clicking on bet you can try to delete it. \n" +
                            "Bet can be deleted only before match start!");
                    grid.deselectAll();
                }
            }
        });
        grid.getStyle().set("padding-left", "10em");
        add(logOut, mainPageButton, userBar, grid);
        getStyle().set("padding-left", "5em");
        getStyle().set("padding-right", "5em");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (mainView.getUser() != null) {
            userName.setValue(mainView.getUser().getUsername());
            bets = mainView.getBigBetFacade().getBetsOfUser(mainView.getUser().getId(),
                    onlyPending.getValue());
            grid.setItems(bets);
            Notification.show("By clicking on bet you can try to delete it. \n" +
                    "Bet can be deleted only before match start!");
        } else {
            mainView.cleanUp();
            event.forwardTo(LoginView.class);
        }
    }
}
