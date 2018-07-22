package com.dmall.miner.wicc.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.dmall.miner.wicc.model.WiccTx;

@Service
public class DataBaseService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void batchInsertWiccTx(List<WiccTx> wiccTxs) {
		String sql = "INSERT INTO wicc_tx (tx_hash, from_user, to_user, quantity, deal_time) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
            public int getBatchSize() {
                return wiccTxs.size();
            }
            
            public void setValues(PreparedStatement ps, int i)throws SQLException {
            	WiccTx wiccTx = wiccTxs.get(i);
                ps.setString(1, wiccTx.getTxHash());
                ps.setString(2, wiccTx.getFrom());
                ps.setString(3, wiccTx.getTo());
                ps.setString(4, wiccTx.getQuantity());
                ps.setString(5, wiccTx.getDateTime());
            }
		});
//		for(WiccTx wiccTx : wiccTxs) {
//			jdbcTemplate.update(sql, wiccTx.getTxHash(), wiccTx.getFrom(), wiccTx.getTo(), wiccTx.getQuantity(), wiccTx.getDateTime());
//		}
	}

}
