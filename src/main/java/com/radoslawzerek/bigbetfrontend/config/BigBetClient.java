package com.radoslawzerek.bigbetfrontend.config;

import com.radoslawzerek.bigbetfrontend.dto.BetDto;
import com.radoslawzerek.bigbetfrontend.dto.BetDtoList;
import com.radoslawzerek.bigbetfrontend.dto.BetProspectDto;
import com.radoslawzerek.bigbetfrontend.dto.BetProspectDtoList;
import com.radoslawzerek.bigbetfrontend.dto.BetProspectsRequestDto;
import com.radoslawzerek.bigbetfrontend.dto.UserDto;
import com.radoslawzerek.bigbetfrontend.dto.UserDtoList;
import com.radoslawzerek.bigbetfrontend.pojo.LogInFeedback;
import com.radoslawzerek.bigbetfrontend.pojo.SignUpFeedback;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@UIScope
@SpringComponent
public class BigBetClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BigBetClient.class);

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private final BigBetApiConfig bigBetApiConfig;

   /* @Autowired
    public BigBetClient(RestTemplate restTemplate, BigBetApiConfig bigBetApiConfig) {
        this.restTemplate = restTemplate;
        this.bigBetApiConfig = bigBetApiConfig;
    }*/

    public List<BetProspectDto> getCurrentBetProspects(BetProspectsRequestDto prospectsRequest) {
        URI url = createUriForGetOddsProspects();

        try {
            BetProspectDtoList response = restTemplate.postForObject(url, prospectsRequest, BetProspectDtoList.class);
            return ofNullable(response.getList()).orElse(new ArrayList<>());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private URI createUriForGetOddsProspects() {
        return UriComponentsBuilder.fromHttpUrl(bigBetApiConfig.getBetsApiEndpoint() +
                "/betprospects")
                .build().encode().toUri();
    }

    public SignUpFeedback signUserUp(UserDto user) {
        URI url = createUriForSignUserUp();

        try {
            SignUpFeedback signUpFeedback = restTemplate.postForObject(url, user, SignUpFeedback.class);
            return ofNullable(signUpFeedback).orElse(new SignUpFeedback(null, "Server communication problem (null response)"));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new SignUpFeedback(null, "Server communication problem");
        }
    }

    public LogInFeedback logUserIn(String login, String password) {
        URI url = createUriForLogUserIn(login, password);

        try {
            LogInFeedback logInFeedback = restTemplate.getForObject(url, LogInFeedback.class);
            return ofNullable(logInFeedback).orElse(new LogInFeedback(null, "Server communication problem (null response)"));
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new LogInFeedback(null, "Server communication problem");
        }
    }

    private URI createUriForLogUserIn(String login, String password) {
        return UriComponentsBuilder.fromHttpUrl(bigBetApiConfig.getBetsApiEndpoint() +
                "/users/" + login + "/" + password)
                .build().encode().toUri();
    }

    public void updateUser(UserDto user) {
        URI url = createUriForSignUserUp();
        restTemplate.put(url, user);
    }

    private URI createUriForSignUserUp() {
        return UriComponentsBuilder.fromHttpUrl(bigBetApiConfig.getBetsApiEndpoint() +
                "/users")
                .build().encode().toUri();
    }

    public void updateUserPassword(Long userId, String newPassword) {
        URI url = createUriForUpdateUserPassword(userId);
        restTemplate.put(url, newPassword);
    }

    private URI createUriForUpdateUserPassword(Long userId) {
        return UriComponentsBuilder.fromHttpUrl(bigBetApiConfig.getBetsApiEndpoint() +
                "/users/" + userId)
                .build().encode().toUri();
    }

    public UserDto getUserById(Long userId) {
        URI url = createUriForGetUserById(userId);

        try {
            return restTemplate.getForObject(url, UserDto.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private URI createUriForGetUserById(Long userId) {
        return UriComponentsBuilder.fromHttpUrl(bigBetApiConfig.getBetsApiEndpoint() +
                "/users/" + userId)
                .build().encode().toUri();
    }

    public List<UserDto> getAllUsers() {
        URI url = createUriForGetAllUsers();

        try {
            UserDtoList response = restTemplate.getForObject(url, UserDtoList.class);
            return response.getUserList();
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private URI createUriForGetAllUsers() {
        return UriComponentsBuilder.fromHttpUrl(bigBetApiConfig.getBetsApiEndpoint() +
                "/users")
                .build().encode().toUri();
    }

    public Boolean checkIfUserExists(String login) {
        URI url = createUriForCheckIfUserExists(login);

        try {
            return restTemplate.getForObject(url, Boolean.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private URI createUriForCheckIfUserExists(String login) {
        return UriComponentsBuilder.fromHttpUrl(bigBetApiConfig.getBetsApiEndpoint() +
                "/users/check/" + login)
                .build().encode().toUri();
    }

    public void addBet(BetDto bet) {

        URI url = createUriForAddBet();

        try {
            restTemplate.postForObject(url, bet, Boolean.class);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private URI createUriForAddBet() {
        return UriComponentsBuilder.fromHttpUrl(bigBetApiConfig.getBetsApiEndpoint() +
                "/bets")
                .build().encode().toUri();
    }

    public List<BetDto> getAllBets() {
        URI url = createUriForGetAllBets();

        return getBetDtos(url);
    }

    private List<BetDto> getBetDtos(URI url) {
        try {
            BetDtoList betsResponse = restTemplate.getForObject(url, BetDtoList.class);
            return ofNullable(betsResponse.getList()).orElse(new ArrayList<>());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private URI createUriForGetAllBets() {
        return UriComponentsBuilder.fromHttpUrl(bigBetApiConfig.getBetsApiEndpoint() +
                "/bets")
                .build().encode().toUri();
    }

    public List<BetDto> getBetsOfUser(Long userId, Boolean pending) {
        URI url = createUriForGetBetsOfUser(userId, pending);
        return getBetDtos(url);
    }

    private URI createUriForGetBetsOfUser(Long userId, Boolean onlyPending) {
        return UriComponentsBuilder.fromHttpUrl(bigBetApiConfig.getBetsApiEndpoint() +
                "/bets/" + userId + "/" + onlyPending)
                .build().encode().toUri();
    }

    public void deleteBet(Long betId) {

        URI url = createUriForDeleteBet(betId);

        try {
            restTemplate.delete(url);
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private URI createUriForDeleteBet(Long betId) {
        return UriComponentsBuilder.fromHttpUrl(bigBetApiConfig.getBetsApiEndpoint() +
                "/bets/" + betId)
                .build().encode().toUri();
    }
}
