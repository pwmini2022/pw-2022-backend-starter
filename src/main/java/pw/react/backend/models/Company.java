package pw.react.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description="All details about the company.")
public class Company implements Serializable {

    private static final long serialVersionUID = -6783504532088859179L;

    public static Company EMPTY = new Company();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes="Automatically generated id.")
    private long id;
    @Column
    @ApiModelProperty(notes="Name should not be empty", required = true)
    private String name;
    @Column(name = "startDate")
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    @ApiModelProperty(notes="Start date")
    private LocalDateTime startDateTime;
    @Column
    @ApiModelProperty(notes="Number of board members", required = true)
    private int boardMembers;
}
