/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.plugins.jodatime.editors;

import griffon.core.resources.editors.AbstractPropertyEditor;
import org.joda.time.Days;

import static griffon.util.GriffonNameUtils.isBlank;
import static java.lang.Math.abs;

/**
 * @author Andres Almiray
 */
public class DaysPropertyEditor extends AbstractPropertyEditor {
    protected void setValueInternal(Object value) {
        if (null == value) {
            super.setValueInternal(null);
        } else if (value instanceof CharSequence) {
            handleAsString(String.valueOf(value));
        } else if (value instanceof Days) {
            super.setValueInternal(value);
        } else if (value instanceof Number) {
            super.setValueInternal(parse((Number) value));
        } else {
            throw illegalValue(value, Days.class);
        }
    }

    protected String getAsTextInternal() {
        return getValue() == null ? "" : String.valueOf(((Days) getValue()).getDays());
    }

    public String getFormattedValue() {
        return getValue() == null ? "" : getValue().toString();
    }

    private void handleAsString(String str) {
        if (isBlank(str)) {
            super.setValueInternal(null);
            return;
        }

        try {
            super.setValueInternal(parse(Integer.parseInt(str)));
            return;
        } catch (NumberFormatException nfe) {
            // ignore
        }

        try {
            super.setValueInternal(Days.parseDays(str));
        } catch (IllegalArgumentException e) {
            throw illegalValue(str, Days.class, e);
        }
    }

    private Days parse(Number number) {
        return Days.days(abs(number.intValue()));
    }
}
