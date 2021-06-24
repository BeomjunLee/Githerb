package com.githerb.domain.plant.type;

import lombok.Getter;

@Getter
public enum FrontNameType {
    SMART("똑똑한"), GREEN("푸릇한"), RED("빨간");

    public String type;

    FrontNameType(String type) {
        this.type = type;
    }

}
