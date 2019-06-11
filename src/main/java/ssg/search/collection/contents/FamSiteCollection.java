package ssg.search.collection.contents;

import QueryAPI510.Search;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.function.Highlightable;
import ssg.search.function.Pageable;
import ssg.search.function.Snippet;
import ssg.search.parameter.Parameter;
import ssg.search.result.FamSite;
import ssg.search.result.Result;

import java.util.ArrayList;
import java.util.List;

public class FamSiteCollection implements Collection, Highlightable, Pageable, Snippet {
    @Override
    public String getCollectionName(Parameter parameter) {
        return "fam_site";
    }

    @Override
    public String getCollectionAliasName(Parameter parameter) {
        return "fam_site";
    }

    @Override
    public String[] getDocumentField(Parameter parameter) {
        return new String[]{
                "SITE_NM",
                "PAGE_TITLE_NM",
                "PAGE_LINK",
                "PAGE_CNTT",
                "OUTLINK_YN"
        };
    }

    @Override
    public String[] getSearchField(Parameter parameter) {
        return new String[]{
                "PAGE_TITLE_NM",
                "PAGE_CNTT"
        };
    }

    @Override
    public Result getResult(Search search, String name, Parameter parameter, Result result) {
        List<FamSite> famSiteList = new ArrayList<FamSite>();
        int count = search.w3GetResultCount(name);
        for(int i=0;i<count;i++){
            FamSite famSite = new FamSite();
            famSite.setSiteNm(search.w3GetField(name, "SITE_NM", i));
            famSite.setPageTitleNm(search.w3GetField(name, "PAGE_TITLE_NM", i));
            famSite.setPageLink(search.w3GetField(name, "PAGE_LINK", i));
            famSite.setPageCntt(search.w3GetField(name, "PAGE_CNTT", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
            famSite.setOutlinkYn(search.w3GetField(name, "OUTLINK_YN", i));
            famSiteList.add(famSite);
        }
        result.setFamSiteCount(search.w3GetResultTotalCount(name));
        result.setFamSiteList(famSiteList);
        return result;
    }

    @Override
    public Call<Info> getPage() {
        return new Call<Info>(){
            public Info apply(Parameter parameter){
                return new Info(0, 100);
            }
        };
    }
}
