package top.totalo.apollo.util;

import com.google.gson.Gson;

/**
 * gson tools.
 */
public class GsonUtil {

    private static final Gson GSON = new Gson();

    public static Gson getInstance() {
        return GSON;
    }
}
