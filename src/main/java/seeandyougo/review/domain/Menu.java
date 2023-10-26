package seeandyougo.review.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Menu {
    @Id
    @GeneratedValue
    @Column(name = "menu_id")
    private Long id;
    @OneToMany
    private List<Review> reviewLinkedList = new ArrayList<>();
}
