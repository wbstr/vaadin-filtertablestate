package com.wcs.wcslib.vaadin.widget.filtertablestate.extension;

import com.wcs.wcslib.vaadin.widget.filtertablestate.shared.ClickFunction;
import java.util.Set;

/**
 *
 * @author gergo
 */
public interface FilterTableClickFunctionHandler {

    /**
     *
     * @return Set of functions which will be accessible for the table
     */
    public Set<ClickFunction> load();

    /**
     * User has set the deafult function.
     *
     * @param functionCode The unique code of the function which has been selected.
     */
    public void setSelected(Integer functionCode);

    /**
     *
     * @return The unique code of the function that is selected by default.
     */
    public Integer getDefaultFunctionCode();
}
