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

import java.util.Date;

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

    private NumberOfAccounts numberOfAccounts;

    private Contribution contribution;

    public OpenPensionFund(Builder builder) {

        this.key = new Key(builder.name, builder.date);

        this.numberOfMembers = builder.numberOfMembers;
        this.numberOfAccounts = builder.numberOfAccounts;
        if (builder.contribution != null) {
            this.contribution = new Contribution(builder.contribution);
        }

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

    public Date getDate() {
        return new Date(key.date.getTime());
    }

    public NumberOfAccounts getNumberOfAccounts() {
        return numberOfAccounts;
    }

    public Contribution getContribution() {
        return contribution;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", key.name)
                .append("date", key.date)
                .append("numberOfMembers", numberOfMembers)
                .append(numberOfAccounts)
                .append(contribution)
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
        private Date date;

        private NumberOfAccounts numberOfAccounts;
        private Contribution.Builder contribution;

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
