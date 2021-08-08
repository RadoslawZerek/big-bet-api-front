package com.radoslawzerek.bigbetfrontend.views.mainviewlayouts;

import com.radoslawzerek.bigbetfrontend.dto.BetDto;
import com.radoslawzerek.bigbetfrontend.dto.BetProspectDto;
import com.radoslawzerek.bigbetfrontend.enums.Winner;
import com.radoslawzerek.bigbetfrontend.views.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class BetProspectLayout extends HorizontalLayout {

    private final ChronoUnit seconds = ChronoUnit.SECONDS;

    public BetProspectLayout(MainView mainView, BetProspectDto prospect) {
        Label startTime = new Label(prospect.getCommence_time().toLocalDateTime()
                .plusHours(1).toString().replace('T', ' '));
        startTime.setWidth("8em");

        TextField homeTeam = new TextField();
        homeTeam.setValue(prospect.getTeams().get(0));
        homeTeam.setWidth("13em");
        homeTeam.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        homeTeam.setReadOnly(true);

        Button homeTeamOdd = new Button(prospect.getH2h().get(0).toString());
        homeTeamOdd.addClickListener(event -> {
            if (seconds.between(prospect.getCommence_time(), ZonedDateTime.now()) < 0) {
                BetDto bet = new BetDto(mainView.getUser(), prospect, Winner.HOME_TEAM);
                mainView.getBetsLayout().add(new BetLayout(mainView, bet));
                Notification.show("Please enter stake to submit the bet");
            } else {
                Notification.show("Too late, you can not make a bet after match start! \n" +
                        "Odd prospect list has been updated.");
                mainView.updateBetProspectsLayout();
            }
        });

        Button drawOdd = new Button(prospect.getH2h().get(1).toString());
        drawOdd.addClickListener(event1 -> {
            if (seconds.between(prospect.getCommence_time(), ZonedDateTime.now()) < 0) {
                BetDto bet = new BetDto(mainView.getUser(), prospect, Winner.DRAW);
                mainView.getBetsLayout().add(new BetLayout(mainView, bet));
                Notification.show("Please enter stake to submit the bet");
            } else {
                Notification.show("Too late, you can not make a bet after match start! \n" +
                        "Odd prospect list has been updated.");
                mainView.updateBetProspectsLayout();
            }
        });

        Button awayTeamOdd = new Button(prospect.getH2h().get(2).toString());
        awayTeamOdd.addClickListener(event2 -> {
            if (seconds.between(prospect.getCommence_time(), ZonedDateTime.now()) < 0) {
                BetDto bet = new BetDto(mainView.getUser(), prospect, Winner.AWAY_TEAM);
                mainView.getBetsLayout().add(new BetLayout(mainView, bet));
                Notification.show("Please enter stake to submit the bet");
            } else {
                Notification.show("Too late, you can not make a bet after match start! \n" +
                        "Odd prospect list has been updated.");
                mainView.updateBetProspectsLayout();
            }
        });

        TextField awayTeam = new TextField();
        awayTeam.setValue(prospect.getTeams().get(1));
        awayTeam.setWidth("13em");
        awayTeam.setReadOnly(true);

        setDefaultVerticalComponentAlignment(Alignment.CENTER);

        add(startTime);
        add(homeTeam);
        add(homeTeamOdd);
        add(drawOdd);
        add(awayTeamOdd);
        add(awayTeam);
    }
}
