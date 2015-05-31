package pl.softech.knf.ofe.shared;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class Money {

    private static final int DEFAULT_SCALE = 10000;

    private final long value;
    private final int scale;

    public Money(long value, int scale) {
        this.value = value;
        this.scale = scale;
    }

    public Money(long value) {
        this(value, DEFAULT_SCALE);
    }

    public Money(double value) {
        this((long) (value * DEFAULT_SCALE), DEFAULT_SCALE);
    }

    public long getValue() {
        return value;
    }

    public double getValueAsDouble() {
        return (double) value / DEFAULT_SCALE;
    }

    public int getScale() {
        return scale;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("value", value)
                .append("scale", scale)
                .toString();
    }
}
