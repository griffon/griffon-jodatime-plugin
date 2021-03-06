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
import griffon.core.resources.formatters.Formatter;
import griffon.plugins.jodatime.formatters.DateTimeFormatter;
import org.joda.time.DateMidnight;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.Date;

import static griffon.util.GriffonNameUtils.isBlank;

/**
 * @author Andres Almiray
 */
public class LocalDateTimePropertyEditor extends AbstractPropertyEditor {
    protected void setValueInternal(Object value) {
        if (null == value) {
            super.setValueInternal(null);
        } else if (value instanceof LocalDateTime) {
            super.setValueInternal(value);
        } else if (value instanceof LocalDate) {
            super.setValueInternal(((LocalDate) value).toDateTimeAtStartOfDay());
        } else if (value instanceof LocalTime) {
            super.setValueInternal(((LocalTime) value).toDateTimeToday());
        } else if (value instanceof DateMidnight) {
            super.setValueInternal(((DateMidnight) value).toDateTime().toLocalDateTime());
        } else if (value instanceof CharSequence) {
            handleAsString(String.valueOf(value));
        } else if (value instanceof Calendar) {
            super.setValueInternal(LocalDateTime.fromCalendarFields((Calendar) value));
        } else if (value instanceof Date) {
            super.setValueInternal(LocalDateTime.fromDateFields((Date) value));
        } else if (value instanceof Number) {
            super.setValueInternal(new LocalDateTime(((Number) value).longValue()));
        } else {
            throw illegalValue(value, LocalDateTime.class);
        }
    }

    private void handleAsString(String str) {
        if (isBlank(str)) {
            super.setValueInternal(null);
            return;
        }

        try {
            super.setValueInternal(new LocalDateTime(Long.parseLong(str)));
            return;
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            // ignore
        }

        try {
            super.setValueInternal(LocalDateTime.parse(str));
        } catch (IllegalArgumentException e) {
            throw illegalValue(str, LocalDateTime.class, e);
        }
    }

    protected Formatter resolveFormatter() {
        return isBlank(getFormat()) ? null : new DateTimeFormatter(getFormat());
    }
}