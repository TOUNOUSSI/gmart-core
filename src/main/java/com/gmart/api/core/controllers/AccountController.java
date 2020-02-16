package com.gmart.api.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmart.api.core.services.AccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("account")
@Scope("session")
@Slf4j
public class AccountController {
	@Autowired
	AccountService accountService;

	@GetMapping("/accounts")
	public List<?> getAllAccounts() {
		return this.accountService.getAll();
	}
}
