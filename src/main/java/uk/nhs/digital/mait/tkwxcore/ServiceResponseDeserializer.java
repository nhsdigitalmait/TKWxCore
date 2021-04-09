/*
 * Copyright 2019 Damian Murphy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.nhs.digital.mait.tkwxcore;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author Damian Murphy
 */
public class ServiceResponseDeserializer 
        implements JsonDeserializer<ServiceResponse>
{

    public ServiceResponseDeserializer() {}
    
    @Override
    public ServiceResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) 
            throws JsonParseException 
    {
        @SuppressWarnings("UnusedAssignment")
        ServiceResponse sr = null;
        JsonObject jso = json.getAsJsonObject();
        int code = jso.get("code").getAsInt();
        String text = jso.get("text").getAsString();
        sr = new ServiceResponse(code, text);
        if (jso.has("objectClass")) {
            String oc = jso.get("objectClass").getAsString();
            try {
                Class cn = Class.forName(oc);
                sr.setObject(context.deserialize(jso.get("object"), cn));
            }
            catch (JsonParseException | ClassNotFoundException e) {
                sr.setObject("Unknown or unparseable class " + oc + " : " + jso.get("object").toString());
            }
        }
        if (jso.has("array")) {
            JsonArray a = 
                    jso.getAsJsonArray("array");
            JsonArray t = jso.getAsJsonArray("objectClasses");
            Object[] obj = new Object[a.size()];
            for (int i = 0; i < a.size(); i++) {
                String oc = t.get(i).getAsString();                
                try {
                    Class cn = Class.forName(oc);
                    obj[i] = context.deserialize(a.get(i), cn);
                }
                catch (JsonParseException | ClassNotFoundException e) {
                    obj[i] = "Unknown or unparseable class " + oc + " : " + a.get(i).toString();
                }
            }
            sr.setArray(obj);
        }

        if (jso.has("metadata")) {
            JsonObject a = jso.getAsJsonObject("metadata");
            JsonObject c = jso.getAsJsonObject("metadataClasses");            
            HashMap<String,Object> md = new HashMap<>();
            Set<Entry<String,JsonElement>> mditems = a.entrySet();
            HashMap<String,String> mditemclasses = getMetaDataClasses(c);
            for (Entry<String,JsonElement> mditem : mditems) {
                String n = mditem.getKey();
                String t = mditemclasses.get(n);
                try {
                    Object o = context.deserialize(mditem.getValue(), Class.forName(t));
                    sr.addMetaDataItem(n, o);
                }
                catch (JsonParseException | ClassNotFoundException e) {
                    sr.addMetaDataItem(n, "Unknown or unparseable class " + t + " : " + mditem.getValue().toString());
                }
            }
        }
        return sr;
    }
    
    private HashMap<String,String> getMetaDataClasses(JsonObject c) {
        HashMap<String,String> classList = new HashMap<>();
        Set<Entry<String,JsonElement>> mdc = c.entrySet();
        for (Entry<String,JsonElement> cd : mdc) {
            String t = cd.getKey();
            String v = cd.getValue().getAsString();
            classList.put(t, v);
        }
        return classList;
    }
}
