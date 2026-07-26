package com.cosmicodyssey.rpg.data;

import androidx.room.TypeConverter;
import java.util.Arrays;
import java.util.List;

public class StringListConverter {
    @TypeConverter
    public static String fromList(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return String.join("|||", list);
    }

    @TypeConverter
    public static List<String> toList(String data) {
        if (data == null || data.isEmpty()) return new java.util.ArrayList<>();
        return new java.util.ArrayList<>(Arrays.asList(data.split("\\|\\|\\|")));
    }
}
