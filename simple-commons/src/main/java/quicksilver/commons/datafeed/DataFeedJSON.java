/*
 * Copyright 2018, 2019, 2020 Niels Gron and Contributors All Rights Reserved.
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
package quicksilver.commons.datafeed;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import tech.tablesaw.io.Source;
import tech.tablesaw.io.json.JsonReadOptions;
import tech.tablesaw.io.json.JsonReader;

public class DataFeedJSON extends DataFeed {

    private final String jsonPath;

    public DataFeedJSON(String baseURLString) {
        this(baseURLString, "");
    }

    public DataFeedJSON(String baseURLString, String jsonPath) {
        super(baseURLString);
        this.jsonPath = jsonPath;
    }

    @Override
    protected void buildDataSet() throws IOException {
        JsonReadOptions.Builder builder = JsonReadOptions.builder(new Source(new ByteArrayInputStream(dataPayload), charset));
        if (!jsonPath.isEmpty()) {
            builder = builder.path(jsonPath);
        }
        dataTable = new JsonReader().read(builder.build());
    }

}
