package org.oaknorth.view;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Heerok on 31-01-2017.
 */
public class BatchImportDataMapping {
    private static Map<String, String> fieldmap = new HashMap<>();

    static {
        fieldmap.put("Company Name", "Company Name");
    }

    public static Map<String, String> getFieldmap() {
        return fieldmap;
    }

}
