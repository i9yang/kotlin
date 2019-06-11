package ssg.search.constant;

import org.apache.commons.lang3.StringUtils;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.parameter.Parameter;

import java.util.StringTokenizer;

@Deprecated
public enum SITE {
	SSG("6005"){
		public String getSiteContentNo() {
			return "6001|6002|6003|6004|6005";
		}
		public String getSitePrefixNm(Parameter parameter){
		    return "SCOM";
		}
		public String getSitePrefixSpecificNm(Parameter parameter){
            return "SCOM";
		}
        public String getCategoryBoost(){
            return "SSGBOOST";
        }
        public String getSiteSalestrMallTab(Parameter parameter){
        	StringBuilder sb = new StringBuilder();
            FrontUserInfo userInfo = parameter.getUserInfo();
            // 이마트, 트레이더스, 분스
            String emSalestrNo      = userInfo.getEmSaleStrNo();
            String emRsvtShppPsblYn = userInfo.getEmRsvtShppPsblYn();
            String trSalestrNo      = userInfo.getTrSaleStrNo();
            String trRsvtShppPsblYn = userInfo.getTrRsvtShppPsblYn();
            String bnSalestrNo      = userInfo.getBnSaleStrNo();
            String bnRsvtShppPsblYn = userInfo.getBnRsvtShppPsblYn();
            // 몰별은 기본 세팅만 하도록 한다.
            sb.append("6005|").append(emSalestrNo).append(emRsvtShppPsblYn)
            .append("|").append(trSalestrNo).append(trRsvtShppPsblYn)
            .append("|").append(bnSalestrNo).append(bnRsvtShppPsblYn)
            .append("|").append("0001");
            return sb.toString();
        }
        public String getSiteSalestrPrefixNm(Parameter parameter) {
            StringBuilder sb = new StringBuilder();
            FrontUserInfo userInfo = parameter.getUserInfo();
            // 이마트, 트레이더스, 분스
            String emSalestrNo      = userInfo.getEmSaleStrNo();
            String emRsvtShppPsblYn = userInfo.getEmRsvtShppPsblYn();
            String trSalestrNo      = userInfo.getTrSaleStrNo();
            String trRsvtShppPsblYn = userInfo.getTrRsvtShppPsblYn();
            String bnSalestrNo      = userInfo.getBnSaleStrNo();
            String bnRsvtShppPsblYn = userInfo.getBnRsvtShppPsblYn();
            // 신몰, 신백
            String deptSalestrNo = StringUtils.defaultIfEmpty(parameter.getSalestrNo(), "0001");
            String pickuSalestrNo = StringUtils.defaultIfEmpty(parameter.getPickuSalestr(), "");
            //if(!pickuSalestrNo.equals(""))deptSalestrNo = pickuSalestrNo;
			sb.append("6005|").append(emSalestrNo).append(emRsvtShppPsblYn)
					.append("|").append(trSalestrNo).append(trRsvtShppPsblYn)
					.append("|").append(bnSalestrNo).append(bnRsvtShppPsblYn)
					.append("|").append(deptSalestrNo);
            return sb.toString();
        }
        public String getSiteSalestrPrefixSpecificNm(Parameter parameter) {
            StringBuilder sb = new StringBuilder();
            FrontUserInfo userInfo = parameter.getUserInfo();
            // 이마트, 트레이더스, 분스
            String emSalestrNo      = userInfo.getEmSaleStrNo();
            String emRsvtShppPsblYn = userInfo.getEmRsvtShppPsblYn();
            String trSalestrNo      = userInfo.getTrSaleStrNo();
            String trRsvtShppPsblYn = userInfo.getTrRsvtShppPsblYn();
            String bnSalestrNo      = userInfo.getBnSaleStrNo();
            String bnRsvtShppPsblYn = userInfo.getBnRsvtShppPsblYn();
            // 신몰, 신백
            String deptSalestrNo = StringUtils.defaultIfEmpty(parameter.getSalestrNo(), "0001");
            sb.append("6005|").append(emSalestrNo).append(emRsvtShppPsblYn)
            .append("|").append(trSalestrNo).append(trRsvtShppPsblYn)
            .append("|").append(bnSalestrNo).append(bnRsvtShppPsblYn)
            .append("|").append(deptSalestrNo);
        	
            return sb.toString();
        }
	},
	SHINSEGAE("6004"){
		public String getSiteContentNo() {
			return "6004|6009";
		}
		public String getSitePrefixNm(Parameter parameter){
		    return "SHIN";
        }
		public String getSitePrefixSpecificNm(Parameter parameter){
		    if(parameter.getSiteNo().equals("6005")){
		        return "SCOM-SM";
		    }else return "SM";
        }
		public String getCategoryBoost() {
            return "SHINBOOST";
        }
		public String getSiteSalestrMallTab(Parameter parameter){
			StringBuilder sb = new StringBuilder();
            String deptSalestrNo = StringUtils.defaultIfEmpty(parameter.getSalestrNo(), "0001");
            sb.append("6005|").append(deptSalestrNo);
            return sb.toString();
		}
        public String getSiteSalestrPrefixNm(Parameter parameter) {
            StringBuilder sb = new StringBuilder();
            String deptSalestrNo = StringUtils.defaultIfEmpty(parameter.getSalestrNo(), "0001");
            String pickuSalestrNo = StringUtils.defaultIfEmpty(parameter.getPickuSalestr(), "");
			//if(!pickuSalestrNo.equals(""))deptSalestrNo = pickuSalestrNo;
			sb.append("6005|").append(deptSalestrNo);
            return sb.toString();
        }
        public String getSiteSalestrPrefixSpecificNm(Parameter parameter) {
            return "6005";
        }
	},
	DEPARTMENT("6009"){
		public String getSiteContentNo() {
			return "6009";
		}
		public String getSitePrefixNm(Parameter parameter){
		    return "SD";
        }
		public String getSitePrefixSpecificNm(Parameter parameter){
		    if(parameter.getSiteNo().equals("6005")){
                return "SCOM-SD";
            }else return "SD";
        }
		public String getCategoryBoost() {
            return "SHINBOOST";
        }
		public String getSiteSalestrMallTab(Parameter parameter){
			StringBuilder sb = new StringBuilder();
            // 신백
            String deptSalestrNo = StringUtils.defaultIfEmpty(parameter.getSalestrNo(), "0001");
            sb.append("6005|").append(deptSalestrNo);
            return sb.toString();
		}
        public String getSiteSalestrPrefixNm(Parameter parameter) {
            StringBuilder sb = new StringBuilder();
            // 신백
            String deptSalestrNo = StringUtils.defaultIfEmpty(parameter.getSalestrNo(), "0001");
            String pickuSalestrNo = StringUtils.defaultIfEmpty(parameter.getPickuSalestr(), "");
			//if(!pickuSalestrNo.equals(""))deptSalestrNo = pickuSalestrNo;
			sb.append(deptSalestrNo);
            return sb.toString();
        }
        public String getSiteSalestrPrefixSpecificNm(Parameter parameter) {
            StringBuilder sb = new StringBuilder();
            // 신백
            String deptSalestrNo = StringUtils.defaultIfEmpty(parameter.getSalestrNo(), "0001");
            sb.append("6005|").append(deptSalestrNo);
            return sb.toString();
        }
	},
	EMART("6001"){
		public String getSiteContentNo() {
			return "6001|6002";   //@ BOOT개발시확인 
		}
		public String getSitePrefixNm(Parameter parameter){
		    return "EMALL";
        }
		public String getSitePrefixSpecificNm(Parameter parameter){
		    if(parameter.getSiteNo().equals("6005")){
                return "SCOM-EM";
            }else return "EM";
        }
		public String getCategoryBoost() {
            return "EMARTBOOST";
        }
		public String getSiteSalestrMallTab(Parameter parameter){
			StringBuilder sb = new StringBuilder();
            FrontUserInfo userInfo = parameter.getUserInfo();
            // 이마트, 트레이더스, 분스
            String emSalestrNo = userInfo.getEmSaleStrNo();
            String emRsvtShppPsblYn = userInfo.getEmRsvtShppPsblYn();
            String trSalestrNo = userInfo.getTrSaleStrNo();
            String trRsvtShppPsblYn = userInfo.getTrRsvtShppPsblYn();
            String bnSalestrNo = userInfo.getBnSaleStrNo();
            String bnRsvtShppPsblYn = userInfo.getBnRsvtShppPsblYn();
            sb.append("6005|").append(emSalestrNo).append(emRsvtShppPsblYn)
                .append("|").append(trSalestrNo).append(trRsvtShppPsblYn)
                .append("|").append(bnSalestrNo).append(bnRsvtShppPsblYn);
            return sb.toString();
		}
        public String getSiteSalestrPrefixNm(Parameter parameter) {
            StringBuilder sb = new StringBuilder();
            FrontUserInfo userInfo = parameter.getUserInfo();
            // 이마트, 트레이더스, 분스
            String emSalestrNo = userInfo.getEmSaleStrNo();
            String emRsvtShppPsblYn = userInfo.getEmRsvtShppPsblYn();
            String trSalestrNo = userInfo.getTrSaleStrNo();
            String trRsvtShppPsblYn = userInfo.getTrRsvtShppPsblYn();
            String bnSalestrNo = userInfo.getBnSaleStrNo();
            String bnRsvtShppPsblYn = userInfo.getBnRsvtShppPsblYn();
            sb.append("6005|").append(emSalestrNo).append(emRsvtShppPsblYn)
                .append("|").append(trSalestrNo).append(trRsvtShppPsblYn)
                .append("|").append(bnSalestrNo).append(bnRsvtShppPsblYn);
            return sb.toString();
        }
        public String getSiteSalestrPrefixSpecificNm(Parameter parameter) {
            StringBuilder sb = new StringBuilder();
            FrontUserInfo userInfo = parameter.getUserInfo();
            String emSalestrNo = userInfo.getEmSaleStrNo();
            String emRsvtShppPsblYn = userInfo.getEmRsvtShppPsblYn();
            sb.append("6005|").append(emSalestrNo).append(emRsvtShppPsblYn);
            return sb.toString();
        }
	},
	TRADERS("6002"){
		public String getSiteContentNo() {
			return "6002";
		}
		public String getSitePrefixNm(Parameter parameter){
		    return "EMALL";
        }
		public String getSitePrefixSpecificNm(Parameter parameter){
		    if(parameter.getSiteNo().equals("6005")){
                return "SCOM-TR";
            }else return "TR";
        }
		public String getCategoryBoost() {
            return "EMARTBOOST";
        }
		public String getSiteSalestrMallTab(Parameter parameter){
			StringBuilder sb = new StringBuilder();
            FrontUserInfo userInfo = parameter.getUserInfo();
            // 이마트, 트레이더스, 분스
            String emSalestrNo = userInfo.getEmSaleStrNo();
            String emRsvtShppPsblYn = userInfo.getEmRsvtShppPsblYn();
            String trSalestrNo = userInfo.getTrSaleStrNo();
            String trRsvtShppPsblYn = userInfo.getTrRsvtShppPsblYn();
            String bnSalestrNo = userInfo.getBnSaleStrNo();
            String bnRsvtShppPsblYn = userInfo.getBnRsvtShppPsblYn();
            sb.append("6005|").append(emSalestrNo).append(emRsvtShppPsblYn)
                .append("|").append(trSalestrNo).append(trRsvtShppPsblYn)
                .append("|").append(bnSalestrNo).append(bnRsvtShppPsblYn);
            return sb.toString();
		}
        public String getSiteSalestrPrefixNm(Parameter parameter) {
            StringBuilder sb = new StringBuilder();
            FrontUserInfo userInfo = parameter.getUserInfo();
            // 이마트, 트레이더스, 분스
            String emSalestrNo = userInfo.getEmSaleStrNo();
            String emRsvtShppPsblYn = userInfo.getEmRsvtShppPsblYn();
            String trSalestrNo = userInfo.getTrSaleStrNo();
            String trRsvtShppPsblYn = userInfo.getTrRsvtShppPsblYn();
            String bnSalestrNo = userInfo.getBnSaleStrNo();
            String bnRsvtShppPsblYn = userInfo.getBnRsvtShppPsblYn();
            sb.append("6005|").append(emSalestrNo).append(emRsvtShppPsblYn)
                .append("|").append(trSalestrNo).append(trRsvtShppPsblYn)
                .append("|").append(bnSalestrNo).append(bnRsvtShppPsblYn);
            return sb.toString();
        }
        public String getSiteSalestrPrefixSpecificNm(Parameter parameter) {
            StringBuilder sb = new StringBuilder();
            FrontUserInfo userInfo = parameter.getUserInfo();
            String trSalestrNo = userInfo.getTrSaleStrNo();
            String trRsvtShppPsblYn = userInfo.getTrRsvtShppPsblYn();
            sb.append("6005|").append(trSalestrNo).append(trRsvtShppPsblYn);
            return sb.toString();
        }
	},
	BOONS("6003"){
		public String getSiteContentNo() {
			return "6003";
		}
		public String getSitePrefixNm(Parameter parameter){
		    return "EMALL";
        }
		public String getSitePrefixSpecificNm(Parameter parameter){
		    if(parameter.getSiteNo().equals("6005")){
                return "SCOM-BN";
            }else return "BN";
        }
		public String getCategoryBoost() {
            return "EMARTBOOST";
        }
		public String getSiteSalestrMallTab(Parameter parameter){
			StringBuilder sb = new StringBuilder();
            FrontUserInfo userInfo = parameter.getUserInfo();
            // 이마트, 트레이더스, 분스
            String emSalestrNo = userInfo.getEmSaleStrNo();
            String emRsvtShppPsblYn = userInfo.getEmRsvtShppPsblYn();
            String trSalestrNo = userInfo.getTrSaleStrNo();
            String trRsvtShppPsblYn = userInfo.getTrRsvtShppPsblYn();
            String bnSalestrNo = userInfo.getBnSaleStrNo();
            String bnRsvtShppPsblYn = userInfo.getBnRsvtShppPsblYn();
            sb.append("6005|").append(emSalestrNo).append(emRsvtShppPsblYn)
                .append("|").append(trSalestrNo).append(trRsvtShppPsblYn)
                .append("|").append(bnSalestrNo).append(bnRsvtShppPsblYn);
            return sb.toString();
		}
        public String getSiteSalestrPrefixNm(Parameter parameter) {
            StringBuilder sb = new StringBuilder();
            FrontUserInfo userInfo = parameter.getUserInfo();
            // 이마트, 트레이더스, 분스
            String emSalestrNo = userInfo.getEmSaleStrNo();
            String emRsvtShppPsblYn = userInfo.getEmRsvtShppPsblYn();
            String trSalestrNo = userInfo.getTrSaleStrNo();
            String trRsvtShppPsblYn = userInfo.getTrRsvtShppPsblYn();
            String bnSalestrNo = userInfo.getBnSaleStrNo();
            String bnRsvtShppPsblYn = userInfo.getBnRsvtShppPsblYn();
            sb.append("6005|").append(emSalestrNo).append(emRsvtShppPsblYn)
                .append("|").append(trSalestrNo).append(trRsvtShppPsblYn)
                .append("|").append(bnSalestrNo).append(bnRsvtShppPsblYn);
            return sb.toString();
        }
        public String getSiteSalestrPrefixSpecificNm(Parameter parameter) {
            StringBuilder sb = new StringBuilder();
            FrontUserInfo userInfo = parameter.getUserInfo();
            String bnSalestrNo = userInfo.getBnSaleStrNo();
            String bnRsvtShppPsblYn = userInfo.getBnRsvtShppPsblYn();
            sb.append("6005|").append(bnSalestrNo).append(bnRsvtShppPsblYn);
            return sb.toString();
        }
	},
	HOWDY("6100"){
		public String getSiteContentNo() {
			return "6100";
		}
		public String getSitePrefixNm(Parameter parameter){
		    return "HOWDY";
        }
		public String getSitePrefixSpecificNm(Parameter parameter){
		    if(parameter.getSiteNo().equals("6005")){
                return "SCOM-HD";
            }else return "HOWDY";
        }
		public String getCategoryBoost() {
            return "HOWDYBOOST";
        }
		public String getSiteSalestrMallTab(Parameter parameter){
            return "6005";
		}
        public String getSiteSalestrPrefixNm(Parameter parameter) {
            return "6005";
        }
        public String getSiteSalestrPrefixSpecificNm(Parameter parameter) {
            return "6005";
        }
	};

