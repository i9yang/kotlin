package ssg.search.constant;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.base.Collection;
import ssg.search.collection.advertising.BanrAdvertisingCollection;
import ssg.search.collection.advertising.CpcAdvertisingCollection;
import ssg.search.collection.advertising.CpcExtAdvertisingCollection;
import ssg.search.collection.brand.BookBrandDispCollection;
import ssg.search.collection.brand.BrandDispCollection;
import ssg.search.collection.brand.BrandMasterCollection;
import ssg.search.collection.brand.MobileBrandDispCollection;
import ssg.search.collection.bshop.*;
import ssg.search.collection.contents.*;
import ssg.search.collection.disp.*;
import ssg.search.collection.es.brand.ESBrandBookCtgIdGroup;
import ssg.search.collection.es.brand.ESBrandCtgIdGroup;
import ssg.search.collection.es.brand.ESMobileCtgIdGroup;
import ssg.search.collection.es.bshop.ESBshopCtgIdGroup;
import ssg.search.collection.es.bshop.ESBshopItem;
import ssg.search.collection.es.disp.*;
import ssg.search.collection.es.global.ESGlobalBrandIdGroup;
import ssg.search.collection.es.global.ESGlobalCtgIdGroup;
import ssg.search.collection.es.global.ESGlobalItem;
import ssg.search.collection.es.rsearch.*;
import ssg.search.collection.es.virtual.ESVirtual;
import ssg.search.collection.global.*;
import ssg.search.collection.quality.SearchQualityCollection;
import ssg.search.collection.recom.BrandRecomCollection;
import ssg.search.collection.recom.CategoryRecomCollection;
import ssg.search.collection.search.*;
import ssg.search.parameter.Parameter;
import ssg.search.util.CollectionUtils;

import java.util.List;

public enum Targets {
    ALL{
        public List<Collection> getCollectionSet(Parameter parameter){
            String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");

            // 검색광고관련추가
            boolean isAdSearch = CollectionUtils.isAdSearch(parameter);
            parameter.setAdSearch(isAdSearch);

            //상시배너조회추가
            parameter.setBanrEverSearch(true);

            if(strSiteNo.equals("6100")){
                List<Collection> collectionList = CollectionUtils.asList(
                        new ItemCollection(),
                        new ItemGroupCollection(),
                        new ItemCategoryCollection(),
                        new LifeMagazineCollection(),
                        new EsBanrCollection()
                );

                if (StringUtils.equals(parameter.LOAD_LEVEL_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, parameter.getLoadLevel())
                        ||  StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())
                        ) {
                    collectionList.clear();

                    collectionList = CollectionUtils.asList(
                            new ItemCollection(),
                            new ItemGroupCollection(),
                            new ItemCategoryCollection()
                    );
                }

                return collectionList;

            } else if (strSiteNo.equals("6001") || strSiteNo.equals("6002")) {
                List<Collection> collectionList = CollectionUtils.asList(
                        new CategoryRecomCollection(),
                        new ItemCollection(),
                        new ItemGroupCollection(),
                        new ItemGroupCategoryCollection(),
                        new BrandRecomCollection(),
                        new BrandRecomCollection(),
                        new MallCollection(),
                        new PostngCollection(),
                        new EsSrchwdrlCollection(),
                        new EventCollection(),
                        new EsSpellCollection(),
                        new EsBanrCollection(),
                        new PnshopCollection(),
                        new TrecipeCollection(),
                        new EsVirtualCategoryCollection(),
                        new FaqCollection(),
                        new BookCollection(),
                        new RecomCollection(),
                        new FamSiteCollection()
                );

                // 내취타취추가
                boolean isRecommendSearch = CollectionUtils.isRecommendSearch(parameter);
                if (isRecommendSearch) {
                    if (StringUtils.isNotBlank(parameter.getUserInfo().getMbrId())) {
                        collectionList.add(new MyTasteCollection());
                    }

                    collectionList.add(new PublicTasteCollection());
                }

                if (StringUtils.equals(parameter.LOAD_LEVEL_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, parameter.getLoadLevel())
                        ||  StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())
                        ) {
                    collectionList.clear();

                    collectionList = CollectionUtils.asList(
                            new ItemCollection(),
                            new ItemGroupCollection(),
                            new ItemCategoryCollection(),
                            new MallCollection(),
                            new BookCollection()
                    );
                }

                // 검색광고관련추가
                if (isAdSearch && strSiteNo.equals("6001") && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL, parameter.getLoadLevel())
                        && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                    collectionList.add(0, new CpcAdvertisingCollection());
                    collectionList.add(new BanrAdvertisingCollection());
                }

