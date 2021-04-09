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

import java.util.Date;
import java.util.Properties;
import uk.nhs.digital.mait.tkwxcore.AbstractToolkitService;
import uk.nhs.digital.mait.tkwxcore.ServiceResponse;

/**
 *
 * @author Damian Murphy
 */
public class NullService 
        extends AbstractToolkitService
{
    private static final Class[] arglist = {TestArg.class};
    
    @Override
    public void boot(Properties p, String s) throws Exception {
        name = s;
        properties = p;
    }

    @Override
    public void reconfigure(Properties p) throws Exception {
    }

    @Override
    public ServiceResponse execute(Object o)
            throws Exception 
    {
        TestArg t = (TestArg)o;
        ServiceResponse sr = new ServiceResponse(200, "OK");
        t.setConcatenation("Hello World");
        sr.setObject(t);
        sr.addMetaDataItem("Date", new Date());
        sr.addMetaDataItem("SvcName", name);
        sr.addMetaDataItem("Props", properties);
        return sr;
    }

    @Override
    public String describe() {
        return "A test";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class[] accepts() {
        return arglist;
    }

    @Override
    public boolean isBooted() {
        return (name != null);
    }

    @Override
    public Exception getBootException() {
        return null;
    }
    
}
