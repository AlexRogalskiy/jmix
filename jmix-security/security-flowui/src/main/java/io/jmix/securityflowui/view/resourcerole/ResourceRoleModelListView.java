/*
 * Copyright 2022 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.securityflowui.view.resourcerole;

import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.Action;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.util.RemoveOperation;
import io.jmix.flowui.view.*;
import io.jmix.security.role.ResourceRoleRepository;
import io.jmix.securitydata.entity.RoleAssignmentEntity;
import io.jmix.securityflowui.component.rolefilter.RoleFilter;
import io.jmix.securityflowui.component.rolefilter.RoleFilterChangeEvent;
import io.jmix.securityflowui.model.BaseRoleModel;
import io.jmix.securityflowui.model.ResourceRoleModel;
import io.jmix.securityflowui.model.RoleModelConverter;
import io.jmix.securityflowui.model.RoleSource;
import io.jmix.securityflowui.util.RemoveRoleConsumer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "resourcerolemodels", layout = DefaultMainViewParent.class)
@UiController("sec_ResourceRoleModel.list")
@UiDescriptor("resource-role-model-list-view.xml")
@LookupComponent("roleModelsTable")
@DialogMode(width = "50em", height = "37.5em")
public class ResourceRoleModelListView extends StandardListView<ResourceRoleModel> {

    @ComponentId
    private DataGrid<ResourceRoleModel> roleModelsTable;
    @ComponentId
    private HorizontalLayout buttonsPanel;

    @ComponentId("roleModelsTable.exportJSON")
    private Action exportJSON;
    @ComponentId("roleModelsTable.exportZIP")
    private Action exportZIP;

    @ComponentId
    private CollectionContainer<ResourceRoleModel> roleModelsDc;

    @Autowired
    private Messages messages;
    @Autowired
    private MessageBundle messageBundle;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Notifications notifications;
    @Autowired
    private RemoveOperation removeOperation;
    @Autowired
    private RoleModelConverter roleModelConverter;
    @Autowired
    private ResourceRoleRepository roleRepository;

    @Subscribe
    public void onInit(InitEvent event) {
        initFilter();
//        initExportMenu();
    }

    private void initFilter() {
        RoleFilter filter = uiComponents.create(RoleFilter.class);
        filter.addRoleFilterChangeListener(this::onRoleFilterChange);

        getContent().addComponentAsFirst(filter);
    }

    private void onRoleFilterChange(RoleFilterChangeEvent event) {
        loadRoles(event);
    }

    private void initExportMenu() {
        MenuBar exportMenuBar = new MenuBar();
        buttonsPanel.add(exportMenuBar);

        MenuItem rootItem = exportMenuBar.addItem(VaadinIcon.DOWNLOAD.create());
        rootItem.add(messageBundle.getMessage("exportMenu.text"));

        SubMenu exportItems = rootItem.getSubMenu();
        exportItems.addItem(exportJSON.getText(), event -> exportJSON.actionPerform(null));
        exportItems.addItem(exportZIP.getText(), event -> exportZIP.actionPerform(null));
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        loadRoles(null);
    }

    private void loadRoles(@Nullable RoleFilterChangeEvent event) {
        List<ResourceRoleModel> roleModels = roleRepository.getAllRoles().stream()
                .filter(role -> event == null || event.matches(role))
                .map(roleModelConverter::createResourceRoleModel)
                .sorted(Comparator.comparing(ResourceRoleModel::getName))
                .collect(Collectors.toList());
        roleModelsDc.setItems(roleModels);
    }

    @Install(to = "roleModelsTable.create", subject = "routeParametersProvider")
    public RouteParameters roleModelsTableCreateRouteParametersProvider() {
        return new RouteParameters(ResourceRoleModelDetailView.ROUTE_PARAM_NAME, StandardDetailView.NEW_ENTITY_ID);
    }

    @Install(to = "roleModelsTable.edit", subject = "routeParametersProvider")
    public RouteParameters roleModelsTableEditRouteParametersProvider() {
        ResourceRoleModel selectedItem = roleModelsTable.getSingleSelectedItem();
        if (selectedItem != null) {
            return new RouteParameters(ResourceRoleModelDetailView.ROUTE_PARAM_NAME, selectedItem.getCode());
        }

        return null;
    }

    @Subscribe("roleModelsTable.remove")
    public void onRoleModelsTableRemove(ActionPerformedEvent event) {
        removeOperation.builder(roleModelsTable)
                .withConfirmation(true)
                .beforeActionPerformed(new RemoveRoleConsumer<>(roleRepository, notifications, messages))
                .afterActionPerformed((afterActionConsumer) -> {
                    List<RoleAssignmentEntity> roleAssignmentEntities = dataManager.load(RoleAssignmentEntity.class)
                            .query("e.roleCode IN :codes")
                            .parameter("codes", afterActionConsumer.getItems().stream()
                                    .map(BaseRoleModel::getCode)
                                    .collect(Collectors.toList()))
                            .list();
                    dataManager.remove(roleAssignmentEntities);
                })
                .remove();
    }

    @Install(to = "roleModelsTable.remove", subject = "enabledRule")
    private boolean roleModelsTableRemoveEnabledRule() {
        return isDatabaseRoleSelected();
    }

    private boolean isDatabaseRoleSelected() {
        Set<ResourceRoleModel> selected = roleModelsTable.getSelectedItems();
        if (selected.size() == 1) {
            ResourceRoleModel roleModel = selected.iterator().next();
            return RoleSource.DATABASE.equals(roleModel.getSource());
        }

        return false;
    }

    @Subscribe("roleModelsTable.exportJSON")
    public void onRoleModelsTableExportJSON(ActionPerformedEvent event) {
//        export(JSON);
    }

    @Subscribe("roleModelsTable.exportZIP")
    public void onRoleModelsTableExportZIP(ActionPerformedEvent event) {
//        export(ZIP);
    }
}
