package pw.react.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pw.react.backend.appException.UnauthorizedException;
import pw.react.backend.dao.CompanyRepository;
import pw.react.backend.model.Company;
import pw.react.backend.model.CompanyLogo;
import pw.react.backend.service.*;
import pw.react.backend.web.UploadFileResponse;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping(path = "/companies")
public class CompanyController {

    private final Logger logger = LoggerFactory.getLogger(CompanyController.class);

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
    public ResponseEntity<String> createCompanies(@RequestHeader HttpHeaders headers, @RequestBody List<Company> companies) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            List<Company> result = repository.saveAll(companies);
            return ResponseEntity.ok(result.stream().map(c -> String.valueOf(c.getId())).collect(joining(",")));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access to resources.");
    }

    private void logHeaders(@RequestHeader HttpHeaders headers) {
        logger.info("Controller request headers {}",
                headers.entrySet()
                        .stream()
                        .map(entry -> String.format("%s->[%s]", entry.getKey(), String.join(",", entry.getValue())))
                        .collect(joining(","))
        );
    }

    @GetMapping(path = "/{companyId}")
    public ResponseEntity<Company> getCompany(@RequestHeader HttpHeaders headers,
                                              @PathVariable Long companyId) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            return ResponseEntity.ok(repository.findById(companyId).orElseGet(() -> Company.EMPTY));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Company.EMPTY);
    }

    @GetMapping(path = "")
    public ResponseEntity<Collection<Company>> getAllCompanies(@RequestHeader HttpHeaders headers) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            return ResponseEntity.ok(repository.findAll());
        }
        throw new UnauthorizedException("Request is unauthorized");
    }

    @PutMapping(path = "/{companyId}")
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
        throw new UnauthorizedException("Request is unauthorized");
    }

    @DeleteMapping(path = "/{companyId}")
    public ResponseEntity<String> updateCompany(@RequestHeader HttpHeaders headers, @PathVariable Long companyId) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            boolean deleted = companyService.deleteCompany(companyId);
            if (!deleted) {
                return ResponseEntity.badRequest().body(String.format("Company with id %s does not exists.", companyId));
            }
            return ResponseEntity.ok(String.format("Company with id %s deleted.", companyId));
        }
        throw new UnauthorizedException("Unauthorized access to resources.");
    }

    @PostMapping("/{companyId}/logo")
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
        throw new UnauthorizedException("Unauthorized access to resources.");
    }

    @GetMapping(value = "/{companyId}/logo", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] getLog(@RequestHeader HttpHeaders headers, @PathVariable Long companyId) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            CompanyLogo companyLogo = companyLogoService.getCompanyLogo(companyId);
            return companyLogo.getData();
        }

        throw new UnauthorizedException("Unauthorized access to resources.");
    }

    @GetMapping(value = "/{companyId}/logo2")
    public ResponseEntity<Resource> getLogo2(@RequestHeader HttpHeaders headers, @PathVariable Long companyId) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            CompanyLogo companyLogo = companyLogoService.getCompanyLogo(companyId);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(companyLogo.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + companyLogo.getFileName() + "\"")
                    .body(new ByteArrayResource(companyLogo.getData()));
        }
        throw new UnauthorizedException("Unauthorized access to resources.");
    }

    @DeleteMapping(value = "/{companyId}/logo")
    public ResponseEntity<String> removeLogo(@RequestHeader HttpHeaders headers, @PathVariable String companyId) {
        logHeaders(headers);
        if (securityService.isAuthorized(headers)) {
            companyLogoService.deleteCompanyLogo(Long.parseLong(companyId));
            return ResponseEntity.ok().body(String.format("Logo for the company with id %s removed.", companyId));
        }
        throw new UnauthorizedException("Unauthorized access to resources.");
    }

}
