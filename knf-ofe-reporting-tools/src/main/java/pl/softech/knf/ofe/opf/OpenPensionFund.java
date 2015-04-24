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

import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.softech.knf.ofe.opf.accounts.NumberOfAccount;

import static java.util.Objects.requireNonNull;

import java.util.Date;

/**
 * <class>OpenPensionFund is fully immutable</class>
 *
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class OpenPensionFund {

    private String name;
    private long numberOfMembers;
    private Date date;

    private NumberOfAccount numberOfAccount;

    public OpenPensionFund(Builder builder) {
        this.name = requireNonNull(builder.name);
        this.date = new Date(requireNonNull(builder.date).getTime());

        this.numberOfMembers = builder.numberOfMembers;
        this.numberOfAccount = builder.numberOfAccount;

    }

    public String getName() {
        return name;
    }

    public long getNumberOfMembers() {
        return numberOfMembers;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public NumberOfAccount getNumberOfAccount() {
        return numberOfAccount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("numberOfMembers", numberOfMembers)
                .append("date", date)
                .toString();
    }

    public static final class Builder {

        private String name;
        private long numberOfMembers;
        private Date date;

        private NumberOfAccount numberOfAccount;


        public Builder withNumberOfAccount(long total, long inactive) {
            this.numberOfAccount = new NumberOfAccount(total, inactive);
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
