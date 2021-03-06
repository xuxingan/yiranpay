
package com.yiranpay.reconciliation.service.impl;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yiranpay.reconciliation.exception.BizException;
import com.yiranpay.reconciliation.mapper.BuildNoMap;
import com.yiranpay.reconciliation.service.IRecBuildNoService;
import com.yiranpay.reconciliation.utils.ReconciliationDateUtils;

import java.util.Date;

/**
 * 生成编号service实现类,每个编号前面都会有一个前缀用来方便区分是那种编号
 */
@Service("recBuildNoService")
public class RecBuildNoServiceImpl implements IRecBuildNoService {

	private static final Log LOG = LogFactory.getLog(RecBuildNoServiceImpl.class);

	/** 对账批次号前缀 **/
	private static final String RECONCILIATION_BATCH_NO = "5555";

	@Autowired
	private BuildNoMap buildNoMap;



	/** 获取对账批次号 **/
	public String buildReconciliationNo() {
		String batchNoSeq = this.getSeqNextValue("RECONCILIATION_BATCH_NO_SEQ");
		String dateString = ReconciliationDateUtils.toString(new Date(), "yyyyMMdd");
		String batchNo = RECONCILIATION_BATCH_NO + dateString + batchNoSeq.substring(batchNoSeq.length() - 8, batchNoSeq.length());
		return batchNo;
	}

	/**
	 * 根据序列名称,获取序列值
	 */
	@Transactional(rollbackFor = Exception.class)
	public String getSeqNextValue(String seqName) {
		String seqNextValue = null;
		try {
			seqNextValue = buildNoMap.getSeqNextValue(seqName);
		} catch (Exception e) {
			LOG.error("生成序号异常：" + "seqName=" + seqName, e);
			throw BizException.DB_GET_SEQ_NEXT_VALUE_ERROR;
		}
		if (StringUtils.isEmpty(seqNextValue)) {
			throw BizException.DB_GET_SEQ_NEXT_VALUE_ERROR;
		}
		return seqNextValue;
	}

}