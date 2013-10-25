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
package com.wcs.wcslib.vaadin.widget.filtertablestate.api;

import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.FilterTableStateProfile;
import java.util.Set;

/**
 *
 * @author gergo
 */
public interface FilterTableStateHandler {

    /**
     * User has saved a profile, we should save it somewhere (e.g. into database).
     *
     * @param profile
     */
    public void save(FilterTableStateProfile profile);

    /**
     * User has deleted the profile with the given name.
     *
     * @param profileName The unique name of the profile which have been deleted.
     */
    public void delete(String profileName);

    /**
     *
     * @return Set of the profiles which have been already saved.
     */
    public Set<FilterTableStateProfile> load();

    /**
     *
     * @return The default profile's unique name. If it is not available, the function must return null value.
     */
    public String getDefaultProfile();

    /**
     * User has set the deafult profile.
     *
     * @param profileName The unique name of the profile which has been selected by default. The null value means that
     * the default profile for the table has been turned off.
     */
    public void setDefaultProfile(String profileName);
}
