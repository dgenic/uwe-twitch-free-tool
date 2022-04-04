package de.uwe.views;

import com.github.twitch4j.helix.domain.Follow;
import com.github.twitch4j.helix.domain.User;
import de.uwe.Context;
import de.uwe.FetchEvent;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

@ApplicationScoped
public class FollowListView {


    private final Label totalLabel = new Label();
    private final TextField textField = new TextField();
    private final Button fetchButton = new Button("fetch follow");
    private final Button abortButton = new Button("abort");
    private final HBox topLayout = new HBox(totalLabel, textField, fetchButton, abortButton);

    private final ProgressBar progressBar = new ProgressBar();

    private final TextField nameFilterTextField = new TextField();
    private final Button nameFilterClearButton = new Button("clear");
    private final HBox filterLayout = new HBox(nameFilterTextField, nameFilterClearButton);

    private final ObservableList<Follow> observableList = FXCollections.observableArrayList();
    private final FilteredList<Follow> filteredList = new FilteredList<>(observableList);
    private final ListView<Follow> listView = new ListView<>(filteredList);

    private final VBox layout = new VBox(topLayout, progressBar, filterLayout, listView);

    private boolean abort = false;

    @Inject
    public Context context;

    @PostConstruct
    public void construct(){

        progressBar.setPrefWidth(Double.MAX_VALUE);

        topLayout.setSpacing(10);
        HBox.setHgrow(textField, Priority.ALWAYS);

        filterLayout.setSpacing(10);
        HBox.setHgrow(nameFilterTextField, Priority.ALWAYS);

        layout.setSpacing(10);
        layout.setPadding(new Insets(10));
        VBox.setVgrow(listView, Priority.ALWAYS);

        listView.setCellFactory(view -> new FollowListCell());
        listView.getStyleClass().add("uwe-list-view");

        totalLabel.textProperty().bind(Bindings.size(listView.getItems()).asString());

        fetchButton.setOnMouseClicked(e -> {

            progressBar.setProgress(0);
            observableList.clear();
            final FetchEvent<Follow> fetchEvent = (sublist, value, total) -> {
                System.out.println("FetchEvent Follow "+value+"/"+total+" "+(value/total));
                Platform.runLater(() -> {
                    progressBar.setProgress(value/total);
                    observableList.addAll(sublist);
                });
                return abort;
            };

            new Thread(() -> {
                final String login = textField.getText();
                if(login.length() < 3) {
                    System.out.println("login too short: "+login);
                    return;
                }
                final User user = context.userByName(login);
                if(user == null) {
                    System.out.println("user not available: "+login);
                    return;
                }
                System.out.println("fetched user: "+user.getLogin());
                context.followsToId(fetchEvent, user.getId(), 0);
                Platform.runLater(() -> {
                    abort = false;
                });
            }).start();

        });

        abortButton.setOnMouseClicked(e -> {
            abort = true;
        });

        nameFilterTextField.setOnKeyTyped(e -> {
            filteredList.setPredicate(follow -> follow.getFromLogin().contains(nameFilterTextField.getText()));
        });

        nameFilterClearButton.setOnMouseClicked(e -> {
            nameFilterTextField.clear();
            filteredList.setPredicate(follow -> true);
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
