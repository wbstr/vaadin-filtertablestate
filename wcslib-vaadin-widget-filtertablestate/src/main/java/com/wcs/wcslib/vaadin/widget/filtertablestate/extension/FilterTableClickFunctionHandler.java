/*
 * Copyright 2013 gergo.
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
package com.wcs.wcslib.vaadin.widget.filtertablestate.extension;

import com.wcs.wcslib.vaadin.widget.filtertablestate.shared.ClickFunction;
import java.util.Set;

/**
 *
 * @author gergo
 */
public interface FilterTableClickFunctionHandler {

    /**
     *
     * @return Set of functions which will be accessible for the table
     */
    public Set<ClickFunction> load();

    /**
     * User has set the deafult function.
     *
     * @param functionCode The unique code of the function which has been selected.
     */
    public void setSelected(Integer functionCode);

    /**
     *
     * @return The unique code of the function that is selected by default.
     */
    public Integer getDefaultFunctionCode();
}
