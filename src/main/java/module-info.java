module ch.fhnw.algdemo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires javafx.graphics;
    requires static lombok;
    requires java.desktop;
    requires javafx.base;

    opens ch.fhnw.algdemo.control to javafx.fxml;
    exports ch.fhnw.algdemo;
    opens ch.fhnw.algdemo.control.algorithm.binarysearch to javafx.fxml;
    opens ch.fhnw.algdemo.control.algorithm.mergesort to javafx.fxml;
    opens ch.fhnw.algdemo.model.algorithm to javafx.fxml;
    opens ch.fhnw.algdemo.control.variable to javafx.fxml;
    opens ch.fhnw.algdemo.control.history to javafx.fxml;
    opens ch.fhnw.algdemo.model.command to javafx.fxml;
}