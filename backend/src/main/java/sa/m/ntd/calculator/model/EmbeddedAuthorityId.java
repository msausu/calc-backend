package sa.m.ntd.calculator.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class EmbeddedAuthorityId implements Serializable {

    private String username;
    private String authority;

    @Override
    public boolean equals(Object other) {
        if (other instanceof EmbeddedAuthorityId eai)
            return username.equals(eai.getUsername()) && authority.equals(eai.getAuthority());
        else throw new IllegalArgumentException("invalid object type");
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, authority);
    }
}
