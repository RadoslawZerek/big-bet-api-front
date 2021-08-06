package com.radoslawzerek.bigbetfrontend.facade;

import com.radoslawzerek.bigbetfrontend.config.BigBetClient;
import com.radoslawzerek.bigbetfrontend.dto.BetDto;
import com.radoslawzerek.bigbetfrontend.dto.BetProspectDto;
import com.radoslawzerek.bigbetfrontend.dto.BetProspectsRequestDto;
import com.radoslawzerek.bigbetfrontend.dto.UserDto;
import com.radoslawzerek.bigbetfrontend.pojo.LogInFeedback;
import com.radoslawzerek.bigbetfrontend.pojo.SignUpFeedback;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UIScope
@SpringComponent
public class BigBetFacade {

    private final BigBetClient bigBetClient;

    @Autowired
    public BigBetFacade(BigBetClient bigBetClient) {
        this.bigBetClient = bigBetClient;
    }

    public SignUpFeedback signUserUp(UserDto user) {
        return bigBetClient.signUserUp(user);
    }

    public LogInFeedback logUserIn(String login, String password) {
        return bigBetClient.logUserIn(login, password);
    }

    public void updateUser(UserDto user) {
        bigBetClient.updateUser(user);
    }

    public void updateUserPassword(Long userId, String newPassword) {
        bigBetClient.updateUserPassword(userId, newPassword);
    }

    public UserDto getUserById(Long userId) {
        return bigBetClient.getUserById(userId);
    }

    public List<UserDto> getAllUsers() {
        return bigBetClient.getAllUsers();
    }

    public Boolean checkIfUserExists(String login) {
        return bigBetClient.checkIfUserExists(login);
    }

    public List<BetProspectDto> getBetProspects(BetProspectsRequestDto prospectsRequest) {
        return bigBetClient.getCurrentBetProspects(prospectsRequest);
    }

    public void addBet(BetDto bet) {
        bigBetClient.addBet(bet);
    }

    public List<BetDto> getBetsOfUser(Long userId, Boolean onlyPending) {
        return bigBetClient.getBetsOfUser(userId, onlyPending);
    }

    public void deleteBet(Long betId) {
        bigBetClient.deleteBet(betId);
    }
}
