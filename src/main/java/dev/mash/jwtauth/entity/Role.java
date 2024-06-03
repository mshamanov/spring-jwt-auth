package dev.mash.jwtauth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a Role in a database.
 *
 * @author Mikhail Shamanov
 */
@Entity
@Data
@ToString(of = {"type"})
@EqualsAndHashCode(exclude = "users")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", unique = true, nullable = false)
    private RoleType type;

    @ManyToMany(mappedBy = "roles")
    private final List<User> users = new ArrayList<>();

    public Role(RoleType type) {
        this.id = null;
        this.type = type;
    }
}
