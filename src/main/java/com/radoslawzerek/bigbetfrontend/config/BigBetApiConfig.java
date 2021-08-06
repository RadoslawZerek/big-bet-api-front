package com.radoslawzerek.bigbetfrontend.config;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@UIScope
@Getter
@SpringComponent
public class BigBetApiConfig {

    @Value("${bets.api.endpoint.prod}")
    private String betsApiEndpoint;

    private final String ELPKey = "soccer_epl";
    private final String LaLigaKey = "soccer_spain_la_liga";
    private final String SerieAKey = "soccer_italy_serie_a";

}
