package ssg.search.collection.global;

import java.util.Iterator;

import com.google.common.collect.ImmutableSet;

import ssg.search.base.Call;
import ssg.search.base.Info;
import ssg.search.collection.base.DispItemCollection;
import ssg.search.constant.Prefixes;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;

public class GlobalDispItemCategoryCollection extends DispItemCollection implements Prefixable{
	@Override
	public String getCollectionAliasName(Parameter parameter){
		return "global";
	}

	public Call<Info> getPrefix(){
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX_GLOBAL
					, Prefixes.FILTER_SITE_NO
					, Prefixes.DISP_CTG_LST
					, Prefixes.SALESTR_LST
					, Prefixes.BRAND_ID
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SPL_VEN_ID
					, Prefixes.LRNK_SPL_VEN_ID
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				return new Info(sb.toString(), 1);
			}
		};
	}

}
