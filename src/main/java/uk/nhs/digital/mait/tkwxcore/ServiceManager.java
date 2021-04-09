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

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;


/**
 *
 * @author Damian Murphy
 */
public class ServiceManager {

    private static final String SERVICENAMELIST = "tkwx.servicemanager.servicelist";
    private static final String TKWXCOMMON = "tkwx.common.";
    private static final String IMPLEMENTINGCLASSNAME = ".implementingclassname";
    private static final String ASYNCHRONOUSBOOT = ".asynchronousboot";
    
    private static final ServiceManager serviceManager = new ServiceManager();
    private final HashMap<String,ToolkitService> services = new HashMap<>();
    private ThreadPoolExecutor threadPool = null;
    private final ArrayList<Exception> serviceBootExceptions = new ArrayList<>();
    
    private ServiceManager() {
        
    }
    
    public Set<String> getServiceNames() { return services.keySet(); }
    
    public ToolkitService getService(String n) { return services.get(n); }
    public void addService(String n, ToolkitService t) { services.put(n, t); }
    
    public static ServiceManager getInstance()
    {
        return serviceManager;
    }
    
    void reportBootException(ToolkitService ts, Exception e) {
        serviceBootExceptions.add(e);
        // TODO: Logger.Log ts.getName(), e.toString()
    }

    public Iterator<Exception> getServiceBootExceptions() { return serviceBootExceptions.iterator(); }
    
    public boolean allReady() {
        for (String s : services.keySet()) {
            ToolkitService ts = services.get(s);
            if (!ts.isBooted())
                return false;
        }        
        return true;
    }
    
    public void initialise(Properties p, ThreadPoolExecutor t) 
            throws Exception
    {
        threadPool = t;
        String slist = p.getProperty(SERVICENAMELIST);
        if (slist == null) {
            throw new IllegalArgumentException("No service list property found: " + SERVICENAMELIST);
        }
        String[] snames = slist.split("[\\s,]");
        for (String n : snames) {
            String sn = AbstractToolkitService.PROPERTYROOT + n;
            String icn = sn + IMPLEMENTINGCLASSNAME;
            String className = p.getProperty(icn);
            if (className == null) {
                throw new IllegalArgumentException("No definition for implementing class name: " + icn);
            }
            Properties sp = new Properties();
            for (String pname : p.stringPropertyNames()) {
                if (pname.startsWith(sn) || pname.startsWith(TKWXCOMMON)) {
                    sp.setProperty(pname, p.getProperty(pname));
                }
            }
            String asb = sn + ASYNCHRONOUSBOOT;
            asb = p.getProperty(asb);
            ToolkitService ts = (ToolkitService)Class.forName(className).getConstructor().newInstance();
            if ((threadPool != null) && (asb != null) && asb.toUpperCase().startsWith("Y")) {
                try {
                    ServiceStarter ss = new ServiceStarter(this, ts, sp, sn);
                    threadPool.execute(ss);
                }
                catch (Exception e) {
                    reportBootException(ts, e);
                }
            } else {
                try {
                    ts.boot(sp, n);
                } catch (Exception e) {
                    reportBootException(ts, e);
                }
            }
            services.put(n, ts);
        }
    }
    
    public void initialise(String pspec, ThreadPoolExecutor t) 
            throws Exception
    {
        Properties p = new Properties();
        p.load(getResourceStream(pspec));
        initialise(p, t);
    }
    
    public static final InputStream getResourceStream(String n)
            throws Exception
    {
        if (n == null)
            throw new IllegalArgumentException("Null resource stream requested");
        if (n.startsWith("file:")) {
            String f = n.substring("file:".length());
            return new FileInputStream(f);
        }
        if (n.startsWith("internal:")) {
            String r = n.substring("internal:".length());
            return ServiceManager.class.getResourceAsStream(r);
        }
        if (n.startsWith("http")) {
            URL u = new URL(n);
            return u.openStream();
        }
        throw new IllegalArgumentException("Unrecognised resource: " + n);
    }    
}
