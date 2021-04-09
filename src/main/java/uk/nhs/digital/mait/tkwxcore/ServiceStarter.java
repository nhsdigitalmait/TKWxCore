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

import java.util.Properties;

/**
 *
 * @author Damian Murphy
 */
public class ServiceStarter 
        implements Runnable
{
    private ToolkitService service = null;
    private Properties props = null;
    private String name = null;
    private ServiceManager manager = null;
    
    ServiceStarter(ServiceManager s, ToolkitService ts, Properties p, String n) {
        service = ts;
        manager = s;
        props = p;
        name = n;
    }
            
    @Override
    public void run() {
        try {
            if (!service.isBooted()) {
                service.boot(props, name);
            }
        }
        catch (Exception e) {
            manager.reportBootException(service, e);
        }
    }
    
}
