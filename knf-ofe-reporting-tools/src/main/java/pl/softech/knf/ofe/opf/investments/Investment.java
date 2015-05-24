package pl.softech.knf.ofe.opf.investments;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class Investment {

    private final Instrument instrument;
    private final long value;

    public Investment(Instrument instrument, long value) {
        this.instrument = instrument;
        this.value = value;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("instrument", instrument)
                .append("value", value)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Investment)) {
            return false;
        }

        Investment that = (Investment) o;

        return new EqualsBuilder()
                .append(instrument, that.instrument)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(instrument)
                .toHashCode();
    }
}
