package ssg.framework.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FrontUserInfo {

    public final static Logger log = LoggerFactory.getLogger(FrontUserInfo.class);

    private String serverName;
    public static final String PREFIX_CACHE_KEY = "front-";

    private String ver;
    //@XStreamOmitField
    //@JsonIgnore
    public String controllerCacheKey;

    //[쿠키로 처리될 항목]
    private String fsId;
    private String fsIdDay;
    private String mbrId;               // 회원 id
    private String mbrType;             // 회원 타입 ( B2C || B2E )
    private String mbrCoId;             // B2C인 경우 제휴 업체 아이디, B2E인 경우 회원사 아이디
    private String adultYn;               // 성인인증여부
    private String loginTime;       // 현재세션 로그인시각
    private String emplYn;                  // 임직원 여부
    private String mbrTypeCd;           // 로그인 타입 ( M002 )
    private String familyYn;            // 패밀리 사이트 가입 여부

    private String aloginToken;            // 자동 로그인
    private String loginYn; // 로그인 여부
    private String userId;

    /**
     * 영구 로그인 유무
     */
    private String keepLogin;
    /**
     * Y 값이면 refresh 대상이 된다.
     */
    private String mobileLoginRefresh;
    public static final String REFRESH_NONE = "0";
    public static final String REFRESH_NEW = "1";
    public static final String REFRESH_FS0 = "2";
    public static final String REFRESH_FS1 = "3";

    //FS1
    private String mbrNm;               // 회원 이름
    private String mbrLoginId;          // 로그인 id
    private String mbrcoLogoImgPathNm;  // b2e 회원사 로고 이미지 패스
    private String mbrSvcSiteLst;      //회원서비스사이트목록 n개존재
    private String deviceCd;                // 회원 접속 장비 정보  Z021
    private String deviceDivCd;             // 10-pc,20-mobile,30-app
    private String deviceDtlCd;                // 회원 접속 상세 장비 정보  Z021
    private String ckWhere;                // 제휴 유입채널코드
    private String pushWhere;                // push 유입채널코드
    private String mckWhere;
    private String sid;                    // 제휴 유입채널코드
    private String plzcmtLitmitYn;          // 플리즈 코멘트 제한
    private String perdcShppYn;         // 정기배송 여부
    private String mbrcoNm;
    private String mbrGrdCd;            // 회원 등급 id
    private String mvnoYn;			// MVNO 클럽 유무
    private String sguid;			// 삼성 냉장고 로그인 식별 정보
    private String vvipYn;			// vvip 여부
    private String birthday;			// 생일 날짜
    private String dptVipYn;		// 백화점 VIP 등급

    public String dmId;
    public boolean updateAffP;          // 채널 제휴용 업데이트 조건
    private String affP1;          // 채널 제휴용
    private String affP2;          // 채널 제휴용
    private String affP3;          // 채널 제휴용
    private String affP4;          // 채널 제휴용
    private String affP5;          // 채널 제휴용

    //영구쿠키(PT)
    private String mbrGrdId;            // 회원 등급 id
    private String shppLocSeq;          // 기본배송지 순번
    private String shipZipCd;           // 우편 번호
    private String emSaleStrNo;        // 이마트거점점포
    private String trSaleStrNo;        // 트레이더스거점포
    private String bnSaleStrNo;        // 분스거점점포
    private String sdSaleStrNo;        // 백화점거점영업점
    private String sdSaleStrNm;         // 백화점거점영업점명
    private String bnSaleStrNm;        // 분스거점점포명
    private String trSaleStrNm;     // 트레이더스거점포명
    private String emSaleStrNm;        // 이마트거점점포명
    private String emRsvtShppPsblYn;     // 예약 배송 여부  - 이마트
    private String trRsvtShppPsblYn;     // 예약 배송 여부  - 트레이더스
    private String emHolyShppSalestrNo;     // 명절 배송 여부  - 이마트
    private String trHolyShppSalestrNo;     // 명절 배송 여부  - 트레이더스
    private String emSaleStrCenterYn;	// 센터여부 - 이몰
    private String bnSaleStrCenterYn; // 센터여부 - 분스
    private String sdRsvtShppPsblYn;		// 백화점예약배송가능여부
    private String emDualSaleStrNo; // 이중화점포번호
    private String cartMbrId;	// 비로그인 장바구니를 위한 임시 MbrId

    private String hwSaleStrNo;
    private String hwRsvtShppPsblYn;
    private String hwHolyShppSalestrNo;
    private String hwSaleStrCenterYn;
    private String hwSaleStrNm;

    //[캐쉬로 처리될 항목]
    private String loginLimitYn;        // 관심 회원 로그인 제한
    private String ordLimitYn;          // 관심 회원 주문 제한
    private String email;               // 이메일
    private String phoneNumber;         // 핸드폰 번호
    private String adminYn;       // 어드민 유저 여부

    /**
     * ckwhere가 바인딩 될 channel id
     */
    private String chnlId;
    private boolean updatedCkWhere;                // 제휴 유입채널코드

    private boolean cacheMode;

    // cache 적용 유무 값
    private String cx;
    private String responseBodyContentType;

    private Map<String, String> tempPT;

    private String countryCode;

    private boolean firstCkwhere;
    private boolean firstPcid;
    private boolean firstFsid;
    private boolean redirectResponse;
    private boolean firstAppStart;

    /**
     * Gender and Age 1 digit(남자1, 여자2) + 3 digit(0~1000)
     */
    private String ga;
    private boolean thehowdy;
    public boolean isThehowdy() {
        return thehowdy;
    }
    public void setThehowdy(boolean thehowdy) {
        this.thehowdy = thehowdy;
    }
    private boolean howdy;

    public boolean isHowdy() {
        return howdy;
    }
    public void setHowdy(boolean howdy) {
        this.howdy = howdy;
    }



    private String domainSiteNo;
    private String depth1;
    private String searchFail;
    private String itemSiteNo;
    private String itemSalestrNo;

    private String mobileAppNo;


    private String bkWhere;
    private String pushId;
    private boolean firstPushId;
    private boolean firstPushInfo;


    public boolean isFirstPushInfo() {
        return firstPushInfo;
    }
    public void setFirstPushInfo(boolean firstPushInfo) {
        this.firstPushInfo = firstPushInfo;
    }
    private String pushDtlId;


    private boolean ajaxRequest;

    private String orderTracking;

    public String currentTimeMillis;
    public String viewCacheProcess;

    private String prevMbrId;

    private String talk = "N";
    public String getTalk() {
        return talk;
    }
    public void setTalk(String talk) {
        this.talk = talk;
    }

    private String currentUrl;
    private String remoteAddress;

    /**
     * 기본 리턴값은 "", default return "";
     * @return
     */
    public String getPrevMbrId() {
        return prevMbrId;
    }
    public void setPrevMbrId(String prevMbrId) {
        this.prevMbrId = prevMbrId;
    }

    public String getPushDtlId() {
        return pushDtlId;
    }
    public void setPushDtlId(String pushDtlId) {
        this.pushDtlId = pushDtlId;
    }


    public boolean isForeignRemoteAddress() {
        if (countryCode != null) {
            if (!countryCode.equals("ZZ") && !countryCode.equals("KR")
                    && !countryCode.equals("--") && !countryCode.equals("N/A")) {
                return true;
            }
        }
        return false;
    }

    public String getResponseBodyContentType() {
        return responseBodyContentType;
    }

    public void setResponseBodyContentType(String accept) {
        if (accept != null && accept.contains("xml")) {
            responseBodyContentType = "xml";
        } else {
            responseBodyContentType = "json";
        }
    }

    public String getMbrcoNm() {
        return mbrcoNm;
    }

    public void setMbrcoNm(String mbrcoNm) {
        this.mbrcoNm = mbrcoNm;
    }

    public boolean isB2EYN() {
        if (getMbrType() != null && getMbrType().equals("B2E")
                && getEmplYn() != null && getEmplYn().equals("Y")) {
            return true;
        } else {
            return false;
        }
    }

    public String getFamilyYn() {
        return familyYn;
    }

    public void setFamilyYn(String familyYn) {
        this.familyYn = familyYn;
    }

    public String getDeviceDtlCd() {
        return deviceDtlCd;
    }

    public void setDeviceDtlCd(String deviceDtlCd) {
        this.deviceDtlCd = deviceDtlCd;
    }

    public String getPerdcShppYn() {
        return perdcShppYn;
    }

    public void setPerdcShppYn(String perdcShppYn) {
        this.perdcShppYn = perdcShppYn;
    }

    public String getMbrGrdId() {
        return mbrGrdId;
    }

    public void setMbrGrdId(String mbrGrdId) {
        this.mbrGrdId = mbrGrdId;
    }

    public String getAffP1() {
        return affP1;
    }

    public void setAffP1(String affP1) {
        this.affP1 = affP1;
    }

    public String getAffP2() {
        return affP2;
    }

    public void setAffP2(String affP2) {
        this.affP2 = affP2;
    }

    public String getAffP3() {
        return affP3;
    }

    public void setAffP3(String affP3) {
        this.affP3 = affP3;
    }

    public String getAffP4() {
        return affP4;
    }

    public void setAffP4(String affP4) {
        this.affP4 = affP4;
    }

    public String getAffP5() {
        return affP5;
    }

    public void setAffP5(String affP5) {
        this.affP5 = affP5;
    }

    public String getEmplYn() {
        return emplYn;
    }

    public void setEmplYn(String emplYn) {
        this.emplYn = emplYn;
    }


    private String bnRsvtShppPsblYn;
    private String bnHolyShppSalestrNo;

    public static final String ZIP_CD = "04781";
    public FrontUserInfo() {
        serverName = "";

        mbrType = "B2C";
        //shipZipCd = "133827";
        shipZipCd = ZIP_CD;


        mbrCoId = "000000";
        adminYn = "N";
        setLoginYn("N");
        shppLocSeq = "0";          // 기본배송지 순번
        emSaleStrNo = "2034";        // 이마트거점점포
        emSaleStrNm = "성수점";        // 이마트거점점포명
        trSaleStrNo = "2154";        // 트레이더스거점포
        trSaleStrNm = "E/T월평점";     // 트레이더스거점포명

        bnSaleStrNo = "2160";        // 분스거점점포
        bnSaleStrNm = "강남점";        // 분스거점점포명

        sdSaleStrNo = "0000";        // 백화점거점영업점
        sdSaleStrNm = "본점";         // 백화점거점영업점명
        adultYn = "N";               // 성인인증여부
        mbrSvcSiteLst = "0000^0000^0000";      //회원서비스사이트목록 n개존재
        ckWhere = "";                // 제휴 유입채널코드
        mckWhere = "";
        sid = "";                    // 제휴 유입채널코드
        emRsvtShppPsblYn = "Y";     // 예약 배송 여부  - 이마트
        trRsvtShppPsblYn = "N";     // 예약 배송 여부  - 트레이더스
        bnRsvtShppPsblYn = "N";     // 예약 배송 여부  - 분스


        emHolyShppSalestrNo = "2034";     // 명절 배송 여부  - 이마트
        trHolyShppSalestrNo = "2154";     // 명절 배송 여부  - 트레이더스
        bnHolyShppSalestrNo = "";         // 명절 배송 여부  - 분스

        perdcShppYn = "N";
        emplYn = "N";
        mvnoYn = "N";		// MVNO 클럽 유무
        emSaleStrCenterYn = "N"; 	// 센터여부 - 이몰
        bnSaleStrCenterYn = "N";    // 센터여부 - 분스
        keepLogin = "N";
        mobileLoginRefresh = REFRESH_NONE;
        ver = "0";

        cacheMode = true;

        // 기본값은 N임. Y값으로 세팅되면 cachef를 타지 않음
        // Y값은 view-Cache.xml에서 정의 해 줘야 함. 여기는 단지 Y,N 구별만 줌.
        cx = "N";
        ga = "00";

        bkWhere = "";
        pushId = "";
        firstPushId = false;
        ajaxRequest = false;

        viewCacheProcess = "NoViewCache";

        aloginToken = "";
        prevMbrId = "";

        hwSaleStrNo = "2468";
        hwRsvtShppPsblYn = "N";
        hwHolyShppSalestrNo = "";
        hwSaleStrCenterYn = "N";
        hwSaleStrNm = "하우디몰";

        pushDtlId = "";

        vvipYn = "N";
        birthday = "";
        sdRsvtShppPsblYn = "N";
        dptVipYn = "N";

        emDualSaleStrNo = "0000";
        cartMbrId = "";
    }

    public String getPlzcmtLitmitYn() {
        return plzcmtLitmitYn;
    }

    public void setPlzcmtLitmitYn(String plzcmtLitmitYn) {
        this.plzcmtLitmitYn = plzcmtLitmitYn;
    }

    public String getEmRsvtShppPsblYn() {
        return emRsvtShppPsblYn;
    }

    public void setEmRsvtShppPsblYn(String emRsvtShppPsblYn) {
        this.emRsvtShppPsblYn = emRsvtShppPsblYn;
    }

    public String getTrRsvtShppPsblYn() {
        return trRsvtShppPsblYn;
    }

    public void setTrRsvtShppPsblYn(String trRsvtShppPsblYn) {
        this.trRsvtShppPsblYn = trRsvtShppPsblYn;
    }

    public String getEmHolyShppSalestrNo() {
        return emHolyShppSalestrNo;
    }

    public void setEmHolyShppSalestrNo(String emHolyShppSalestrNo) {
        this.emHolyShppSalestrNo = emHolyShppSalestrNo;
    }

    public String getLoginLimitYn() {
        return loginLimitYn;
    }

    public void setLoginLimitYn(String loginLimitYn) {
        this.loginLimitYn = loginLimitYn;
    }

    public String getOrdLimitYn() {
        return ordLimitYn;
    }

    public void setOrdLimitYn(String ordLimitYn) {
        this.ordLimitYn = ordLimitYn;
    }

    public String getDeviceCd() {
        return deviceCd;
    }

    public void setDeviceCd(String deviceCd) {
        this.deviceCd = deviceCd;
    }

    /*public String getReciNm() {
     return reciNm;
     }

     public void setReciNm(String reciNm) {
     this.reciNm = reciNm;
     }

     public String getReciTel() {
     return reciTel;
     }

     public void setReciTel(String reciTel) {
     this.reciTel = reciTel;
     }

     public String getReciHp() {
     return reciHp;
     }

     public void setReciHp(String reciHp) {
     this.reciHp = reciHp;
     }

     public String getReciAddr1() {
     return reciAddr1;
     }

     public void setReciAddr1(String reciAddr1) {
     this.reciAddr1 = reciAddr1;
     }

     public String getReciAddr2() {
     return reciAddr2;
     }

     public void setReciAddr2(String reciAddr2) {
     this.reciAddr2 = reciAddr2;
     }

     public String getReciShipZipCd() {
     return reciShipZipCd;
     }

     public void setReciShipZipCd(String reciShipZipCd) {
     this.reciShipZipCd = reciShipZipCd;
     }*/
    public String getMbrcoLogoImgPathNm() {
        return mbrcoLogoImgPathNm;
    }

    public void setMbrcoLogoImgPathNm(String mbrcoLogoImgPathNm) {
        this.mbrcoLogoImgPathNm = mbrcoLogoImgPathNm;
    }

    public String getAdminYn() {
        return adminYn;
    }

    public void setAdminYn(String adminYn) {
        this.adminYn = adminYn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMbrId() {
        return mbrId;
    }

    public void setMbrId(String mbrId) {
        this.mbrId = mbrId;
        setUserId(getMbrId());
    }

    public String getMbrNm() {
        return mbrNm;
    }

    public void setMbrNm(String mbrNm) {
        this.mbrNm = mbrNm;
    }

    /**
     * B2E 회원이면서 임직원의 경우 리턴값을 B2EY로 리턴해 준다.
     *
     * @return
     */
    public String getMbrType2() {
        if (mbrType.equals("B2E") && emplYn.equals("Y")) {
            return mbrType.concat("Y");
        } else {
            return mbrType;
        }
    }

    public String getMbrType() {
        return mbrType;
    }

    public void setMbrType(String mbrType) {
        this.mbrType = mbrType;
    }

    public String getMbrTypeCd() {
        return mbrTypeCd;
    }

    public void setMbrTypeCd(String mbrTypeCd) {
        this.mbrTypeCd = mbrTypeCd;
    }

    public String getMbrLoginId() {
        return mbrLoginId;
    }

    public void setMbrLoginId(String mbrLoginId) {
        this.mbrLoginId = mbrLoginId;
    }

    public String getMbrGrdCd() {
        return mbrGrdCd;
    }

    public void setMbrGrdCd(String mbrGrdCd) {
        this.mbrGrdCd = mbrGrdCd;
    }

    public String getShipZipCd() {
        if(shipZipCd != null && shipZipCd.length() == 6) {
            return ZIP_CD;
        } else {
            return shipZipCd;
        }
    }

    public void setShipZipCd(String shipZipCd) {
        this.shipZipCd = shipZipCd;
    }

    public String getMbrCoId() {
        return mbrCoId;
    }

    public void setMbrCoId(String mbrCoId) {
        this.mbrCoId = mbrCoId;
    }

    public String getShppLocSeq() {
        return shppLocSeq;
    }

    public void setShppLocSeq(String shppLocSeq) {
        this.shppLocSeq = shppLocSeq;
    }

    public String getEmSaleStrNo() {
        return emSaleStrNo;
    }

    public void setEmSaleStrNo(String emSaleStrNo) {
        this.emSaleStrNo = emSaleStrNo;
    }

    public String getEmSaleStrNm() {
        return emSaleStrNm;
    }

    public void setEmSaleStrNm(String emSaleStrNm) {
        this.emSaleStrNm = emSaleStrNm;
    }

    public String getTrSaleStrNo() {
        return trSaleStrNo;
    }

    public void setTrSaleStrNo(String trSaleStrNo) {
        this.trSaleStrNo = trSaleStrNo;
    }

    public String getTrSaleStrNm() {
        return trSaleStrNm;
    }

    public void setTrSaleStrNm(String trSaleStrNm) {
        this.trSaleStrNm = trSaleStrNm;
    }

    public String getBnSaleStrNo() {
        return bnSaleStrNo;
    }

    public void setBnSaleStrNo(String bnSaleStrNo) {
        this.bnSaleStrNo = bnSaleStrNo;
    }

    public String getBnSaleStrNm() {
        return bnSaleStrNm;
    }

    public void setBnSaleStrNm(String bnSaleStrNm) {
        this.bnSaleStrNm = bnSaleStrNm;
    }

    public String getSdSaleStrNo() {
        return sdSaleStrNo;
    }

    public void setSdSaleStrNo(String sdSaleStrNo) {
        this.sdSaleStrNo = sdSaleStrNo;
    }

    public String getSdSaleStrNm() {
        return sdSaleStrNm;
    }

    public void setSdSaleStrNm(String sdSaleStrNm) {
        this.sdSaleStrNm = sdSaleStrNm;
    }

    public String getEmDualSaleStrNo() {
        if("0000".equals(emDualSaleStrNo)){
            if("2439".equals(getEmSaleStrNo())){
                return "2034";
            } else 	if ("2449".equals(getEmSaleStrNo())){
                return "2037";
            } else {
                return emDualSaleStrNo;
            }
        } else {
            return emDualSaleStrNo;
        }

    }

    public void setEmDualSaleStrNo(String emDualSaleStrNo) {
        this.emDualSaleStrNo = emDualSaleStrNo;
    }

    public String getCartMbrId() {
        return cartMbrId;
    }

    /**
     * 현재 UserInfo에 cartMbrId가 세팅되어 있는지 여부를 확인함.
     *
     * @return cartMbrId가 세팅되어 있다면 true
     */
    public boolean existsCartMbrId() {
        if (cartMbrId != null && !"".equals(cartMbrId)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param cartMbrId the cartMbrId to set
     */
    public void setCartMbrId(String cartMbrId) {
        this.cartMbrId = cartMbrId;
    }

    public String getAdultYn() {
        return adultYn;
    }

    public void setAdultYn(String adultYn) {
        this.adultYn = adultYn;
    }

    public String getMbrSvcSiteLst() {
        return mbrSvcSiteLst;
    }

    public void setMbrSvcSiteLst(String mbrSvcSiteLst) {
        this.mbrSvcSiteLst = mbrSvcSiteLst;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getCkWhere() {
        return ckWhere;
    }

    public void setCkWhere(String ckWhere) {
        this.ckWhere = ckWhere;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * JSESSIONID와 equals
     *
     * @return the fsId
     */
    public String getFsId() {
        return fsId;
    }

    public void setFsId(String fsId) {
        this.fsId = fsId;
    }

    public String getFsIdDay() {
        return fsIdDay;
    }

    public void setFsIdDay(String fsIdDay) {
        this.fsIdDay = fsIdDay;
    }

    /**
     * 로그인이 된 상태에서만 호출 가능합니다. 회원ID를 기준으로 cache key를 조합한다.
     *
     * @return the cacheId
     */
    public String getCacheId() {
        if (getMbrId() == null) {
            throw new LoginFailException("로그인 기반이어야 합니다.");
        }

        return PREFIX_CACHE_KEY.concat(getMbrId());
    }


    /**
     * @return the updateAffP
     */
    public boolean isUpdateAffP() {
        return updateAffP;
    }

    /**
     * @param updateAffP the updateAffP to set
     */
    public void setUpdateAffP(boolean updateAffP) {
        this.updateAffP = updateAffP;
    }

    // 화면에서 호출 함. 프레임웍에서 호출하지 않음. 홍지선님이 호출함.

    public void updateAffP() {
        setUpdateAffP(true);
    }

    /**
     * @return the chnlId
     */
    public String getChnlId() {
        return chnlId;
    }

    /**
     * @param chnlId the chnlId to set
     */
    public void setChnlId(String chnlId) {
        this.chnlId = chnlId;
    }

    /**
     * @return the controllerCacheKey
     */
    public String getControllerCacheKey() {
        return controllerCacheKey;
    }

    /**
     * @param controllerCacheKey the controllerCacheKey to set
     */
    public void setControllerCacheKey(String controllerCacheKey) {
        this.controllerCacheKey = controllerCacheKey;
    }

    /**
     * @return the dmId
     */
    public String getDmId() {
        return dmId;
    }

    /**
     * @param dmId the dmId to set
     */
    public void setDmId(String dmId) {
        this.dmId = dmId;
    }

    /**
     * @return the updatedCkWhere
     */
    public boolean isUpdatedCkWhere() {
        return updatedCkWhere;
    }

    /**
     * @param updatedCkWhere the updatedCkWhere to set
     */
    public void setUpdatedCkWhere(boolean updatedCkWhere) {
        this.updatedCkWhere = updatedCkWhere;
    }

    @JsonIgnore
    private String expire;

    public void setControllerCacheKeyExpire(String expire) {
        this.expire = expire;
    }

    public String getControllerCacheKeyExpire() {
        return expire;
    }

    /**
     * @return the mvnoYn
     */
    public String getMvnoYn() {
        return mvnoYn;
    }

    /**
     * @param mvnoYn the mvnoYn to set
     */
    public void setMvnoYn(String mvnoYn) {
        this.mvnoYn = mvnoYn;
    }

    public String getEmSaleStrCenterYn() {
        return emSaleStrCenterYn;
    }

    public void setEmSaleStrCenterYn(String emSaleStrCenterYn) {
        this.emSaleStrCenterYn = emSaleStrCenterYn;
    }

    public String getBnSaleStrCenterYn() {
        return bnSaleStrCenterYn;
    }

    public void setBnSaleStrCenterYn(String bnSaleStrCenterYn) {
        this.bnSaleStrCenterYn = bnSaleStrCenterYn;
    }

    /**
     * 마스킹된 이메일 정보 리턴 ex) will****@emart.com
     *
     * @return
     */
    public String getMaskingEmail() {
        if (email != null) {
            String[] temp = email.split("@");
            if (temp == null || temp.length != 2) {
                return email;
            }

            String maskingEmail = "";
            int maskingCnt = 4;
            int maxLen = temp[0].length();

            if (maxLen > maskingCnt) {
                maskingEmail = temp[0].substring(0, (maxLen - maskingCnt)) + StringUtils.repeat("*", maskingCnt) + "@" + temp[1];
            } else {
                maskingEmail = temp[0].substring(0, 1) + StringUtils.repeat("*", maxLen - 1) + "@" + temp[1];
            }

            return maskingEmail;
        } else {
            return "";
        }
    }

    /**
     * 마스킹된 휴대폰 번호 리턴 ex) 010-****-9999
     *
     * @return
     */
    public String getMaskingPhoneNumber() {
        if (phoneNumber != null) {
            String[] temp = phoneNumber.split("-");
            if (temp == null || temp.length != 3) {
                return phoneNumber;
            }

            return temp[0] + "-****-" + temp[2];
        } else {
            return "";
        }
    }

    /**
     * @return the keepLogin
     */
    public String getKeepLogin() {
        return keepLogin;
    }

    /**
     * @param keepLogin the keepLogin to set
     */
    public void setKeepLogin(String keepLogin) {
        this.keepLogin = keepLogin;
    }

    /**
     * @return the mobileLoginRefresh
     */
    public String getMobileLoginRefresh() {
        return mobileLoginRefresh;
    }

    /**
     * @param mobileLoginRefresh the mobileLoginRefresh to set
     */
    public void setMobileLoginRefresh(String mobileLoginRefresh) {
        this.mobileLoginRefresh = mobileLoginRefresh;
    }

    /**
     * @return the ver
     */
    public String getVer() {
        return ver;
    }

    /**
     * @param ver the ver to set
     */
    public void setVer(String ver) {
        this.ver = ver;
    }

    /**
     * @return the cacheMode
     */
    public boolean isCacheMode() {
        return cacheMode;
    }

    /**
     * @param cacheMode default value = true, false 일 때는 cache를 타지 않는다.
     */
    public void setCacheMode(boolean cacheMode) {
        this.cacheMode = cacheMode;
    }

    /**
     * @return the mckWhere
     */
    public String getMckWhere() {
        return mckWhere;
    }

    /**
     * @param mckWhere the mckWhere to set
     */
    public void setMckWhere(String mckWhere) {
        this.mckWhere = mckWhere;
    }

    private String pcId;

    public void setPcId(String pcId) {
        this.pcId = pcId;
    }

    public String getPcId() {
        return pcId;
    }

    /**
     * @return the cx
     */
    public String getCx() {
        return cx;
    }

    /**
     * @param cx the cx to set
     */
    public void setCx(String cx) {
        this.cx = cx;
    }

    /**
     * @return the tempPT
     */
    public Map<String, String> getTempPT() {
        return tempPT;
    }

    /**
     * @param tempPT the tempPT to set
     */
    public void setTempPT(Map<String, String> tempPT) {
        this.tempPT = tempPT;
    }

    /**
     * @return the pushWhere
     */
    public String getPushWhere() {
        return pushWhere;
    }

    /**
     * @param pushWhere the pushWhere to set
     */
    public void setPushWhere(String pushWhere) {
        this.pushWhere = pushWhere;
    }

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode the countryCode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTrHolyShppSalestrNo() {
        return trHolyShppSalestrNo;
    }

    public void setTrHolyShppSalestrNo(String trHolyShppSalestrNo) {
        this.trHolyShppSalestrNo = trHolyShppSalestrNo;
    }

    /**
     * @return the ga
     */
    public String getGa() {
        return ga;
    }

    public void setGa(String ga) {
        this.ga = ga;
    }

    public Gender getGenderByPT() {

        String prefix = getGa().substring(0, 1);
        if (prefix.equals("1")) {
            return Gender.MALE;
        } else if (prefix.equals("2")) {
            return Gender.FEMALE;
        } else {
            return Gender.NONE;
        }
    }

    public String getAgeForTracking() {
        if (getGa().substring(1).equals("0")) {
            return "";
        } else {
            return getGa().substring(1);
        }
    }

    /**
     * @return the firstCkwhere
     */
    public boolean isFirstCkwhere() {
        return firstCkwhere;
    }

    /**
     * @param firstCkwhere the firstCkwhere to set
     */
    public void setFirstCkwhere(boolean firstCkwhere) {
        this.firstCkwhere = firstCkwhere;
    }

    /**
     * @return the firstPcid
     */
    public boolean isFirstPcid() {
        return firstPcid;
    }

    /**
     * @param firstPcid the firstPcid to set
     */
    public void setFirstPcid(boolean firstPcid) {
        this.firstPcid = firstPcid;
    }

    /**
     * @return the firstFsid
     */
    public boolean isFirstFsid() {
        return firstFsid;
    }

    /**
     * @param firstFsid the firstFsid to set
     */
    public void setFirstFsid(boolean firstFsid) {
        this.firstFsid = firstFsid;
    }

    /**
     * @return the deviceDivCd
     */
    public String getDeviceDivCd() {
        return deviceDivCd;
    }

    /**
     * @param deviceDivCd the deviceDivCd to set
     */
    public void setDeviceDivCd(String deviceDivCd) {
        this.deviceDivCd = deviceDivCd;
    }

    /**
     * @return the domainSiteNo
     */
    public String getDomainSiteNo() {
        return domainSiteNo;
    }

    /**
     * @param domainSiteNo the domainSiteNo to set
     */
    public void setDomainSiteNo(String domainSiteNo) {
        this.domainSiteNo = domainSiteNo;
    }

    /**
     * @return the depth1
     */
    public String getDepth1() {
        return depth1;
    }

    /**
     * @param depth1 the depth1 to set
     */
    public void setDepth1(String depth1) {
        this.depth1 = depth1;
    }

    /**
     * @return the searchFail
     */
    public String getSearchFail() {
        return searchFail;
    }

    /**
     * @param searchFail the searchFail to set
     */
    public void setSearchFail(String searchFail) {
        this.searchFail = searchFail;
    }

    /**
     * @return the itemSiteNo
     */
    public String getItemSiteNo() {
        return itemSiteNo;
    }

    /**
     * @param itemSiteNo the itemSiteNo to set
     */
    public void setItemSiteNo(String itemSiteNo) {
        this.itemSiteNo = itemSiteNo;
    }

    /**
     * @return the itemSalestrNo
     */
    public String getItemSalestrNo() {
        return itemSalestrNo;
    }

    /**
     * @param itemSalestrNo the itemSalestrNo to set
     */
    public void setItemSalestrNo(String itemSalestrNo) {
        this.itemSalestrNo = itemSalestrNo;
    }

    /**
     * @return the redirectResponse
     */
    public boolean isRedirectResponse() {
        return redirectResponse;
    }

    /**
     * @param redirectResponse the redirectResponse to set
     */
    public void setRedirectResponse(boolean redirectResponse) {
        this.redirectResponse = redirectResponse;
    }

    /**
     * @return the mobileAppNo
     */
    public String getMobileAppNo() {
        return mobileAppNo;
    }

    /**
     * @param mobileAppNo the mobileAppNo to set
     */
    public void setMobileAppNo(String mobileAppNo) {
        this.mobileAppNo = mobileAppNo;
    }

    /**
     * @return the bkWhere
     */
    public String getBkWhere() {
        return bkWhere;
    }

    /**
     * @param bkWhere the bkWhere to set
     */
    public void setBkWhere(String bkWhere) {
        this.bkWhere = bkWhere;
    }

    /**
     * @return the pushId
     */
    public String getPushId() {
        return pushId;
    }

    /**
     * @param pushId the pushId to set
     */
    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    /**
     * @return the firstPushId
     */
    public boolean isFirstPushId() {
        return firstPushId;
    }

    /**
     * @param firstPushId the firstPushId to set
     */
    public void setFirstPushId(boolean firstPushId) {
        this.firstPushId = firstPushId;
    }

    /**
     * @return the ajaxRequest
     */
    public boolean isAjaxRequest() {
        return ajaxRequest;
    }

    /**
     * @param ajaxRequest the ajaxRequest to set
     */
    public void setAjaxRequest(boolean ajaxRequest) {
        this.ajaxRequest = ajaxRequest;
    }

    private String requestFfirstCookie = "";

    public boolean ischangedFfirst() {
        return ischangedFfirst(getRequestFfirstCookie());
    }
    public boolean ischangedFfirst(String bindValue) {
        if(bindValue != null) {
            if(bindValue.length() == 3 || bindValue.length() == 4) {
                if(bindValue.substring(0,1).equals("1")) {
                    setFirstCkwhere(true);
                }

                if(bindValue.substring(1,2).equals("1")) {
                    setFirstPcid(true);
                }

                if(bindValue.substring(2,3).equals("1")) {
                    setFirstFsid(true);
                }

                if(bindValue.length() == 4) {
                    if(bindValue.substring(3,4).equals("1")) {
                        setFirstPushId(true);
                    }
                }
            }
        }

        if(isFirstPcid() || isFirstFsid() || isFirstCkwhere() || isFirstPushId()) {
            return true;
        } else {
            return false;
        }
    }

    public String getFfirstCookie() {
        StringBuilder sb = new StringBuilder();
        sb.append(isFirstCkwhere() ? "1" : "0");
        sb.append(isFirstPcid() ? "1" : "0");
        sb.append(isFirstFsid()? "1" : "0");
        sb.append(isFirstPushId() ? "1" : "0" );
        return sb.toString();
    }

    /**
     * @return the requestFfirstCookie
     */
    public String getRequestFfirstCookie() {
        return requestFfirstCookie;
    }

    /**
     * @param requestFfirstCookie the requestFfirstCookie to set
     */
    public void setRequestFfirstCookie(String requestFfirstCookie) {
        this.requestFfirstCookie = requestFfirstCookie;
    }

    /**
     * @return the serverName
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * @param serverName the serverName to set
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * @return the firstAppStart
     */
    public boolean isFirstAppStart() {
        return firstAppStart;
    }

    /**
     * @param firstAppStart the firstAppStart to set
     */
    public void setFirstAppStart(boolean firstAppStart) {
        this.firstAppStart = firstAppStart;
    }

    private boolean changedCkwhereOrPushId;
    public boolean isChangedCkwhereOrPushId() {
        return changedCkwhereOrPushId;
    }
    public void setChangedCkwhereOrPushId(boolean changedCkwhereOrPushId) {
        this.changedCkwhereOrPushId = changedCkwhereOrPushId;
    }

    /**
     * @return the orderTracking
     */
    public String getOrderTracking() {
        return orderTracking;
    }

    /**
     * @param orderTracking the orderTracking to set
     */
    public void setOrderTracking(String orderTracking) {
        this.orderTracking = orderTracking;
    }

    /**
     * @return the bnRsvtShppPsblYn
     */
    public String getBnRsvtShppPsblYn() {
        return bnRsvtShppPsblYn;
    }

    /**
     * @param bnRsvtShppPsblYn the bnRsvtShppPsblYn to set
     */
    public void setBnRsvtShppPsblYn(String bnRsvtShppPsblYn) {
        this.bnRsvtShppPsblYn = bnRsvtShppPsblYn;
    }

    /**
     * @return the bnHolyShppSalestrNo
     */
    public String getBnHolyShppSalestrNo() {
        return bnHolyShppSalestrNo;
    }

    /**
     * @param bnHolyShppSalestrNo the bnHolyShppSalestrNo to set
     */
    public void setBnHolyShppSalestrNo(String bnHolyShppSalestrNo) {
        this.bnHolyShppSalestrNo = bnHolyShppSalestrNo;
    }

    @JsonIgnore
    private ForTrackingItemView ForTrackingItemView = new ForTrackingItemView();

    /**
     * @return the ForTrackingItemView
     */
    public ForTrackingItemView getForTrackingItemView() {
        return ForTrackingItemView;
    }

    /**
     * @param ForTrackingItemView the ForTrackingItemView to set
     */
    public void setForTrackingItemView(ForTrackingItemView ForTrackingItemView) {
        this.ForTrackingItemView = ForTrackingItemView;
    }

    /**
     * @return the currentTimeMillis
     */
    public String getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    /**
     * @param currentTimeMillis the currentTimeMillis to set
     */
    public void setCurrentTimeMillis(String currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    /**
     * @return the viewCacheProcess
     */
    public String getViewCacheProcess() {
        return viewCacheProcess;
    }

    /**
     * @param viewCacheProcess the viewCacheProcess to set
     */
    public void setViewCacheProcess(String viewCacheProcess) {
        this.viewCacheProcess = viewCacheProcess;
    }

    /**
     * @return the aloginToken
     */
    public String getAloginToken() {
        return aloginToken;
    }

    /**
     * @param aloginToken the aloginToken to set
     */
    public void setAloginToken(String aloginToken) {
        this.aloginToken = aloginToken;
    }

    /**
     * @return the hwSaleStrNo
     */
    public String getHwSaleStrNo() {
        return "2468";
    }

    /**
     * @param hwSaleStrNo the hwSaleStrNo to set
     */
    public void setHwSaleStrNo(String hwSaleStrNo) {
        this.hwSaleStrNo = hwSaleStrNo;
    }

    /**
     * @return the hwRsvtShppPsblYn
     */
    public String getHwRsvtShppPsblYn() {
        return "N";
    }

    /**
     * @param hwRsvtShppPsblYn the hwRsvtShppPsblYn to set
     */
    public void setHwRsvtShppPsblYn(String hwRsvtShppPsblYn) {
        this.hwRsvtShppPsblYn = hwRsvtShppPsblYn;
    }

    /**
     * @return the hwHolyShppSalestrNo
     */
    public String getHwHolyShppSalestrNo() {
        return "";
    }

    /**
     * @param hwHolyShppSalestrNo the hwHolyShppSalestrNo to set
     */
    public void setHwHolyShppSalestrNo(String hwHolyShppSalestrNo) {
        this.hwHolyShppSalestrNo = hwHolyShppSalestrNo;
    }

    /**
     * @return the hwSaleStrCenterYn
     */
    public String getHwSaleStrCenterYn() {
        return "N";
    }

    /**
     * @param hwSaleStrCenterYn the hwSaleStrCenterYn to set
     */
    public void setHwSaleStrCenterYn(String hwSaleStrCenterYn) {
        this.hwSaleStrCenterYn = hwSaleStrCenterYn;
    }

    /**
     * @return the hwSaleStrNm
     */
    public String getHwSaleStrNm() {
        return "하우디몰";
    }

    /**
     * @param hwSaleStrNm the hwSaleStrNm to set
     */
    public void setHwSaleStrNm(String hwSaleStrNm) {
        this.hwSaleStrNm = hwSaleStrNm;
    }

    /**
     * @return the sguid
     */
    public String getSguid() {
        return sguid;
    }

    /**
     * @param sguid the sguid to set
     */
    public void setSguid(String sguid) {
        this.sguid = sguid;
    }

    /**
     * @return the vvipYn
     */
    public String getVvipYn() {
        return vvipYn;
    }

    /**
     * @param vvipYn the vvipYn to set
     */
    public void setVvipYn(String vvipYn) {
        this.vvipYn = vvipYn;
    }

    /**
     * @return the birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * @param birthday the birthday to set
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * @return the dptVipYn
     */
    public String getDptVipYn() {
        return dptVipYn;
    }

    /**
     * @param dptVipYn the dptVipYn to set
     */
    public void setDptVipYn(String dptVipYn) {
        this.dptVipYn = dptVipYn;
    }

    /**
     * @return the sdRsvtShppPsblYn
     */
    public String getSdRsvtShppPsblYn() {
        return sdRsvtShppPsblYn;
    }

    /**
     * @param sdRsvtShppPsblYn the sdRsvtShppPsblYn to set
     */
    public void setSdRsvtShppPsblYn(String sdRsvtShppPsblYn) {
        this.sdRsvtShppPsblYn = sdRsvtShppPsblYn;
    }

    public enum Gender {

        MALE("남성", "1"),
        FEMALE("여성", "2"),
        NONE("모름", "");

        private final String value;
        private String type;

        Gender(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public String getType() {
            return type;
        }
    }

    public enum MobileWebOs {
        ANDROID("A"), //
        IOS("I"), //
        NONE(""),// 모바일웹이 아니거나 알 수 없음
        ;

        private final String value;

        MobileWebOs(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    static public class ForTrackingItemView {
        private String dispCtgId;
        private String stdCtgId;
        private String brandId;
        private String sellPrice;
        private String bestAmt;

        /**
         * @return the dispCtgId
         */
        public String getDispCtgId() {
            return dispCtgId;
        }

        /**
         * @param dispCtgId the dispCtgId to set
         */
        public void setDispCtgId(String dispCtgId) {
            this.dispCtgId = dispCtgId;
        }

        /**
         * @return the stdCtgId
         */
        public String getStdCtgId() {
            return stdCtgId;
        }

        /**
         * @param stdCtgId the stdCtgId to set
         */
        public void setStdCtgId(String stdCtgId) {
            this.stdCtgId = stdCtgId;
        }

        /**
         * @return the brandId
         */
        public String getBrandId() {
            return brandId;
        }

        /**
         * @param brandId the brandId to set
         */
        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }


        /**
         * @return the bestAmt
         */
        public String getBestAmt() {
            return bestAmt;
        }

        /**
         * @param bestAmt the bestAmt to set
         */
        public void setBestAmt(String bestAmt) {
            this.bestAmt = bestAmt;
        }

        /**
         * @return the sellPrice
         */
        public String getSellPrice() {
            return sellPrice;
        }

        /**
         * @param sellPrice the sellPrice to set
         */
        public void setSellPrice(String sellPrice) {
            this.sellPrice = sellPrice;
        }
    }


    public class LoginFailException extends RuntimeException {

        public LoginFailException(String message) {
            super(message);
        }

    }


    public String getLoginYn() {
        return loginYn;
    }

    public void setLoginYn(String loginYn) {
        this.loginYn = loginYn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
}

