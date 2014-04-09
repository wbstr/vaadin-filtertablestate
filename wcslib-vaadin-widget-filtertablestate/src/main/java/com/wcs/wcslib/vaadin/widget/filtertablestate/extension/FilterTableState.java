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

import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.Notification;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.FilterTableStateHandler;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.ColumnInfo;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.FilterTableStateProfile;
import com.wcs.wcslib.vaadin.widget.filtertablestate.shared.ClickFunction;
import com.wcs.wcslib.vaadin.widget.filtertablestate.shared.FilterTableStateSharedState;
import com.wcs.wcslib.vaadin.widget.filtertablestate.shared.FilterTableStateRpc;
import com.wcs.wcslib.vaadin.widget.filtertablestate.shared.FilterTableStateSharedState.FilterTableStateMessageKey;
import static com.wcs.wcslib.vaadin.widget.filtertablestate.shared.FilterTableStateSharedState.FilterTableStateMessageKey.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import org.tepi.filtertable.FilterTable;

/**
 *
 * @author gergo
 */
public class FilterTableState extends AbstractExtension {

    private FilterTableStateHandler stateHandler;
    private FilterTableClickFunctionHandler functionHandler;
    private Map<String, FilterTableStateProfile> profiles;
    private FilterTable table;
    private FilterTableStateProfile originalProfile;
    private FilterTableStateRpc rpc = new FilterTableStateRpc() {
        @Override
        public void setSelectedProfile(String profileName) {
            initSelectedProfile(profiles.get(profileName));
        }

        @Override
        public void saveProfile(String profileName) {
            if (profileName.isEmpty()) {
                Notification.show(getMsg(PROFILE_NAME_REQUIRED), Notification.Type.ERROR_MESSAGE);
                return;
            }
            if (getState().stateProfiles.contains(profileName)) {
                Notification.show(getMsg(PROFILE_NAME_EXIST), Notification.Type.ERROR_MESSAGE);
                return;
            }
            FilterTableStateProfile profile = FilterTableStateGenerator.generate(table, profileName);
            stateHandler.save(profile);
            profiles.put(profileName, profile);
            initProfilesInState();
        }

        @Override
        public void deleteProfile(String profileName) {
            stateHandler.delete(profileName);
            profiles.remove(profileName);
            initProfilesInState();
        }

        @Override
        public void resetProfile() {
            table.setSortContainerPropertyId(null);
            initSelectedProfile(originalProfile);
            initDefaultProfile();
        }

        @Override
        public void setSelectedFunction(Integer functionCode) {
            getState().selectedFunctionCode = functionCode;
            functionHandler.setSelected(functionCode);
        }

        @Override
        public void setDefaultProfile(String profileName) {
            getState().defaultProfile = profileName;
            stateHandler.setDefaultProfile(profileName);
        }
    };

    public FilterTableState() {
        registerRpc(rpc);
    }

    public void extend(FilterTable table, FilterTableStateHandler stateHandler, FilterTableClickFunctionHandler functionHandler) {
        extend(table, stateHandler);
        this.functionHandler = functionHandler;
        getState().functions = functionHandler.load();
        if (isValidFunctionCode(functionHandler.getDefaultFunctionCode())) {
            getState().selectedFunctionCode = functionHandler.getDefaultFunctionCode();
        }
    }

    public void extend(FilterTable table, FilterTableStateHandler stateHandler) {
        super.extend(table);
        this.table = table;
        this.stateHandler = stateHandler;
        originalProfile = FilterTableStateGenerator.generate(table, null);
        initProfiles(stateHandler.load());
        initDefaultProfile();
        setMessages(getDefaultResourceBundle());
    }

    public void setMessages(ResourceBundle resourceBundle) {
        for (FilterTableStateMessageKey key : FilterTableStateMessageKey.values()) {
            if (resourceBundle.containsKey(key.getKey())) {
                String value = resourceBundle.getString(key.getKey());
                if (value != null) {
                    getState().messages.put(key, value);
                }
            }
        }
    }

    @Override
    protected FilterTableStateSharedState getState() {
        return (FilterTableStateSharedState) super.getState();
    }

    private void initProfiles(Set<FilterTableStateProfile> profiles) {
        this.profiles = new HashMap<String, FilterTableStateProfile>();
        if (profiles != null) {
            for (FilterTableStateProfile profile : profiles) {
                Iterator<ColumnInfo> iterator = profile.getColumnInfos().iterator();
                while (iterator.hasNext()) {
                    ColumnInfo columnInfo = iterator.next();
                    if (!table.getContainerPropertyIds().contains(columnInfo.getPropertyId())
                            && table.getColumnGenerator(columnInfo.getPropertyId()) == null) {
                        iterator.remove();
                    }
                }
                this.profiles.put(profile.getName(), profile);
            }
        }
        initProfilesInState();
    }

    private void initProfilesInState() {
        List<String> profiles = new ArrayList<String>(this.profiles.keySet());
        Collections.sort(profiles);
        getState().stateProfiles = profiles;
    }

    private void initDefaultProfile() {
        FilterTableStateProfile defaultProfile = profiles.get(stateHandler.getDefaultProfile());
        if (defaultProfile == null) {
            getState().defaultProfile = null;
            return;
        }
        getState().defaultProfile = defaultProfile.getName();
        initSelectedProfile(defaultProfile);
    }

    private void initSelectedProfile(FilterTableStateProfile selectedProfile) {
        if (selectedProfile == null) {
            return;
        }
        getState().selectedProfile = selectedProfile.getName();

        List<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>(selectedProfile.getColumnInfos());
        Collections.sort(columnInfos, new ColumnInfoComparator());

        Object[] visibleColumns = new Object[columnInfos.size()];

        for (int i = 0; i < columnInfos.size(); i++) {
            ColumnInfo columnInfo = columnInfos.get(i);
            visibleColumns[i] = columnInfo.getPropertyId();
        }
        table.setVisibleColumns(visibleColumns);

        for (ColumnInfo columnInfo : columnInfos) {
            table.setFilterFieldValue(columnInfo.getPropertyId(), columnInfo.getFilter());
            table.setColumnCollapsed(columnInfo.getPropertyId(), columnInfo.isCollapsed());
            if (columnInfo.getWidth() < 0 && columnInfo.getWidthRatio() != 0) {
				table.setColumnExpandRatio(columnInfo.getPropertyId(),
						columnInfo.getWidthRatio());
			} else {
				table.setColumnWidth(columnInfo.getPropertyId(),
						columnInfo.getWidth());
			}
            if (columnInfo.getSortOrder().isSorted()) {
                table.sort(new Object[]{columnInfo.getPropertyId()}, new boolean[]{columnInfo.getSortOrder().isAscending()});
                table.setSortContainerPropertyId(columnInfo.getPropertyId());
            }
        }
    }

    private ResourceBundle getDefaultResourceBundle() {
        return ResourceBundle.getBundle("com.wcs.wcslib.vaadin.widget.filtertablestate.message.FilterTableStateMessages");
    }

    private String getMsg(FilterTableStateMessageKey key) {
        return getState().messages.get(key);
    }

    private static class ColumnInfoComparator implements Comparator<ColumnInfo> {

        @Override
        public int compare(ColumnInfo o1, ColumnInfo o2) {
            Integer ndx1 = o1.getIndex();
            Integer ndx2 = o2.getIndex();
            return ndx1.compareTo(ndx2);
        }
    }

    private boolean isValidFunctionCode(Integer functionCode) {
        for (ClickFunction function : getState().functions) {
            if (function.getCode().equals(functionCode)) {
                return true;
            }
        }
        return false;
    }
}
