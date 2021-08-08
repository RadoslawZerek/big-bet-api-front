package com.radoslawzerek.bigbetfrontend.views.mainviewlayouts;

import com.radoslawzerek.bigbetfrontend.dto.BetDto;
import com.radoslawzerek.bigbetfrontend.dto.UserDto;
import com.radoslawzerek.bigbetfrontend.enums.Winner;
import com.radoslawzerek.bigbetfrontend.views.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;


public class BetLayout extends VerticalLayout {

    private final ChronoUnit seconds = ChronoUnit.SECONDS;

    public BetLayout(MainView mainView, BetDto betDto) {
        Label hTeam = new Label(betDto.getBetProspect().getTeams().get(0));
        Label aTeam = new Label(betDto.getBetProspect().getTeams().get(1));
        Label vs = new Label("vs");
        hTeam.getStyle().set("font-size", "18px");
        hTeam.getStyle().set("font-weight", "bold");
        aTeam.getStyle().set("font-size", "18px");
        aTeam.getStyle().set("font-weight", "bold");
        HorizontalLayout teamsLine = new HorizontalLayout();
        teamsLine.add(hTeam, vs, aTeam);
        teamsLine.setDefaultVerticalComponentAlignment(Alignment.CENTER);


        Label startLabel = new Label("Start: ");
        Label startTimeLabel = new Label(betDto.getBetProspect().getCommence_time()
                .toLocalDateTime().plusHours(1).toString().replace('T', ' '));
        startTimeLabel.getStyle().set("font-weight", "bold");
        Label betOnLabel = new Label(" Bet on: ");
        Label betOnTeamLabel = new Label(betDto.getTippedWinner().toString());
        betOnTeamLabel.getStyle().set("font-weight", "bold");
        HorizontalLayout startAndBetOnLine = new HorizontalLayout();
        startAndBetOnLine.add(startLabel, startTimeLabel, betOnLabel, betOnTeamLabel);

        BigDecimal odd;
        if (Winner.HOME_TEAM.equals(betDto.getTippedWinner())) {
            odd = betDto.getBetProspect().getH2h().get(0);
        } else if (Winner.DRAW.equals(betDto.getTippedWinner())) {
            odd = betDto.getBetProspect().getH2h().get(1);
        } else {
            odd = betDto.getBetProspect().getH2h().get(2);
        }

        HorizontalLayout stakeLine = new HorizontalLayout();
        BigDecimalField stakeField = new BigDecimalField("Stake");
        stakeField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        stakeField.setPrefixComponent(new Icon(VaadinIcon.EURO));
        stakeField.setValue(BigDecimal.ZERO);
        stakeField.setValueChangeMode(ValueChangeMode.EAGER);

        BigDecimalField oddField = new BigDecimalField("Odd");
        oddField.setValue(odd);
        oddField.setWidth("4em");
        oddField.setReadOnly(true);

        BigDecimalField possibleWinField = new BigDecimalField("Possible win");
        possibleWinField.setReadOnly(true);
        possibleWinField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        possibleWinField.setPrefixComponent(new Icon(VaadinIcon.EURO));
        possibleWinField.setValue(BigDecimal.ZERO);

        stakeField.addValueChangeListener(event -> {
            if (stakeField.getValue() != null) {
                possibleWinField.setValue(odd.multiply(stakeField.getValue()).setScale(2, RoundingMode.HALF_UP));
            }
        });
        stakeField.focus();

        stakeLine.add(stakeField, oddField, possibleWinField);

        HorizontalLayout buttonLine = new HorizontalLayout();

        Button submit = new Button("Submit");
        submit.addClickListener(event1 -> {
            if (seconds.between(betDto.getBetProspect().getCommence_time(), ZonedDateTime.now()) > 0) {
                Notification.show("Too late, you can not make a bet after match start!\n" +
                        "Bet has been canceled");
                mainView.getBetsLayout().remove(this);
                mainView.updateBetProspectsLayout();
            } else if (stakeField.getValue() == null || stakeField.getValue().compareTo(BigDecimal.ZERO) <= 0
                    || stakeField.getValue().compareTo(mainView.getUser().getBalance()) > 0) {
                Notification.show("Please put stake greater than 0 but not greater than your current balance");
            } else {
                betDto.setCreated(LocalDateTime.now());
                betDto.setStake(stakeField.getValue().setScale(2, RoundingMode.HALF_UP));
                UserDto user = mainView.getBigBetFacade().getUserById(mainView.getUser().getId());
                BigDecimal newBalance = user.getBalance().subtract(stakeField.getValue().setScale(2, RoundingMode.HALF_UP));
                user.setBalance(newBalance);
                mainView.setUser(user);
                mainView.updateUserBar();
                betDto.setUser(user);
                mainView.getBigBetFacade().addBet(betDto);
                Notification.show("Bet was successfully registered, you can see it in My Bets.\n" +
                        "!!!Bet can be deleted only before match start!!!");
                mainView.getBetsLayout().remove(this);
            }
        });

        Button cancel = new Button("Cancel");
        cancel.addClickListener(event2 -> {
            mainView.getBetsLayout().remove(this);
        });
        buttonLine.add(submit, cancel);

        add(teamsLine, startAndBetOnLine, stakeLine, buttonLine);
        getStyle().set("border-style", "solid");
        getStyle().set("border-width", "1px");
        stakeField.focus();
    }
}
