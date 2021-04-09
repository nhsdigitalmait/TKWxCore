/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.nhs.digital.mait.tkwxcore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 *
 * @author Damian Murphy
 */
public class ServiceResponse {
    
    private int code = -1;
    private String text = null;
    private Object object = null;
    private String objectClass = null;
    private String[] objectClasses = null;
    private Object[] array = null;
    private HashMap<String,Object> metadata = null;
    private HashMap<String,String> metadataClasses = null;
    private String error = null;
    private static Gson gson = null;
    
    /**
     * Create a new ServiceResponse with the given analogues of an HTTP response code and text
     * 
     * @param c Response code
     * @param t Response text
     */
    public ServiceResponse(int c, String t) {
        code = c;
        text = t;
    }
    
    public ServiceResponse() {}
    
    /**
     * Convenience method for remote TKW service invocations. Where a caller has set up a
     * remote request and sent any arguments to it, the response is expected to be a JSON
     * serialised ServiceResponse. So de-serialise a ServiceResponse from the given InputStream.
     * 
     * @param in
     * @return
     * @throws Exception
     */
    public static ServiceResponse getRemoteResponse(InputStream in) 
            throws Exception
    {
        if (gson == null) {
            GsonBuilder gb = new GsonBuilder();
            gb.registerTypeAdapter(ServiceResponse.class, new ServiceResponseDeserializer());
            gson = gb.create();
        }
        InputStreamReader isr = new InputStreamReader(in);
        ServiceResponse r = gson.fromJson(isr, ServiceResponse.class);
        return r;
    }
    
    
    public static ServiceResponse getRemoteResponse(String s) 
            throws Exception
    {
        if (gson == null) {
            gson = new Gson();
        }
        ServiceResponse r = gson.fromJson(s, ServiceResponse.class);
        return r;
    }
    
    public String getError() { return error; }
    public void setError(Throwable t) { error = t.toString(); }
    public void setError(String s) { error = s; }
    
    public int getCode() { return code; }
    public String getText() { return text; }
    
    public void setObject(Object o) { 
        object = o; 
        objectClass = o.getClass().getName();
    }
    public Object getObject() { return object; }
    public void setArray(Object[] a) { 
        array = a; 
        objectClasses = new String[a.length];
        int i = 0;
        for (Object o : a) {
            objectClasses[i++] = o.getClass().getName();
        }
    }
    public Object[] getArray() { return array; }
    
    public void addMetaDataItem(String s, Object o) {
        if (metadata == null) {
            metadata = new HashMap<>();
            metadataClasses = new HashMap<>();
        }
        metadata.put(s, o);
        metadataClasses.put(s, o.getClass().getName());
    }
    
    public HashMap<String,Object> getMetaData() { return metadata; }
    public Object getMetaDataItem(String s) {
        if (metadata == null)
            return null;
        return metadata.get(s);
    }
}
