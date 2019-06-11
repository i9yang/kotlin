package ssg.search.collection.contents;

import QueryAPI510.Search;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.constant.Sorts;
import ssg.search.function.Highlightable;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.function.Sortable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.result.ShoppingMagazine;
import ssg.search.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class LifeMagazineCollection implements Collection, Pageable, Prefixable, Highlightable, Sortable{

	public String getCollectionName(Parameter parameter) {
		return "shoppingmagazine";
	}

	public String getCollectionAliasName(Parameter parameter) {
		return "lifemagazine";
	}

	public String[] getDocumentField(Parameter parameter) {
		return new String[]{
			"SHPG_MGZ_ID",
			"SHPG_MGZ_NM",
			"SRCHWD",
			"IMG_FILE_NM",
			"MAI_TITLE_NM_1",
			"MAI_TITLE_NM_2",
			"BANR_DESC",
			"DISP_STRT_DTS",
			"LNKD_URL", 
			"SHPG_CONTENTS_CD",
			"SHPG_CONTENTS_NM",
			"SHPG_CATEGORY_CD",
			"SHPG_CATEGORY_NM",
			"SHPG_MGZ_TYPE_CD"
		};
	}

	public String[] getSearchField(Parameter parameter) {
		return new String[]{
			"SHPG_MGZ_ID",
			"MAI_TITLE_NM_1",
			"MAI_TITLE_NM_2",
			"BANR_DESC"
		};
	}

	public Call<Info> getPrefix() {
		return new Call<Info>(){
			public Info apply(Parameter parameter) {
				StringBuilder prefix = new StringBuilder();

				//접속 사이트
				String siteNo = parameter.getSiteNo();
				if (StringUtils.isNotBlank(siteNo)) {
				    if(!siteNo.equals("6100")) {
				        prefix.append("<APL_SITE_NO_LST:contains:").append(siteNo).append("|6100").append(">");
				    }else {
				        prefix.append("<APL_SITE_NO_LST:contains:").append(siteNo).append(">");
				    }
				}
				
				//접속 미디어 체크 10 : PC, 20 : 모바일 - 웹과 앱을 따로 구분하지 않는다.
				String aplTgtMediaCd = parameter.getAplTgtMediaCd();
				if (StringUtils.isNotBlank(aplTgtMediaCd)) {
					prefix.append("<APL_MEDIA_CD:contains:").append(aplTgtMediaCd).append(">");
				}
				
				//쇼핑매거진 유형 코드 D324 10:매거진, 20:하우디 저널
                //pc만 (매거진+하우디) 보여줌, 하우디 검색에서는 하우디만
                String shpgMgzTypeCd = StringUtils.defaultIfEmpty(parameter.getShpgMgzTypeCd(),  "10");
                if( !aplTgtMediaCd.equals("10") || siteNo.equals("6100")) {
                    prefix.append("<SHPG_MGZ_TYPE_CD:contains:").append(shpgMgzTypeCd).append(">");
                }
	            
				return new Info(prefix.toString(),1);
			}
		};
	}
	
	public Call<Info> getPage(){
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				String strTarget = StringUtils.defaultIfEmpty(parameter.getTarget(), "").toLowerCase();
				String shpgMgzTypeCd = StringUtils.defaultIfEmpty(parameter.getShpgMgzTypeCd(),  "10");
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				
				if(shpgMgzTypeCd.equals("20")){
					//하우디는 무조건 전체 내려줌
					strCount = "100";
					strPage = "1";
				}else if (strTarget.equalsIgnoreCase("all")) {
					strPage  = "1";
					strCount = "100";
				}else{
					// Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
					if(strCount.equals("") || strCount.equals("0")){
						strCount = "20";
					}
				}
				
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}
	
	public Call<Info> getSort() {
		return new Call<Info>(){
			public Info apply(Parameter parameter) {
				List<Sort> sortList = Lists.newArrayList();
				sortList.add(Sorts.DISP_STRT_DTS.getSort(parameter));
				return new Info(sortList);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result) {
		List<ShoppingMagazine> shoppingMagazineList = new ArrayList<ShoppingMagazine>();
		ShoppingMagazine shoppingMagazine;
		int count = search.w3GetResultCount(name);
		
		for(int i=0;i<count;i++){
			shoppingMagazine = new ShoppingMagazine();
			
			shoppingMagazine.setShpgMgzId(search.w3GetField(name, "SHPG_MGZ_ID", i));
			shoppingMagazine.setShpgMgzNm(search.w3GetField(name, "SHPG_MGZ_NM", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
			shoppingMagazine.setShpgMgzTypeCd(search.w3GetField(name, "SHPG_MGZ_TYPE_CD", i));
			shoppingMagazine.setSrchwd(search.w3GetField(name, "SRCHWD", i));
			shoppingMagazine.setDispStrtDts(search.w3GetField(name, "DISP_STRT_DTS", i));
			shoppingMagazine.setDispEndDts(search.w3GetField(name, "DISP_END_DTS", i));
			shoppingMagazine.setImgFileNm(search.w3GetField(name, "IMG_FILE_NM", i));
			shoppingMagazine.setMaiTitleNm1(search.w3GetField(name, "MAI_TITLE_NM_1", i));
			shoppingMagazine.setMaiTitleNm2(search.w3GetField(name, "MAI_TITLE_NM_2", i));
			shoppingMagazine.setLnkdUrl(search.w3GetField(name, "LNKD_URL", i));
			shoppingMagazine.setBanrDesc(search.w3GetField(name, "BANR_DESC", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
			shoppingMagazine.setAplSiteNoLst(search.w3GetField(name, "APL_SITE_NO_LST", i));
			shoppingMagazine.setShpgContentsCd(search.w3GetField(name, "SHPG_CONTENTS_CD", i));
			shoppingMagazine.setShpgContentsNm(search.w3GetField(name, "SHPG_CONTENTS_NM", i));
			shoppingMagazine.setShpgCategoryCd(search.w3GetField(name, "SHPG_CATEGORY_CD", i));
			shoppingMagazine.setShpgCategoryNm(search.w3GetField(name, "SHPG_CATEGORY_NM", i));
			
			shoppingMagazineList.add(shoppingMagazine);
			
			
		}
		result.setLifeMagazineCount(search.w3GetResultTotalCount(name));
		result.setLifeMagazineList(shoppingMagazineList);
		return result;
	}

}
