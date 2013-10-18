/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wcs.wcslib.vaadin.widget.filtertablestate.shared;

import com.vaadin.shared.communication.SharedState;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author gergo
 */
public class FIlterTableStateSharedState extends SharedState {

    public List<String> stateProfiles;
    public Set<ClickFunction> functions = new HashSet<ClickFunction>();
    public String selectedProfile;
    public String defaultProfile;
    public Integer selectedFunctionCode;

}
