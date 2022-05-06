module ZIPandSEND {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens SendAndReceive to javafx.fxml;
    exports SendAndReceive;
    exports MyUtils;
    opens MyUtils to javafx.fxml;
    exports ZipAndUnzip;
    opens ZipAndUnzip to javafx.fxml;
}