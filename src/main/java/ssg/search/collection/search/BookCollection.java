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
import ssg.search.constant.Filters;
import ssg.search.constant.Prefixes;
import ssg.search.constant.Sorts;
import ssg.search.function.Filterable;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.function.Sortable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Book;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class BookCollection implements Collection, Prefixable, Pageable, Filterable, Sortable{

	public String getCollectionName(Parameter parameter){
		return "book";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "book";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"SITE_NO",
			"ITEM_ID",
			"ITEM_NM",
			"DISP_CTG_LCLS_ID",
			"DISP_CTG_LCLS_NM",
			"DISP_CTG_MCLS_ID",
			"DISP_CTG_MCLS_NM",
			"DISP_CTG_SCLS_ID",
			"DISP_CTG_SCLS_NM",
			"DISP_CTG_DCLS_ID",
			"DISP_CTG_DCLS_NM",
			"AUTHOR_NM",
			"TRLTPE_NM",
			"PUBSCO_NM",
			"SELLPRC",
			"SHPP_TYPE_DTL_CD",
			"FXPRC",
			"SALESTR_LST",
			"ITEM_REG_DIV_CD"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{
			"ITEM_ID",
			"ITEM_NM",
			"ITEM_SRCHWD_NM",
			"MDL_NM",
			"ISBN",
			"BOOK_ENG_NM",
			"ORTITL_NM",
			"SUBTITL_NM",
			"AUTHOR_NM",
			"TRLTPE_NM",
			"PUBSCO_NM"
		};
	}

	public Result getResult(Search search, String name, Parameter parameter,Result result){
		List<Book> bookList = new ArrayList<Book>();
		Book book;
		int count = search.w3GetResultCount(name);
		StringBuilder bookItemIds = new StringBuilder();
		for(int i=0;i<count;i++){
			book = new Book();
			String strSiteNo = search.w3GetField(name, "SITE_NO", i);
			String strItemId = search.w3GetField(name, "ITEM_ID", i);
			String salestrLst = search.w3GetField(name, "SALESTR_LST", i);
			String itemRegDivCd  = search.w3GetField(name, "ITEM_REG_DIV_CD", i);
			String strSalestrNo = "";
			
			book.setSiteNo(strSiteNo);
			book.setItemId(strItemId);
			book.setItemNm(search.w3GetField(name,   "ITEM_NM", i));
			book.setAuthorNm(search.w3GetField(name, "AUTHOR_NM", i));
			book.setTrltpeNm(search.w3GetField(name, "TRLTPE_NM", i));
			book.setPubscoNm(search.w3GetField(name, "PUBSCO_NM", i));
			book.setFxprc(search.w3GetField(name, "FXPRC", i));
//            book.setDispCtgLclsId(search.w3GetField(name,"DISP_CTG_LCLS_ID",i));
//            book.setDispCtgLclsNm(search.w3GetField(name,"DISP_CTG_LCLS_NM",i));
//            book.setDispCtgMclsId(search.w3GetField(name,"DISP_CTG_MCLS_ID",i));
//            book.setDispCtgMclsNm(search.w3GetField(name,"DISP_CTG_MCLS_NM",i));
//            book.setDispCtgSclsId(search.w3GetField(name,"DISP_CTG_SCLS_ID",i));
//            book.setDispCtgSclsNm(search.w3GetField(name,"DISP_CTG_SCLS_NM",i));
//            book.setDispCtgDclsId(search.w3GetField(name,"DISP_CTG_DCLS_ID",i));
//            book.setDispCtgDclsNm(search.w3GetField(name,"DISP_CTG_DCLS_NM",i));
            book.setSellprc(search.w3GetField(name,"SELLPRC",i));
            book.setShppTypeDtlCd(search.w3GetField(name,"SHPP_TYPE_DTL_CD",i));
            
            // 상품의 이빨이 빠졌을 때 점포값이 필수인 것들이 있기 때문에 강제로  점포 매핑작업을 한다.
        	FrontUserInfo userInfo = parameter.getUserInfo();
        	if(strSiteNo.equals("6009") && itemRegDivCd.equals("30")){
        		if(salestrLst.indexOf("0001")>-1){
        			int idx = 0;
        			salestrLst = salestrLst.replace("0001", "").trim();
        			salestrLst = salestrLst.replace("0001,", "").trim();
        			for(StringTokenizer st = new StringTokenizer(salestrLst," ");st.hasMoreTokens();){
        				if(idx>1)break;
        				strSalestrNo = st.nextToken().replace("D", "");
        	            idx++;
        			}
        		}
        		else{
        			strSalestrNo = salestrLst.replaceAll("\\p{Space}", "").replace("D", "");
        			strSalestrNo = salestrLst.replaceAll("\\p{Space}", "").replace("Y", "");	//백화점쓱배송점포처리
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
        	book.setSalestrNo(strSalestrNo);
            StringBuilder ids = new StringBuilder().append(strSiteNo).append(":").append(strItemId).append(":").append(strSalestrNo).append(",");
			if(bookItemIds.indexOf(ids.toString()) < 0){
				bookItemIds.append(ids);
        	}
			bookList.add(book);
		}
		result.setBookItemIds(bookItemIds.toString());
		result.setBookList(bookList);
		result.setBookCount(search.w3GetResultTotalCount(name));
		return result;
	}

	public Call<Info> getPage(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				// Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
				if(strCount.equals("") || strCount.equals("0")){
					strCount = "40";
				}
				if (parameter.getTarget().equalsIgnoreCase("all")) {
					strPage = "1";
					strCount = "5";
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}

	public Call<Info> getPrefix(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX
					, Prefixes.FILTER_SITE_NO 
					, Prefixes.SALESTR_LST
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SRCH_CTG_ITEM_PREFIX
					, Prefixes.BRAND_ID
					, Prefixes.DEVICE_CD
					, Prefixes.SRCH_PSBL_YN
					, Prefixes.SCOM_EXPSR_YN
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}				
				return new Info(sb.toString(),1);
			}
		};
	}

	public Call<Info> getFilter() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info(Filters.PRC_FILTER.getFilter(parameter));
			}
		};
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

}
