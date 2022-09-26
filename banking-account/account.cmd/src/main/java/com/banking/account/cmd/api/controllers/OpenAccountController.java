package com.banking.account.cmd.api.controllers;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.cmd.api.dto.OpenAccountResponse;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/openbankaccount")
public class OpenAccountController {

    @Autowired
    private CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand openAccountCommand){
       var id = UUID.randomUUID().toString();
        openAccountCommand.setId(id);
       try{
           commandDispatcher.send(openAccountCommand);
           return new ResponseEntity<>(new OpenAccountResponse("Cuenta creada exitosamente",id), HttpStatus.CREATED);
       } catch (Exception ex){
         log.error(ex.toString());
         return new ResponseEntity<>(new BaseResponse(ex.toString()),HttpStatus.BAD_REQUEST);
       }
    }
}
