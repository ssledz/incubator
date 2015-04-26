package pl.softech.knf.ofe.opf.accounts;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by ssledz on 25.04.15.
 */
public class NumberOfAccount {

    private final long total;
    private final long inactive;

    public NumberOfAccount(long total, long inactive) {
        this.total = total;
        this.inactive = inactive;
    }

    public long getTotal() {
        return total;
    }

    public long getInactive() {
        return inactive;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("total", total)
                .append("inactive", inactive)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof NumberOfAccount)) {
            return false;
        }

        NumberOfAccount that = (NumberOfAccount) o;

        return new EqualsBuilder()
                .append(total, that.total)
                .append(inactive, that.inactive)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(total)
                .append(inactive)
                .toHashCode();
    }
}
