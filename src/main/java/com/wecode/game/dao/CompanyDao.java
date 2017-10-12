package com.wecode.game.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.wecode.game.bean.Company;
import com.wecode.game.exception.IntConvertBoolException;
import com.wecode.game.util.MapConvertBeanUtil;
import com.wecode.game.util.SpringJdbc;

@Repository("companyDao")
public class CompanyDao 
{
	@Resource
	private SpringJdbc jdbcBase;
	private static Logger log = Logger.getLogger(CompanyDao.class);
	
	public Company getCompanyInfo()
	{
		String sql = "select * from company";
		List< Map<String, Object> > resultSet = jdbcBase.query(sql);
		Company company = null;
		try 
		{
			company = MapConvertBeanUtil.resultToBean(resultSet.get(0), Company.class);
		} 
		catch (IllegalArgumentException | InstantiationException
				| IllegalAccessException | IntConvertBoolException e) {
			log.error("chagne company to bean fail", e);
		}
		return company;
	}

	public boolean updateVendorId(Company company) 
	{
		String sql = "update company set vendorId = ? where id = ?";
		Object[] params = new Object[]{company.vendorId, company.id};
		
		int optResult = jdbcBase.update(sql, params);
		if ( optResult < 0 )
		{
			log.error("update company error");
			return false;
		}
		return true;
	}
}
