package com.radoslawzerek.bigbetfrontend.views.mainviewlayouts;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class BetProspectDescription extends HorizontalLayout {

    public BetProspectDescription() {

        Label empty1 = new Label();
        empty1.setWidth("2em");
        Label startTimeLabel = new Label("Match start");
        startTimeLabel.getStyle().set("font-weight", "bold");
        Label empty2 = new Label();
        empty2.setWidth("5.5em");
        Label odds = new Label("Odds: ");
        Label empty3 = new Label();
        empty3.setWidth("1.5em");
        Label homeTeam = new Label("HOME TEAM ");
        homeTeam.getStyle().set("font-weight", "bold");
        Label empty4 = new Label();
        empty4.setWidth("0.25em");
        Label draw = new Label(" DRAW ");
        draw.getStyle().set("font-weight", "bold");
        Label empty5 = new Label();
        empty5.setWidth("0.25em");
        Label awayTeam = new Label(" AWAY TEAM");
        awayTeam.getStyle().set("font-weight", "bold");

        add(empty1, startTimeLabel, empty2, odds, empty3, homeTeam, empty4, draw, empty5, awayTeam);

        getStyle().set("border-top-style", "solid");
        getStyle().set("border-width", "1px");
        getStyle().set("padding-top", "20px");
        setWidthFull();
    }
}
