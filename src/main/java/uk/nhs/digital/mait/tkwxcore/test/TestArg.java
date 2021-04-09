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

/**
 *
 * @author Damian Murphy
 */
public class TestArg {
    
    private String name = null;
    private String value = null;
    private String concatenation = null;
    private String original = null;
    
    public TestArg(String n, String v) {
        name = n;
        value = v;
        concatenation = name + " : " + value;
        original = name + " : " + value;
    }
    
    public String getName() { return name; }
    public String getValue() { return value; }
    public void setConcatenation(String s) { concatenation = s; }
    public String getOriginal() { return original; }
    public boolean isChanged() { return concatenation.contentEquals(original); }    
}
