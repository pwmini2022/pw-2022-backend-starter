package pw.react.backend.service;

import org.springframework.web.multipart.MultipartFile;
import pw.react.backend.model.CompanyLogo;

public interface LogoService {
    CompanyLogo storeLogo(long companyId, MultipartFile file);
    CompanyLogo getCompanyLogo(long companyId);
    void deleteCompanyLogo(long companyId);
}
