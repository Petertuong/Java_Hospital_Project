package ui.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.util.function.Function;

public final class TableUtil {

    private TableUtil() {}

    public static <T> void setStringColumn(TableColumn<T, String> col, Function<T, String> getter) {
        col.setCellValueFactory(cd -> new SimpleStringProperty(getter.apply(cd.getValue())));
    }

    public static <T> void setNullableStringColumn(TableColumn<T, String> col, Function<T, String> getter) {
        col.setCellValueFactory(cd -> {
            String v = getter.apply(cd.getValue());
            return new SimpleStringProperty(v == null ? "" : v);
        });
    }
}
