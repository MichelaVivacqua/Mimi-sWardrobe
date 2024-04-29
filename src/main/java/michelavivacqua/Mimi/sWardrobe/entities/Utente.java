package michelavivacqua.Mimi.sWardrobe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import michelavivacqua.Mimi.sWardrobe.enums.Ruolo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Utente implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String propic;
    private String password;
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;
    @JsonIgnore
    @OneToMany(mappedBy = "utente")
    private List<Indumento> indumenti;


    public Utente(String username, String name, String surname, String email, String propic, String password, Ruolo ruolo) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.propic = propic;
        this.password=password;
        this.ruolo=ruolo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Questo metodo deve ritornare la lista dei ruoli (SimpleGrantedAuthority) dell'utente
        return List.of(new SimpleGrantedAuthority(this.ruolo.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
