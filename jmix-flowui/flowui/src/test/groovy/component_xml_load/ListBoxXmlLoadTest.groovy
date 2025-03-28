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

package component_xml_load

import component_xml_load.screen.ListBoxView
import io.jmix.core.DataManager
import io.jmix.core.SaveContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import test_support.entity.sales.Order
import test_support.spec.FlowuiTestSpecification

@SpringBootTest
class ListBoxXmlLoadTest extends FlowuiTestSpecification {

    @Autowired
    DataManager dataManager

    @Autowired
    JdbcTemplate jdbcTemplate

    @Override
    void setup() {
        registerScreenBasePackages("component_xml_load.screen")

        def saveContext = new SaveContext()
        def order = dataManager.create(Order)
        def order1 = dataManager.create(Order)

        saveContext.saving(order, order1)
        dataManager.save(saveContext)
    }

    @Override
    void cleanup() {
        jdbcTemplate.execute("delete from TEST_ORDER")
    }

    def "Load ListBox component from XML"() {
        when: "Open the ComponentView"
        def listBoxView = openScreen(ListBoxView)

        then: "ListBox attributes will be loaded"
        verifyAll(listBoxView.listBox) {
            id.get() == "listBox"
            classNames.containsAll(["className", "className1"])
            enabled
            height == "100%"
            maxHeight == "200px"
            maxWidth == "200px"
            minHeight == "100px"
            minWidth == "100px"
            readOnly
            visible
            width == "100%"
        }
    }

    def "Load MultiSelectListBox component from XML"() {
        when: "Open the ComponentView"
        def listBoxView = openScreen(ListBoxView)

        then: "MultiSelectListBox attributes will be loaded"
        verifyAll(listBoxView.multiSelectListBox) {
            id.get() == "multiSelectListBox"
            classNames.containsAll(["className", "className1"])
            enabled
            height == "100%"
            maxHeight == "200px"
            maxWidth == "200px"
            minHeight == "100px"
            minWidth == "100px"
            readOnly
            visible
            width == "100%"
        }
    }
}
