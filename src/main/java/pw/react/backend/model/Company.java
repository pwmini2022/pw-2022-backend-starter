package pw.react.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import pw.react.backend.utils.JsonDateDeserializer;
import pw.react.backend.utils.JsonDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "company")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Company implements Serializable {

    private static final long serialVersionUID = -6783504532088859179L;

    public static Company EMPTY = new Company();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "startDate")
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private LocalDateTime startDateTime;
    @Column(name = "boardMembers")
    private int boardMembers;
}
