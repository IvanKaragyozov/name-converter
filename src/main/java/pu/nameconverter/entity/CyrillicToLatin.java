package pu.nameconverter.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cyrillic_to_latin")
public class CyrillicToLatin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cyrillic_name")
    private String cyrillicName;

    @Column(name = "latin_name")
    private String latinName;
}
