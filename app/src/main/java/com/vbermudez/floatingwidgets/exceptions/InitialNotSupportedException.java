package com.vbermudez.floatingwidgets.exceptions;

import androidx.annotation.Nullable;

public class InitialNotSupportedException extends Exception {
    public InitialNotSupportedException() {
        super("The requested initial letter package is not supported.");
    }
}
