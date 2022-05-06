module com.lbw.gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires ZIPandSEND;

    opens Gui to javafx.fxml;
    exports Gui;
}