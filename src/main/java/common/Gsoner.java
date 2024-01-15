package common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @description 将gson对象提至工具类中，用于把对象序列化和反序列化
 */
public class Gsoner {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

}
