package de.uwe;

import de.uwe.views.FollowListView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

        stage.setScene(scene);
        stage.setTitle("Uwe-Twitch-Free-Tool");
        stage.getIcons().add(appImage);
        stage.centerOnScreen();
        stage.show();

        context.init();


    }

}
