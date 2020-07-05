/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/ .
 */
package com.github.crashdemons.playerheads.testutils;

/**
 *
 * @author crashdemons (crashenator at gmail.com)
 */
public class TestOutput {
    private final String prefix;
    public TestOutput(Object obj){
        prefix = obj.getClass().getSimpleName();
    }
    public void println(String s){
        System.out.println(prefix+": "+s);
    }
}
