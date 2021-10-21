package pw.react.backend.services;

import pw.react.backend.models.Company;

public interface CompanyService {
    Company updateCompany(Long id, Company updatedCompany);
    boolean deleteCompany(Long companyId);
}
