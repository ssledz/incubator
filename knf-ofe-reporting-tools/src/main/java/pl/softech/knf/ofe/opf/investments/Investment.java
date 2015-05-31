package pl.softech.knf.ofe.opf.investments;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.softech.knf.ofe.shared.Money;

import java.util.Objects;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class Investment {

    private final Instrument instrument;
    private final Money money;

    public Investment(Instrument instrument, double money) {
        this.instrument = Objects.requireNonNull(instrument);
        this.money = new Money(money);
    }

    public Investment(Instrument instrument, long money) {
        this.instrument = Objects.requireNonNull(instrument);
        this.money = new Money(money);
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public Money getMoney() {
        return money;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("instrument", instrument)
                .append("money", money)
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
