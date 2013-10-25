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
package com.wcs.wcslib.vaadin.widget.filtertablestate.api.model;

import java.util.Set;

/**
 *
 * @author gergo
 */
public class FilterTableStateProfile {

    private String name;
    private Set<ColumnInfo> columnInfos;

    public FilterTableStateProfile(String name, Set<ColumnInfo> columnInfos) {
        this.name = name;
        this.columnInfos = columnInfos;
    }

    public FilterTableStateProfile() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ColumnInfo> getColumnInfos() {
        return columnInfos;
    }

    public void setColumnInfos(Set<ColumnInfo> columnInfos) {
        this.columnInfos = columnInfos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FilterTableStateProfile) {
            FilterTableStateProfile other = (FilterTableStateProfile) obj;
            return name != null && name.equals(other.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
