package com.nxtlife.mgs.filtering.filter;

import com.querydsl.core.types.Predicate;

public interface FilterBuilder<T> {
  
	Predicate build(T filter);

//	Predicate build(AwardFilter filter);
}
