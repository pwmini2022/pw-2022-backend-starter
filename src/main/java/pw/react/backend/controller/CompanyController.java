package pw.react.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pw.react.backend.dao.CompanyRepository;
import pw.react.backend.exceptions.UnauthorizedException;
import pw.react.backend.models.Company;
import pw.react.backend.models.CompanyLogo;
import pw.react.backend.services.*;
import pw.react.backend.web.UploadFileResponse;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping(path = CompanyController.COMPANIES_PATH)
@Slf4j
@Api(tags = {"Companies", "Companies-logo"})
public class CompanyController {

    public static final String COMPANIES_PATH = "/companies";

    private final CompanyRepository repository;
    private final SecurityProvider securityService;
    private final CompanyService companyService;
    private LogoService companyLogoService;

    @Autowired
    public CompanyController(CompanyRepository repository, SecurityProvider securityService, CompanyService companyService) {
        this.repository = repository;
        this.securityService = securityService;
        this.companyService = companyService;
    }

    @Autowired
    public void setCompanyLogoService(LogoService companyLogoService) {
        this.companyLogoService = companyLogoService;
    }

    @PostMapping(path = "")
    @ApiOperation(value = "Create company", notes = "Creates company in the db and returns its ID.")
    public ResponseEntity<String> createCompanies(@RequestHeader HttpHeaders headers, @RequestBody List<Company> companies) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            List<Company> result = repository.saveAll(companies);
            return ResponseEntity.ok(result.stream().map(c -> String.valueOf(c.getId())).collect(joining(",")));
        }
        throw new UnauthorizedException("Unauthorized access to resources.", COMPANIES_PATH);
    }

    private void logHeaders(@RequestHeader HttpHeaders headers) {
        log.info("Controller request headers {}",
                headers.entrySet()
                        .stream()
                        .map(entry -> String.format("%s->[%s]", entry.getKey(), String.join(",", entry.getValue())))
                        .collect(joining(","))
        );
    }

    @GetMapping(path = "/{companyId}")
    @ApiOperation(value = "Find Company by id.", notes = "Returns company object.")
    public ResponseEntity<Company> getCompany(@RequestHeader HttpHeaders headers,
                                              @PathVariable Long companyId) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            return ResponseEntity.ok(repository.findById(companyId).orElseGet(() -> Company.EMPTY));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Company.EMPTY);
    }

    @GetMapping(path = "")
    @ApiOperation(value = "Gets all companies.", notes = "Returns collection of companies.")
    public ResponseEntity<Collection<Company>> getAllCompanies(@RequestHeader HttpHeaders headers) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            return ResponseEntity.ok(repository.findAll());
        }
        throw new UnauthorizedException("Request is unauthorized", COMPANIES_PATH);
    }

    @PutMapping(path = "/{companyId}")
    @ApiOperation(value = "Updates company by id.", notes = "Returns updated company.")
    public ResponseEntity<Company> updateCompany(@RequestHeader HttpHeaders headers,
                                                 @PathVariable Long companyId,
                                                 @RequestBody Company updatedCompany) {
        logHeaders(headers);
        Company result;
        if (securityService.isAuthorized(headers)) {
            result = companyService.updateCompany(companyId, updatedCompany);
            if (Company.EMPTY.equals(result)) {
                return ResponseEntity.badRequest().body(updatedCompany);
            }
            return ResponseEntity.ok(result);
        }
        throw new UnauthorizedException("Request is unauthorized", COMPANIES_PATH);
    }

    @DeleteMapping(path = "/{companyId}")
    @ApiOperation(value = "Deletes company id.", notes = "Returns an ID of deleted company.")
    public ResponseEntity<String> updateCompany(@RequestHeader HttpHeaders headers, @PathVariable Long companyId) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            boolean deleted = companyService.deleteCompany(companyId);
            if (!deleted) {
                return ResponseEntity.badRequest().body(String.format("Company with id %s does not exists.", companyId));
            }
            return ResponseEntity.ok(String.format("Company with id %s deleted.", companyId));
        }
        throw new UnauthorizedException("Unauthorized access to resources.", COMPANIES_PATH);
    }

    @PostMapping("/{companyId}/logo")
    @ApiOperation(value = "Creates logo for the company with specific id.",
            notes = "Returns details about uploaded file.")
    public ResponseEntity<UploadFileResponse> uploadLogo(@RequestHeader HttpHeaders headers,
                                                         @PathVariable Long companyId,
                                                         @RequestParam("file") MultipartFile file) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            CompanyLogo companyLogo = companyLogoService.storeLogo(companyId, file);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/companies/" + companyId + "/logo/")
                    .path(companyLogo.getFileName())
                    .toUriString();
            return ResponseEntity.ok(new UploadFileResponse(
                    companyLogo.getFileName(), fileDownloadUri, file.getContentType(), file.getSize()
            ));
        }
        throw new UnauthorizedException("Unauthorized access to resources.", COMPANIES_PATH);
    }

    @GetMapping(value = "/{companyId}/logo", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperation(value = "Gets the logo for the company based on company id.", notes = "Returns file as bytes array.")
    public @ResponseBody byte[] getLog(@RequestHeader HttpHeaders headers, @PathVariable Long companyId) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            CompanyLogo companyLogo = companyLogoService.getCompanyLogo(companyId);
            return companyLogo.getData();
        }

        throw new UnauthorizedException("Unauthorized access to resources.", COMPANIES_PATH);
    }

    @GetMapping(value = "/{companyId}/logo2")
    @ApiOperation(value = "Gets the logo for the company based on company id. Version 2.",
            notes = "Returns file as bytes array.")
    public ResponseEntity<Resource> getLogo2(@RequestHeader HttpHeaders headers, @PathVariable Long companyId) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            CompanyLogo companyLogo = companyLogoService.getCompanyLogo(companyId);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(companyLogo.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + companyLogo.getFileName() + "\"")
                    .body(new ByteArrayResource(companyLogo.getData()));
        }
        throw new UnauthorizedException("Unauthorized access to resources.", COMPANIES_PATH);
    }

    @DeleteMapping(value = "/{companyId}/logo")
    @ApiOperation(value = "Deletes the logo for the company based on company id.", notes = "Returns company id.")
    public ResponseEntity<String> removeLogo(@RequestHeader HttpHeaders headers, @PathVariable String companyId) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            companyLogoService.deleteCompanyLogo(Long.parseLong(companyId));
            return ResponseEntity.ok().body(String.format("Logo for the company with id %s removed.", companyId));
        }
        throw new UnauthorizedException("Unauthorized access to resources.", COMPANIES_PATH);
    }

}
