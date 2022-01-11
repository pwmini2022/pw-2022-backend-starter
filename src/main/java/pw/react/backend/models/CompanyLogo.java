package pw.react.backend.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "company_logo")
@Data
@NoArgsConstructor
@ApiModel(description="All details about the company logo.")
public class CompanyLogo {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @ApiModelProperty(notes = "Id of the entity.")
    private String id;
    @ApiModelProperty(notes = "Logo file name.")
    private String fileName;
    @ApiModelProperty(notes = "Logo file type.")
    private String fileType;
    @ApiModelProperty(notes = "Company Id for the logo.")
    private long companyId;
    @Lob
    @ApiModelProperty(notes = "Actual data as bytes.")
    private byte[] data;

    public CompanyLogo(String fileName, String fileType, long companyId, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.companyId = companyId;
        this.data = data;
    }
}
