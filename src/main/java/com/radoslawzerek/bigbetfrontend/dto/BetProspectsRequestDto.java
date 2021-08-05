package com.radoslawzerek.bigbetfrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BetProspectsRequestDto {
    private Long id;
    private Long userId;
    private List<String> leagues;
    private LocalDateTime created;

    public BetProspectsRequestDto(Long userId, List<String> leagues) {
        this.userId = userId;
        this.leagues = leagues;
        this.created = LocalDateTime.now();
    }
}
