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

import uk.nhs.digital.mait.tkwxcore.ServiceManager;
import uk.nhs.digital.mait.tkwxcore.ServiceResponse;
import uk.nhs.digital.mait.tkwxcore.ToolkitService;

/**
 *
 * @author Damian Murphy
 */
public class TestMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServiceManager sm = ServiceManager.getInstance();
        try {
            sm.initialise("internal:/testproperties", null);
            TestArg t = new TestArg("foo", "bar");
            ToolkitService svc = sm.getService("RST");
            ServiceResponse sr = svc.execute(t);
            System.out.println(sr.getObject());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
