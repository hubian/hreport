package org.hubian.hreport.client.model;


import lombok.Data;

/**
 * Created by masen on 2017/1/12.
 */
@Data
public class HReporter {
    public HReporter(String name, String ip){
        this.name=name;
        this.ip=ip;
    }
    private String name;
    private String ip;
}
