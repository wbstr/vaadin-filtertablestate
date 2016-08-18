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

import com.vaadin.annotations.Theme;
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
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.FilterTableStateHandler;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.ColumnInfo;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.FilterTableStateProfile;
import com.wcs.wcslib.vaadin.widget.filtertablestate.api.model.SortOrder;
import com.wcs.wcslib.vaadin.widget.filtertablestate.extension.FilterTableClickFunctionHandler;
import com.wcs.wcslib.vaadin.widget.filtertablestate.extension.FilterTableState;
import com.wcs.wcslib.vaadin.widget.filtertablestate.shared.ClickFunction;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.FilterGenerator;
import org.tepi.filtertable.FilterTable;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@Theme("filtertablestate")
public class WidgetTestApplication extends UI {

    private VerticalLayout layout;

    private enum FunctionCode {

        SINGLE_SELECT(0, true, false, "Single selection"),
        MULTI_SELECT(1, true, true, "Multi selection"),
        DISABLE_SELECT(2, false, false, "Disable selection");
        private int code;
        private boolean selectable;
        private boolean multiselect;
        private String label;

        private FunctionCode(int code, boolean selectable, boolean multiselect, String label) {
            this.code = code;
            this.selectable = selectable;
            this.multiselect = multiselect;
            this.label = label;
        }

        static FunctionCode findByCode(int code) {
            for (FunctionCode functionCode : values()) {
                if (functionCode.code == code) {
                    return functionCode;
                }
            }
            return null;
        }
    }

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
                Notification.show("The profile has been saved!");
            }

            @Override
            public void delete(String profileName) {
                Notification.show("The profile has been removed!");
            }

            @Override
            public Set<FilterTableStateProfile> load() {
                return buildProfiles();
            }

            @Override
            public String getDefaultProfile() {
                return null;
            }

            @Override
            public void setDefaultProfile(String profileName) {
                if (profileName == null) {
                    Notification.show("Default profile has been turned off!");
                } else {
                    Notification.show("Default profile has been set: " + profileName);
                }
            }
        };
    }

    private FilterTableClickFunctionHandler createClickFunctionHandler() {
        return new FilterTableClickFunctionHandler() {
            Set<ClickFunction> functions = createClickFunctions();

            @Override
            public Set<ClickFunction> load() {
                return functions;
            }

            @Override
            public void setSelected(Integer functionCode) {
                FunctionCode fc = FunctionCode.findByCode(functionCode);
                if (fc != null) {
                    filterTable.setSelectable(fc.selectable);
                    filterTable.setMultiSelect(fc.multiselect);
                    filterTable.setValue(null);
                    filterTable.refreshRowCache();
                    Notification.show("Function has been selected: " + fc.label);
                }
            }

            @Override
            public Integer getDefaultFunctionCode() {
                return FunctionCode.DISABLE_SELECT.code;
            }
        };
    }

    private Set<ClickFunction> createClickFunctions() {
        Set<ClickFunction> functions = new HashSet<ClickFunction>();
        for (FunctionCode functionCode : FunctionCode.values()) {
            functions.add(new ClickFunction(functionCode.code, functionCode.label));
        }
        return functions;
    }

    private Set<FilterTableStateProfile> buildProfiles() {
        Set<FilterTableStateProfile> profiles = new HashSet<FilterTableStateProfile>();
        Set<ColumnInfo> columnInfos = new HashSet<ColumnInfo>();


        columnInfos.add(new ColumnInfo("id", "", SortOrder.ASCENDING, false, -1, 0, 0));
        columnInfos.add(new ColumnInfo("name", "", SortOrder.DESCENDING, true, -1, 0, 1));
        columnInfos.add(new ColumnInfo("state", State.PROCESSING, SortOrder.UNSORTED, false, -1, 0, 2));

        columnInfos.add(new ColumnInfo("date", null, SortOrder.UNSORTED, false, -1, 0, 5));
        columnInfos.add(new ColumnInfo("validated", false, SortOrder.UNSORTED, false, -1, 0, 3));
        profiles.add(new FilterTableStateProfile("Test profile 1", columnInfos));

        columnInfos = new HashSet<ColumnInfo>();
        columnInfos.add(new ColumnInfo("id", "", SortOrder.UNSORTED, false, -1, 0, 0));
        columnInfos.add(new ColumnInfo("name", "", SortOrder.UNSORTED, false, -1, 0, 1));
        columnInfos.add(new ColumnInfo("state", State.FINISHED, SortOrder.UNSORTED, false, -1, 0, 2));

        columnInfos.add(new ColumnInfo("date", null, SortOrder.ASCENDING, false, -1, 0, 5));
        columnInfos.add(new ColumnInfo("validated", true, SortOrder.UNSORTED, false, -1, 0, 3));
        profiles.add(new FilterTableStateProfile("Test profile 2", columnInfos));
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