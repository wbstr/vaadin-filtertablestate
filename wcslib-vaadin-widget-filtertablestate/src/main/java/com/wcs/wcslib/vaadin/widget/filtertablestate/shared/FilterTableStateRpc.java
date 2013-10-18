/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
