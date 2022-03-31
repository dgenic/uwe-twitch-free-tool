package de.uwe;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import javafx.application.Application;

@QuarkusMain
public class App implements QuarkusApplication {

    
    @Override
    public int run(String... args) throws Exception {
        System.out.println("App.run");

        Application.launch(MainApp.class, args);
        return 0;
    }
    

    
}
