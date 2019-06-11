package ssg.search.collection.search;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.constant.Prefixes;
import ssg.search.constant.Sorts;
import ssg.search.constant.Targets;
import ssg.search.function.*;
import ssg.search.parameter.Parameter;
import ssg.search.result.Postng;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class PostngCollection implements Collection, Prefixable, Pageable, Rankable, Boostable, Sortable{
	public String getCollectionName(Parameter parameter){
		return "postng";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "postng";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"SITE_NO",
			"ITEM_ID",
			"ITEM_NM",
			"POSTNG_ID",
			"POSTNG_TITLE_NM",
			"POSTNG_EVAL_SCR",
			"POSTNG_WRTPE_IDNF_ID",
			"SELLPRC","ITEM_REG_DIV_CD", "SALESTR_LST"
		};
	}

	public String[] getSearchField(Parameter parameter){
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
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
	
	public Call<Info> getBoost() {
		return new Call<Info>(){
			public Info apply(Parameter parameter) {
				return CollectionUtils.getCategoryBoosting(parameter);
			}
		};
	}

	public Call<Info> getRank() {
		return new Call<Info>(){
			public Info apply(Parameter parameter) {
				return CollectionUtils.getCollectionRanking(parameter);
			}
		};
	}

	public Call<Info> getPage() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strPage  = "1";
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				Targets targets = CollectionUtils.getTargets(parameter);
				// Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
				if(targets.equals(Targets.ALL)){
					strCount = "9";
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}

	public Call<Info> getPrefix() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					Prefixes.SRCH_PREFIX, 
					Prefixes.FILTER_SITE_NO, 
					//Prefixes.BRAND_ID, 
					Prefixes.SALESTR_LST, 
					Prefixes.DEVICE_CD, 
					Prefixes.SPL_VEN_ID, 
					Prefixes.LRNK_SPL_VEN_ID, 
					Prefixes.MBR_CO_TYPE
					//Prefixes.SRCH_CTG_ITEM_PREFIX
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				return new Info(sb.toString(), 1);
			}
		};
	}

	public Call<Info> getSort() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				Sorts sorts 			= Sorts.BEST;
				String	 	strSort 		= StringUtils.defaultIfEmpty(parameter.getSort(), "best");
				List<Sort> sortList 		= Lists.newArrayList();
				try{
					sorts = Sorts.valueOf(strSort.toUpperCase());
				} catch(IllegalArgumentException e){}
				if(sorts.equals(Sorts.BEST)){
					sortList.add(Sorts.WEIGHT.getSort(parameter));
					sortList.add(Sorts.RANK.getSort(parameter));
					sortList.add(Sorts.THRD.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
					sortList.add(Sorts.PRCASC.getSort(parameter));
				}else{
					sortList.add(sorts.getSort(parameter));
				}
				return new Info(sortList);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		int count = search.w3GetResultCount(name);
		StringBuilder srchItemIds = new StringBuilder(result.getSrchItemIds());
		List<Postng> postngList = new ArrayList<Postng>();
		Postng postng;
		for(int i=0;i<count;i++){
			postng = new Postng();
			String strItemId = search.w3GetField(name,"ITEM_ID",i);
			String strSiteNo = search.w3GetField(name,"SITE_NO",i);
			String strItemNm = search.w3GetField(name,"ITEM_NM",i);
			String strPostngId = search.w3GetField(name,"POSTNG_ID",i);
			String strPostngNm = search.w3GetField(name,"POSTNG_TITLE_NM",i);
			String strPostngEvalScr = search.w3GetField(name,"POSTNG_EVAL_SCR",i);
			String strPostngWrtpeIdnfId = search.w3GetField(name, "POSTNG_WRTPE_IDNF_ID", i);
			String strSalestrLst = search.w3GetField(name, "SALESTR_LST", i);
			String strSalestrNo = "";
			String itemRegDivCd = search.w3GetField(name, "ITEM_REG_DIV_CD", i);
			String sellprc     = search.w3GetField(name, "SELLPRC", i);

			postng.setSiteNo(strSiteNo);
			postng.setItemId(strItemId);
			postng.setItemNm(strItemNm);
			postng.setPostngId(strPostngId);
			postng.setPostngTitleNm(strPostngNm);
			postng.setPostngEvalScr(strPostngEvalScr);
			postng.setPostngWrtpeIdnfId(strPostngWrtpeIdnfId);

			postng.setSellprc(sellprc);

			// 상품의 이빨이 빠졌을 때 점포값이 필수인 것들이 있기 때문에 강제로  점포 매핑작업을 한다.
			FrontUserInfo userInfo = parameter.getUserInfo();
			if(strSiteNo.equals("6009") && itemRegDivCd.equals("30")){
				if(strSalestrLst.indexOf("0001")>-1){
					int idx = 0;
					strSalestrLst = strSalestrLst.replace("0001,", "").trim();
					strSalestrLst = strSalestrLst.replace("0001", "").trim();
					for(StringTokenizer st = new StringTokenizer(strSalestrLst," "); st.hasMoreTokens();){
						if(idx>1)break;
						strSalestrNo = st.nextToken().replace("D", "");
						idx++;
					}
				}
				else{
					strSalestrNo = strSalestrLst.replaceAll("\\p{Space}", "").replace("D", "");
					strSalestrNo = strSalestrLst.replaceAll("\\p{Space}", "").replace("Y", "");	//백화점쓱배송점포처리
				}
			}
			// 그외 이/트/분 상품의 경우에는 거점점포의 데이터를 넣어준다.
			else if(strSiteNo.equals("6001") && itemRegDivCd.equals("20")){
				strSalestrNo = userInfo.getEmSaleStrNo();
			}else if(strSiteNo.equals("6002") && itemRegDivCd.equals("20")){
				strSalestrNo = userInfo.getTrSaleStrNo();
			}else if(strSiteNo.equals("6003") && itemRegDivCd.equals("20")){
				strSalestrNo = userInfo.getBnSaleStrNo();
			}else{
				strSalestrNo = "6005";
			}
			postng.setSalestrNo(strSalestrNo);

			StringBuilder ids = new StringBuilder().append(strSiteNo).append(":").append(strItemId).append(":").append(strSalestrNo).append(",");
			if(srchItemIds.indexOf(ids.toString()) < 0){
				srchItemIds.append(ids);
			}
			postngList.add(postng);
		}
		// POSTNG LIST
		result.setPostngList(postngList);
		result.setPostngCount(search.w3GetResultTotalCount(name));
		result.setSrchItemIds(srchItemIds.toString());
		return result;
	}
}
