package com.radoslawzerek.bigbetfrontend.pojo;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@UIScope
@SpringComponent
@Getter
public class LeaguesMap {
    private final Map<String, String> leagues;

    public LeaguesMap() {
        leagues = new HashMap<>();
        leagues.put("Serie A - Italy", "soccer_italy_serie_a");
        leagues.put("La Liga - Spain", "soccer_spain_la_liga");
        leagues.put("English Premier League", "soccer_epl");
    }
}
