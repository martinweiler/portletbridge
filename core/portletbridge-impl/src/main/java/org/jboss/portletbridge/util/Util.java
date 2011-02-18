/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.portletbridge.util;

import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.application.Application;

/**
 *
 * @author Waldemar Kłaczyński
 */
public class Util {

    private static final Map<String, Pattern> patternCache =
            new LRUMap<String, Pattern>(15);

    /**
     * <p>A slightly more efficient version of
     * <code>String.split()</code> which caches
     * the <code>Pattern</code>s in an LRUMap instead of
     * creating a new <code>Pattern</code> on each
     * invocation.</p>
     * @param toSplit the string to split
     * @param regex the regex used for splitting
     * @return the result of <code>Pattern.spit(String, int)</code>
     */
    public synchronized static String[] split(String toSplit, String regex) {
        Pattern pattern = patternCache.get(regex);
        if (pattern == null) {
            pattern = Pattern.compile(regex);
            patternCache.put(regex, pattern);
        }
        return pattern.split(toSplit, 0);
    }

    public static int compareVersion(String v1, String v2) {
        if (v1 == null && v2 == null) {
            return 0;
        } else if (v1 == null) {
            return -1;
        } else if (v2 == null) {
            return 1;
        }

        String[] arr1 = v1.split("[^a-zA-Z0-9]+"),
                arr2 = v2.split("[^a-zA-Z0-9]+");

        int i1, i2, i3;

        for (int ii = 0, max = Math.min(arr1.length, arr2.length); ii <= max; ii++) {
            if (ii == arr1.length) {
                return ii == arr2.length ? 0 : -1;
            } else if (ii == arr2.length) {
                return 1;
            }

            try {
                i1 = Integer.parseInt(arr1[ii]);
            } catch (Exception x) {
                i1 = Integer.MAX_VALUE;
            }

            try {
                i2 = Integer.parseInt(arr2[ii]);
            } catch (Exception x) {
                i2 = Integer.MAX_VALUE;
            }

            if (i1 != i2) {
                return i1 - i2;
            }

            i3 = arr1[ii].compareTo(arr2[ii]);

            if (i3 != 0) {
                return i3;
            }
        }

        return 0;
    }

    public static int compareCurrentJSFVersion(String version) {
        String current = Application.class.getPackage().getImplementationVersion();
        return compareVersion(current, version);
    }

}