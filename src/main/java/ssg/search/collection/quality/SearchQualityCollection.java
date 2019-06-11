package ssg.search.collection.quality;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.collection.search.ItemCollection;
import ssg.search.constant.Prefixes;
import ssg.search.constant.Sorts;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Item;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 131544 on 2016-10-13.
 */
public class SearchQualityCollection extends ItemCollection implements Collection, Prefixable {
	@Override
	public String getCollectionName(Parameter parameter) {
		return "item";
	}

	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "srch_qual_item";
	}

	@Override
	public String[] getDocumentField(Parameter parameter) {
		return new String[]{
				"ITEM_ID",
				"SRCH_INSP_STAT_CD"
			};
	}

	@Override
	public String[] getSearchField(Parameter parameter) {
		
		
		
		if ("itemId".equals(parameter.getPrefixFilter())) {
			return new String[]{
					"ITEM_ID",
				};
		} else if ("mdlNm".equals(parameter.getPrefixFilter())) {
			return new String[]{
					"MDL_NM",
				};
		} else if ("brandNm".equals(parameter.getPrefixFilter())) {
			return new String[]{
					"BRAND_NM",
				};
		} else if ("itemNm".equals(parameter.getPrefixFilter())) {
			return new String[]{
					"ITEM_NM",
				};
		} else {
			return new String[]{
					"ITEM_ID",
					"ITEM_NM",
					"ITEM_SRCHWD_NM",
					"MDL_NM",
					"BRAND_NM",
					"TAG_NM",
					"GNRL_STD_DISP_CTG"
				};
		}
	}

	
	public Call<Info> getSort() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				Sorts  	 	sorts 			= Sorts.BEST;
				String	 	strSort 		= StringUtils.defaultIfEmpty(parameter.getSort(), "best");
				List<Sort> 	sortList 		= Lists.newArrayList();
				try{
					sorts = Sorts.valueOf(strSort.toUpperCase());
				} catch(IllegalArgumentException e){}
				if(sorts.equals(Sorts.BEST)){
					sortList.add(Sorts.WEIGHT.getSort(parameter));
					sortList.add(Sorts.RANK.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
				}else{
					sortList.add(sorts.getSort(parameter));
				}
				return new Info(sortList);
			}
		};
	}
	
	public Call<Info> getPrefix() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
						 Prefixes.SRCH_PREFIX
						, Prefixes.FILTER_SITE_NO
						, Prefixes.SRCH_CTG_ITEM_PREFIX
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				
				sb.append(getSalestrLstPrefix(parameter));		//SALESTR_LST
				sb.append(getSrchInspStatCdPrefix(parameter));	//SRCH_INSP_STAT_CD
				sb.append(getScomExpsrYnPrefix(parameter));		//SCOM_EXPSR_YN
				sb.append(getClsPrefix(parameter));				//CLS
				sb.append(getModIdFilter(parameter));			//FILTER
				
				return new Info(sb.toString(),1);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		int count = search.w3GetResultCount(name);
		int totalCount = search.w3GetResultTotalCount(name);
		List<Item> itemList = Lists.newArrayList();
		StringBuilder srchItemIds = new StringBuilder();
		Item item;
        for(int i=0;i<count;i++){
        	item = new Item();
        	String strItemId = search.w3GetField(name,"ITEM_ID",i);
        	String strSrchInspStatCd = search.w3GetField(name,"SRCH_INSP_STAT_CD",i);
        	
        	item.setItemId(strItemId);
        	item.setSrchInspStatCd(strSrchInspStatCd);
        	
        	itemList.add(item);
        }
        
        HashSet<Item> listSet = new HashSet<Item>(itemList);
        List<Item> processedList = new ArrayList<Item>(listSet);
        
        for (Item sItem : processedList) {
        	StringBuilder ids = new StringBuilder().append(sItem.getItemId()).append(":").append(sItem.getSrchInspStatCd()).append(",");
        	if(srchItemIds.indexOf(ids.toString()) < 0){
        	    srchItemIds.append(ids);
        	}
		}
        
        result.setItemList(processedList);
        result.setItemCount(totalCount);
        result.setSrchItemIds(srchItemIds.toString());
        
		return result;
	}
	
	public Call<Info> getPage() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				if(strCount.equals("") || strCount.equals("0")){
					strCount = "40";
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}
	
	public Call<Info> getFilter() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				sb.append(getRegDtFilter(parameter));
				return new Info(sb.toString(),1);
			}
		};
	}
	
	public Call<Info> getBoost(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				if (parameter.getQuery() != null && parameter.getQuery() != "") {
					return CollectionUtils.getCategoryBoosting(parameter);
				} else {
					return null;
				}
				
			}
		};
	}
	
	private String getScomExpsrYnPrefix(Parameter parameter) {
		String prefix = "<SCOM_EXPSR_YN:contains:";
	    
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		
		if(strSiteNo.equals("6005")){
			return prefix + "Y" + ">";
		}
		
		return "";
	}
	
	private String getClsPrefix(Parameter parameter) {
		String prefix = "<CLS:contains:";
	    
		if (StringUtils.isNotEmpty(parameter.getCls())) {
			return prefix + parameter.getCls() + ">";
		}
		
		return "";
	}
	
	private String getSalestrLstPrefix(Parameter parameter) {
		FrontUserInfo userInfo = parameter.getUserInfo();
		
		String prefix = "<SALESTR_LST:contains:";
	    
		String emSalestrNo = StringUtils.defaultIfEmpty(userInfo.getEmSaleStrNo(), "");
		String emRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getEmRsvtShppPsblYn(),"N");
		String trSalestrNo = StringUtils.defaultIfEmpty(userInfo.getTrSaleStrNo(), "");
		String trRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getTrRsvtShppPsblYn(),"N");
		String bnSalestrNo = StringUtils.defaultIfEmpty(userInfo.getBnSaleStrNo(), "");
		String bnRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getBnRsvtShppPsblYn(),"N");
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		String deptSalestrNo = StringUtils.defaultIfEmpty(parameter.getSalestrNo(), "0001");
		String bojungSalestrNo = "2439";
		String gimpoSalestrNo = "2449";
		String trGusungSalestrNo = "2152";
		String hwSalestrNo =  "2468";
		
		
		if(strSiteNo.equals("6005")){
			return prefix + "6005|" + deptSalestrNo + "|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + bojungSalestrNo + "Y" + "|" + gimpoSalestrNo + "Y" + "|" + hwSalestrNo + "N" + ">";
		}
		else if(strSiteNo.equals("6004")){
			return prefix + "6005|" + deptSalestrNo  + ">";
		}
		else if(strSiteNo.equals("6009")){
			return prefix + deptSalestrNo + ">";
		}
		
		else if(strSiteNo.equals("6001")||strSiteNo.equals("6002")||strSiteNo.equals("6003")){
			return prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + "Y" + "|" + trSalestrNo + "N" + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + bojungSalestrNo + "Y" + "|" + gimpoSalestrNo + "Y" + "|" + trGusungSalestrNo + "Y" + "|" + trGusungSalestrNo + "N" + ">";
		} 
		
		else if(strSiteNo.equals("6100")){
			return prefix + hwSalestrNo + "N"  + ">";
		} 
		
		else if(strSiteNo.equals("6200")||strSiteNo.equals("6300")){
			return prefix + "6005"  + ">";
		}
		
				
		return "";
	}
	 
	private String getSrchInspStatCdPrefix(Parameter parameter) {
		if(StringUtils.isNotBlank(parameter.getSrchInspStatCd())) {
			return "<SRCH_INSP_STAT_CD:contains:" + parameter.getSrchInspStatCd() + ">";
		}
		
		return "";
	}

	private boolean checkDate(String date){
		String dt = date.replaceAll("-", "");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		sdf.setLenient(false);

		try{
			sdf.parse(dt);
		}catch(Exception e) {
			return false;
		}

		return true;
	}
	
	
	private String getRegDtFilter(Parameter parameter) {
		if (StringUtils.isNotBlank(parameter.getSrchStrtRegDts()) && StringUtils.isNotBlank(parameter.getSrchEndRegDts())
				&& checkDate(parameter.getSrchStrtRegDts()) && checkDate(parameter.getSrchEndRegDts())
				) {
			StringBuilder fsb = new StringBuilder();
		    fsb.append("<ITEM_REG_DT:gte:").append(parameter.getSrchStrtRegDts()).append("><ITEM_REG_DT:lte:").append(parameter.getSrchEndRegDts()).append(">");
			return fsb.toString();
		}
		
		return "";
	}
	
	private String getModIdFilter(Parameter parameter) {
		if (StringUtils.isNotBlank(parameter.getSrchModId())) {
			StringBuilder fsb = new StringBuilder();
		    fsb.append("<FILTER:contains:").append("DK").append(parameter.getSrchModId()).append(">");
			return fsb.toString();
		}
		
		return "";
	}
}
