/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wcs.wcslib.vaadin.widget.filtertablestate.shared;

import java.io.Serializable;

/**
 *
 * @author gergo
 */
public class ClickFunction implements Serializable{

    private Integer code;
    private String name;

    public ClickFunction() {
    }

    public ClickFunction(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer id) {
        this.code = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClickFunction other = (ClickFunction) obj;
        return code != null && code.equals(other.getCode());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.code;
        return hash;
    }
}
