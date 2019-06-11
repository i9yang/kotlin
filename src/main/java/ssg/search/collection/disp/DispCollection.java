package ssg.search.collection.disp;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Info;
import ssg.search.collection.base.DispItemCollection;
import ssg.search.constant.Cls;
import ssg.search.constant.Filters;
import ssg.search.constant.Prefixes;
import ssg.search.constant.Shpp;
import ssg.search.function.Filterable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.util.CollectionUtils;

import java.util.Iterator;

public class DispCollection extends DispItemCollection implements Prefixable, Filterable{
	
	public Call<Info> getPrefix(){
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX_DISP
					, Prefixes.FILTER_SITE_NO
					, Prefixes.DISP_CTG_LST
					, Prefixes.BRAND_ID
					, Prefixes.SALESTR_LST
					, Prefixes.SIZE
					, Prefixes.COLOR
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SPL_VEN_ID
					, Prefixes.LRNK_SPL_VEN_ID
					, Prefixes.SCOM_EXPSR_YN
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				// 혜택 필터 ( 이마트 점포상품만 보기, 이마트 온라인상품 )
				String strCls = StringUtils.defaultIfEmpty(parameter.getCls(), "");
				if(strCls.equalsIgnoreCase("emart")){
					sb.append("<CLS:contains:").append(Cls.EMART.getPrefix()).append(">");
				}
				if(strCls.equalsIgnoreCase("emartonline")){
					sb.append("<CLS:contains:").append(Cls.EMARTONLINE.getPrefix()).append(">");
				}
				// 무료배송, 특가상품
				String strFilter = StringUtils.defaultIfEmpty(parameter.getFilter(), "");
				if(strFilter.equals("free")){
					sb.append("<SHPP:contains:").append(Shpp.FREE.getPrefix()).append(">");
				}
				if(strFilter.indexOf("spprice")>-1){
					sb.append("<CLS:contains:").append(Cls.SP_PRICE.getPrefix()).append("|").append(Cls.OBANJANG.getPrefix()).append(">");
				}
				// 상품 관련 필터 ( 매직 픽업, 퀵배송 )
				String shppPrefix = CollectionUtils.getShppPrefix(parameter);
				if(shppPrefix!=null && !shppPrefix.equals("")){
					sb.append(shppPrefix);
				}
				return new Info(sb.toString(), 1);
			}
		};
	}
	
	public Call<Info> getFilter(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info(Filters.PRC_FILTER.getFilter(parameter));
			}
		};
	}
}