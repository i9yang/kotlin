package ssg.search.collection.contents;

import QueryAPI510.Search;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.constant.Targets;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Pnshop;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.List;

public class PnshopCollection implements Collection, Prefixable, Pageable{

	public String getCollectionName(Parameter parameter){
		return "pnshop";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "pnshop";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"SITE_NO",
			"ORI_SITE_NO",
			"DISP_CMPT_ID",
			"DISP_CMPT_NM",
			"IMG_FILE_NM1",
			"IMG_FILE_NM2",
			"IMG_FILE_NM3",
			"IMG_FILE_NM4",
			"MOD_DTS",
			"DISP_CMPT_TYPE_DTL_LST",
			"MOBILE_DISPLAY_YN",
			"MAI_TITLE_NM_1",
			"MAI_TITLE_NM_2",
			"SUBTITL_NM_1",
			"SUBTITL_NM_2",
			"OSMU_YN",
			"PNSHOP_TYPE_CD"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{
		    "DISP_CMPT_ID",
			"DISP_CMPT_NM",
			"SRCHWD_NM",
			"BRAND_LST",
			"BANR_CNTT"
		};
	}
	
	public Call<Info> getPrefix() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				StringBuilder prefix = new StringBuilder();
				String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
				Targets targets = CollectionUtils.getTargets(parameter);
				// SHIN
				if(strSiteNo.equals("6004")){
					if(targets.equals(Targets.ALL)){
						prefix.append("<SITE_NO:contains:6004|6009>");
					} else {
						prefix.append("<SITE_NO:contains:6004>");
					}
				}
				// SSG
				else if(strSiteNo.equals("6005")){
					prefix.append("<SITE_NO:contains:6005>");
					
					//PC는 배너사이즈가 통일이 되서 백화점도 한번에 호출
					if(targets.equals(Targets.ALL)){
						prefix.append("<ORI_SITE_NO:contains:6001|6002|6003|6004|6009|6100|6200|6300>");
					} else {
						prefix.append("<ORI_SITE_NO:contains:6001|6002|6003|6004|6100|6200|6300>");
					}
				}
				// E/T/B
				else if(strSiteNo.equals("6001")||strSiteNo.equals("6002")){   //@ BOOT개발시확인 
					String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
	                if(!strFilterSiteNo.equals("")){
	                    prefix.append("<SITE_NO:contains:").append(strFilterSiteNo).append(">");
	                }else{
	                	prefix.append("<SITE_NO:contains:6001|6002>");
	                }
				}
				else{
					prefix.append("<SITE_NO:contains:").append(strSiteNo).append(">");
				}
				// SET B2C, B2E
				prefix.append(Prefixes.MBR_CO_TYPE_CONTENTS.getPrefix(parameter));
				// SET DISP_CTG_LST
				prefix.append(Prefixes.DISP_CTG_LST.getPrefix(parameter));

				// 도서인 경우에는 BOOK_YN 사용한다.
				if(targets.equals(Targets.BOOK) || targets.equals(Targets.MOBILE_BOOK)){
					prefix.append("<BOOK_YN:contains:Y>");
				}
				//OSMU 기획전
				prefix.append("<OSMU_YN:contains:Y>");
				return new Info(prefix.toString(),1);
			}
		};
	}

	public Result getResult(Search search, String name, Parameter parameter, Result result){
		List<Pnshop> pnshopList = Lists.newArrayList();
	    Pnshop pnshop;
	    int count = search.w3GetResultCount(name);
	    for(int i=0;i<count;i++){
	    	pnshop = new Pnshop();
			pnshop.setSiteNo(search.w3GetField(name, "SITE_NO", i));
			pnshop.setDispCmptId(search.w3GetField(name, "DISP_CMPT_ID", i));
			pnshop.setDispCmptNm(search.w3GetField(name, "DISP_CMPT_NM", i));
			pnshop.setDispCmptTypeDtlLst(search.w3GetField(name, "DISP_CMPT_TYPE_DTL_LST", i));
			
			if(parameter.getTarget().equals("mobile_all" )|| parameter.getTarget().equals("mobile_pnshop" )){
				String imgFileNm = StringUtils.defaultIfEmpty(search.w3GetField(name, "IMG_FILE_NM1", i), "");
				if(!imgFileNm.equals("")) imgFileNm = "http://static.ssgcdn.com"+ imgFileNm;
				pnshop.setImgFileNm1(imgFileNm);
			}else{
				pnshop.setImgFileNm1(search.w3GetField(name, "IMG_FILE_NM1", i));
			}
			pnshop.setImgFileNm2(search.w3GetField(name, "IMG_FILE_NM2", i));
			pnshop.setImgFileNm3(search.w3GetField(name, "IMG_FILE_NM3", i));
			pnshop.setImgFileNm4(search.w3GetField(name, "IMG_FILE_NM4", i));
			pnshop.setModDts(search.w3GetField(name, "MOD_DTS", i));
			pnshop.setMobileDisplayYn(search.w3GetField(name, "MOBILE_DISPLAY_YN", i));
			pnshop.setOriSiteNo(search.w3GetField(name, "ORI_SITE_NO", i));
			pnshop.setMaiTitleNm1(search.w3GetField(name, "MAI_TITLE_NM_1", i));
			pnshop.setMaiTitleNm2(search.w3GetField(name, "MAI_TITLE_NM_2", i));
			pnshop.setSubtitlNm1(search.w3GetField(name, "SUBTITL_NM_1", i));
			pnshop.setSubtitlNm2(search.w3GetField(name, "SUBTITL_NM_2", i));
			pnshop.setOsmuYn(search.w3GetField(name, "OSMU_YN", i));
			pnshop.setPnshopTypeCd(search.w3GetField(name, "PNSHOP_TYPE_CD", i));
			pnshopList.add(pnshop);
        }
	    result.setPnshopCount(search.w3GetResultTotalCount(name));
        result.setPnshopList(pnshopList);
		return result;
	}

	public Call<Info> getPage(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strPage  = "1";
				String strCount = "10";
				String strTarget = StringUtils.defaultIfEmpty(parameter.getTarget(), "").toLowerCase();
				// PNSHOP TARGET 일때만 값을 세팅한다.
				if((strTarget.startsWith("pnshop") || strTarget.startsWith("mobile_pnshop") ) && !strCount.equals("") ){
					strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
					strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				} else if (strTarget.equalsIgnoreCase("all")) {
					strPage  = "1";
					strCount = "100";
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}
}
