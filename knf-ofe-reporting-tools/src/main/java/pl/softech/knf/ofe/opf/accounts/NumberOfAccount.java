package pl.softech.knf.ofe.opf.accounts;

import org.apache.commons.lang3.builder.ToStringBuilder;

import static java.util.Objects.requireNonNull;

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
}
