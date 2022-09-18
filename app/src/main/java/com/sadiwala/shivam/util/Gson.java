package com.sadiwala.shivam.util;

import androidx.work.Data;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Gson {
    private static String TAG = "Gson";
    private static com.google.gson.Gson instance =
            new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    // Reference - https://futurestud.io/tutorials/gson-builder-special-values-of-floats-doubles
                    .serializeSpecialFloatingPointValues()
                    .registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
                        @Override
                        public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
                            SimpleDateFormat format = AaryaDateFormats.getSimpleDateFormatyyyyMMddTHHmmssSSSZ();
                            format.setTimeZone(TimeZone.getTimeZone("GMT+0000"));
                            return new JsonPrimitive(format.format(date));
                        }
                    })
                    .disableHtmlEscaping()
                    .create();

    private Gson() {
    }

    public static com.google.gson.Gson getInstance() {
        return instance;
    }

    /**
     * Custom deserializer for fixing a bug on server where for some input fields it saves data as
     * array and for some as single object. See http://www.javacreed.com/gson-deserialiser-example/
     */
    private static class CodeNameTypeAdapter implements JsonDeserializer<List<Data>> {
        public List<Data> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
            List<Data> vals = new ArrayList<Data>();
            if (json.isJsonArray()) {
                //even though its an error we will be only using first item as data is only 1. This
                //implementation is because there is corrupted data for some client where data has only
                //one item but it comes as array.
                for (JsonElement e : json.getAsJsonArray()) {
                    vals.add((Data) ctx.deserialize(e, Data.class));
                }
            } else if (json.isJsonObject()) {
                vals.add((Data) ctx.deserialize(json, Data.class));
            } else {
                throw new JsonSyntaxException("Unexpected JSON type: " + json.getClass() + ",  Data : " + json.toString());
            }
            return vals;
        }
    }


}