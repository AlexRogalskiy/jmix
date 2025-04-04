/*
 * Copyright 2021 Haulmont.
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

package io.jmix.multitenancyui.screen.tenant;

import io.jmix.core.EntityStates;
import io.jmix.multitenancy.entity.Tenant;
import io.jmix.ui.component.TextField;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("mten_Tenant.edit")
@UiDescriptor("tenant-edit.xml")
@EditedEntityContainer("tenantDc")
public class TenantEdit extends StandardEditor<Tenant> {

    @Autowired
    private TextField<String> tenantIdField;

    @Autowired
    private EntityStates entityStates;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        tenantIdField.setEditable(entityStates.isNew(getEditedEntity()));
    }
}