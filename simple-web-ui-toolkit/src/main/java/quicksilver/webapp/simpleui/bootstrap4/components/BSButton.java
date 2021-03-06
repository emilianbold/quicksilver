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

package quicksilver.webapp.simpleui.bootstrap4.components;

import quicksilver.webapp.simpleui.HtmlStream;
import quicksilver.webapp.simpleui.html.components.HTMLText;

/*
    Example : <button type="button" class="btn btn-primary btn-sm">Small button</button>

    W3Schools : https://www.w3schools.com/bootstrap4/bootstrap_buttons.asp
    Bootstrap Docs : https://getbootstrap.com/docs/4.1/components/buttons/
 */

public class BSButton extends BSComponentContainer {

    private String id;
    private String text;
    private String hyperlink;
    private boolean isActive;
    private boolean isOutline;
    private boolean isCollapsed;
    private boolean isDropdown;
    private boolean isSplit;

    public BSButton(String text) {
        this(text, BSComponent.Type.PRIMARY);
    }

    public BSButton(String text, BSComponent.Type type) {
        this(text, type, false, (String) null);
    }

    public BSButton(String text, BSComponent.Type type, boolean isOutline, BSCollapse collapse) {
        this(text, type, isOutline, "#" + collapse.getId(), true, false, false);
    }

    public BSButton(String text, BSComponent.Type type, boolean isOutline) {
        this(text, type, isOutline, (String) null);
    }

    public BSButton(String text, BSComponent.Type type, boolean isOutline, String hyperLink) {
        this(text, type, isOutline, hyperLink, false, false, false);
    }

    public BSButton(String text, BSComponent.Type type, boolean isOutline, String hyperLink, boolean isCollapsed, boolean isDropdown, boolean isActive) {
        setType(type);
        this.text = text;
        this.hyperlink = hyperLink;
        this.isActive = isActive;
        this.isCollapsed = isCollapsed;
        this.isOutline = isOutline;
        this.isDropdown = isDropdown;

        add(new HTMLText(text));

    }

    public BSButton(String text, BSComponent.Type type, boolean isOutline, String hyperLink, boolean isCollapsed, boolean isDropdown, boolean isSplit, boolean isActive) {
        setType(type);
        this.text = text;
        this.hyperlink = hyperLink;
        this.isActive = isActive;
        this.isCollapsed = isCollapsed;
        this.isOutline = isOutline;
        this.isDropdown = isDropdown;
        this.isSplit = isSplit;

        add(new HTMLText(text));

    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getText() {
        return text;
    }

    protected void defineAttributes() {

        putComponentAttribute(COMPONENT_ATTRIB_NAME, "Button");
        putComponentAttribute(COMPONENT_ATTRIB_TAG_CLOSE, Boolean.TRUE);
        if ( hyperlink != null ) {
            putComponentAttribute(COMPONENT_ATTRIB_TAG_NAME, "a");
            addTagAttribute("href", hyperlink);
        } else {
            putComponentAttribute(COMPONENT_ATTRIB_TAG_NAME, "button");
            addTagAttribute("type", "button" );
        }

        addTagAttribute("class", getClassNames());

        String style = getStyle();
        if ( style != null ) {
            addTagAttribute("style", style);
        }

        if ( id != null ) {
            addTagAttribute("id", id);
        }

        if (isCollapsed) {
            addTagAttribute("data-toggle", "collapse");
            addTagAttribute("aria-expanded", "false");
            addTagAttribute("aria-controls", "collapseExample");
        } else if ( isDropdown ) {
            addTagAttribute("data-toggle", "dropdown");
            addTagAttribute("aria-haspopup", "true");
            addTagAttribute("aria-expanded", "false");
        }

    }

    protected String getClassNames() {
        StringBuilder cNames = new StringBuilder();

        cNames.append("btn");

        if ( isOutline ) {
            cNames.append(" btn-outline-").append(getType().getTypeName());
        } else {
            cNames.append(" btn-").append(getType().getTypeName());
        }
        if ( getSize() == Size.SMALL ) {
            cNames.append(" btn-sm");
        } else if ( getSize() == Size.LARGE ) {
            cNames.append(" btn-lg");
        }
        if ( isDropdown ) {
            cNames.append(" dropdown-toggle");
        }
        if ( isSplit ) {
            cNames.append(" dropdown-toggle-split");
        }
        if ( isActive ) {
            cNames.append(" active");
        }

        return cNames.toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

}
