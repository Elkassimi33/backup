package com.youssef.http.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class Json {
    private static ObjectMapper myObjectMapper=defaultObjectMapper();

    private static ObjectMapper defaultObjectMapper() {
        ObjectMapper om =new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    public static JsonNode parse(String jsonString) throws JsonProcessingException {
        return  myObjectMapper.readTree(jsonString);
    }

    public static <A> A formJson(JsonNode node, Class<A> aClass) throws JsonProcessingException {
        return  myObjectMapper.treeToValue(node, aClass);
    }

    public static JsonNode toJson(Object object) throws JsonProcessingException {
        return  myObjectMapper.valueToTree(object);
    }

    public String stringify(JsonNode jsonNode) throws JsonProcessingException {
        return generateJson(jsonNode,false);
    }

    public String stringifyPretty(JsonNode jsonNode) throws JsonProcessingException {
        return generateJson(jsonNode,true);
    }

    private static String generateJson(Object object,boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter=myObjectMapper.writer();
        if (pretty) {
            objectWriter=objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        return objectWriter.writeValueAsString(object);
    }
}
