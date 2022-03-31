package de.uwe.views;

import com.github.twitch4j.helix.domain.BitsLeaderboard;
import de.uwe.Context;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class BitsLeaderboardView {

    private final Button fetchButton = new Button("fetch");

    private final ObservableList<String> observableList = FXCollections.observableArrayList();
    private final ListView<String> listView = new ListView<>(observableList);

    private final VBox layout = new VBox(fetchButton, listView);

    @Inject
    public Context context;

    @PostConstruct
    public void construct(){

        layout.setSpacing(10);
        layout.setPadding(new Insets(10));
        VBox.setVgrow(listView, Priority.ALWAYS);

        fetchButton.setOnMouseClicked(e -> {

            final BitsLeaderboard bitsLeaderboard = context.getBitsLeaderboard();
            final List<String> result = bitsLeaderboard.getEntries().stream()
                    .map(entry -> entry.getUserLogin()+" "+entry.getRank()+" "+ entry.getScore()).toList();
            observableList.setAll(result);

        });

    }

    public Node getNode(){
        return layout;
    }
}
