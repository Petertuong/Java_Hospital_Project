package ui; // giữ nguyên package của em

import com.google.gson.Gson;

public class GsonTest {
    public static void main(String[] args) {
        Gson gson = new Gson();
        String json = gson.toJson("hello");
        System.out.println(json);
    }
}
