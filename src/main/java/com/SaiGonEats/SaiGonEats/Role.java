package com.SaiGonEats.SaiGonEats;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    ADMIN(1),
    EMPLOYEE(2),
    USER(3);

    public final long value;
}
