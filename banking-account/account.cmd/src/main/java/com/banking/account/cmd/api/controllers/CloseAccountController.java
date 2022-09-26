package com.banking.account.cmd.api.controllers;

import com.banking.account.cmd.api.command.CloseAccountCommand;
import com.banking.account.cmd.api.command.RemoveAccountCommand;
import com.banking.account.common.dto.BaseResponse;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/closeaccount")
public class CloseAccountController {
    @Autowired
    private CommandDispatcher commandDispatcher;

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> closeAccount(@PathVariable(value = "id") String id){
        try{
            commandDispatcher.send(new CloseAccountCommand(id));
            return new ResponseEntity<>(new BaseResponse("Cuenta cerrada exitosamente"), HttpStatus.OK);
        } catch (Exception ex){
            log.error(String.valueOf(ex));
            return new ResponseEntity<>(new BaseResponse(ex.toString()),HttpStatus.BAD_REQUEST);
        }
    }
}
