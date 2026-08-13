module ch.fhnw.algdemo.test {
    requires ch.fhnw.algdemo;
    requires org.junit.jupiter.api;
    requires org.junit.platform.commons;

    opens ch.fhnw.algdemo.test.util to org.junit.platform.commons;
}