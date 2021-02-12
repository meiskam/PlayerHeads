/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.compatibility.exceptions;

/**
 * Exception indicating that the Compatibility library project was misconfigured
 * when it was built, not correctly finalizing support information.
 * 
 * This generally means that support providers (CompatibilityProvider
 * implementations) were not indicated to have been added.
 * 
 * @author crashdemons (crashenator at gmail.com)
 */
public class CompatibilityMisconfiguredException extends CompatibilityException {

    /**
     * Constructor for misconfiguration exceptions
     * @param s the string message for the exception
     * @param e the exception cause
     */
    public CompatibilityMisconfiguredException(String s, Exception e) {
        super(s, e);
    }

    /**
     * Constructor for misconfiguration exceptions
     * @param s the string message for the exception
     */
    public CompatibilityMisconfiguredException(String s) {
        super(s);
    }
}
