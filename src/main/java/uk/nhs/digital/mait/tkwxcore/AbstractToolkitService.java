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
public abstract class AbstractToolkitService 
        implements ToolkitService
{
    public static final String PROPERTYROOT = "tkwx.";
    protected String name = null;
    protected Properties properties = null;
    
    @Override
    public boolean isBooted() { return (name != null); }
    
    @Override
    public String getName() { return name; }
    
    protected boolean isSet(String s, boolean f)
            throws IllegalArgumentException
    {
        String flag = getProperty(s, f);
        if (flag == null)
            return false;
        return (flag.toUpperCase().startsWith("Y"));
    }
    
    protected String getProperty(String s, boolean f) 
            throws IllegalArgumentException
    {
        StringBuilder sb = new StringBuilder(PROPERTYROOT);
        sb.append(name);
        sb.append(".");
        sb.append(s);
        String r = sb.toString();
        String p = properties.getProperty(r);
        if (f && (p == null)) {
            throw new IllegalArgumentException("Property not set: " + r);
        }
        return p;
    }
        
}
