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
package com.wcs.wcslib.vaadin.widget.filtertablestate.shared;

import com.vaadin.shared.communication.SharedState;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author gergo
 */
public class FilterTableStateSharedState extends SharedState {

    public enum FilterTableStateMessageKey {

        FUNCTIONS("filtertablestate.functions"),
        COLUMNS("filtertablestate.columns"),
        SELECT_FUNCTION("filtertablestate.function.select"),
        NEW_PROFILE("filtertablestate.profile.new"),
        DEFAULT_PROFILE("filtertablestate.profile.default"),
        SELECT_PROFILE("filtertablestate.profile.select"),
        DELETE_PROFILE("filtertablestate.profile.delete"),
        DEFAULT_PROFILE_ON("filtertablestate.profile.default.on"),
        DEFAULT_PROFILE_OFF("filtertablestate.profile.default.off"),
        SAVE_PROFILE("filtertablestate.profile.save"),
        PROFILE_NAME_REQUIRED("filtertablestate.profile.name.required"),
        PROFILE_NAME_EXIST("filtertablestate.profile.name.exist"),
        PROFILES("filtertablestate.profiles");
        private String key;


        private FilterTableStateMessageKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
    public Map<FilterTableStateMessageKey, String> messages = new HashMap<FilterTableStateMessageKey, String>();
    public List<String> stateProfiles;
    public Set<ClickFunction> functions = new HashSet<ClickFunction>();
    public String selectedProfile;
    public String defaultProfile;
    public Integer selectedFunctionCode;
}
