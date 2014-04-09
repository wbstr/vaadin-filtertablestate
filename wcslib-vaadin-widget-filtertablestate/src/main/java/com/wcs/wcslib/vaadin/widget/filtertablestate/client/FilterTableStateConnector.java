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
package com.wcs.wcslib.vaadin.widget.filtertablestate.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.Action;
import com.vaadin.client.ui.VLabel;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.client.ui.VTextField;
import com.vaadin.client.ui.VVerticalLayout;
import com.vaadin.shared.ui.Connect;
import org.tepi.filtertable.gwt.client.ui.FilterTableConnector;
import org.tepi.filtertable.gwt.client.ui.VFilterTable;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Function;
import static com.google.gwt.query.client.GQuery.*;
import com.google.gwt.user.client.Event;
import com.vaadin.client.VConsole;
import com.wcs.wcslib.vaadin.widget.filtertablestate.extension.FilterTableState;
import com.wcs.wcslib.vaadin.widget.filtertablestate.shared.ClickFunction;
import com.wcs.wcslib.vaadin.widget.filtertablestate.shared.FilterTableStateSharedState;
import com.wcs.wcslib.vaadin.widget.filtertablestate.shared.FilterTableStateRpc;
import com.wcs.wcslib.vaadin.widget.filtertablestate.shared.FilterTableStateSharedState.FilterTableStateMessageKey;
import static com.wcs.wcslib.vaadin.widget.filtertablestate.shared.FilterTableStateSharedState.FilterTableStateMessageKey.*;

/**
 *
 * @author gergo
 */
@Connect(FilterTableState.class)
public class FilterTableStateConnector extends AbstractExtensionConnector implements AttachEvent.Handler {

    private static final String COLUMN_SELECTOR_STYLE = "v-table-column-selector";
    private static final String HIDEABLE_STYLE = "w-filtertablestate-hideable ";
    private static final String BUTTON_STYLE = "w-filtertablestate-button ";
    private static final String SAVE_BUTTON_STYLE = BUTTON_STYLE + "w-filtertablestate-savebutton";
    private static final String RESAVE_BUTTON_STYLE = BUTTON_STYLE
			+ "w-filtertablestate-resavebutton";
    private static final String DELETE_BUTTON_STYLE = BUTTON_STYLE + "w-filtertablestate-deletebutton";
    private static final String DEFAULT_PROFILE_BUTTON_STYLE = BUTTON_STYLE + "w-filtertablestate-defaultprofilebutton";
    private static final String DEFAULT_PROFILE_BUTTON_OPEN_STYLE = BUTTON_STYLE + "w-filtertablestate-defaultprofilebutton-open";
    private static final String RESET_BUTTON_STYLE = BUTTON_STYLE + "w-filtertablestate-resetbutton";
    private static final String ADD_BUTTON_STYLE = BUTTON_STYLE + "w-filtertablestate-addbutton";
    private static final String TEXTFIELD_STYLE = "w-filtertablestate-textfield";
    private static final String LAYOUT_STYLE = "v-contextmenu w-filtertablestate-layout";
    private static final String PROFILE_LAYOUT_STYLE = "w-filtertablestate-profile-layout";
    private static final String FUNCTION_LAYOUT_STYLE = "w-filtertablestate-function-layout";
    private static final String COLUMN_LAYOUT_STYLE = "w-filtertablestate-column-layout";
    private static final String PROFILE_ADD_LAYOUT_STYLE = "w-filtertablestate-profile-add-layout";
    private static final String ITEM_STYLE = "w-filtertablestate-item";
    private static final String LABEL_STYLE = "w-filtertablestate-label";
    private static final String SELECTED_STYLE = "v-on";
    private static final String UNSELECTED_STYLE = "v-off";
    private VFilterTable filterTable;
    private Element buttonElement;
    private FilterTableStateRpc rpc = RpcProxy.create(FilterTableStateRpc.class, this);
    private VOverlay overlay;
    private VVerticalLayout layout;
    private VTextField tf;
    private HTML newProfileDiv;

