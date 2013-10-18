package com.wcs.wcslib.vaadin.widget.filtertablestate.api;

import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.FilterTableStateProfile;
import java.util.Set;

/**
 *
 * @author gergo
 */
public interface FilterTableStateHandler {

    /**
     * User has saved a profile, we should save it somewhere (e.g. into database).
     *
     * @param profile
     */
    public void save(FilterTableStateProfile profile);

    /**
     * User has deleted the profile with the given name.
     *
     * @param profileName The unique name of the profile which have been deleted.
     */
    public void delete(String profileName);

    /**
     *
     * @return Set of the profiles which have been already saved.
     */
    public Set<FilterTableStateProfile> load();

    /**
     *
     * @return The default profile's unique name. If it is not available, the function must return null value.
     */
    public String getDefaultProfile();

    /**
     * User has set the deafult profile.
     *
     * @param profileName The unique name of the profile which has been selected by default. The null value means that the
     * default profile for the table has been turned off.
     */
    public void setDefaultProfile(String profileName);
}
