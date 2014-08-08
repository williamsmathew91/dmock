package dmock.util;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named
public class KeyConcatUtil {
    public static final String DELIMITER = ":";

    public String concat(String path, String method) {
        return path.concat(DELIMITER).concat(method);
    }
}
