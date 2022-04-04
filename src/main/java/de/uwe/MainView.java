package de.uwe;

import de.uwe.views.FollowListView;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Objects;

@ApplicationScoped
public class MainView {

    public static String darkStyle = Objects.requireNonNull(MainView.class.getClassLoader().getResource("css/modena_dark.css")).toExternalForm();
    public static String listViewCss = Objects.requireNonNull(MainView.class.getClassLoader().getResource("css/style.css")).toExternalForm();
    public static Image appImage = new Image(Objects.requireNonNull(MainView.class.getClassLoader().getResourceAsStream("images/kartoffel.png")));

    public static double WIDTH = 400;
	public static double HEIGHT = 600;

    @Inject
    public Context context;

    @Inject
    public FollowListView followListView;

    public void start(@Observes Stage stage) {

       // final HBox root = new HBox(bitsLeaderboardView.getNode());

        Scene scene = new Scene((Parent) followListView.getNode(), WIDTH, HEIGHT);

        scene.getStylesheets().addAll(darkStyle, listViewCss);
        scene.setFill(Color.rgb(60, 60, 60));

        stage.setScene(scene);
        stage.setTitle("Uwe-Twitch-Free-Tool");
        stage.getIcons().add(appImage);
        stage.centerOnScreen();
        stage.show();

        new Thread(() -> {
            context.init();
            Platform.runLater(() -> {
                System.out.println("context initialized!");
            });
        }).start();



    }

}
