package sa.m.ntd.calculator.model;

import jakarta.persistence.*;

@Entity
@Table(name = "AUTHORITIES")
public class Authority {

    @EmbeddedId
    private EmbeddedAuthorityId username;

    @ManyToOne(optional = false)
    @MapsId("username")
    @JoinColumn(name = "username", referencedColumnName = "username")
    CalculatorUser user;
}
