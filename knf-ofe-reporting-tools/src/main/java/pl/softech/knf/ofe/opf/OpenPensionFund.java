/*
 * Copyright 2015 Sławomir Śledź <slawomir.sledz@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.softech.knf.ofe.opf;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.softech.knf.ofe.opf.accounts.NumberOfAccounts;
import pl.softech.knf.ofe.opf.contributions.Contribution;
import pl.softech.knf.ofe.opf.investments.Investment;
import pl.softech.knf.ofe.shared.Money;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import static java.util.Objects.requireNonNull;

/**
 * <class>OpenPensionFund is fully immutable</class>
 *
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class OpenPensionFund {

    private Key key;

    private long numberOfMembers;

    private Money netAssets;

    private Money accountingUnitValue;

    private NumberOfAccounts numberOfAccounts;

    private Contribution contribution;

    private Collection<Investment> investmens;

    public OpenPensionFund(Builder builder) {

        this.key = new Key(builder.name, builder.date);

        this.numberOfMembers = builder.numberOfMembers;
        this.numberOfAccounts = builder.numberOfAccounts;
        if (builder.contribution != null) {
            this.contribution = new Contribution(builder.contribution);
        }

        this.investmens = builder.investmens;
        this.netAssets = builder.netAssets;
        this.accountingUnitValue = builder.accountingUnitValue;

    }

    public Collection<Investment> getInvestmens() {
        return Collections.unmodifiableCollection(investmens);
    }

    public Key getKey() {
        return key;
    }

    public String getName() {
        return key.name;
    }

    public long getNumberOfMembers() {
        return numberOfMembers;
    }

    public Money getNetAssets() {
        return netAssets;
    }

    public Date getDate() {
        return new Date(key.date.getTime());
    }

    public NumberOfAccounts getNumberOfAccounts() {
        return numberOfAccounts;
    }

    public Contribution getContribution() {
        return contribution;
    }

    public Money getAccountingUnitValue() {
        return accountingUnitValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", key.name)
                .append("date", key.date)
                .append("numberOfMembers", numberOfMembers)
                .append("netAssets", netAssets)
                .append("accountingUnitValue", accountingUnitValue)
                .append(numberOfAccounts)
                .append(contribution)
                .append(investmens)
                .toString();
    }

    public static final class Key {

        private final String name;
        private final Date date;

        public Key(String name, Date date) {
            this.name = requireNonNull(name);
            this.date = new Date(requireNonNull(date).getTime());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (!(o instanceof Key)) {
                return false;
            }

            Key key = (Key) o;

            return new EqualsBuilder()
                    .append(name, key.name)
                    .append(date, key.date)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(name)
                    .append(date)
                    .toHashCode();
        }
    }

    public static final class Builder {

        private String name;
        private long numberOfMembers;
        private Money accountingUnitValue;
        private Money netAssets;
        private Date date;

        private NumberOfAccounts numberOfAccounts;
        private Contribution.Builder contribution;

        private Collection<Investment> investmens = new LinkedList<>();

        public Builder addInvestment(Investment investment) {
            investmens.add(investment);
            return this;
        }

        public Builder addInvestments(Collection<Investment> investments) {
            investmens.addAll(investments);
            return this;
        }

        public Builder withAccountingUnitValue(Money accountingUnitValue) {
            this.accountingUnitValue = accountingUnitValue;
            return this;
        }

        public Builder withAccountingUnitValue(long accountingUnitValue) {
            this.accountingUnitValue = new Money(accountingUnitValue);
            return this;
        }

        public Builder withAccountingUnitValue(double accountingUnitValue) {
            this.accountingUnitValue = new Money(accountingUnitValue);
            return this;
        }

        public Builder withNetAssets(double netAssets) {
            this.netAssets = new Money(netAssets);
            return this;
        }

        public Builder withNetAssets(long netAssets) {
            this.netAssets = new Money(netAssets);
            return this;
        }

        public Builder withNetAssets(Money netAssets) {
            this.netAssets = netAssets;
            return this;
        }

        public Builder withContribution(Contribution.Builder contribution) {
            this.contribution = contribution;
            return this;
        }

        public Builder withNumberOfAccount(long total, long inactive) {
            this.numberOfAccounts = new NumberOfAccounts(total, inactive);
            return this;
        }

        public Builder withKey(Key key) {
            this.name = key.name;
            this.date = key.date;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withNumberOfMembers(long numberOfMembers) {
            this.numberOfMembers = numberOfMembers;
            return this;
        }

        public Builder withDate(Date date) {
            this.date = date;
            return this;
        }
    }

}
