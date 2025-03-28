package io.jmix.flowui.kit.meta.component;

import com.vaadin.flow.component.applayout.AppLayout;
import io.jmix.flowui.kit.component.main.ListMenu;
import io.jmix.flowui.kit.component.main.UserIndicator;
import io.jmix.flowui.kit.meta.StudioComponent;
import io.jmix.flowui.kit.meta.StudioProperty;
import io.jmix.flowui.kit.meta.StudioPropertyType;
import io.jmix.flowui.kit.meta.StudioUiKit;

@StudioUiKit
public interface StudioMainViewComponents {

    @StudioComponent(
            name = "ListMenu",
            classFqn = "io.jmix.flowui.component.main.JmixListMenu",
            category = "Main View",
            xmlElement = "listMenu",
            icon = "io/jmix/flowui/kit/meta/icon/mainview/listMenu.svg",
            availablePlaceRegExp = "(^(mainView/appLayout)?((/drawerLayout)|(/navigationBar))$)" +
                    "|(^((mainView/appLayout)?((/drawerLayout)|(/navigationBar)))?(/hasComponents)*$)",
            properties = {
                    @StudioProperty(xmlAttribute = "className", type = StudioPropertyType.VALUES_LIST),
                    @StudioProperty(xmlAttribute = "height", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "id", type = StudioPropertyType.COMPONENT_ID),
                    @StudioProperty(xmlAttribute = "loadMenuConfig", type = StudioPropertyType.BOOLEAN,
                            defaultValue = "false"),
                    @StudioProperty(xmlAttribute = "maxHeight", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "maxWidth", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "metaClass", type = StudioPropertyType.ENTITY_NAME),
                    @StudioProperty(xmlAttribute = "minHeight", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "minWidth", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "visible", type = StudioPropertyType.BOOLEAN,
                            defaultValue = "true"),
                    @StudioProperty(xmlAttribute = "width", type = StudioPropertyType.SIZE)
            }
    )
    ListMenu listMenu();

    @StudioComponent(
            name = "AppLayout",
            classFqn = "com.vaadin.flow.component.applayout.AppLayout",
            category = "Main View",
            xmlElement = "appLayout",
            icon = "io/jmix/flowui/kit/meta/icon/mainview/appLayout.svg",
            availablePlaceRegExp = "^mainView$",
            properties = {
                    @StudioProperty(xmlAttribute = "drawerOpened", type = StudioPropertyType.BOOLEAN,
                            defaultValue = "true"),
                    @StudioProperty(xmlAttribute = "primarySection", type = StudioPropertyType.ENUMERATION,
                            options = {"DRAWER", "NAVBAR"}),
            }
    )
    AppLayout appLayout();

    @StudioComponent(
            name = "UserIndicator",
            classFqn = "io.jmix.flowui.kit.component.main.UserIndicator",
            category = "Main View",
            xmlElement = "userIndicator",
            icon = "io/jmix/flowui/kit/meta/icon/mainview/userIndicator.svg",
            availablePlaceRegExp = "(^(mainView/appLayout)?((/drawerLayout)|(/navigationBar))$)" +
                    "|(^((mainView/appLayout)?((/drawerLayout)|(/navigationBar)))?(/hasComponents)*$)",
            properties = {
                    @StudioProperty(xmlAttribute = "className", type = StudioPropertyType.VALUES_LIST),
                    @StudioProperty(xmlAttribute = "enabled", type = StudioPropertyType.BOOLEAN,
                            defaultValue = "true"),
                    @StudioProperty(xmlAttribute = "height", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "id", type = StudioPropertyType.COMPONENT_ID),
                    @StudioProperty(xmlAttribute = "maxHeight", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "maxWidth", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "minHeight", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "minWidth", type = StudioPropertyType.SIZE),
                    @StudioProperty(xmlAttribute = "title", type = StudioPropertyType.LOCALIZED_STRING),
                    @StudioProperty(xmlAttribute = "visible", type = StudioPropertyType.BOOLEAN,
                            defaultValue = "true"),
                    @StudioProperty(xmlAttribute = "width", type = StudioPropertyType.SIZE)
            }
    )
    UserIndicator userIndicator();
}
