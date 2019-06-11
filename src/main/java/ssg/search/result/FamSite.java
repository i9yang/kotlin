package ssg.search.result;

public class FamSite {
    private String pageTitleNm;
    private String siteNm;
    private String pageLink;
    private String pageCntt;
    private String outlinkYn;

    public String getPageTitleNm() {
        return pageTitleNm;
    }

    public void setPageTitleNm(String pageTitleNm) {
        this.pageTitleNm = pageTitleNm;
    }

    public String getSiteNm() {
        return siteNm;
    }

    public void setSiteNm(String siteNm) {
        this.siteNm = siteNm;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    public String getPageCntt() {
        return pageCntt;
    }

    public void setPageCntt(String pageCntt) {
        this.pageCntt = pageCntt;
    }

    @Override
    public String toString() {
        return "FamSite{" +
                "pageTitleNm='" + pageTitleNm + '\'' +
                ", siteNm='" + siteNm + '\'' +
                ", pageLink='" + pageLink + '\'' +
                ", pageCntt='" + pageCntt + '\'' +
                '}';
    }

    public String getOutlinkYn() {
        return outlinkYn;
    }

    public void setOutlinkYn(String outlinkYn) {
        this.outlinkYn = outlinkYn;
    }
}