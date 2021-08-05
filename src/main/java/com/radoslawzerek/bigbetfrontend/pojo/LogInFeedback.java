package com.radoslawzerek.bigbetfrontend.pojo;

import com.radoslawzerek.bigbetfrontend.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LogInFeedback {
    private UserDto user;
    private String message;
}
