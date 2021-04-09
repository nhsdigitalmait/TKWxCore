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
public interface ToolkitService {
    public void boot(Properties p, String s) throws Exception;
    public void reconfigure(Properties p) throws Exception;
    public ServiceResponse execute(Object o) throws Exception;
    public String describe();
    public String getName();
    public Class[] accepts();
    public boolean isBooted();
    public Exception getBootException();
}