    @Override
    public FilterTableStateSharedState getState() {
        return (FilterTableStateSharedState) super.getState();
    }

    @Override
    protected void extend(ServerConnector target) {
        FilterTableConnector ftc = (FilterTableConnector) target;
        filterTable = ftc.getWidget();

        filterTable.addAttachHandler(this);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            addPopupOpenButton();
        }
    }

    public void addPopupOpenButton() {
        GQuery querySelector = $("." + COLUMN_SELECTOR_STYLE, filterTable);

        buttonElement = DOM.createDiv();
        querySelector.after(buttonElement);
        querySelector.remove();

        buttonElement.addClassName(COLUMN_SELECTOR_STYLE);

        addButtonClickListener(buttonElement);
//        querySelector = $("." + COLUMN_SELECTOR_STYLE_CLASS, filterTable);
//        querySelector.live(Event.ONCLICK, new Function() {
//            @Override
//            public boolean f(Event e) {
//                popupOpenClickListener();
//                return true;
//            }
//        });
    }

    private native void addButtonClickListener(Element el) /*-{
     var self = this; 
     el.onclick = $entry(function() { 
     self.@com.wcs.wcslib.vaadin.widget.filtertablestate.client.FilterTableStateConnector::popupOpenClickListener()();
     }); 
     }-*/;

    private void popupOpenClickListener() {
        createPopupLayout();
        overlay.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                int left = buttonElement.getAbsoluteLeft() - offsetWidth;
                int top = buttonElement.getAbsoluteTop() + buttonElement.getClientHeight();
                overlay.setPopupPosition(left, top);
            }
        });
    }

    private void createPopupLayout() {
        overlay = new VOverlay(true, false, true);
        overlay.setOwner(filterTable);
        layout = new VVerticalLayout();
        layout.setStyleName(LAYOUT_STYLE);

        initClickFunctions();
        initColumnVisibilities();
        initProfileLayout();
        initProfileSaveFields();

        overlay.add(layout);
    }

    private void initClickFunctions() {
        HTML functionMainDiv = new HTML();
        functionMainDiv.addStyleName(FUNCTION_LAYOUT_STYLE);
        if (getState().functions.isEmpty()) {
            return;
        }
        VLabel vLabel = new VLabel(getMsg(FUNCTIONS));
        vLabel.addStyleName(LABEL_STYLE);
        $(functionMainDiv).append($(vLabel));
        layout.add(functionMainDiv);

        for (final ClickFunction function : getState().functions) {

            HTML functionDiv = new HTML();
            functionDiv.addStyleName(ITEM_STYLE);
            functionDiv.setTitle(getMsg(SELECT_FUNCTION));

            InlineHTML functionSpan = new InlineHTML("<div>" + function.getName() + "</div>");

            if (getState().selectedFunctionCode != null && getState().selectedFunctionCode.equals(function.getCode())) {
                functionSpan.addStyleName(SELECTED_STYLE);
            } else {
                functionSpan.addStyleName(UNSELECTED_STYLE);
            }
            $(functionDiv).click(new Function() {
                @Override
                public void f() {
                    overlay.hide();
                    overlay.removeFromParent();
                    rpc.setSelectedFunction(function.getCode());
                }
            });

            $(functionDiv).append($(functionSpan));
            $(functionMainDiv).append($(functionDiv));
        }
    }

    private void initColumnVisibilities() {
        HTML functionMainDiv = new HTML();
        functionMainDiv.addStyleName(COLUMN_LAYOUT_STYLE);

        VLabel vLabel = new VLabel(getMsg(COLUMNS));
        vLabel.addStyleName(LABEL_STYLE);
        $(functionMainDiv).append($(vLabel));
        layout.add(functionMainDiv);

        for (final Action action : filterTable.tHead.getActions()) {
            HTML html = new HTML(action.getHTML());
            html.addStyleName(ITEM_STYLE);

            $(html).click(new Function() {
                @Override
                public void f() {
                    action.execute();
                    overlay.hide();
                    overlay.removeFromParent();
                }
            });
            $(functionMainDiv).append($(html));
        }
    }

    private void initProfileLayout() {
        final HTML profileLayoutDiv = new HTML();
        profileLayoutDiv.setStyleName(PROFILE_LAYOUT_STYLE);
        layout.add(profileLayoutDiv);
        $("." + PROFILE_LAYOUT_STYLE).live(Event.ONMOUSEOVER, new Function() {
            @Override
            public boolean f(Event e) {
                displayBtn(profileLayoutDiv, false);
                return true;
            }
        });
        $("." + PROFILE_LAYOUT_STYLE).live(Event.ONMOUSEOUT, new Function() {
            @Override
            public boolean f(Event e) {
                displayBtn(profileLayoutDiv, true);
                return true;
            }
        });
        VLabel vLabel = new VLabel(getMsg(PROFILES));
        vLabel.addStyleName(LABEL_STYLE);
        $(profileLayoutDiv).append($(vLabel));

        initAddProfileBtn(vLabel, profileLayoutDiv);
        initResetProfileBtn(vLabel, profileLayoutDiv);

        for (final String profile : getState().stateProfiles) {
            HTML profileSpan = initProfileSpan(profile, profileLayoutDiv);

            HTML hideableDiv = new HTML();
            $(hideableDiv).css("display", "inline");
            hideableDiv.addStyleName(HIDEABLE_STYLE);
            
            
            InlineHTML saveProfileBtn = null;
			if (getState().selectedProfile != null
					&& getState().selectedProfile.equals(profile)) {
				saveProfileBtn = initProfileSaveButton(profile,
						profileLayoutDiv);
			}
            
            InlineHTML deleteProfileBtn = initProfileDeleteButton(profile, profileLayoutDiv);
            InlineHTML defaultProfileBtn = initDefaultProfileButton(profile, profileLayoutDiv);
            
            if (saveProfileBtn != null) {
				$(hideableDiv).append($(saveProfileBtn));
			}
            
            $(hideableDiv).append($(deleteProfileBtn));
            $(hideableDiv).append($(defaultProfileBtn));


            HTML profileDiv = new HTML();
            $(profileDiv).append($(profileSpan));
            $(profileDiv).append($(hideableDiv));

            $(profileLayoutDiv).append($(profileDiv));
        }
        displayBtn(profileLayoutDiv, true);
    }

    private void displayBtn(HTML profileLayoutDiv, boolean hide) {
        GQuery hideAble = $("." + HIDEABLE_STYLE, profileLayoutDiv);
        String opacity = hide ? "0" : "1";

        for (Element element : hideAble.elements()) {
            GQuery btns = $("." + BUTTON_STYLE, element);
            btns.css("opacity", opacity);
        }
    }

    private void initAddProfileBtn(VLabel vLabel, HTML layout) {
        InlineHTML addBtn = new InlineHTML();
        addBtn.setStyleName(ADD_BUTTON_STYLE);
        addBtn.setTitle(getMsg(NEW_PROFILE));
        $(addBtn).click(new Function() {
            @Override
            public void f() {
                $(newProfileDiv).slideDown(150).delay(150, new Function() {
                    @Override
                    public void f() {
                        overlay.positionOrSizeUpdated();
                    }
                });
            }
        });
        $(layout).append($(addBtn));
        vLabel.getElement().appendChild(addBtn.getElement());
    }

    private void initResetProfileBtn(VLabel vLabel, HTML layout) {
        InlineHTML resetBtn = new InlineHTML();
        resetBtn.setStyleName(RESET_BUTTON_STYLE);
        resetBtn.setTitle(getMsg(DEFAULT_PROFILE));
        $(resetBtn).click(new Function() {
            @Override
            public void f() {
                overlay.hide();
                overlay.removeFromParent();
                rpc.resetProfile();
            }
        });
        $(layout).append($(resetBtn));
        vLabel.getElement().appendChild(resetBtn.getElement());
    }

    private HTML initProfileSpan(final String profile, HTML layout) {
        HTML profileDiv = new HTML();

        InlineHTML profileSpan = new InlineHTML("<div>" + profile + "</div>");
        profileDiv.addStyleName(ITEM_STYLE);

        profileDiv.setTitle(getMsg(SELECT_PROFILE));
        if (getState().selectedProfile != null && getState().selectedProfile.equals(profile)) {
            profileDiv.addStyleName(SELECTED_STYLE);
        } else {
            profileDiv.addStyleName(UNSELECTED_STYLE);
        }
        $(profileDiv).click(new Function() {
            @Override
            public void f() {
                overlay.hide();
                overlay.removeFromParent();
                rpc.setSelectedProfile(profile);
            }
        });
        $(profileDiv).append($(profileSpan));
        $(layout).append($(profileDiv));
        return profileDiv;
    }
    
    private InlineHTML initProfileSaveButton(final String profile, HTML layout) {
		InlineHTML saveBtn = new InlineHTML();
		saveBtn.setStyleName(RESAVE_BUTTON_STYLE);
		saveBtn.setTitle(getMsg(SAVE_PROFILE));
		$(saveBtn).click(new Function() {
			@Override
			public void f() {
				/*
				 * overlay.hide(); overlay.removeFromParent();
				 */
				rpc.saveProfile(profile, false);
			}
		});
		$(layout).append($(saveBtn));
		return saveBtn;
	}

    private InlineHTML initProfileDeleteButton(final String profile, HTML layout) {
        InlineHTML delBtn = new InlineHTML();
        delBtn.setStyleName(DELETE_BUTTON_STYLE);
        delBtn.setTitle(getMsg(DELETE_PROFILE));
        $(delBtn).click(new Function() {
            @Override
            public void f() {
                overlay.hide();
                overlay.removeFromParent();
                rpc.deleteProfile(profile);
            }
        });
        $(layout).append($(delBtn));
        return delBtn;
    }

    private InlineHTML initDefaultProfileButton(final String profile, HTML layout) {
        InlineHTML defaultProfileBtn = new InlineHTML();
        if (!profile.equals(getState().defaultProfile)) {
            defaultProfileBtn.addStyleName(DEFAULT_PROFILE_BUTTON_OPEN_STYLE);
            defaultProfileBtn.setTitle(getMsg(DEFAULT_PROFILE_ON));
        } else {
            defaultProfileBtn.addStyleName(DEFAULT_PROFILE_BUTTON_STYLE);
            defaultProfileBtn.setTitle(getMsg(DEFAULT_PROFILE_OFF));
        }

        $(defaultProfileBtn).click(new Function() {
            @Override
            public void f() {
                overlay.hide();
                overlay.removeFromParent();
                if (profile.equals(getState().defaultProfile)) {
                    rpc.setDefaultProfile(null);
                } else {
                    rpc.setDefaultProfile(profile);
                }
            }
        });

        $(layout).append($(defaultProfileBtn));
        return defaultProfileBtn;
    }

    private void initProfileSaveFields() {
        newProfileDiv = new HTML();
        newProfileDiv.addStyleName(PROFILE_ADD_LAYOUT_STYLE);
        newProfileDiv.setWidth("100%");

        tf = new VTextField();
        tf.addStyleName(TEXTFIELD_STYLE);
        $(newProfileDiv).append($(tf));

        InlineHTML saveBtn = new InlineHTML();
        saveBtn.setStyleName(SAVE_BUTTON_STYLE);
        saveBtn.setTitle(getMsg(SAVE_PROFILE));

        $(saveBtn).click(new Function() {
            @Override
            public void f() {
                overlay.hide();
                overlay.removeFromParent();
                rpc.saveProfile(tf.getText());
                rpc.setSelectedProfile(tf.getText());
            }
        });

        $(newProfileDiv).append($(saveBtn));
        layout.add(newProfileDiv);
        $(newProfileDiv).slideUp(0);
    }

    private String getMsg(FilterTableStateMessageKey key) {
        return getState().messages.get(key);
    }
}
