package org.thane.utils;

import com.google.gson.*;
import net.minecraft.server.v1_13_R2.*;
import org.thane.ThaneGuns;

import java.util.Iterator;
import java.util.Map;

public class NBTUtil {

    public static NBTBase objectToNBT(Object object) {
        JsonElement element = new Gson().toJsonTree(object);
        return jsonElementToNBT(element);
    }

    public static <T> T nbtToObject(NBTBase nbt, Class<T> type) {
        return new Gson().fromJson(nbtToJsonElement(nbt), type);
    }

    public static JsonElement nbtToJsonElement(NBTBase nbt) {
        switch (nbt.getTypeId()) {
            case NBTType.END:
                return null;
            case NBTType.BYTE:
                return new JsonPrimitive(((NBTTagByte) nbt).g() == 1);
            case NBTType.SHORT:
                return new JsonPrimitive(((NBTTagShort) nbt).f());
            case NBTType.INT:
                return new JsonPrimitive(((NBTTagInt) nbt).e());
            case NBTType.LONG:
                return new JsonPrimitive(((NBTTagLong) nbt).d());
            case NBTType.FLOAT:
                return new JsonPrimitive(((NBTTagFloat) nbt).i());
            case NBTType.DOUBLE:
                return new JsonPrimitive(((NBTTagDouble) nbt).asDouble());
            case NBTType.BYTE_ARRAY:
                JsonArray byteArray = new JsonArray();
                for (byte bite : ((NBTTagByteArray) nbt).c()) {
                    byteArray.add(bite == 1);
                }
                return byteArray;
            case NBTType.STRING:
                return new JsonPrimitive(((NBTTagString) nbt).b_());
            case NBTType.LIST:
                JsonArray listArray = new JsonArray();
                for (NBTBase nbtBase : ((NBTTagList) nbt)) {
                    listArray.add(nbtToJsonElement(nbtBase));
                }
                return listArray;
            case NBTType.COMPOUND:
                JsonObject jsonObject = new JsonObject();
                for (String string : ((NBTTagCompound) nbt).getKeys()) {
                    jsonObject.add(string, nbtToJsonElement(((NBTTagCompound) nbt).get(string)));
                }
                return jsonObject;
            case NBTType.INT_ARRAY:
                JsonArray intArray = new JsonArray();
                for (int integer : ((NBTTagIntArray) nbt).d()) {
                    intArray.add(integer);
                }
                return intArray;
            case NBTType.LONG_ARRAY:
                JsonArray longArray = new JsonArray();
                for (long longNumber : ((NBTTagLongArray) nbt).d()) {
                    longArray.add(longNumber);
                }
                return longArray;
        }
        return null;
    }

    public static NBTBase jsonElementToNBT(JsonElement element) {
        if (element.isJsonObject()) {
            NBTTagCompound compound = new NBTTagCompound();
            for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
                compound.set(entry.getKey(), jsonElementToNBT(entry.getValue()));
            }
            return compound;

        } else if (element.isJsonArray()) {

            JsonArray array = (JsonArray) element;
            Iterator<JsonElement> iterator = array.iterator();
            NBTTagList list = new NBTTagList();
            while (iterator.hasNext()) {
                JsonElement json = iterator.next();
                list.add(jsonElementToNBT(json));
            }
            return list;

        } else if (element.isJsonNull()) {
            return new NBTTagEnd();
        } else if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = (JsonPrimitive) element;

            if (primitive.isBoolean()) {
                return new NBTTagByte((byte) (primitive.getAsBoolean() ? 1 : 0));
            } else if (primitive.isString()) {
                return new NBTTagString(primitive.getAsString());
            } else if (primitive.isNumber()) {
                Number number = primitive.getAsNumber();

                if (number instanceof Integer) {
                    return new NBTTagInt(number.intValue());
                } else if (number instanceof Long) {
                    return new NBTTagLong(number.longValue());
                } else if (number instanceof Byte) {
                    return new NBTTagByte(number.byteValue());
                } else if (number instanceof Short) {
                    return new NBTTagShort(number.shortValue());
                } else if (number instanceof Double) {
                    return new NBTTagDouble(number.doubleValue());
                } else if (number instanceof Float) {
                    return new NBTTagFloat(number.floatValue());
                }
            }
        }
        return new NBTTagEnd();
    }

    private static class NBTType {

        static final byte END = 0;
        static final byte BYTE = 1;
        static final byte SHORT = 2;
        static final byte INT = 3;
        static final byte LONG = 4;
        static final byte FLOAT = 5;
        static final byte DOUBLE = 6;
        static final byte BYTE_ARRAY = 7;
        static final byte STRING = 8;
        static final byte LIST = 9;
        static final byte COMPOUND = 10;
        static final byte INT_ARRAY = 11;
        static final byte LONG_ARRAY = 12;
    }
}