                return collectionList;

            } else if (strSiteNo.equals("6004")) {
                List<Collection> collectionList = CollectionUtils.asList(
                        new CategoryRecomCollection(),
                        new ItemCollection(),
                        new ItemGroupCollection(),
                        new ItemGroupCategoryCollection(),
                        new BrandRecomCollection(),
                        new MallCollection(),
                        new PostngCollection(),
                        new EsSrchwdrlCollection(),
                        new EventCollection(),
                        new EsSpellCollection(),
                        new EsBanrCollection(),
                        new PnshopCollection(),
                        new PnshopSdCollection(),
                        new EsVirtualCategoryCollection(),
                        new FaqCollection(),
                        new BookCollection(),
                        new RecomCollection(),
                        new FamSiteCollection()
                );

                // 내취타취추가
                boolean isRecommendSearch = CollectionUtils.isRecommendSearch(parameter);
                if (isRecommendSearch) {
                    if (StringUtils.isNotBlank(parameter.getUserInfo().getMbrId())) {
                        collectionList.add(new MyTasteCollection());
                    }

                    collectionList.add(new PublicTasteCollection());
                }

                if (StringUtils.equals(parameter.LOAD_LEVEL_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, parameter.getLoadLevel())
                        ||  StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())
                        ) {
                    collectionList.clear();

                    collectionList = CollectionUtils.asList(
                            new ItemCollection(),
                            new ItemGroupCollection(),
                            new ItemCategoryCollection(),
                            new MallCollection(),
                            new BookCollection()
                    );
                }

                // 검색광고관련추가
                if (isAdSearch && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL, parameter.getLoadLevel())
                        && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                    collectionList.add(0, new CpcAdvertisingCollection());
                    collectionList.add(new BanrAdvertisingCollection());
                }

                return collectionList;

            } else if (strSiteNo.equals("6009")) {
                List<Collection> collectionList = CollectionUtils.asList(
                        new CategoryRecomCollection(),
                        new ItemCollection(),
                        new ItemGroupCollection(),
                        new ItemGroupCategoryCollection(),
                        new BrandRecomCollection(),
                        new MallCollection(),
                        new PostngCollection(),
                        new EsSrchwdrlCollection(),
                        new EventCollection(),
                        new EsSpellCollection(),
                        new EsBanrCollection(),
                        new PnshopCollection(),
                        new EsVirtualCategoryCollection(),
                        new FaqCollection(),
                        new RecomCollection(),
                        new FamSiteCollection()
                );

                // 내취타취추가
                boolean isRecommendSearch = CollectionUtils.isRecommendSearch(parameter);
                if (isRecommendSearch) {
                    if (StringUtils.isNotBlank(parameter.getUserInfo().getMbrId())) {
                        collectionList.add(new MyTasteCollection());
                    }

                    collectionList.add(new PublicTasteCollection());
                }

                if (StringUtils.equals(parameter.LOAD_LEVEL_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, parameter.getLoadLevel())
                        ||  StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())
                        ) {
                    collectionList.clear();

                    collectionList = CollectionUtils.asList(
                            new ItemCollection(),
                            new ItemGroupCollection(),
                            new ItemGroupCategoryCollection(),
                            new MallCollection()
                    );
                }
                
                // 검색광고관련추가
                if (isAdSearch && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL, parameter.getLoadLevel())
                        && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                    collectionList.add(new BanrAdvertisingCollection());
                }

                return collectionList;

            } else if (strSiteNo.equals("6005")) {
                List<Collection> collectionList = CollectionUtils.asList(
                        new CategoryRecomCollection(),
                        new ItemCollection(),
                        new ItemGroupCollection(),
                        new ItemGroupCategoryCollection(),
                        new BrandRecomCollection(),
                        new MallCollection(),
                        new PostngCollection(),
                        new LifeMagazineCollection(),
                        new EventCollection(),
                        new IssueThemeCollection(),
                        new EsSrchwdrlCollection(),
                        new EsSpellCollection(),
                        new EsBanrCollection(),
                        new PnshopCollection(),
                        new PnshopSdCollection(),
                        new TrecipeCollection(),
                        new EsVirtualCategoryCollection(),
                        new FaqCollection(),
                        new BookCollection(),
                        new RecomCollection(),
                        new StarfieldCollection(),
                        new FamSiteCollection()
                );

                // 내취타취추가
                boolean isRecommendSearch = CollectionUtils.isRecommendSearch(parameter);
                if (isRecommendSearch) {
                    if (StringUtils.isNotBlank(parameter.getUserInfo().getMbrId())) {
                        collectionList.add(new MyTasteCollection());
                    }

                    collectionList.add(new PublicTasteCollection());
                }

                if (StringUtils.equals(parameter.LOAD_LEVEL_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, parameter.getLoadLevel())
                        ||  StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())
                        ) {
                    collectionList.clear();

                    collectionList = CollectionUtils.asList(
                            new ItemCollection(),
                            new ItemGroupCollection(),
                            new ItemGroupCategoryCollection(),
                            new MallCollection(),
                            new BookCollection()
                    );
                }

                // 검색광고관련추가
                if (isAdSearch && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL, parameter.getLoadLevel())
                        && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                    collectionList.add(0, new CpcAdvertisingCollection());
                    collectionList.add(new BanrAdvertisingCollection());
                }

                return collectionList;
            } else if (strSiteNo.equals("6200") || strSiteNo.equals("6003") || strSiteNo.equals("6300")) {
                List<Collection> collectionList = CollectionUtils.asList(
                        new CategoryRecomCollection(),
                        new ItemCollection(),
                        new ItemGroupCollection(),
                        new ItemGroupCategoryCollection(),
                        new EventCollection(),
                        new BrandRecomCollection(),
                        new MallCollection(),
                        new PostngCollection(),
                        new EsSpellCollection(),
                        new EsBanrCollection(),
                        new PnshopCollection(),
                        new EsVirtualCategoryCollection(),
                        new FaqCollection(),
                        new RecomCollection()
                );

                if (StringUtils.equals(parameter.LOAD_LEVEL_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, parameter.getLoadLevel())
                        ||  StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())
                        ) {
                    collectionList.clear();

                    collectionList = CollectionUtils.asList(
                            new ItemCollection(),
                            new ItemGroupCollection(),
                            new ItemGroupCategoryCollection(),
                            new MallCollection()
                    );
                }

                return collectionList;
            } else if (strSiteNo.equals("6400")) {
                List<Collection> collectionList = CollectionUtils.asList(
                        new EsSpellCollection(),
                        new EsBanrCollection(),
                        new StarfieldCollection()
                );

                if (StringUtils.equals(parameter.LOAD_LEVEL_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, parameter.getLoadLevel())
                        ||  StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                    collectionList.clear();
                    collectionList = CollectionUtils.asList(
                            new StarfieldCollection()
                    );
                }

                return collectionList;
            }

            return CollectionUtils.asList(
                    new CategoryRecomCollection(),
                    new ItemCollection(),
                    new ItemGroupCollection(),
                    new ItemGroupCategoryCollection(),
                    new BrandRecomCollection(),
                    new MallCollection(),
                    new PostngCollection(),
                    new LifeMagazineCollection(),
                    new EventCollection(),
                    new IssueThemeCollection(),
                    new EsSrchwdrlCollection(),
                    new EsSpellCollection(),
                    new EsBanrCollection(),
                    new PnshopCollection(),
                    new PnshopSdCollection(),
                    new EsVirtualCategoryCollection(),
                    new FaqCollection(),
                    new BookCollection(),
                    new RecomCollection()
            );
        }
    },
    ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            boolean isAdSearch = CollectionUtils.isAdSearch(parameter);
            parameter.setAdSearch(isAdSearch);

            List<Collection> collectionList = CollectionUtils.asList(new ItemCollection());

            // 검색광고관련추가
            if (isAdSearch && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL, parameter.getLoadLevel())
                    && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                collectionList.add(0, new CpcAdvertisingCollection());
            }

            return collectionList;
        }
    },
    CATEGORY{
        public List<Collection> getCollectionSet(Parameter parameter){
            String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");

            // 검색광고관련추가
            //원래 카테고리는 광고상품을 노출하지 않지만 pc에서 뒤로 가기 할경우 타겟을 카테고리로 던지는 경우 때문에 추가
            boolean isAdSearch = false;

            if (StringUtils.isBlank(parameter.getCtgId())) {
                isAdSearch = CollectionUtils.isAdSearch(parameter);
                parameter.setAdSearch(isAdSearch);
            }

            if(strSiteNo.equals("6100")){
                List<Collection> collectionList = CollectionUtils.asList(
                        new ItemCollection(),
                        new ItemGroupCollection(),
                        new ItemCategoryCollection(),
                        new LifeMagazineCollection()
                );

                return collectionList;
            }else{
                List<Collection> collectionList = CollectionUtils.asList(
                        new CategoryRecomCollection(),
                        new ItemCollection(),
                        new ItemGroupCollection(),
                        new ItemGroupCategoryCollection(),
                        new BrandRecomCollection()
                );

                // 검색광고관련추가
                if (isAdSearch && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL, parameter.getLoadLevel())
                        && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                    collectionList.add(0, new CpcAdvertisingCollection());
                    collectionList.add(new BanrAdvertisingCollection());
                }

                return collectionList;
            }
        }
    },
    BOOK{
        public List<Collection> getCollectionSet(Parameter parameter){
            // 가끔 앱 외주 개발자들이 mobile 인데 그냥 book target 호출하던 기억이 있어서 일단 방어코딩
            if(parameter.getSrchVer()>1.0){
                String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
                if(strSiteNo.equals("6005")){
                    List<Collection> returnList = CollectionUtils.asList(
                            new BookCollection(),
                            new BookGroupCategoryCollection(),
                            new BookMallCollection(),
                            new EsSrchwdrlCollection(),
                            new EsSpellCollection()
                    );

                    return returnList;

                } else {
                    List<Collection> returnList = CollectionUtils.asList(
                            new BookCollection(),
                            new BookGroupCategoryCollection(),
                            new EsSrchwdrlCollection(),
                            new EsSpellCollection()
                    );

                    return returnList;
                }
            }else{
                List<Collection> returnList = CollectionUtils.asList(
                        new BookCollection(),
                        new BookCategoryCollection(),
                        new BookGroupCollection(),
                        new ItemMallCollection(),
                        new EsSrchwdrlCollection(),
                        new EsSpellCollection()
                )
                        ;
                return returnList;
            }
        }
    },
    //2019년 1월에 강업후 삭제예정
    MOBILE_ALL{
        public List<Collection> getCollectionSet(Parameter parameter){
            // 검색광고관련추가
            boolean isAdSearch = CollectionUtils.isAdSearch(parameter);
            parameter.setAdSearch(isAdSearch);

            //상시배너조회추가
            parameter.setBanrEverSearch(true);

            String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");

            // 스타필드 조회시에는 바로 리턴
            if(strSiteNo.equals("6400")) {
                List<Collection> returnList = CollectionUtils.asList(
                        new EsSpellCollection(),
                        new EsBanrCollection(),
                        new StarfieldCollection()
                );

                if (StringUtils.equals(parameter.LOAD_LEVEL_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, parameter.getLoadLevel())
                        ||  StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                    returnList.clear();
                    returnList = CollectionUtils.asList(
                            new StarfieldCollection()
                    );
                }

                return returnList;
            }

            List<Collection> returnList = CollectionUtils.asList(
                    new ItemCollection(),
                    new ItemGroupCollection(),
                    new MallCollection(),
                    new EsSrchwdrlCollection(),
                    new EsSpellCollection(),
                    new EsBanrCollection(),
                    new StoreCollection(),
                    new MobilePnshopCollection(),
                    new LifeMagazineCollection(),
                    new IssueThemeCollection(),
                    new BrandMasterCollection()
            );

            if(strSiteNo.equals("6001") ||strSiteNo.equals("6004") ||strSiteNo.equals("6005")){
                returnList.add(new BookCollection());
                
            }
            if(strSiteNo.equals("6001") ||strSiteNo.equals("6005")){
                returnList.add(new TrecipeCollection());
            }
            if(strSiteNo.equals("6001") ||strSiteNo.equals("6004") ||strSiteNo.equals("6009") ||strSiteNo.equals("6005")){
                returnList.add(new EsVirtualCategoryCollection());
            }
            if(strSiteNo.equals("6005")) {
                returnList.add(new StarfieldCollection());
            }

            if (StringUtils.equals(parameter.LOAD_LEVEL_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, parameter.getLoadLevel())
                    ||  StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())
                    ) {
                returnList.clear();

                returnList = CollectionUtils.asList(
                        new ItemCollection(),
                        new MallCollection()
                );
            }

            // 검색광고관련추가
            if (isAdSearch && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL, parameter.getLoadLevel())
                    && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                //상품
                if(strSiteNo.equals("6001") ||strSiteNo.equals("6004") ||strSiteNo.equals("6005")){
                    returnList.add(0, new CpcAdvertisingCollection());
                }
                
                //배너
                if(strSiteNo.equals("6001") ||strSiteNo.equals("6004") ||strSiteNo.equals("6009") ||strSiteNo.equals("6005")){              
                    returnList.add(new BanrAdvertisingCollection());
                }
            }

            return returnList;
        }
    },
    MOBILE{
        public List<Collection> getCollectionSet(Parameter parameter){
            // 검색광고관련추가
            boolean isAdSearch = CollectionUtils.isAdSearch(parameter);
            parameter.setAdSearch(isAdSearch);

            //상시배너조회추가
            parameter.setBanrEverSearch(true);

            String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
            List<Collection> returnList = null;

            //스타필드 조회시에는 바로 리턴
            if(strSiteNo.equals("6400")) {
                returnList = CollectionUtils.asList(
                        new EsSpellCollection(),
                        new EsBanrCollection(),
                        new StarfieldCollection()
                );

                if (StringUtils.equals(parameter.LOAD_LEVEL_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, parameter.getLoadLevel())
                        ||  StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                    returnList.clear();
                    returnList = CollectionUtils.asList(
                            new StarfieldCollection()
                    );
                }

                return returnList;
            }

            returnList = CollectionUtils.asList(
                    new ItemCollection(),
                    new ItemCategoryCollection(),
                    new ItemGroupCollection(),
                    new ItemMallCollection(),
                    new EsSrchwdrlCollection(),
                    new EsSpellCollection(),
                    new EsBanrCollection(),
                    new EsVirtualCategoryCollection()
            );
            if(strSiteNo.equals("6001")){
                returnList.add(new BookCollection());
                returnList.add(new FamSiteCollection());
            }
            if(strSiteNo.equals("6004")){
                returnList.add(new BookCollection());
                returnList.add(new PnshopCollection());
                returnList.add(new PnshopSdCollection());
                returnList.add(new FamSiteCollection());
            }
            else if(strSiteNo.equals("6005")){
                returnList.add(new BookCollection());
                returnList.add(new PnshopCollection());
                returnList.add(new PnshopSdCollection());
                returnList.add(new FamSiteCollection());
            }
            else if(strSiteNo.equals("6009")){
                returnList.add(new PnshopSdCollection());
                returnList.add(new FamSiteCollection());
            }


            if (StringUtils.equals(parameter.LOAD_LEVEL_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, parameter.getLoadLevel())
                    ||  StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())
                    ) {
                returnList.clear();

                returnList = CollectionUtils.asList(
                        new ItemCollection(),
                        new ItemCategoryCollection(),
                        new ItemGroupCollection(),
                        new ItemMallCollection()
                );
            }

            // 검색광고관련추가
            if (isAdSearch && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL, parameter.getLoadLevel())
                    && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                //상품
                if(strSiteNo.equals("6001") ||strSiteNo.equals("6004") ||strSiteNo.equals("6005")){
                    returnList.add(0, new CpcAdvertisingCollection());
                }
                
                //배너
                if(strSiteNo.equals("6001") ||strSiteNo.equals("6004") ||strSiteNo.equals("6009") ||strSiteNo.equals("6005")){              
                    returnList.add(new BanrAdvertisingCollection());
                }
            }

            return returnList;
        }
    },
    //2019년 1월에 강업후 삭제예정
    MOBILE_DTL{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new CategoryRecomCollection(),
                    new ItemCommCategoryCollection(),
                    new ItemGroupCollection(),
                    new ItemMallCollection(),
                    new BrandRecomCollection()
            );
            return returnList;
        }
    },
    //변경예정 2019년 1월 강업후 삭제 MOBILE_BRAND_OMNI_DTL 
    MOBILE_DTL_BRAND{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new ItemGroupCollection(),
                    new BrandRecomCollection()
            );
            return returnList;
        }
    },
    MOBILE_ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            boolean isAdSearch = CollectionUtils.isAdSearch(parameter);
            parameter.setAdSearch(isAdSearch);

            List<Collection> returnList = CollectionUtils.asList(
                    new ItemCollection()
            );

            // 검색광고관련추가
            if (isAdSearch && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL, parameter.getLoadLevel())
                    && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                returnList.add(0, new CpcAdvertisingCollection());
            }

            return returnList;
        }
    },
    //2019년 1월에 강업후 삭제예정
    MOBILE_RECOM_ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            return Lists.newArrayList();
        }
    },
    MOBILE_NORESULT{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new ItemMallCollection());
        }
    },
    //2019년 1월에 강업후 삭제예정
    MOBILE_BOOK{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new BookCollection(),
                    new BookCategoryCollection(),
                    new BookGroupCollection(),
                    new ItemMallCollection(),
                    new EsSrchwdrlCollection(),
                    new EsSpellCollection()
            )
                    ;
            return returnList;
        }
    },
    MOBILE_BRAND_ALL{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new DispCollection(),
                    new MobileBrandDispCollection(),
                    new DispCommBrandGroupCollection()
            )
                    ;
            return returnList;
        }
    },
    MOBILE_BRAND{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new DispCollection(),
                    new MobileBrandDispCollection()
            )
                    ;
            return returnList;
        }
    },
    MOBILE_BRAND_ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new DispCollection());
        }
    },
    MOBILE_BRAND_DTL{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new MobileBrandDispCollection(),
                    new DispCommBrandGroupCollection()
            )
                    ;
            return returnList;
        }
    },
    //2019년 1월에 강업후 삭제예정
    MOBILE_PNSHOP{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new MobilePnshopCollection());
        }
    },
    DISP{
        public List<Collection> getCollectionSet(Parameter parameter){
            // 부하 LEVEL에 따라 getCollection 조절
            if(parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_WARNING) || parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING)){
                return CollectionUtils.asList(
                        new DispCollection(),
                        new DispItemGroupCollection()
                )
                        ;
            }else if(parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_LIMIT) || parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT)){
                return CollectionUtils.asList(
                        new DispCollection()
                )
                        ;
            }

            String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
            List<Collection> returnList = CollectionUtils.asList(
                    new DispCollection(),
                    new DispItemGroupCollection(),
                    new BookDispCollection()
            )
                    ;
            if(strSiteNo.equals("6005")){
                returnList.add(new DispMallCollection());
                returnList.add(new BookDispMallCollection());
            }
            return returnList;
        }
    },
    DISP_ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new DispCollection());
        }
    },
    DISP_DTL{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new DispCommItemGroupCollection());
        }
    },
    //  페레가모 등의 특정 샵
    SHOP{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new ItemCollection());
        }
    },
    SPSHOP{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new ItemCollection());
        }
    },
    POSTNG{
        public List<Collection> getCollectionSet(Parameter parameter){
            String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
            return null;
        }
    },
    FAQ{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new FaqCollection());
        }
    },
    RECIPE{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new RecipeCollection());
        }
    },
    RECOM{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new RecomCollection());
        }
    },
    PNSHOP{
        public List<Collection> getCollectionSet(Parameter parameter){
            String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
            List<Collection> returnList = Lists.newArrayList();
            if(StringUtils.contains("6004,6005", strSiteNo)){
                returnList.add(new PnshopCollection());
                returnList.add(new PnshopSdCollection());
            }
            else if(strSiteNo.equals("6009")){
                returnList.add(new PnshopSdCollection());
            }
            return returnList;
        }
    },
    BRAND_DISP{
        public List<Collection> getCollectionSet(Parameter parameter){
            if(parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_WARNING) || parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING)){
                return CollectionUtils.asList(
                        new DispCollection()
                )
                        ;
            }else if(parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_LIMIT) || parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT)){
                return CollectionUtils.asList(
                        new DispCollection()
                )
                        ;
            }
            String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
            List<Collection> returnList = CollectionUtils.asList(
                    new DispCollection(),
                    new BookDispCollection(),
                    new BrandDispCollection(),
                    new BookBrandDispCollection()
            )
                    ;
            if(strSiteNo.equals("6005")){
                returnList.add(new DispMallCollection());
                returnList.add(new BookDispMallCollection());
            }
            return returnList;
        }
    },
    BRAND_DTL{
        public List<Collection> getCollectionSet(Parameter parameter){
            return null;
        }
    },
    PARTNER{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new ItemCollection());
        }
    },
    VIRTUAL{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new ItemCollection());
        }
    },
    GLOBAL{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new GlobalItemCategoryCollection(), new GlobalGroupCategoryCollection(),  new GlobalItemCollection());
        }
    },
    GLOBAL_ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new GlobalGroupCategoryCollection(), new GlobalItemCollection());
        }
    },
    GLOBAL_CATEGORY{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new GlobalDispItemCategoryCollection(), new GlobalDispGroupCategoryCollection());
        }
    },
    GLOBAL_BRAND{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new GlobalDispItemBrandCollection(), new GlobalDispGroupBrandCollection());
        }
    },
    BSHOP{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new BshopItemCategoryCollection(),
                    new BshopItemCollection(),
                    new BshopGroupCollection(),
                    new BshopCollection()
            )
                    ;
            return returnList;
        }
    },
    BSHOP_BRAND{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new BshopCollection(), new BshopGroupCollection());
        }
    },
    BSHOP_ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new BshopItemCollection());
        }
    },
    BSHOP_DISP_ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new BshopDispItemCollection());
        }
    },
    BSHOP_DISP{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new BshopDispGroupCategoryCollection(),
                    new BshopDispItemCollection()
            )
                    ;
            return returnList;
        }
    },
    ISSUETHEME{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new IssueThemeCollection());
        }
    },
    ES_DISP{
        public List<Collection> getCollectionSet(Parameter parameter){
            String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
            List<Collection> returnList = CollectionUtils.asList(
                    new ESItem(),
                    new ESSizeLstGroup(),
                    new ESBookItem()
            );

            if(strSiteNo.equals("6005")){
                returnList.add(new ESMallCount());
                returnList.add(new ESBookMallCount());
            }

            returnList.add(new ESSellPrcLstGroup());
            returnList.add(new ESBrandIdGroup());

            return returnList;
        }
    },
    ES_DISP_ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new ESItem());
        }
    },
    ES_DISP_DTL{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new ESBrandIdGroup(), new ESSellPrcGroup());
        }
    },
    ES_SPCSHOP{
        public List<Collection> getCollectionSet(Parameter parameter){
            ESItem esItem = new ESItem();
            esItem.setCollectionName("spcshop");

            ESSizeLstGroup esSizeLstGroup = new ESSizeLstGroup();
            esSizeLstGroup.setCollectionName("spcshop");

            ESSellPrcLstGroup esSellPrcLstGroup = new ESSellPrcLstGroup();
            esSellPrcLstGroup.setCollectionName("spcshop");

            ESBrandIdGroup esBrandIdGroup = new ESBrandIdGroup();
            esBrandIdGroup.setCollectionName("spcshop");

            ESSellPrcLstGroup esSellLstPrcGroup =   new ESSellPrcLstGroup();
            esSellLstPrcGroup.setCollectionName("spcshop");

            List<Collection> returnList = CollectionUtils.asList(
                    esItem,
                    esSizeLstGroup,
                    esSellPrcLstGroup,
                    esBrandIdGroup,
                    esSellLstPrcGroup
            );

            return returnList;
        }
    },
    ES_BRAND_DISP{
        public List<Collection> getCollectionSet(Parameter parameter){
            String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
            List<Collection> returnList = CollectionUtils.asList(
                    new ESItem(),
                    new ESBookItem(),
                    new ESBrandCtgIdGroup(),
                    new ESBrandBookCtgIdGroup()
            )
                    ;
            if(strSiteNo.equals("6005")){
                returnList.add(new ESMallCount());
                returnList.add(new ESBookMallCount());
            }

            returnList.add(new ESSellPrcLstGroup());
            return returnList;
        }
    },
    /*
        ES_MOBILE_BRAND, ES_MOBILE_BRAND_ITEM
        삭제 금지 - 구버전 브랜드에서 사용
        /api/brand/search/datas.ssg
     */
    ES_MOBILE_BRAND{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new ESItem(),
                    new ESMobileCtgIdGroup()
            );

            return returnList;
        }
    },
    ES_MOBILE_BRAND_ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new ESItem());
        }
    },
    ES_PSNZ_BRAND_ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new ESItem());
        }
    },
    ES_VIRTUAL{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new ESVirtual());
        }
    },


    ES_GLOBAL_CATEGORY{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new ESGlobalItem(), new ESGlobalBrandIdGroup());
        }
    },
    ES_GLOBAL_BRAND{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new ESGlobalItem(), new ESGlobalCtgIdGroup());
        }
    },
    ES_BSHOP_DISP_ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new ESBshopItem());
        }
    },
    ES_BSHOP_DISP{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new ESBshopCtgIdGroup(),
                    new ESBshopItem()
            )
                    ;
            return returnList;
        }
    },
    ES_BUNDLE{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new ESItem(),
                    new ESBookItem(),
                    new ESBundleCtgIdGroup(),
                    new ESBundleBookCtgIdGroup(),
                    new ESBrandIdGroup(),
                    new ESSellPrcLstGroup()
            )
                    ;
            return returnList;
        }
    },


    SRCH_QUAL_ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            return CollectionUtils.asList(new SearchQualityCollection());
        }
    },
    //변경예정 2019년 1월 강업후 삭제 CHAT_GIFT_OMNI_ALL 
    CHAT_GIFT_ALL{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(
                    new ItemCollection(),
                    new ItemGroupCollection(),
                    new EsSrchwdrlCollection(),
                    new EsSpellCollection(),
                    new CategoryRecomCollection(),
                    new EsVirtualCategoryCollection(),
                    new BookCollection()
            );
        }
    },
    //변경예정 2019년 1월 강업후 삭제 CHAT_GIFT_OMNI_ITEM
    CHAT_GIFT_ITEM{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(
                    new ItemCollection(),
                    new BookCollection()
            );
        }
    },
    //변경예정 2019년 1월 강업후 삭제 CHAT_GIFT_OMNI_DTL
    CHAT_GIFT_DTL{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            List<Collection> returnList = CollectionUtils.asList(
                    new CategoryRecomCollection(),
                    new ItemCommCategoryCollection(),
                    new ItemGroupCollection(),
                    new ItemMallCollection(),
                    new BrandRecomCollection()
            );
            return returnList;
        }
    },
    //변경예정 2019년 1월 강업후 삭제 CHAT_OMNI_ALL 
    CHAT_SEARCH_ALL{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(
                    new ItemCollection(),
                    new ItemGroupCollection(),
                    new EsSrchwdrlCollection(),
                    new EsSpellCollection(),
                    new MobilePnshopCollection(),
                    new LifeMagazineCollection(),
                    new IssueThemeCollection(),
                    new TrecipeCollection(),
                    new CategoryRecomCollection(),
                    new EsVirtualCategoryCollection(),
                    new BookCollection()
            );
        }
    },
    //변경예정 2019년 1월 강업후 삭제 CHAT_OMNI_ITEM
    CHAT_SEARCH_ITEM{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(
                    new ItemCollection(),
                    new BookCollection()
            );
        }
    },
    //변경예정 2019년 1월 강업후 삭제 CHAT_OMNI_DTL
    CHAT_SEARCH_DTL{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(
                    new CategoryRecomCollection(),
                    new ItemCommCategoryCollection(),
                    new ItemGroupCollection(),
                    new ItemMallCollection(),
                    new BrandRecomCollection()
            );
        }
    },
    CHAT_VEN_ITEMS{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(
                    new ItemCollection(),
                    new ItemCommCategoryCollection()
            );
        }
    },
    
    // CHAT과 GIFT관련 신규 추가 시작
    CHAT_OMNI_ALL{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            List<Collection> returnList = CollectionUtils.asList(
                            new ItemCollection(),
                            new ItemGroupCollection(),
                            new EsSrchwdrlCollection(),
                            new EsSpellCollection(),
                            new MobilePnshopCollection(),
                            new LifeMagazineCollection(),
                            new TrecipeCollection(),
                            new EsVirtualCategoryCollection(),
                            new BookCollection(),
                            new BrandMasterCollection(),
                            new StarfieldCollection()
                    );
            
            // 내취타취추가
            boolean isRecommendSearch = CollectionUtils.isRecommendSearch(parameter);
            if (isRecommendSearch) {
                if (StringUtils.isNotBlank(parameter.getUserInfo().getMbrId())) {
                    returnList.add(new MyTasteCollection());
                }
                returnList.add(new PublicTasteCollection());
            } 
            
           return returnList;
        }
    },
    CHAT_GIFT_OMNI_ALL{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(
                    new ItemCollection(),
                    new ItemGroupCollection(),
                    new BookCollection()
            );
        }
    },
    CHAT_OMNI_ITEM{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(
                    new ItemCollection(),
                    new BookCollection()
            );
        }
    },
    CHAT_GIFT_OMNI_ITEM{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(
                    new ItemCollection(),
                    new BookCollection()
            );
        }
    },
    CHAT_OMNI_DTL{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(
                    new CategoryRecomCollection(),
                    new ItemCommCategoryCollection(),
                    new ItemGroupCollection(),
                    new ItemMallCollection(),
                    new BrandRecomCollection()
            );
        }
    },
    CHAT_GIFT_OMNI_DTL{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            List<Collection> returnList = CollectionUtils.asList(
                    new CategoryRecomCollection(),
                    new ItemCommCategoryCollection(),
                    new ItemGroupCollection(),
                    new ItemMallCollection(),
                    new BrandRecomCollection()
            );
            return returnList;
        }
    },
    // CHAT과 GIFT관련 신규 추가 끝
    LIFEMAGAZINE{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(
                    new LifeMagazineCollection()
            );
        }
    },
    TRECIPE{
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(
                    new TrecipeCollection()
            );
        }
    },
    TASTE{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            FrontUserInfo userInfo = parameter.getUserInfo();
            if(StringUtils.isNotBlank(userInfo.getMbrId())){
                return CollectionUtils.asList(new MyTasteCollection(), new PublicTasteCollection());
            }
            return CollectionUtils.asList(new PublicTasteCollection());
        }
    }
    ,
    BRANDMASTER{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(new BrandMasterCollection());
        }
    },
    MOBILE_TASTE{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> collectionList = CollectionUtils.asList(
                    new EsVirtualCategoryCollection()
            );

            // 내취타취추가
            boolean isRecommendSearch = CollectionUtils.isRecommendSearch(parameter);

            if (isRecommendSearch) {
                if (StringUtils.isNotBlank(parameter.getUserInfo().getMbrId())) {
                    collectionList.add(new MyTasteCollection());
                }

                collectionList.add(new PublicTasteCollection());
            }

            return collectionList;
        }
    },
    AD_CPC{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(new CpcAdvertisingCollection());
        }
    },
    AD_CPC_EXT{
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(new CpcExtAdvertisingCollection());
        }
    },
    STARFIELD{
        @Override
        public List<Collection> getCollectionSet(Parameter parameter) {
            return CollectionUtils.asList(new StarfieldCollection());
        }
    },
    MOBILE_OMNI_ALL{
        public List<Collection> getCollectionSet(Parameter parameter){
            // 검색광고관련추가
            boolean isAdSearch = CollectionUtils.isAdSearch(parameter);
            parameter.setAdSearch(isAdSearch);

            //상시배너조회추가
            parameter.setBanrEverSearch(true);

            String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");

            // 스타필드 조회시에는 바로 리턴
            if(strSiteNo.equals("6400")) {
                List<Collection> returnList = CollectionUtils.asList(
                        new EsSpellCollection(),
                        new EsBanrCollection(),
                        new StarfieldCollection()
                );

                if (StringUtils.equals(parameter.LOAD_LEVEL_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, parameter.getLoadLevel())
                        ||  StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                    returnList.clear();
                    returnList = CollectionUtils.asList(
                            new StarfieldCollection()
                    );
                }

                return returnList;
            }

            List<Collection> returnList = CollectionUtils.asList(
                    new ItemCollection(),
                    new ItemGroupCollection(),
                    new MallCollection(),
                    new EsSpellCollection(),
                    new EsSrchwdrlCollection(),
                    new EsBanrCollection(),
                    new MobilePnshopCollection(),
                    new LifeMagazineCollection(),
                    new BrandMasterCollection()
            );

            if(strSiteNo.equals("6001") ||strSiteNo.equals("6004") ||strSiteNo.equals("6005")){
                returnList.add(new BookCollection());
            }
            if(strSiteNo.equals("6001") ||strSiteNo.equals("6005")){
                returnList.add(new TrecipeCollection());
            }
            if(strSiteNo.equals("6001") ||strSiteNo.equals("6004") ||strSiteNo.equals("6009") ||strSiteNo.equals("6005")){
                returnList.add(new EsVirtualCategoryCollection());
            }
            if(strSiteNo.equals("6005")) {
                returnList.add(new StarfieldCollection());
            }
            
            // 내취타취추가
            boolean isRecommendSearch = CollectionUtils.isRecommendSearch(parameter);
            if (isRecommendSearch) {
                if (StringUtils.isNotBlank(parameter.getUserInfo().getMbrId())) {
                    returnList.add(new MyTasteCollection());
                }

                returnList.add(new PublicTasteCollection());
            }

            if (StringUtils.equals(parameter.LOAD_LEVEL_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_LIMIT, parameter.getLoadLevel())
                    ||  StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) || StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())
                    ) {
                returnList.clear();

                returnList = CollectionUtils.asList(
                        new ItemCollection(),
                        new MallCollection()
                );
            }

            // 검색광고관련추가
            if (isAdSearch && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL, parameter.getLoadLevel())
                    && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                //상품
                if(strSiteNo.equals("6001") ||strSiteNo.equals("6004") ||strSiteNo.equals("6005")){
                    returnList.add(0, new CpcAdvertisingCollection());
                }
                
                //배너
                if(strSiteNo.equals("6001") ||strSiteNo.equals("6004") ||strSiteNo.equals("6009") ||strSiteNo.equals("6005")){              
                    returnList.add(new BanrAdvertisingCollection());
                }
            }

            return returnList;
        }
    },
    MOBILE_OMNI_ITEM{
        public List<Collection> getCollectionSet(Parameter parameter){
            boolean isAdSearch = CollectionUtils.isAdSearch(parameter);
            parameter.setAdSearch(isAdSearch);

            List<Collection> returnList = CollectionUtils.asList(
                    new ItemCollection()
            );

            // 검색광고관련추가
            if (isAdSearch && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_GENERAL, parameter.getLoadLevel())
                    && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING, parameter.getLoadLevel()) && !StringUtils.equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT, parameter.getLoadLevel())) {
                returnList.add(0, new CpcAdvertisingCollection());
            }

            return returnList;
        }
    },
    MOBILE_OMNI_DTL{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new CategoryRecomCollection(),
                    new ItemCommCategoryCollection(),
                    new ItemGroupCollection(),
                    new ItemMallCollection(),
                    new BrandRecomCollection()
            );
            return returnList;
        }
    },
    MOBILE_OMNI_BOOK{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new BookCollection()
            );
            return returnList;
        }
    },
    MOBILE_BRAND_OMNI_DTL{
        public List<Collection> getCollectionSet(Parameter parameter){
            List<Collection> returnList = CollectionUtils.asList(
                    new ItemGroupCollection(),
                    new BrandRecomCollection()
            );
            return returnList;
        }
    },
    
    ;
    public abstract List<Collection> getCollectionSet(Parameter parameter);
}
