package ssg.search.constant;

import org.apache.commons.lang3.StringUtils;
import ssg.search.parameter.Parameter;

/**
 * 검색 FILTERING 에 사용되는 혜택에 관련한 ENUM
 * @author 131544
 *
 */
@Deprecated
public enum BENEFIT {
	// 무료배송
	SHPP {
		public String getPrefix(Parameter parameter) {
			StringBuffer bf = new StringBuffer();
			bf.append("<FREE_SHPP_YN:contains:Y>");
			return bf.toString();
		}
	},
	// 쿠폰
	COUPON {
		public String getPrefix(Parameter parameter) {
			StringBuffer bf = new StringBuffer();
			String strMbrCoId    = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrCoId(), "");
			String strMbrGrd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrGrdId(), "");
			String strDeviceCd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getDeviceCd(),"");
			String strCkwhere = StringUtils.defaultIfEmpty(parameter.getUserInfo().getCkWhere(), "");
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
			bf.append(makePrefix(strMbrCoId, strMbrGrd, strDeviceCd, strCkwhere, strSiteNo, "200"));
			return bf.toString();
		}
	},
	// 적립금
	MILEAGE {
		public String getPrefix(Parameter parameter) {
			StringBuffer bf = new StringBuffer();
			String strMbrCoId    = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrCoId(), "");
			String strMbrGrd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrGrdId(), "");
			String strDeviceCd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getDeviceCd(),"");
			String strCkwhere = StringUtils.defaultIfEmpty(parameter.getUserInfo().getCkWhere(), "");
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
			bf.append(makePrefix(strMbrCoId, strMbrGrd, strDeviceCd, strCkwhere, strSiteNo, "500"));
			bf.append(">");
			return bf.toString();
		}
	},
	// 신세계 포인트, 포인트
	SPOINT {
		public String getPrefix(Parameter parameter) {
			StringBuffer bf = new StringBuffer();
			String strMbrCoId    = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrCoId(), "");
			String strMbrGrd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrGrdId(), "");
			String strDeviceCd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getDeviceCd(),"");
			String strCkwhere = StringUtils.defaultIfEmpty(parameter.getUserInfo().getCkWhere(), "");
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
			bf.append(makePrefix(strMbrCoId, strMbrGrd, strDeviceCd, strCkwhere, strSiteNo, "504"));
			return bf.toString();
		}
	},
	// 청구할인
	DISCOUNT {
		public String getPrefix(Parameter parameter) {
			StringBuffer bf = new StringBuffer();
			String strMbrCoId    = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrCoId(), "");
			String strMbrGrd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrGrdId(), "");
			String strDeviceCd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getDeviceCd(),"");
			String strCkwhere = StringUtils.defaultIfEmpty(parameter.getUserInfo().getCkWhere(), "");
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
			bf.append(makePrefix(strMbrCoId, strMbrGrd, strDeviceCd, strCkwhere, strSiteNo, "503"));
			return bf.toString();
		}
	},
	// 무이자 할부
	INSTALLMENT {
		public String getPrefix(Parameter parameter) {
			StringBuffer bf = new StringBuffer();
			String strMbrCoId    = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrCoId(), "");
			String strMbrGrd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrGrdId(), "");
			String strDeviceCd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getDeviceCd(),"");
			String strCkwhere = StringUtils.defaultIfEmpty(parameter.getUserInfo().getCkWhere(), "");
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
			bf.append(makePrefix(strMbrCoId, strMbrGrd, strDeviceCd, strCkwhere, strSiteNo, "509"));
			return bf.toString();
		}
	},
	// N+1
	NPLUS {
		public String getPrefix(Parameter parameter) {
			StringBuffer bf = new StringBuffer();
			String strMbrCoId    = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrCoId(), "");
			String strMbrGrd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrGrdId(), "");
			String strDeviceCd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getDeviceCd(),"");
			String strCkwhere = StringUtils.defaultIfEmpty(parameter.getUserInfo().getCkWhere(), "");
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
			bf.append(makePrefix(strMbrCoId, strMbrGrd, strDeviceCd, strCkwhere, strSiteNo, "511"));
			return bf.toString();
		}
	},
	// 사은품
	PRIZE {
		public String getPrefix(Parameter parameter){
			StringBuffer bf = new StringBuffer();
			String strMbrCoId    = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrCoId(), "");
			String strMbrGrd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getMbrGrdId(), "");
			String strDeviceCd = StringUtils.defaultIfEmpty(parameter.getUserInfo().getDeviceCd(),"");
			String strCkwhere = StringUtils.defaultIfEmpty(parameter.getUserInfo().getCkWhere(), "");
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
			bf.append(makePrefix(strMbrCoId, strMbrGrd, strDeviceCd, strCkwhere, strSiteNo, "508"));
			return bf.toString();
		}
	}
	;

	public abstract String getPrefix(Parameter parameter);

	public String makePrefix(String strMbrCoId,String strMbrGrd, String strDeviceCd, String strCkwhere, String strSiteNo, String strOfferCd){
		StringBuffer bf = new StringBuffer();
		bf.append("<OFFER_B2C:contains:ALL").append("-").append(strOfferCd);
		if(!strMbrCoId.equals(""))bf.append("|").append(strMbrCoId).append("-").append(strOfferCd);
		bf.append("><OFFER_B2E:contains:ALL").append("-").append(strOfferCd);
		if(!strMbrCoId.equals(""))bf.append("|").append(strMbrCoId).append("-").append(strOfferCd);
		bf.append("<OFFER_SITE_NO:contains:ALL").append("-").append(strOfferCd);
		if(!strSiteNo.equals(""))bf.append("|").append(strSiteNo).append("-").append(strOfferCd);
		bf.append("><OFFER_MGR_GRD:contains:ALL").append("-").append(strOfferCd);
		if(!strMbrGrd.equals(""))bf.append("|").append(strMbrGrd).append("-").append(strOfferCd);
		bf.append("><OFFER_DEVICE_CD:contains:ALL").append("-").append(strOfferCd);
		if(!strDeviceCd.equals(""))bf.append("|").append(strDeviceCd).append("-").append(strOfferCd);
		bf.append("><OFFER_CHNL_ID:contains:ALL").append("-").append(strOfferCd);
		if(!strCkwhere.equals(""))bf.append("|").append(strCkwhere).append("-").append(strOfferCd);
		bf.append(">");
		return bf.toString();
	}

}
