/*
 * Copyright 2018-2019 Niels Gron and Contributors All Rights Reserved.
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

package quicksilver.webapp.simpleserver.controllers.root.components.charts;

import quicksilver.webapp.simpleserver.controllers.components.toolbar.TBarCharts;
import quicksilver.webapp.simpleserver.controllers.root.components.AbstractComponentsPage;
import quicksilver.webapp.simpleui.bootstrap4.components.BSNavbarToolbarContainer;
import quicksilver.webapp.simpleui.bootstrap4.components.BSPanel;

public abstract class AbstractComponentsChartsPage extends AbstractComponentsPage {

    protected TBarCharts toolbar;

    public AbstractComponentsChartsPage() {
        getComponentNavTab().setActiveItem("Charts");
    }

    protected BSPanel createContentPanelTop() {
        toolbar = new TBarCharts();
        BSPanel panel = super.createContentPanelTop();
        panel.add(new BSNavbarToolbarContainer(toolbar));
        return panel;
    }

}
