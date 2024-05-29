package br.com.tales.application;

import br.com.tales.domain.dto.AccountDTO;
import br.com.tales.domain.enums.AccountStatus;
import br.com.tales.domain.model.Account;
import br.com.tales.domain.service.AccountService;
import br.com.tales.infrastracture.csv.AccountCSV;
import br.com.tales.infrastracture.response.ResponseMessage;
import br.com.tales.infrastracture.response.ResponsePagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody AccountDTO account) {
        return ResponseEntity.ok(accountService.updateAccounts(id, account));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Account> changeSatus(@PathVariable Long id, @RequestParam AccountStatus status) {
        return ResponseEntity.ok(accountService.changeStatusAccount(id, status));
    }

    @GetMapping
    public ResponseEntity getAccounts(@RequestParam @Parameter(description = "YYYY-MM-DD") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInit,
                                      @RequestParam @Parameter(description = "YYYY-MM-DD") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim,
                                      @RequestParam String description, @RequestParam(value="page", defaultValue="1") Integer page,
                                      @RequestParam(value="linesPerPage", defaultValue="10") Integer linesPerPage) {
        Page<Account> accounts = accountService.getAccounts(dtInit, dtFim, description, page, linesPerPage);
        return ResponseEntity.ok().body(new ResponsePagination(accounts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/paid")
    public ResponseEntity<BigDecimal> getTotalPaidPerPeriod(@RequestParam @Parameter(description = "YYYY-MM-DD") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtInit,
                                                               @RequestParam @Parameter(description = "YYYY-MM-DD") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dtFim) {
        return ResponseEntity.ok(accountService.getTotalPaidPerPeriod(dtInit, dtFim));
    }

//    @Operation(parameters = {@Parameter( content = @Content(schema = @Schema(type = "multipart/form-data",  format = "binary")))})
    @Operation(parameters = {
            @Parameter(name = "file",
                    description = "File to upload",
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")))
    })
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(MultipartFile file) {
        String message = "";

        if (AccountCSV.hasCSVFormat(file)) {
            try {
                accountService.uploadFileAccount(file);

                message = "File uploaded successfully: " + file.getOriginalFilename();

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Unable to upload file: " + file.getOriginalFilename() + ". " +
                        "Make sure it is separated by ; or the date format in the pattern YYYY-MM-DD";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please choose the file account.csv!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }
}
