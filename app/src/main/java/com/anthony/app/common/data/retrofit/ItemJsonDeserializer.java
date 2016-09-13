package com.anthony.app.common.data.retrofit;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *Created by Anthony on 2016/7/8.
 * Class Note:
 * 1 parse SerializeName in annotation ,field match
 * 2 String field fault-tolerant ,all of the String field will be parse
 *
 */
public class ItemJsonDeserializer<T> implements JsonDeserializer {
    @Override
    public T deserialize(JsonElement json, Type typeOfT,
                         JsonDeserializationContext context) throws JsonParseException {
        Object obj = null;
        try {
            final JsonObject jsonObj = json.getAsJsonObject();

            Class clazz = (Class) typeOfT;
            Field[] fields = clazz.getDeclaredFields();
            obj = clazz.newInstance();

            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                f.setAccessible(true);
                String name = f.getName();
                JsonElement element = jsonObj.get(name);

                if (element == null) {
                    List<String> list = getSerializedName(f);
                    if (list != null && list.size() > 0) {
                        list.add(name);
                        for (String s : list) {
                            if (jsonObj.get(s) != null) {
                                element = jsonObj.get(s);
                                break;
                            }
                        }
                    }
                }

                parseJsonElement(context, element, f, obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (T) obj;
    }

    private void parseJsonElement(JsonDeserializationContext context, JsonElement element, Field f, Object obj) {
        if (element == null)
            return;

        try {
            String type = f.getType().getName();
            if (type.endsWith("String")) {
                f.set(obj, parseAsString(element));
            } else if (type.endsWith("int") || type.endsWith("Integer")) {
                f.set(obj, element.getAsInt());
            } else if (type.endsWith("List")) {
                Type fc = f.getGenericType();
                f.set(obj, context.deserialize(element, fc));
            } else {
                f.set(obj, context.deserialize(element, Class.forName(type)));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<String> getSerializedName(Field field) {
        boolean isPresent = field.isAnnotationPresent(SerializedName.class);
        if (isPresent) {
            List<String> list = new ArrayList<>();
            SerializedName serializedName = field.getAnnotation(SerializedName.class);
            String s1 = serializedName.value();
            String[] s2 = serializedName.alternate();
            list.add(s1);
            for (int i = 0; i < s2.length; i++) {
                list.add(s2[i]);
            }
            return list;
        }

        return null;
    }

    private String parseAsString(JsonElement element) {
        if(element.isJsonArray() || element.isJsonObject())
            return element.toString();
        else if (element.isJsonNull())
            return "";
        else if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            return primitive.getAsString();
        }

        return "";
    }
}
