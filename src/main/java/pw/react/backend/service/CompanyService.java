package pw.react.backend.service;

import pw.react.backend.model.Company;

public interface CompanyService {
    Company updateCompany(Long id, Company updatedCompany);
    boolean deleteCompany(Long companyId);
}
