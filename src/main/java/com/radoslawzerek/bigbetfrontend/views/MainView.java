package com.radoslawzerek.bigbetfrontend.views;

import com.radoslawzerek.bigbetfrontend.dto.BetProspectDto;
import com.radoslawzerek.bigbetfrontend.dto.BetProspectsRequestDto;
import com.radoslawzerek.bigbetfrontend.dto.UserDto;
import com.radoslawzerek.bigbetfrontend.enums.Role;
import com.radoslawzerek.bigbetfrontend.facade.BigBetFacade;
import com.radoslawzerek.bigbetfrontend.pojo.LeaguesMap;
import com.radoslawzerek.bigbetfrontend.views.mainviewlayouts.BetProspectDescription;
import com.radoslawzerek.bigbetfrontend.views.mainviewlayouts.BetProspectLayout;
import com.radoslawzerek.bigbetfrontend.views.mainviewlayouts.UserLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@UIScope
@PreserveOnRefresh
@SpringComponent
@Route
@Theme(value = Lumo.class)
public class MainView extends VerticalLayout implements BeforeEnterObserver, RouterLayout {

    private UserDto user = null;
    private Button adminButton = new Button("Go to Admin page");
    private CheckboxGroup<String> selectLeague = new CheckboxGroup<>();
    private HorizontalLayout userBar = new HorizontalLayout();
    private HorizontalLayout betProspectsBar = new HorizontalLayout();
    private HorizontalLayout mainBetsLayout = new HorizontalLayout();
    private VerticalLayout betProspectsLayout = new VerticalLayout();
    private VerticalLayout betsLayout = new VerticalLayout();
    private BetProspectDescription description = new BetProspectDescription();
    private BigBetFacade bigBetFacade;
    private LeaguesMap leaguesMap;

    @Autowired
    public MainView(BigBetFacade bigBetFacade, LeaguesMap leaguesMap) {
        this.bigBetFacade = bigBetFacade;
        this.leaguesMap = leaguesMap;

        Button getBetProspects = new Button("Get available bet prospects");
        getBetProspects.addClickListener(event -> updateBetProspectsLayout());

        Button logOut = new Button("Log Out");
        logOut.addClickListener(event -> {
            cleanUp();
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        });

        setHorizontalComponentAlignment(Alignment.END, logOut);

        adminButton.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("admin")));

        setHorizontalComponentAlignment(Alignment.END, adminButton);

        Button accountSettingsButton = new Button("Account settings");
        accountSettingsButton.addClickListener(event6 -> getUI().ifPresent(ui -> ui.navigate("user")));

        setHorizontalComponentAlignment(Alignment.END, accountSettingsButton);

        selectLeague.setItems(new ArrayList<>(leaguesMap.getLeagues().keySet()));

        userBar.setDefaultVerticalComponentAlignment(Alignment.END);


        betProspectsBar.setWidthFull();
        betProspectsBar.add(getBetProspects, selectLeague);
        description.setVisible(false);
        mainBetsLayout.add(betProspectsLayout, betsLayout);

        add(logOut, adminButton, accountSettingsButton, userBar, betProspectsBar, description, mainBetsLayout);
        getStyle().set("padding-left", "5em");
        getStyle().set("padding-right", "5em");
    }

    public void updateBetProspectsLayout() {
        betProspectsLayout.removeAll();
        if (selectLeague.getSelectedItems().size() > 0) {
            description.setVisible(true);
            BetProspectsRequestDto prospectsRequest = new BetProspectsRequestDto(user.getId(),
                    new ArrayList<>(selectLeague.getSelectedItems()));
            List<BetProspectDto> list = bigBetFacade.getBetProspects(prospectsRequest);
            for (BetProspectDto prospect : list) {
                betProspectsLayout.add(new BetProspectLayout(this, prospect));
            }
        } else {
            Notification.show("Select at least 1 league to see it's bet prospects");
            description.setVisible(false);
        }
    }

    protected void cleanUp() {
        user = null;
        userBar.removeAll();
        betProspectsLayout.removeAll();
        betsLayout.removeAll();
        selectLeague.deselectAll();
        description.setVisible(false);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (user == null) {
            event.forwardTo(LoginView.class);
        } else {
            user = bigBetFacade.getUserById(user.getId());
            updateUserBar();
            adminButton.setVisible(Role.ADMIN.equals(user.getRole()));
        }
    }

    public void updateUserBar() {
        userBar.removeAll();
        userBar.add(new UserLayout(this));
    }
}
