package ru.netology.backend.model;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Data
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FILES")
public class FilesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "files_id", referencedColumnName = "id")
    private Users users;

    @Lob
    @Column(nullable = false)
    private byte[] content;

    @Column(nullable = false)
    private long size;
}

