package com.gamesList.gamesList.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// If a POST request doesn't include a name, id, rating etc... then it won't show up.
// if removed, they'll be set to NULL
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Game {
    @Id
    @UuidGenerator
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    private String name;
    private String protagonist;
    private int rating;
    private String photoUrl;

}
