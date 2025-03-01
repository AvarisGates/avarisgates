package com.avaris.avarisgates.core.player;


import net.fabricmc.fabric.api.attachment.v1.AttachmentType;

public abstract class PlayerResource {
    protected long value = 0;
    protected long maxValue = 100;

    public long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long max_value) {
        this.maxValue = max_value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
