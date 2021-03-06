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

package quicksilver.webapp.simpleserver.controllers.root.components.bootstrap;

import quicksilver.webapp.simpleui.bootstrap4.components.*;
import quicksilver.webapp.simpleui.html.components.HTMLHeading;

public class Card extends AbstractComponentsBootstrapPage {

    public Card() {
        getSideBar().setActiveItem("Card");
    }

    protected BSPanel createContentPanelCenter() {

        BSPanel panel = new BSPanel();

        panel.add(new BSText("<br>"));
        panel.add(new BSText("List of Card Components"));
        panel.add(new BSText("<br>"));
        panel.add(new HTMLHeading("Body", 3));
        panel.add(new BSCard()
                    .body(new BSText("Card text"))
                    .style("width: 18rem;"));

        panel.add(new HTMLHeading("Titles, text, and links", 3));
        panel.add(new BSCard()
                .body("Card title", "Subtitle",
                        "The card text which looks much better if it actually fills a few lines.",
                        "#", "Card Link", "#", "Another link")
                .style("width: 18rem;"));

        panel.add(new HTMLHeading("Images", 3));
        panel.add(new BSCard()
            .image("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Vincent_van_Gogh_-_Self-Portrait_-_Google_Art_Project_%28454045%29.jpg/190px-Vincent_van_Gogh_-_Self-Portrait_-_Google_Art_Project_%28454045%29.jpg", "Wikipedia")
            .body("Van Gogh")
            .style("width: 18rem;"));

        panel.add(new HTMLHeading("List groups", 3));
        panel.add(new BSCard()
                .listGroup("Hello", "List", "World")
                .style("width: 18rem;"));

        panel.add(new BSText("<br>"));

        panel.add(new BSCard()
                .header("Featured")
                .listGroup("Hello", "List", "World")
                .style("width: 18rem;"));

        panel.add(new HTMLHeading("Header and footer", 3));
        panel.add(new BSCard()
                .header("Featured")
                .body("Special title", null, "Some content to display with more words.")
                .body(new BSButton("Click me"), new BSButton("Or me", BSComponent.Type.SECONDARY)));
        return panel;
    }

}
