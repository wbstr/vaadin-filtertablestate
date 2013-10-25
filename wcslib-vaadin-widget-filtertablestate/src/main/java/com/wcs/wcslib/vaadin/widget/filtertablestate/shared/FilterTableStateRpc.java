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

import com.vaadin.shared.communication.ServerRpc;

/**
 *
 * @author gergo
 */
public interface FilterTableStateRpc extends ServerRpc {

    public void resetProfile();

    public void setSelectedProfile(String profileName);

    public void setDefaultProfile(String profileName);

    public void saveProfile(String profileName);

    public void deleteProfile(String profileName);

    public void setSelectedFunction(Integer functionCode);
}
