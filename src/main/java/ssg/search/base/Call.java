package ssg.search.base;

import ssg.search.parameter.Parameter;

import com.google.common.collect.ImmutableMap;

public interface Call<T> {
	public Info apply(Parameter parameter);
}