	String siteNo;
	SITE(String siteNo){
		this.siteNo = siteNo;
	}

	public String getSiteNo(){
		return this.siteNo;
	}

	public abstract String getSiteContentNo();
	public abstract String getSitePrefixNm(Parameter parameter);
	public abstract String getSitePrefixSpecificNm(Parameter parameter);
	public abstract String getSiteSalestrPrefixNm(Parameter parameter);
	public abstract String getSiteSalestrPrefixSpecificNm(Parameter parameter);
	public abstract String getSiteSalestrMallTab(Parameter parameter);
	public abstract String getCategoryBoost();
	
	public String getSitePrefixItem(){
	    StringBuilder prefix = new StringBuilder();
        int ct = 0;
        prefix.append("<").append("SITE_NO").append(":contains:");
        for(StringTokenizer st = new StringTokenizer(this.getSiteContentNo(),"|");st.hasMoreElements();){
            String t = st.nextToken();
            if(ct>0)prefix.append("|");
            prefix.append(t);
            ct++;
        }
        prefix.append(">");
        return prefix.toString();
	}

	public String getSitePrefix(){
		StringBuilder prefix = new StringBuilder();
		prefix.append("<").append("SITE_NO").append(":contains:").append(this.getSiteNo()).append(">");
		return prefix.toString();
	}
}
