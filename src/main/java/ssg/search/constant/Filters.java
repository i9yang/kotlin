package ssg.search.constant;

import org.apache.commons.lang3.StringUtils;

import ssg.search.parameter.Parameter;

public enum Filters{
	PRC_FILTER{
		public String getFilter(Parameter parameter) {
			String strMinPrc = StringUtils.defaultIfEmpty(parameter.getMinPrc(), "-1");
		    String strMaxPrc = StringUtils.defaultIfEmpty(parameter.getMaxPrc(), "-1");
		    // 숫자로 변환되는 경우에만 보냄
		    int minprc;
		    int maxprc;
		    
		    try{
		        minprc = Integer.parseInt(strMinPrc);
		        maxprc = Integer.parseInt(strMaxPrc);
		    }catch(NumberFormatException ne){
		        minprc = -1;
		        maxprc = -1;
		    }
		    StringBuilder fsb = new StringBuilder();
		    if(minprc>-1 && maxprc >-1){
		        fsb.append("<SELLPRC:gte:").append(strMinPrc).append("><SELLPRC:lte:").append(strMaxPrc).append(">");
		    }else if(minprc>-1){
		        fsb.append("<SELLPRC:gte:").append(strMinPrc).append(">");
		    }else if(maxprc>-1){
		        fsb.append("<SELLPRC:lte:").append(strMaxPrc).append(">");
		    }
			return fsb.toString();
		}
	};
	public abstract String getFilter(Parameter parameter);
}
