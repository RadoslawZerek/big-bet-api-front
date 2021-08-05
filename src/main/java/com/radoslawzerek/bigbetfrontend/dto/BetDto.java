package com.radoslawzerek.bigbetfrontend.dto;

import com.radoslawzerek.bigbetfrontend.enums.Winner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BetDto {
    private Long id;
    private UserDto user;
    private BetProspectDto betProspect;
    private LocalDateTime created;
    private Winner tippedWinner;
    private BigDecimal odd;
    private BigDecimal stake;
    private boolean finalized;
    private Winner winner;
    private boolean won;
    private BigDecimal cashWin;

    public BetDto(UserDto user, BetProspectDto betProspect, Winner tippedWinner) {
        this.user = user;
        this.betProspect = betProspect;
        this.tippedWinner = tippedWinner;
        if (Winner.HOME_TEAM.equals(tippedWinner)) {
            this.odd = betProspect.getH2h().get(0).setScale(2, RoundingMode.HALF_UP);
        } else if (Winner.DRAW.equals(tippedWinner)) {
            this.odd = betProspect.getH2h().get(1).setScale(2, RoundingMode.HALF_UP);
        } else {
            this.odd = betProspect.getH2h().get(2).setScale(2, RoundingMode.HALF_UP);
        }
    }
}
