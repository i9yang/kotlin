package ssg.search.function;

import ssg.search.base.Call;
import ssg.search.base.Info;

public interface Morphable{
	public Call<Info> getMorph();
}