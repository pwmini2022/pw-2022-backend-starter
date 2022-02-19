package pw.react.backend.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pw.react.backend.models.Company;
import pw.react.backend.utils.JsonDateDeserializer;
import pw.react.backend.utils.JsonDateSerializer;

import java.time.LocalDateTime;

public class CompanyDto
{
    public static final CompanyDto EMPTY = new CompanyDto(-1, "", null, -1);

    private final long id;
    private final String name;
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private final LocalDateTime startDateTime;
    private final int boardMembers;

    public CompanyDto(long id, String name, LocalDateTime startDateTime, int boardMembers) {
        this.id = id;
        this.name = name;
        this.startDateTime = startDateTime;
        this.boardMembers = boardMembers;
    }

    public static CompanyDto valueFrom(Company company) {
        return new CompanyDto(company.getId(), company.getName(), company.getStartDateTime(), company.getBoardMembers());
    }

    public static Company convertToCompany(CompanyDto dto) {
        Company company = new Company();
        company.setId(dto.getId());
        company.setName(dto.getName());
        company.setStartDateTime(dto.getStartDateTime());
        company.setBoardMembers(dto.getBoardMembers());
        return company;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public int getBoardMembers() {
        return boardMembers;
    }
}
