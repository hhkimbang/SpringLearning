package com.example.demo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.BankAccount;
import com.example.demo.exception.BankAccountException;
import com.example.demo.model.BankAccountInfo;

@Repository
public class BankAccountDAO {

	@Autowired
	private EntityManager entityManager;

	public BankAccountDAO() {

	}

	public BankAccount findById(Long id) {
		return this.entityManager.find(BankAccount.class, id);
	}

	public List<BankAccountInfo> listBankAccountInfo() {
		String sql = "SELECT new " + BankAccountInfo.class.getName() + "(e.id,e.fullName,e.balance)" + " FROM "
				+ BankAccount.class.getName();
		Query query = entityManager.createQuery(sql, BankAccountInfo.class);
		return query.getResultList();
	}

	// MANDATORY: Bắt buộc phải có Transaction đã được tạo trước đó.
	@Transactional(propagation = Propagation.MANDATORY)
	public void addAmount(Long id, double amount) throws BankAccountException {
		BankAccount account = this.findById(id);
		if (account == null) {
			throw new BankAccountException("Account Not Found " + id);
		}
		double newBalance = account.getBalance() + amount;
		if (account.getBalance() + amount < 0) {
			throw new BankAccountException(
					"The money in account '" + id + "' is not enough (" + account.getBalance() + " )");
		}
		account.setBalance(newBalance);
	}

	// Không bắt ngoại lệ BankTransactionException trong phương thức này.
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BankAccountException.class)
	public void sendMoney(Long fromAccountId, Long toAccountid, double amount) throws BankAccountException {
		addAmount(toAccountid, amount);
		addAmount(fromAccountId, -amount);
	}
}
