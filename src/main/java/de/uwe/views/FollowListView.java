package de.uwe.views;

import com.github.twitch4j.helix.domain.Follow;
import com.github.twitch4j.helix.domain.FollowList;
import de.uwe.Context;
import de.uwe.FetchEvent;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class FollowListView {

    private final Button fetchButton = new Button("fetch follow");

    private final ObservableList<Follow> observableList = FXCollections.observableArrayList();
    private final ListView<Follow> listView = new ListView<>(observableList);

    private final VBox layout = new VBox(fetchButton, listView);

    @Inject
    public Context context;

    @PostConstruct
    public void construct(){

        layout.setSpacing(10);
        layout.setPadding(new Insets(10));
        VBox.setVgrow(listView, Priority.ALWAYS);

        listView.setCellFactory(view -> new FollowListCell());

        fetchButton.setOnMouseClicked(e -> {

            final FetchEvent<Follow> fetchEvent = (sublist, value, total) -> {
                System.out.println("FetchEvent Follow "+value+"/"+total+" "+(value/total));
                return false;
            };

            new Thread(() -> {
                final List<Follow> follows = context.followsToId(fetchEvent);
                Platform.runLater(() -> {
                    observableList.setAll(follows);
                });
            }).start();

        });

    }

    public Node getNode(){
        return layout;
    }

    private static class FollowListCell extends ListCell<Follow> {

        @Override
        protected void updateItem(Follow item, boolean empty) {
            super.updateItem(item, empty);
            if(empty){
                setText(null);
                setGraphic(null);
            }else{
                setText(item.getFromLogin());
                setGraphic(null);
            }
        }
    }


}
