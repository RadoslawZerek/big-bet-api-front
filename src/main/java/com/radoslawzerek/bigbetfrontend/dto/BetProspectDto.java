package com.radoslawzerek.bigbetfrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BetProspectDto {
    private Long id;
    private Integer sport_key;
    private List<String> teams;
    private ZonedDateTime commence_time;
    private List<BigDecimal> h2h;
}
