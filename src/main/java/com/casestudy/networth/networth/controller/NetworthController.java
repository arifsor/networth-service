package com.casestudy.networth.networth.controller;

import com.casestudy.networth.networth.model.Networth;
import com.casestudy.networth.networth.model.request.BalanceUpdateRequest;
import com.casestudy.networth.networth.service.NetworthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("v1/networth")
public class NetworthController {

    private NetworthService networthService;

    @Autowired
    public NetworthController(NetworthService networthService) {
        this.networthService = networthService;
    }

    @GetMapping
    public ResponseEntity<Networth> getNetworth(HttpServletRequest request, @RequestParam String currencyIsoCode) {
      return new ResponseEntity<>(networthService.getNetworth(currencyIsoCode), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Networth> updateAccountBalance(HttpServletRequest request, @RequestBody BalanceUpdateRequest balanceUpdateRequest) {
        return new ResponseEntity<>(networthService.updateBalance(balanceUpdateRequest), HttpStatus.OK);
    }

}
