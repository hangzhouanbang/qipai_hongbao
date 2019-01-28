package com.anbang.qipai.hongbao.cqrs.q.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.hongbao.cqrs.q.dao.MemberHongbaodianAccountDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dao.MemberHongbaodianRecordDboDao;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianAccountDbo;
import com.anbang.qipai.hongbao.cqrs.q.dbo.MemberHongbaodianRecordDbo;
import com.anbang.qipai.hongbao.web.vo.RecordSummaryTexts;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.TextAccountingSummary;

@Service
public class MemberHongbaodianService {

	@Autowired
	private MemberHongbaodianAccountDboDao memberHongbaodianAccountDboDao;

	@Autowired
	private MemberHongbaodianRecordDboDao memberHongbaodianRecordDboDao;

	/**
	 * 创建新红包点账户
	 */
	public void createHongbaodianAccountForNewMember(String accountId, String memberId) {
		MemberHongbaodianAccountDbo account = new MemberHongbaodianAccountDbo();
		account.setId(accountId);
		account.setMemberId(memberId);
		memberHongbaodianAccountDboDao.insert(account);
	}

	/**
	 * 根据用户id查询红包点账户
	 */
	public MemberHongbaodianAccountDbo findAccountByMemberId(String memberId) {
		return memberHongbaodianAccountDboDao.findByMemberId(memberId);
	}

	/**
	 * 记录红包点流水
	 */
	public MemberHongbaodianRecordDbo withdraw(AccountingRecord record, String memberId) {
		MemberHongbaodianRecordDbo dbo = new MemberHongbaodianRecordDbo();
		dbo.setAccountId(record.getAccountId());
		dbo.setAccountingAmount((int) record.getAccountingAmount());
		dbo.setAccountingNo(record.getAccountingNo());
		dbo.setAccountingTime(record.getAccountingTime());
		dbo.setBalanceAfter((int) record.getBalanceAfter());
		dbo.setMemberId(memberId);
		dbo.setSummary(record.getSummary());
		memberHongbaodianRecordDboDao.insert(dbo);

		memberHongbaodianAccountDboDao.updateBalance(record.getAccountId(), (int) record.getBalanceAfter());
		return dbo;
	}

	/**
	 * 根据用户id查询红包点流水
	 */
	// public ListPage findMemberHongbaodianRecordByMemberId(int page, int size,
	// String memberId) {
	// long amount = memberHongbaodianRecordDboDao.countAmountByMemberId(memberId);
	// List<MemberHongbaodianRecordDbo> recordList =
	// memberHongbaodianRecordDboDao.findByMemberId(page, size,
	// memberId);
	// ListPage listPage = new ListPage(recordList, page, size, (int) amount);
	// return listPage;
	// }

	/**
	 * 根据用户id查询红包点流水
	 */
	public List<MemberHongbaodianRecordDbo> findMemberHongbaodianRecordByMemberId(String memberId) {
		List<MemberHongbaodianRecordDbo> recordList = memberHongbaodianRecordDboDao.findByMemberId(memberId);
		return recordList;
	}

	/**
	 * 根据用户id和摘要查询红包点流水
	 */
	public List<MemberHongbaodianRecordDbo> findMemberHongbaodianRecordByMemberIdAndSummary(String memberId,
			String summary) {
		List<MemberHongbaodianRecordDbo> recordList = memberHongbaodianRecordDboDao.findByMemberIdAndSummary(memberId,
				summary);
		for (int i = 0; i < recordList.size(); i++) {
			TextAccountingSummary textSummary = (TextAccountingSummary) recordList.get(i).getSummary();
			TextAccountingSummary newSummary = new TextAccountingSummary(
					RecordSummaryTexts.getSummaryText(textSummary.getText()));
			recordList.get(i).setSummary(newSummary);
		}
		return recordList;
	}
}
