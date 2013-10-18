/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wcs.wcslib.vaadin.widget.filtertablestate.api.model;

/**
 *
 * @author gergo
 */
public enum SortOrder {

    ASCENDING,
    DESCENDING,
    UNSORTED;

    public boolean isSorted() {
        return !this.equals(UNSORTED);
    }

    public boolean isAscending() {
        return this.equals(ASCENDING);
    }

    public boolean isDescending() {
        return this.equals(DESCENDING);
    }
}
