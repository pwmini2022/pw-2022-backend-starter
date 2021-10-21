package pw.react.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "company_logo")
@Data
@NoArgsConstructor
public class CompanyLogo {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String fileName;
    private String fileType;
    private long companyId;
    @Lob
    private byte[] data;

    public CompanyLogo(String fileName, String fileType, long companyId, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.companyId = companyId;
        this.data = data;
    }
}
