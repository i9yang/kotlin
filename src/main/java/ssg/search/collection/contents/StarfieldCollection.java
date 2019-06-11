package ssg.search.collection.contents;

import QueryAPI510.Search;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.result.Starfield;
import ssg.search.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class StarfieldCollection implements Collection, Pageable, Prefixable{

	@Override
	public String getCollectionName(Parameter parameter) {
		return "starfield";
	}

	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "starfield";
	}
	
	@Override
	public String[] getDocumentField(Parameter parameter) {
		return new String[] {
				"OFFLINE_STR_ID",
				"OFFLINE_STR_NM",
				"OFFLINE_BLDG_ID",
				"OFFLINE_FLO_ID",
				"OFFLINE_FLO_NM",
				"OFFLINE_SHOP_ID",
				"OFFLINE_SHOP_NM",
				"OFFLINE_SHOP_DESC",
				"OFFLINE_SHOP_STAT_CD",
				"OFFLINE_SHOP_TELNO1",
				"OFFLINE_SHOP_TELNO2",
				"OFFLINE_SHOP_TAG_CNTT",
				"PC_IMG_URL",
				"MOBIL_IMG_URL",
				"BRNLG_IMG_URL",
				"BRNLG_IMG_URL1",
				"BRNLG_IMG_URL2",
				"BRNLG_APP_IMG_URL",
				"SF_SPCL_SHOP_URL",
				"WDAY_STRT_HM",
				"WDAY_END_HM",
				"HOLI_STRT_HM",
				"HOLI_END_HM",
				"SF_STR_LCLS_CTG_ID",
				"SF_STR_LCLS_CTG_NM",
				"SF_STR_MCLS_CTG_ID",
				"SF_STR_MCLS_CTG_NM",
				"SF_PST_XCOR_VAL",
				"SF_PST_YCOR_VAL",
				"SF_PST_POI_LVL",
				"PST_DISP_YN",
				"PST_USE_YN",
				"BRAND_ID",
				"SPCSHOP_URL"
		};
	}

	@Override
	public String[] getSearchField(Parameter parameter) {
		return new String[]{
				"OFFLINE_SHOP_ID",
				"OFFLINE_STR_NM",
				"OFFLINE_SHOP_NM",
				"OFFLINE_SHOP_DESC",
				"OFFLINE_SHOP_TAG_CNTT",
				"SF_STR_LCLS_CTG_NM",
				"SF_STR_MCLS_CTG_NM"
		};
	}

	@Override
	public Result getResult(Search search, String name, Parameter parameter, Result result) {
		List<Starfield> starfieldList = new ArrayList<Starfield>();
		Starfield starfield;
		int count = search.w3GetResultCount(name);
		
		for(int i=0;i<count;i++) {
			starfield = new Starfield();
			
			starfield.setOfflineStrId(search.w3GetField(name, "OFFLINE_STR_ID", i));
			starfield.setOfflineStrNm(search.w3GetField(name, "OFFLINE_STR_NM", i));
			starfield.setOfflineBldgId(search.w3GetField(name, "OFFLINE_BLDG_ID", i));
			starfield.setOfflineFloId(search.w3GetField(name, "OFFLINE_FLO_ID", i));
			starfield.setOfflineFloNm(search.w3GetField(name, "OFFLINE_FLO_NM", i));
			starfield.setOfflineShopId(search.w3GetField(name, "OFFLINE_SHOP_ID", i));
			starfield.setOfflineShopNm(search.w3GetField(name, "OFFLINE_SHOP_NM", i));
			starfield.setOfflineShopDesc(search.w3GetField(name, "OFFLINE_SHOP_DESC", i));
			starfield.setOfflineShopStatCd(search.w3GetField(name, "OFFLINE_SHOP_STAT_CD", i));
			starfield.setOfflineShopTelNo1(search.w3GetField(name, "OFFLINE_SHOP_TELNO1", i));
			starfield.setOfflineShopTelNo2(search.w3GetField(name, "OFFLINE_SHOP_TELNO2", i));
			starfield.setOfflineShopTagCntt(search.w3GetField(name, "OFFLINE_SHOP_TAG_CNTT", i));
			starfield.setPcImgUrl(search.w3GetField(name, "PC_IMG_URL", i));
			starfield.setMobilImgUrl(search.w3GetField(name, "MOBIL_IMG_URL", i));
			starfield.setBrnlgImgUrl(search.w3GetField(name, "BRNLG_IMG_URL", i));
			starfield.setBrnlgImgUrl1(search.w3GetField(name, "BRNLG_IMG_URL1", i));
			starfield.setBrnlgImgUrl2(search.w3GetField(name, "BRNLG_IMG_URL2", i));
			starfield.setBrnlgAppImgUrl(search.w3GetField(name, "BRNLG_APP_IMG_URL", i));
			starfield.setSfSpclShopUrl(search.w3GetField(name, "SF_SPCL_SHOP_URL", i));
			starfield.setWdayStrtHm(search.w3GetField(name, "WDAY_STRT_HM", i));
			starfield.setWdayEndHm(search.w3GetField(name, "WDAY_END_HM", i));
			starfield.setHoliStrtHm(search.w3GetField(name, "HOLI_STRT_HM", i));
			starfield.setHoliEndHm(search.w3GetField(name, "HOLI_END_HM", i));
			starfield.setSfStrLclsCtgId(search.w3GetField(name, "SF_STR_LCLS_CTG_ID", i));
			starfield.setSfStrLclsCtgNm(search.w3GetField(name, "SF_STR_LCLS_CTG_NM", i));
			starfield.setSfStrMclsCtgId(search.w3GetField(name, "SF_STR_MCLS_CTG_ID", i));
			starfield.setSfStrMclsCtgNm(search.w3GetField(name, "SF_STR_MCLS_CTG_NM", i));
			starfield.setSfPstXcorVal(search.w3GetField(name, "SF_PST_XCOR_VAL", i));
			starfield.setSfPstYcorVal(search.w3GetField(name, "SF_PST_YCOR_VAL", i));
			starfield.setSfPstPoiLvl(search.w3GetField(name, "SF_PST_POI_LVL", i));
			starfield.setPstDispYn(search.w3GetField(name, "PST_DISP_YN", i));
			starfield.setPstUseYn(search.w3GetField(name, "PST_USE_YN", i));
			starfield.setBrandId(search.w3GetField(name, "BRAND_ID", i));
			starfield.setSpcshopUrl(search.w3GetField(name, "SPCSHOP_URL", i));
			starfieldList.add(starfield);
		}
		result.setStarfieldCount(search.w3GetResultTotalCount(name));
		result.setStarfieldList(starfieldList);
		return result;
	}

	@Override
	public Call<Info> getPage() {
		return new Call<Info>() {
			@Override
			public Info apply(Parameter parameter) {
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "80");
				String strTarget = StringUtils.defaultIfEmpty(parameter.getTarget(), "").toLowerCase();
				// Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
				if(strCount.equals("") || strCount.equals("0") || strTarget.equalsIgnoreCase("all")) {
					strPage  = "1";
					strCount = "80";
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}

	@Override
	public Call<Info> getPrefix() {
		return new Call<Info>() {
			@Override
			public Info apply(Parameter parameter) {
				StringBuilder prefix = new StringBuilder();
				
				String offlineStrId = parameter.getOfflineStrId();
				String offlineBldgId = parameter.getOfflineBldgId();
				String offlineFloId = parameter.getOfflineFloId();
				String sfStrLclsCtgId = parameter.getSfStrLclsCtgId();
				String sfStrMclsCtgId = parameter.getSfStrMclsCtgId();
				
				if(StringUtils.isNotBlank(offlineStrId)) 	prefix.append("<OFFLINE_STR_ID:contains:").append(offlineStrId).append(">");
				if(StringUtils.isNotBlank(offlineBldgId)) 	prefix.append("<OFFLINE_BLDG_ID:contains:").append(offlineBldgId).append(">");
				if(StringUtils.isNotBlank(offlineFloId)) 	prefix.append("<OFFLINE_FLO_ID:contains:").append(offlineFloId).append(">");
				if(StringUtils.isNotBlank(sfStrLclsCtgId)) 	prefix.append("<SF_STR_LCLS_CTG_ID:contains:").append(sfStrLclsCtgId).append(">");
				if(StringUtils.isNotBlank(sfStrMclsCtgId))	prefix.append("<SF_STR_MCLS_CTG_ID:contains:").append(sfStrMclsCtgId).append(">");
				
				return new Info(prefix.toString(), 1);
			}
		};
	}

}
