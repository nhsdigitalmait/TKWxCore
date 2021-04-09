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

import com.google.gson.Gson;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Properties;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

/**
 *
 * @author Damian Murphy
 */
public class HttpRemoteToolkitService 
        extends AbstractToolkitService
{
    private static final String REMOTEURLPROPERTY = "remoteurl";
    private static final String TLSMUTUALPROPERTY = "tlsmutualauth";
    private static final String KEYSTORETYPEPROPERTY = "keystoretype";
    private static final String KEYSTOREPASSPROPERTY = "keystorepass";
    private static final String KEYSTOREPROPERTY = "keystore";
    
    private URL remoteUrl = null;
    private boolean tlsMutualAuth = false;
    private SSLContext sslContext = null;    
    private Gson gson = null;
    private Exception bootException = null;
    
    /**
     *
     * @param p
     * @param s
     * @throws Exception
     */
    @Override
    public void boot(Properties p, String s) 
            throws Exception 
    {
        try {
            name = s;
            properties = p;
            remoteUrl = new URL(getProperty(REMOTEURLPROPERTY, true));
            tlsMutualAuth = isSet(TLSMUTUALPROPERTY, false);
            if (tlsMutualAuth) {
                initialiseSSLContext();
            }
            gson = new Gson();
        }
        catch (Exception e) {
            bootException = e;
            throw e;
        }
    }

    private void initialiseSSLContext() 
            throws Exception
    {
        String kstype = getProperty(KEYSTORETYPEPROPERTY, false);
        if (kstype == null) 
            kstype = "jks";
        String kspass = getProperty(KEYSTOREPASSPROPERTY, false);
        if (kspass == null)
            kspass = "";
        KeyStore ks = KeyStore.getInstance(kstype);
        ks.load(ServiceManager.getResourceStream(getProperty(KEYSTOREPROPERTY, true)), kspass.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, kspass.toCharArray());
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
    }
    
    /**
     *
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public ServiceResponse execute(Object o) throws Exception {
        String pass = gson.toJson(o);
        URLConnection u = remoteUrl.openConnection();
        if (tlsMutualAuth) {
            ((HttpsURLConnection)u).setSSLSocketFactory(sslContext.getSocketFactory());
        }
        u.connect();
        u.getOutputStream().write(pass.getBytes());
        ServiceResponse r = ServiceResponse.getRemoteResponse(u.getInputStream());
        return r;
    }

    @Override
    public String describe() {
        return "Generic remote caller for HTTP(S) accessible TKW services";
    }

    @Override
    public Class[] accepts() {
        return null;
    }

    @Override
    public void reconfigure(Properties p) throws Exception {
        boot(p, name);
    }

    @Override
    public boolean isBooted() {
        return (name != null);    
    }

    @Override
    public Exception getBootException() {
        return bootException;
    }

    @Override
    public String getName() {
        return name;
    }
    
}
