package org.michaelbel.ui.view;

import android.support.annotation.IntDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@IntDef({
    EmptyViewMode.MODE_NO_CONNECTION,
    EmptyViewMode.MODE_NO_SHOWS,
    EmptyViewMode.MODE_NO_RESULTS
})
@Documented
@EmptyViewMode
public @interface EmptyViewMode {
    int MODE_NO_CONNECTION = 0;
    int MODE_NO_SHOWS = 1;
    int MODE_NO_RESULTS = 2;
}