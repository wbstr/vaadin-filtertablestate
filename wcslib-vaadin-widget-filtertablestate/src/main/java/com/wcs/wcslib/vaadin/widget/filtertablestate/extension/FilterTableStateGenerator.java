/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wcs.wcslib.vaadin.widget.filtertablestate.extension;

import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.ColumnInfo;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.FilterTableStateProfile;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.SortOrder;
import java.util.HashSet;
import java.util.Set;
import org.tepi.filtertable.FilterTable;

/**
 *
 * @author gergo
 */
public class FilterTableStateGenerator {

    public static FilterTableStateProfile generate(FilterTable filterTable, String profileName) {
        
        Set<ColumnInfo> columnInfos = new HashSet<ColumnInfo>();
        addVisibleColumns(columnInfos, filterTable);
        addCollapsedColumns(columnInfos, filterTable);
        
        return new FilterTableStateProfile(profileName, columnInfos);
    }

    private static int getColumnIndex(FilterTable filterTable, Object propertyId) {
        for (int i = 0; i < filterTable.getVisibleColumns().length; i++) {
            if (filterTable.getVisibleColumns()[i].equals(propertyId)) {
                return i;
            }
        }
        return 0;
    }
    
    private static void addVisibleColumns(Set<ColumnInfo> columnInfos, FilterTable filterTable) {
        for (Object propertyId : filterTable.getVisibleColumns()) {
            columnInfos.add(createColumnInfo(propertyId, filterTable));
        }
    }
    
    private static void addCollapsedColumns(Set<ColumnInfo> columnInfos, FilterTable filterTable) {
        for (Object propertyId : filterTable.getContainerPropertyIds()) {
            if (filterTable.isColumnCollapsible(propertyId)) {
                columnInfos.add(createColumnInfo(propertyId, filterTable));
            }
        }
    }
    
    private static ColumnInfo createColumnInfo(Object propertyId, FilterTable filterTable) {
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setPropertyId((String) propertyId);
        columnInfo.setCollapsed(filterTable.isColumnCollapsed(propertyId));
        columnInfo.setFilter(filterTable.getFilterFieldValue(propertyId));
        columnInfo.setWidth(filterTable.getColumnWidth(propertyId));
        columnInfo.setIndex(getColumnIndex(filterTable, propertyId));
        if (propertyId.equals(filterTable.getSortContainerPropertyId())) {
            if (filterTable.isSortAscending()) {
                columnInfo.setSortOrder(SortOrder.ASCENDING);
            } else {
                columnInfo.setSortOrder(SortOrder.DESCENDING);
            }
        }
        return columnInfo;
    }
}
