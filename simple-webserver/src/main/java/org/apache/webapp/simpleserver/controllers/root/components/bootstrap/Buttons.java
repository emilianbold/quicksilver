/*
 * Copyright 2018 Niels Gron and Contributors All Rights Reserved.
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

package org.apache.webapp.simpleserver.controllers.root.components.bootstrap;

import org.apache.webapp.simpleui.bootstrap4.components.*;
import org.apache.webapp.simpleui.html.components.HTMLHeading;
import org.apache.webapp.simpleui.html.components.HTMLLineBreak;

public class Buttons extends AbstractComponentsBootstrapPage {

    public Buttons() {
        getSideBar().setActiveItem("Buttons");
    }

    protected BSPanel createContentPanelCenter() {

        BSPanel panel = new BSPanel();

        panel.add(new HTMLLineBreak(1));
        panel.add(new BSText("List of Buttons Components"));
        panel.add(new HTMLLineBreak(2));

        panel.add(new HTMLHeading("Example Buttons", 3));

        panel.add(new BSButton("Primary", BSComponent.Type.PRIMARY));
        panel.add(new BSButton("Secondary", BSComponent.Type.SECONDARY));
        panel.add(new BSButton("Success", BSComponent.Type.SUCCESS));
        panel.add(new BSButton("Danger", BSComponent.Type.DANGER));
        panel.add(new BSButton("Warning", BSComponent.Type.WARNING));
        panel.add(new BSButton("Info", BSComponent.Type.INFO));
        panel.add(new BSButton("Light", BSComponent.Type.LIGHT));
        panel.add(new BSButton("Dark", BSComponent.Type.DARK));
        panel.add(new BSButton("Link", BSComponent.Type.LINK));

        panel.add(new HTMLLineBreak(2));

        panel.add(new HTMLHeading("Outline Buttons", 3));

        panel.add(new BSButton("Primary", BSComponent.Type.PRIMARY, true, null));
        panel.add(new BSButton("Secondary", BSComponent.Type.SECONDARY, true, null));
        panel.add(new BSButton("Success", BSComponent.Type.SUCCESS, true, null));
        panel.add(new BSButton("Danger", BSComponent.Type.DANGER, true, null));
        panel.add(new BSButton("Warning", BSComponent.Type.WARNING, true, null));
        panel.add(new BSButton("Info", BSComponent.Type.INFO, true, null));
        panel.add(new BSButton("Light", BSComponent.Type.LIGHT, true, null));
        panel.add(new BSButton("Dark", BSComponent.Type.DARK, true, null));
        panel.add(new BSButton("Link", BSComponent.Type.LINK, true, null));

        return panel;
    }

}
