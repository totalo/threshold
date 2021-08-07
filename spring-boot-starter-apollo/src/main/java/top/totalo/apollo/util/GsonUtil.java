package top.totalo.apollo.util;

import com.google.gson.Gson;

/**
 * gson 工具类
 */
public class GsonUtil {

    private static final Gson GSON = new Gson();

    public static Gson getInstance() {
        return GSON;
    }
}
