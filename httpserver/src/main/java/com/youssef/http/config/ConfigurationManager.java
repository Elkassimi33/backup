package com.youssef.http.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.youssef.http.exception.HttpConfigurationException;
import com.youssef.http.util.Json;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {

    private static ConfigurationManager myConfigurationManager;
    public static Configuration myConfiguration;

    private ConfigurationManager(){}

    public static ConfigurationManager getInstance(){
        if(myConfigurationManager==null)
            myConfigurationManager=new ConfigurationManager();
        return myConfigurationManager;
    }

    /**
     * Used to load a configuration file by the path provided
     */
    public void loadConfigurationFile(String filePath)  {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException(e);
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i;
        try {


            while ((i = fileReader.read()) != -1) {
                stringBuffer.append((char) i);
            }
        }catch (IOException e) {
            throw new HttpConfigurationException(e);
        }

        JsonNode conf= null;
        try {
            conf = Json.parse(stringBuffer.toString());
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing configuration file",e);
        }
        try {
            myConfiguration=Json.formJson(conf,Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing configuration file,internal",e);
        }
    }

    /**
     * Return the current loaded configuration
     */

    public Configuration getCurrentConfiguration(){
        if(myConfiguration==null)
            throw new HttpConfigurationException("No current configuration set. ");
        return myConfiguration;
    }

}
