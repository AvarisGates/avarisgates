package com.avaris.avarisgates.core.player;


import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public abstract class PlayerResource {
    protected Long value = 0L;
    protected Long maxValue = 100L;

    public Long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long max_value) {
        this.maxValue = max_value;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
