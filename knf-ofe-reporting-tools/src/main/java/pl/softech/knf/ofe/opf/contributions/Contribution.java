package pl.softech.knf.ofe.opf.contributions;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.softech.knf.ofe.shared.Money;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class Contribution {

    private final Money amount;
    private final long number;
    private final Money interests;
    private final Money averageBasis;

    public Contribution(Builder builder) {
        this.amount = builder.amount;
        this.number = builder.number;
        this.interests = builder.interests;
        this.averageBasis = builder.averageBasis;
    }

    public Money getAmount() {
        return amount;
    }

    public long getNumber() {
        return number;
    }

    public Money getInterests() {
        return interests;
    }

    public Money getAverageBasis() {
        return averageBasis;
    }


    public Builder toBuilder() {
        return new Builder()
                .withAmount(amount)
                .withInterests(interests)
                .withNumber(number)
                .withAverageBasis(averageBasis);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Contribution)) {
            return false;
        }

        Contribution that = (Contribution) o;

        return new EqualsBuilder()
                .append(amount, that.amount)
                .append(number, that.number)
                .append(interests, that.interests)
                .append(averageBasis, that.averageBasis)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(amount)
                .append(number)
                .append(interests)
                .append(averageBasis)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("amount", amount)
                .append("number", number)
                .append("interests", interests)
                .append("averageBasis", averageBasis)
                .toString();
    }

    public static class Builder {

        private Money amount;
        private long number;
        private Money interests;
        private Money averageBasis;

        public Builder withAmount(long amount) {
            this.amount = new Money(amount);
            return this;
        }

        public Builder withAmount(Money amount) {
            this.amount = amount;
            return this;
        }

        public Builder withAmount(double amount) {
            this.amount = new Money(amount);
            return this;
        }

        public Builder withNumber(long number) {
            this.number = number;
            return this;
        }

        public Builder withInterests(long interests) {
            this.interests = new Money(interests);
            return this;
        }

        public Builder withInterests(double interests) {
            this.interests = new Money(interests);
            return this;
        }

        public Builder withInterests(Money interests) {
            this.interests = interests;
            return this;
        }

        public Builder withAverageBasis(long averageBasis) {
            this.averageBasis = new Money(averageBasis);
            return this;
        }

        public Builder withAverageBasis(double averageBasis) {
            this.averageBasis = new Money(averageBasis);
            return this;
        }

        public Builder withAverageBasis(Money averageBasis) {
            this.averageBasis = averageBasis;
            return this;
        }
    }

}

