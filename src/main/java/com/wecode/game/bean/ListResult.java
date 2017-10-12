package com.wecode.game.bean;

import java.util.List;

public class ListResult<T>
{
	public Long count;
	public List<T> list;
	
	public ListResult(Long count, List<T> list)
	{
		this.count = count;
		this.list = list;
	}
}
