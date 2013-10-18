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
