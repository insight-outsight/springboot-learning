package org.springbootlearning.api.service.es;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ESEntity {

    @JsonIgnore
    protected String id;
    protected String routingKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

}
