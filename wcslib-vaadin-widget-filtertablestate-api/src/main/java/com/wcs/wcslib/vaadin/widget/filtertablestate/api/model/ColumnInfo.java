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

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author gergo
 */
public class ColumnInfo implements Serializable {

    private String propertyId;
    private Object filter;
    private SortOrder sortOrder = SortOrder.UNSORTED;
    private boolean collapsed;
    private int width;
    private int index;
    private float widthRatio;

    public ColumnInfo(String propertyId, Object filter, SortOrder sortOrder, boolean collapsed, int width, float widthRatio, int index) {
        this.propertyId = propertyId;
        this.filter = filter;
        this.sortOrder = sortOrder;
        this.collapsed = collapsed;
        this.width = width;
        this.widthRatio = widthRatio;
        this.index = index;
    }

    public ColumnInfo() {
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public Object getFilter() {
        return filter;
    }

    public void setFilter(Object filter) {
        this.filter = filter;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public int getWidth() {
        return width < 1 ? -1 : width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    public float getWidthRatio() {
		return widthRatio;
	}

	public void setWidthRatio(float widthRatio) {
		this.widthRatio = widthRatio;
	}

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.propertyId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ColumnInfo other = (ColumnInfo) obj;
        return propertyId != null && propertyId.equals(other.getPropertyId());
    }
}
