package de.uwe;


import javax.enterprise.inject.spi.CDI;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        System.out.println("MainApp.start");
        CDI.current().getBeanManager().getEvent().fire(primaryStage);

    }

    @Override
    public void stop() throws Exception {

        System.out.println("MainApp.stop");
        super.stop();

    }
    
}
