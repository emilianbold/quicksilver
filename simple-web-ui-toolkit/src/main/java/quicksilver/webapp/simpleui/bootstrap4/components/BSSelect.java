/*
 * Copyright 2018-2020 Niels Gron and Contributors All Rights Reserved.
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

/*
    Example :

    W3Schools :
    Bootstrap Docs : https://getbootstrap.com/docs/4.2/components/forms/
 */

public class BSSelect extends BSComponentContainer {

    private final boolean isMultiple;
    private final String id;
    private Boolean validated = null;

    public BSSelect() {
        this(null, false);
    }

    public BSSelect(boolean isMultiple) {
        this(null, isMultiple);
    }

    public BSSelect(String id, boolean isMultiple) {
        this.id = id;
        this.isMultiple = isMultiple;
    }

    @Override
    protected void defineAttributes() {

        putComponentAttribute(COMPONENT_ATTRIB_NAME, "Select");
        putComponentAttribute(COMPONENT_ATTRIB_TAG_CLOSE, Boolean.TRUE);

        if ( isMultiple ) {
            putComponentAttribute(COMPONENT_ATTRIB_TAG_NAME, "select multiple");
        } else {
            putComponentAttribute(COMPONENT_ATTRIB_TAG_NAME, "select");
        }

        addTagAttribute("class", getClassNames());

        if (id != null) {
            addTagAttribute("id", id);
            //TODO: perhaps allow user to set another name?
            addTagAttribute("name", id);
        }

    }

    @Override
    protected String getClassNames() {
        StringBuilder cNames = new StringBuilder();

        cNames.append("form-control");
        if (getSize() != Size.NORMAL) {
            cNames.append(" form-control-").append(getSize().getSizeName());
        }

        if (validated != null) {
            cNames.append(validated ? " is-valid" : " is-invalid");
        }

        return cNames.toString();
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

}
