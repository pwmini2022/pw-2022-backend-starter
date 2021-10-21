package pw.react.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
