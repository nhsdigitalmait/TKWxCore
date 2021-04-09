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
package uk.nhs.digital.mait.tkwxcore.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Properties;
import uk.nhs.digital.mait.tkwxcore.AbstractToolkitService;
import uk.nhs.digital.mait.tkwxcore.RemoteToolkitServiceAdapter;
import uk.nhs.digital.mait.tkwxcore.ServiceResponse;
import uk.nhs.digital.mait.tkwxcore.ServiceResponseDeserializer;

/**
 *
 * @author Damian Murphy
 */
public class RemoteServiceTest 
        extends AbstractToolkitService
{
    private static final Class[] arglist = {TestArg.class};
    
    private Gson gson = null;    
    private RemoteToolkitServiceAdapter remote = null;
    
    @Override
    public void boot(Properties p, String s) 
            throws Exception 
    {
        name = s;
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(ServiceResponse.class, new ServiceResponseDeserializer());
        gson = gb.create();
        
        NullService ns = new NullService();
        remote = new RemoteToolkitServiceAdapter(p, "Remote test", ns, TestArg.class);
        remote.initialise();
        // Wait a bit for an asynchronous boot test
        // Thread.sleep(10000);
    }
    
    
    @Override
    public ServiceResponse execute(Object o)
            throws Exception 
    {
        String pass = gson.toJson(o);
        String back = remote.call(pass);
        ServiceResponse sr = gson.fromJson(back, ServiceResponse.class);
        return sr;
    }

    @Override
    public void reconfigure(Properties p) throws Exception {
        
    }

    @Override
    public String describe() {
        return "Remote service test";
    }

    @Override
    public Class[] accepts() {
        return arglist;
    }

    @Override
    public Exception getBootException() {
        return null;
    }
}
