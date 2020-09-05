package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.dao.BankAccountDAO;
import com.example.demo.exception.BankAccountException;
import com.example.demo.form.SendMoneyForm;
import com.example.demo.model.BankAccountInfo;

@Controller
public class MainController {

	@Autowired
	private BankAccountDAO bankAccountDAO;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showBankAccountInfoList(Model model) {
		List<BankAccountInfo> list = this.bankAccountDAO.listBankAccountInfo();
		model.addAttribute("bankAccountInfoList", list);
		return "accountsPage";
	}

	@RequestMapping(value = "/sendMoney", method = RequestMethod.GET)
	public String processSendMoneyPage(Model model) {
		SendMoneyForm sendMoneyForm = new SendMoneyForm(1L, 2L, 700d);
		model.addAttribute("sendMoneyForm", sendMoneyForm);
		return "sendMoneyPage";
	}

	@RequestMapping(value = "/sendMoney", method = RequestMethod.POST)
	public String processSendMoney(Model model, SendMoneyForm sendMoneyForm) {
		System.out.println("Send Money: " + sendMoneyForm.getAmount());

		try {
			bankAccountDAO.sendMoney(sendMoneyForm.getFromAccountId(), sendMoneyForm.getToAccountId(),
					sendMoneyForm.getAmount());
		} catch (BankAccountException e) {
			model.addAttribute("errorMessage", "Error: " + e.getMessage());
			e.printStackTrace();
		}
		return "redirect:/ ";
	}
}
