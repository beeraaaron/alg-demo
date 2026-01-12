module ch.fhnw.algdemo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires javafx.graphics;

    opens ch.fhnw.algdemo.control to javafx.fxml;
    exports ch.fhnw.algdemo;
}