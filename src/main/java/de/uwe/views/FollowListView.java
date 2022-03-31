package de.uwe.views;

import com.github.twitch4j.helix.domain.Follow;
import com.github.twitch4j.helix.domain.FollowList;
import de.uwe.Context;
import de.uwe.FetchEvent;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class FollowListView {


    private final Label totalLabel = new Label();
    private final Region spacer = new Region();
    private final Button fetchButton = new Button("fetch follow");

    private final HBox topLayout = new HBox(totalLabel, spacer, fetchButton);

    private final ObservableList<Follow> observableList = FXCollections.observableArrayList();
    private final ListView<Follow> listView = new ListView<>(observableList);

    private final VBox layout = new VBox(topLayout, listView);

    @Inject
    public Context context;

    @PostConstruct
    public void construct(){

        HBox.setHgrow(spacer, Priority.ALWAYS);
        layout.setSpacing(10);
        layout.setPadding(new Insets(10));
        VBox.setVgrow(listView, Priority.ALWAYS);

        listView.setCellFactory(view -> new FollowListCell());
        listView.getStyleClass().add("uwe-list-view");

        totalLabel.textProperty().bind(Bindings.size(listView.getItems()).asString());

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

        public static final SimpleDateFormat followAtInstantFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        private final Label followAtLabel = new Label();
        private final Label nameLabel = new Label();

        private final HBox layout = new HBox(followAtLabel, nameLabel);

        public FollowListCell() {

            layout.setSpacing(5);

            getStyleClass().add("uwe-list-cell");
            followAtLabel.setStyle("-fx-text-fill: -fx-color-yellow;");
            nameLabel.setStyle("-fx-text-fill: -fx-color-blue;");
        }

        @Override
        protected void updateItem(Follow item, boolean empty) {
            super.updateItem(item, empty);
            if(empty){
                setText(null);
                setGraphic(null);
            }else{

                followAtLabel.setText(followAtInstantFormat.format(Date.from(item.getFollowedAtInstant())));
                nameLabel.setText(item.getFromLogin());
                setGraphic(layout);
            }
        }
    }


}
