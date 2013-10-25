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
package com.wcs.wcslib.vaadin.widget.filtertablestate;

import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.wcs.wcslib.vaadin.widget.filtertablestate.shared.ClickFunction;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.FilterTableStateHandler;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.ColumnInfo;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.FilterTableStateProfile;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.SortOrder;
import com.wcs.wcslib.vaadin.widget.filtertablestate.extension.FilterTableClickFunctionHandler;
import com.wcs.wcslib.vaadin.widget.filtertablestate.extension.FilterTableState;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.FilterGenerator;
import org.tepi.filtertable.FilterTable;
import org.tepi.filtertable.datefilter.DateInterval;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class WidgetTestApplication extends UI {

    private VerticalLayout layout;

    @Override
    protected void init(VaadinRequest request) {
        layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        setContent(layout);

        addFilterTableExample();
    }

    public enum State {

        CREATED, PROCESSING, PROCESSED, FINISHED;
    }
    private FilterTable filterTable;

    private void addFilterTableExample() {
        filterTable = buildFilterTable();
        new FilterTableState().extend(filterTable, createFilterTableStateHandler(), createClickFunctionHandler());
        layout.addComponent(filterTable);
    }

    private FilterTableStateHandler createFilterTableStateHandler() {
        return new FilterTableStateHandler() {
            @Override
            public void save(FilterTableStateProfile profile) {
            }

            @Override
            public void delete(String profileName) {
            }

            @Override
            public Set<FilterTableStateProfile> load() {
                return new HashSet<FilterTableStateProfile>();
            }

            @Override
            public String getDefaultProfile() {
                return null;
            }

            @Override
            public void setDefaultProfile(String profileName) {
                Notification.show("Alapértelmezett profil kiválasztva: " + profileName);
            }
        };
    }

    private FilterTableClickFunctionHandler createClickFunctionHandler() {
        return new FilterTableClickFunctionHandler() {
            @Override
            public Set<ClickFunction> load() {
                return createClickFunctions();
            }

            @Override
            public void setSelected(Integer functionCode) {
                Notification.show("Funkció kiválasztva: " + functionCode);
            }

            @Override
            public Integer getDefaultFunctionCode() {
                return 0;
            }
        };
    }

    private Set<ClickFunction> createClickFunctions() {
        Set<ClickFunction> functions = new HashSet<ClickFunction>();
        functions.add(new ClickFunction(0, "Navigálás"));
        functions.add(new ClickFunction(1, "Gyorsnézet"));
        return functions;
    }

    private Set<FilterTableStateProfile> buildProfiles() {
        Set<FilterTableStateProfile> profiles = new HashSet<FilterTableStateProfile>();
        Set<ColumnInfo> columnInfos = new HashSet<ColumnInfo>();


        columnInfos.add(new ColumnInfo("id", null, SortOrder.ASCENDING, false, -1, 0));
        columnInfos.add(new ColumnInfo("name", "a", SortOrder.DESCENDING, true, -1, 1));
        columnInfos.add(new ColumnInfo("state", State.PROCESSING, SortOrder.UNSORTED, false, -1, 2));
        columnInfos.add(new ColumnInfo("ilyenNincs", State.PROCESSING, SortOrder.UNSORTED, false, -1, 2));

        Date from = new GregorianCalendar(2013, 8, 1, 8, 0, 0).getTime();
        Date to = new GregorianCalendar(2013, 9, 1, 9, 0, 0).getTime();
        columnInfos.add(new ColumnInfo("date", new DateInterval(from, to), SortOrder.UNSORTED, false, -1, 5));
        columnInfos.add(new ColumnInfo("validated", true, SortOrder.UNSORTED, false, -1, 3));
        profiles.add(new FilterTableStateProfile("Teszt", columnInfos));

        columnInfos = new HashSet<ColumnInfo>();
        columnInfos.add(new ColumnInfo("id", "21", SortOrder.ASCENDING, false, -1, 0));
        columnInfos.add(new ColumnInfo("name", null, SortOrder.DESCENDING, false, -1, 1));
        columnInfos.add(new ColumnInfo("state", State.PROCESSED, SortOrder.UNSORTED, false, -1, 2));

        columnInfos.add(new ColumnInfo("date", null, SortOrder.UNSORTED, false, -1, 5));
        columnInfos.add(new ColumnInfo("validated", true, SortOrder.UNSORTED, false, -1, 3));
        profiles.add(new FilterTableStateProfile("Teszt 2", columnInfos));
        return profiles;
    }

    private FilterTable buildFilterTable() {
        FilterTable table = new FilterTable();
        table.setSizeFull();
        table.setFilterDecorator(new DemoFilterDecorator());
        table.setFilterGenerator(new DemoFilterGenerator());
        table.setContainerDataSource(buildContainer());
        table.setFilterBarVisible(true);
        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        return table;
    }

    private IndexedContainer buildContainer() {
        IndexedContainer cont = new IndexedContainer();
        Calendar c = Calendar.getInstance();

        cont.addContainerProperty("name", String.class, null);
        cont.addContainerProperty("id", Integer.class, null);
        cont.addContainerProperty("state", State.class, null);
        cont.addContainerProperty("date", Date.class, null);
        cont.addContainerProperty("validated", Boolean.class, null);

        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            cont.addItem(i);
            /* Set name and id properties */
            cont.getContainerProperty(i, "name").setValue("Order " + i);
            cont.getContainerProperty(i, "id").setValue(i);
            /* Set state property */
            int rndInt = random.nextInt(4);
            State stateToSet = State.CREATED;
            if (rndInt == 0) {
                stateToSet = State.PROCESSING;
            } else if (rndInt == 1) {
                stateToSet = State.PROCESSED;
            } else if (rndInt == 2) {
                stateToSet = State.FINISHED;
            }
            cont.getContainerProperty(i, "state").setValue(stateToSet);
            /* Set date property */
            cont.getContainerProperty(i, "date").setValue(c.getTime());
            c.add(Calendar.DAY_OF_MONTH, 1);
            /* Set validated property */
            cont.getContainerProperty(i, "validated").setValue(
                    random.nextBoolean());
        }
        return cont;
    }

    public class DemoFilterGenerator implements FilterGenerator {

        @Override
        public Container.Filter generateFilter(Object propertyId, Object value) {
            if ("id".equals(propertyId)) {
                /* Create an 'equals' filter for the ID field */
                if (value != null && value instanceof String) {
                    try {
                        return new Compare.Equal(propertyId,
                                Integer.parseInt((String) value));
                    } catch (NumberFormatException ignored) {
                        // If no integer was entered, just generate default filter
                    }
                }
            }
            // For other properties, use the default filter
            return null;
        }

        @Override
        public AbstractField<?> getCustomFilterComponent(Object propertyId) {
            return null;
        }

        @Override
        public void filterRemoved(Object propertyId) {
        }

        @Override
        public void filterAdded(Object propertyId, Class<? extends Container.Filter> filterType, Object value) {
        }

        @Override
        public Container.Filter generateFilter(Object propertyId, Field<?> originatingField) {
            return null;
        }

        @Override
        public Container.Filter filterGeneratorFailed(Exception reason, Object propertyId, Object value) {
            return null;
        }
    }

    public class DemoFilterDecorator implements FilterDecorator {

        @Override
        public String getEnumFilterDisplayName(Object propertyId, Object value) {
            if ("state".equals(propertyId)) {
                State state = (State) value;
                switch (state) {
                    case CREATED:
                        return "Order has been created";
                    case PROCESSING:
                        return "Order is being processed";
                    case PROCESSED:
                        return "Order has been processed";
                    case FINISHED:
                        return "Order is delivered";
                }
            }
            // returning null will output default value
            return null;
        }

        @Override
        public Resource getEnumFilterIcon(Object propertyId, Object value) {
            if ("state".equals(propertyId)) {
                State state = (State) value;
                switch (state) {
                    case CREATED:
                        return new ThemeResource("../runo/icons/16/document.png");
                    case PROCESSING:
                        return new ThemeResource("../runo/icons/16/reload.png");
                    case PROCESSED:
                        return new ThemeResource("../runo/icons/16/ok.png");
                    case FINISHED:
                        return new ThemeResource("../runo/icons/16/globe.png");
                }
            }
            return null;
        }

        @Override
        public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
            if ("validated".equals(propertyId)) {
                return value ? "Validated" : "Not validated";
            }
            // returning null will output default value
            return null;
        }

        @Override
        public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
            if ("validated".equals(propertyId)) {
                return value ? new ThemeResource("../runo/icons/16/ok.png")
                        : new ThemeResource("../runo/icons/16/cancel.png");
            }
            return null;
        }

        @Override
        public Locale getLocale() {
            // will use the application locale
            return null;
        }

        @Override
        public String getFromCaption() {
            return "Start date:";
        }

        @Override
        public String getToCaption() {
            return "End date:";
        }

        @Override
        public String getSetCaption() {
            // use default caption
            return null;
        }

        @Override
        public String getClearCaption() {
            // use default caption
            return null;
        }

        @Override
        public boolean isTextFilterImmediate(Object propertyId) {
            return true;
        }

        @Override
        public int getTextChangeTimeout(Object propertyId) {
            return 1000;
        }

        @Override
        public Resolution getDateFieldResolution(Object propertyId) {
            return Resolution.SECOND;
        }

        @Override
        public String getDateFormatPattern(Object propertyId) {
            return null;
        }

        @Override
        public String getAllItemsVisibleString() {
            return null;
        }

        @Override
        public NumberFilterPopupConfig getNumberFilterPopupConfig() {
            return null;
        }

        @Override
        public boolean usePopupForNumericProperty(Object propertyId) {
            return false;
        }
    }
}