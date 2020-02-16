package com.gmart.api.core.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gmart.api.core.entities.UserCore;
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
	@ResponseBody
	public List<UserCore> getAllAccounts( HttpServletRequest request,HttpServletResponse response) {
		return this.accountService.getAll();
	}
}
