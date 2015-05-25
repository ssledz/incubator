package pl.softech.knf.ofe.opf.investments;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static java.util.Objects.requireNonNull;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class Instrument {

    private Long id;
    private final String identifier;
    private final String name;
    private final String description;

    public Instrument(Long id, String identifier, String name, String description) {
        this(identifier, name, description);
        this.id = id;
    }

    /**
     * @param identifier  must not be null
     * @param name        must not be null
     * @param description can be null
     */
    public Instrument(String identifier, String name, String description) {
        this.identifier = requireNonNull(identifier);
        this.name = requireNonNull(name);
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Instrument)) {
            return false;
        }

        Instrument that = (Instrument) o;

        return new EqualsBuilder()
                .append(identifier, that.identifier)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(identifier)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("identifier", identifier)
                .toString();
    }
}
