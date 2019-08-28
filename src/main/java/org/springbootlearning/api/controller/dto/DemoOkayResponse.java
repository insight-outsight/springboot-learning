package org.springbootlearning.api.controller.dto;

import java.util.Map;

import com.google.common.collect.Maps;

public class DemoOkayResponse extends BaseOkayResponse<Map<String, String>> {

    public static void main(String[] args) {
        DemoOkayResponse s = new DemoOkayResponse();
        Map<String, String> m = Maps.newHashMap();
        m.put("k1", "v1");
        m.put("k3", "v2325");
        s.setResData(m);
        System.out.println(s);
    }

}
