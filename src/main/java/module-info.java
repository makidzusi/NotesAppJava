module com.stanislav.todoappjava {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;
    requires javafx.web;
    requires com.google.gson;
    requires jdk.jsobject;
    opens com.stanislav.todoappjava.controllers to javafx.fxml;
    opens com.stanislav.todoappjava.utils to javafx.web;
    opens com.stanislav.todoappjava to javafx.fxml;
    exports com.stanislav.todoappjava;
}