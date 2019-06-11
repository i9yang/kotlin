package ssg.search.util;

import QueryAPI510.Search;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.parameter.Parameter;
import ssg.search.result.*;

import java.util.*;

public class ResultUtils {
	public static Map<String, String> getSiteGroup(String siteInfo, String scomInfo){
		Map<String, String> mallCountMap = Maps.newHashMap();
		for(Iterator<String> iter = Splitter.on("@").trimResults().split(siteInfo).iterator();iter.hasNext();){
			String[] mallIds = iter.next().split("\\^");
			mallCountMap.put(mallIds[0], mallIds[2]);
		}
		for(Iterator<String> iter = Splitter.on("@").trimResults().split(scomInfo).iterator();iter.hasNext();){
			String[] mallIds = iter.next().split("\\^");
			if(mallIds[0].equals("Y")){
		        mallCountMap.put("6005", mallIds[2]);
		    }
		}
		return mallCountMap;
	}
	public static List<Size> getSizeGroup(String sizeInfo){
		Size size;
		List<Size> sizeList = Lists.newArrayList();
		for(Iterator<String> iter = Splitter.on("@").trimResults().split(sizeInfo).iterator();iter.hasNext();){
    		size = new Size();
    		String[] sizeIds = iter.next().split("\\^");
    		if(sizeIds.length>2){
    			size.setSizeCd(sizeIds[0]);
    			size.setSizeNm(sizeIds[1]);
    			size.setSizeCount(sizeIds[2]);
                sizeList.add(size);
            }
    	}
    	Collections.sort(sizeList, new Comparator<Size>(){
            public int compare(Size c1,Size c2){
            	try{
            		int k1 = Integer.parseInt(c1.getSizeCd());
            		int k2 = Integer.parseInt(c2.getSizeCd());
            		if(k1<k2){
	                    return -1;
	                } else if(k1>k2){
	                    return 1;
	                } else{
	                    return 0;
	                }
            	}catch(NumberFormatException ne){
            		return 0;
            	}
            }
        });
		return sizeList;
	}
	public static List<Brand> getBrandGroup(String brandInfo){
		Brand brand = null;
    	List<Brand> brandList = Lists.newArrayList();
    	for(Iterator<String> iter = Splitter.on("@").trimResults().split(brandInfo).iterator();iter.hasNext();){
            brand = new Brand();
            String[] brandIds = iter.next().split("\\^");
            if(brandIds.length>2){
                brand.setBrandId(brandIds[0]);
                brand.setBrandNm(brandIds[1].replaceAll("\\++", " "));
                brand.setItemCount(brandIds[2]);
                brandList.add(brand);
            }
        }
        return brandList;
	}
	public static List<Category> getGlobalCategoryGroup(String siteNo, String selectedCtgId, String ctgLst, String target){
		List<Category> ctgList  = new ArrayList<Category>();
        List<Category> ctgList2 = new ArrayList<Category>();
        List<Category> ctgList3 = new ArrayList<Category>();
        String ctgLv2Id = "";
        String ctgLv3Id = "";
        String selectedCtgLv = "";
        for(Iterator<String> iter = Splitter.on("@").trimResults().split(ctgLst).iterator();iter.hasNext();){
        	String ctgElement = iter.next();
			if(ctgElement!=null && !ctgElement.equals("")){
				int i=0;
				Category c = new Category();
				String ctgType = "";
				String ctgCls  = "";
				String ctgLv   = "";
				String priorCtgId = "";
				for(Iterator<String> ctgToken = Splitter.on("^").trimResults().split(ctgElement).iterator();ctgToken.hasNext();){
					String ctg = ctgToken.next();
					if(ctg!=null && !ctg.equals("")){
						// CTG_ID
						if(i == 0){
							c.setCtgId(ctg);
						}
						// CTGNM__CTGLV__SITE__CLS__TYPE__PRIOR_DISP_CTG_ID
						else if(i == 1){
							String[] ctgNms = ctg.split("\\__");
							if(ctgNms.length>=6){
								ctgLv = ctgNms[1];
								c.setCtgNm(ctgNms[0].replaceAll("\\++", " "));
								c.setCtgLevel(Integer.parseInt(ctgLv));
								c.setSiteNo(ctgNms[2]);
								c.setPriorCtgId(ctgNms[5]);
								priorCtgId = ctgNms[5];
								ctgCls  = ctgNms[3];
								ctgType = ctgNms[4];
							}
						}
						// ITEM_COUNT
						else if(i == 2){
							c.setCtgItemCount(Integer.parseInt(ctg));
						}
					}
					if(i>0 && i%2 == 0){
						i = 0;
					}else i++;
				}
				// 현재 넘어온 카테고리 ID 를 기준으로 레벨과 ID를 추적한다.
				if(selectedCtgId!=null && !selectedCtgId.equals("") && selectedCtgId.equals(c.getCtgId())){
					selectedCtgLv = ctgLv;
					// 선택된 카테고리가 중카
					if(selectedCtgLv.equals("2")){
						ctgLv2Id = c.getCtgId();
					}
					// 선택된 카테고리가 소카
					else if(selectedCtgLv.equals("3")){
						ctgLv2Id = c.getPriorCtgId();
						ctgLv3Id = c.getCtgId();
					}
				}
				if(selectedCtgLv.equals("2") && selectedCtgId.equals(c.getCtgId())){
					c.setSelectedArea("selected");
				}
				// 해외직구관 카테고리만 ADD 한다.
				if(siteNo.equals(c.getSiteNo())){
					if(ctgCls.equals("20") && ctgType.equals("10") && ctgLv.equals("2") && priorCtgId.equals("6000015276")){
						ctgList2.add(c);
					}else if(ctgCls.equals("20") && ctgType.equals("10") && ctgLv.equals("3")){
						ctgList3.add(c);
					}
				}
			}
        }
        int totalCount = 0;
        String selectedPriorCtgId = "";
        if(ctgList3!=null && ctgList3.size()>0 && ctgList3!=null && ctgList3.size()>0){
        	// CATEGORY LEVEL3 SORT
	        Collections.sort(ctgList3,new Comparator<Category>() {
	        	public int compare(Category c1, Category c2){
	        		String p1 = c1.getPriorCtgId();
	        		String p2 = c2.getPriorCtgId();
	        		return (p1).compareTo(p2);
	        	}
	        });
            if(target.equals("global_brand")){
            	for(int index=0;index<ctgList2.size();index++){
            		Category c2 = ctgList2.get(index);
            		ctgList.add(c2);
					for (Category c3 : ctgList3) {
						if(c2.getCtgId().equals(c3.getPriorCtgId())){
							ctgList.add(c3);
							if(selectedCtgId.equals(c3.getCtgId())){
								c2.setSelectedArea("selected");
							}
						}
					}
				}
            }
        }
        return ctgList;
	}
	public static List<Category> getBrandCategoryGroup(String siteNo, String selectedCtgId, String ctgLst){
		List<Category> ctgList  = new ArrayList<Category>();
		List<Category> ctgList1 = new ArrayList<Category>();
		List<Category> ctgList2 = new ArrayList<Category>();
		List<Category> ctgList3 = new ArrayList<Category>();

		int ctgLv1 = 1;
		int ctgLv2 = 2;
		int ctgLv3 = 3;

		if(siteNo.equals("6001")){
			ctgLv1 = 1;
			ctgLv2 = 2;
		}

		for(Iterator<String> iter = Splitter.on("@").trimResults().split(ctgLst).iterator();iter.hasNext();){
			String ctgElement = iter.next();
			if(ctgElement!=null && !ctgElement.equals("")){
				int i=0;
				Category c = new Category();
				String ctgType = "";
				String ctgCls  = "";
				String ctgLv   = "";
				for(Iterator<String> ctgToken = Splitter.on("^").trimResults().split(ctgElement).iterator();ctgToken.hasNext();){
					String ctg = ctgToken.next();
					if(ctg!=null && !ctg.equals("")){
						// CTG_ID
						if(i == 0){
							c.setCtgId(ctg);
						}
						// CTGNM__CTGLV__SITE__CLS__TYPE__PRIOR_DISP_CTG_ID
						else if(i == 1){
							String[] ctgNms = ctg.split("\\__");
							if(ctgNms.length>=6){
								ctgLv = ctgNms[1];
								c.setCtgNm(ctgNms[0].replaceAll("\\++", " "));
								c.setCtgLevel(Integer.parseInt(ctgLv));
								c.setSiteNo(ctgNms[2]);
								c.setPriorCtgId(ctgNms[5]);
								ctgCls  = ctgNms[3];
								ctgType = ctgNms[4];
							}
						}
						// ITEM_COUNT
						else if(i == 2){
							c.setCtgItemCount(Integer.parseInt(ctg));
						}
					}
					if(i>0 && i%2 == 0){
						i = 0;
					}else i++;
				}
				// 현재의 사이트와 동일할 경우에만 add
				if(siteNo.equals(c.getSiteNo())){
					if(ctgCls.equals("10") && ctgType.equals("10") && ctgLv.equals(String.valueOf(ctgLv1))){
						ctgList1.add(c);
					}else if(ctgCls.equals("10") && ctgType.equals("10") && ctgLv.equals(String.valueOf(ctgLv2))){
						ctgList2.add(c);
					}else if(ctgCls.equals("10") && ctgType.equals("10") && ctgLv.equals(String.valueOf(ctgLv3)) && siteNo.equals("6003")){
						ctgList3.add(c);
					}
				}
			}
		}
		if(ctgList1!=null && ctgList1.size()>0 && ctgList2!=null && ctgList2.size()>0){
			// CATEGORY LEVEL1 SORT
			Collections.sort(ctgList1,new Comparator<Category>() {
				public int compare(Category c1, Category c2){
					String d1 = c1.getCtgId();
					String d2 = c2.getCtgId();
					return (d1).compareTo(d2);
				}
			});

			// CATEGORY LEVEL2 SORT
			Collections.sort(ctgList2,new Comparator<Category>() {
				public int compare(Category c1, Category c2){
					String p1 = c1.getPriorCtgId();
					String p2 = c2.getPriorCtgId();
					return (p1).compareTo(p2);
				}
			});

			// CATEGORY LEVEL3 SORT
			Collections.sort(ctgList3,new Comparator<Category>() {
				public int compare(Category c1, Category c2){
					String p1 = c1.getPriorCtgId();
					String p2 = c2.getPriorCtgId();
					return (p1).compareTo(p2);
				}
			});

			int totalCount = 0;
			String selectedPriorCtgId = "";		// 선택된 상위 카테고리를 찾는다.
			for (Category c1 : ctgList1) {
				if(selectedCtgId!=null && !selectedCtgId.equals("") && c1.getCtgId().equals(selectedCtgId)){
					c1.setSelectedArea("selected");
				}
				totalCount+=c1.getCtgItemCount();
				ctgList.add(c1);
				for (Category c2 : ctgList2) {
					if(c1.getCtgId().equals(c2.getPriorCtgId())){
						if(selectedCtgId!=null && !selectedCtgId.equals("") && c2.getCtgId().equals(selectedCtgId)){
							selectedPriorCtgId = c2.getPriorCtgId();
						}
						ctgList.add(c2);
						if(ctgList3!=null && ctgList3.size()>0) {
							for (Category c3 : ctgList3) {
								if (c2.getCtgId().equals(c3.getPriorCtgId())) {
									if (selectedCtgId != null && !selectedCtgId.equals("") && c3.getCtgId().equals(selectedCtgId)) {
										selectedPriorCtgId = c3.getPriorCtgId();
									}
									ctgList.add(c3);
								}
							}
						}
					}
				}
			}
			// 선택된 카테고리 찾기
			if(selectedPriorCtgId!=null && !selectedPriorCtgId.equals("")){
				for (int i=0;i<ctgList.size();i++) {
					Category c = ctgList.get(i);
					if(c.getCtgId().equals(selectedPriorCtgId)){
						c.setSelectedArea("selected");
						ctgList.set(i,c);
					}
				}
			}
			// SSG의 경우에는 전체 넣어주기
			if(siteNo.equals("6005")){
				Category totCtg = new Category();
				totCtg.setCtgId("0000000000");
				totCtg.setPriorCtgId("0000000000");
				totCtg.setCtgLevel(1);
				totCtg.setCtgNm("전체");
				totCtg.setCtgItemCount(totalCount);
				ctgList.add(0, totCtg);
			}
		}
		return ctgList;
	}

	public static List<Category> getNewBrandCategoryGroup(String siteNo, String selectedCtgId, String ctgLst){
		List<Category> ctgList  = new ArrayList<Category>();
        List<Category> ctgList1 = new ArrayList<Category>();
        List<Category> ctgList2 = new ArrayList<Category>();

        int ctgLv1 = 1;
        int ctgLv2 = 2;

        for(Iterator<String> iter = Splitter.on("@").trimResults().split(ctgLst).iterator();iter.hasNext();){
        	String ctgElement = iter.next();
			if(ctgElement!=null && !ctgElement.equals("")){
				int i=0;
				Category c = new Category();
				String ctgType = "";
				String ctgCls  = "";
				String ctgLv   = "";
				for(Iterator<String> ctgToken = Splitter.on("^").trimResults().split(ctgElement).iterator();ctgToken.hasNext();){
					String ctg = ctgToken.next();
					if(ctg!=null && !ctg.equals("")){
						// CTG_ID
						if(i == 0){
							c.setCtgId(ctg);
						}
						// CTGNM__CTGLV__SITE__CLS__TYPE__PRIOR_DISP_CTG_ID
						else if(i == 1){
							String[] ctgNms = ctg.split("\\__");
							if(ctgNms.length>=6){
								ctgLv = ctgNms[1];
								c.setCtgNm(ctgNms[0].replaceAll("\\++", " "));
								c.setCtgLevel(Integer.parseInt(ctgLv));
								c.setSiteNo(ctgNms[2]);
								c.setPriorCtgId(ctgNms[5]);
								ctgCls  = ctgNms[3];
								ctgType = ctgNms[4];
							}
						}
						// ITEM_COUNT
						else if(i == 2){
							c.setCtgItemCount(Integer.parseInt(ctg));
						}
					}
					if(i>0 && i%2 == 0){
						i = 0;
					}else i++;
				}
				// 현재의 사이트와 동일할 경우에만 add
				if(siteNo.equals(c.getSiteNo())){
					if(ctgCls.equals("10") && ctgType.equals("10") && ctgLv.equals(String.valueOf(ctgLv1))){
						ctgList1.add(c);
					}else if(ctgCls.equals("10") && ctgType.equals("10") && ctgLv.equals(String.valueOf(ctgLv2))){
						ctgList2.add(c);
					}
				}
			}
        }
        if(ctgList1!=null && ctgList1.size()>0 && ctgList2!=null && ctgList2.size()>0){
        	// CATEGORY LEVEL1 SORT
	        Collections.sort(ctgList1,new Comparator<Category>() {
	        	public int compare(Category c1, Category c2){
	        		String d1 = c1.getCtgId();
	        		String d2 = c2.getCtgId();
	        		return (d1).compareTo(d2);
	        	}
			});

	        // CATEGORY LEVEL2 SORT
	        Collections.sort(ctgList2,new Comparator<Category>() {
	        	public int compare(Category c1, Category c2){
	        		String p1 = c1.getPriorCtgId();
	        		String p2 = c2.getPriorCtgId();
	        		return (p1).compareTo(p2);
	        	}
	        });

	        int totalCount = 0;
	        String selectedPriorCtgId = "";		// 선택된 상위 카테고리를 찾는다.
	        for (Category c1 : ctgList1) {
	        	if(selectedCtgId!=null && !selectedCtgId.equals("") && c1.getCtgId().equals(selectedCtgId)){
	        		c1.setSelectedArea("selected");
	        	}
	        	totalCount+=c1.getCtgItemCount();
	        	ctgList.add(c1);
				for (Category c2 : ctgList2) {
					if(c1.getCtgId().equals(c2.getPriorCtgId())){
						if(selectedCtgId!=null && !selectedCtgId.equals("") && c2.getCtgId().equals(selectedCtgId)){
							selectedPriorCtgId = c2.getPriorCtgId();
			        	}
						ctgList.add(c2);
					}
				}
			}
	        // 선택된 카테고리 찾기
	        if(selectedPriorCtgId!=null && !selectedPriorCtgId.equals("")){
	        	for (int i=0;i<ctgList.size();i++) {
	        		Category c = ctgList.get(i);
					if(c.getCtgId().equals(selectedPriorCtgId)){
						c.setSelectedArea("selected");
						ctgList.set(i,c);
					}
				}
	        }

        }
        return ctgList;
	}
	
	public static Result getItemResult(String name,Parameter parameter, Result result, Search search){
		int count = search.w3GetResultCount(name);
		List<Item> itemList = Lists.newArrayList();
		StringBuilder srchItemIds = new StringBuilder();
		Item item;
        for(int i=0;i<count;i++){
        	item = getItemSetting(search, name, i, parameter);
        	StringBuilder ids = new StringBuilder().append(item.getSiteNo()).append(":").append(item.getItemId()).append(":").append(item.getSalestrNo()).append(",");
        	if(srchItemIds.indexOf(ids.toString()) < 0){
        	    srchItemIds.append(ids);
        	}
        	itemList.add(item);
        }
        // 실패검색어 추천상품을 위해
        if(name.equals("mall")){
        	result.setNoResultItemRecomList(itemList);
        	result.setNoResultItemRecomIds(srchItemIds.toString());
        	result.setNoResultItemCount(search.w3GetResultTotalCount(name));
        }
        // BOOK LIST
        else if(name.indexOf("book")>-1){
        	result.setBookItemIds(srchItemIds.toString());
    		result.setBookCount(search.w3GetResultTotalCount(name));
    		result.setBookItemList(itemList);
        }
        // ITEM LIST
        else{
        	result.setItemList(itemList);
        	result.setItemCount(search.w3GetResultTotalCount(name));
        	result.setSrchItemIds(srchItemIds.toString());
        }
		return result;
	}
	
	
	public static Result getAdItemResult(String name,Parameter parameter, Result result, Search search){
		int count = search.w3GetResultCount(name);
		List<Item> itemList = Lists.newArrayList();
		StringBuilder srchItemIds = new StringBuilder();
		Item item;
		
		int page = parameter.getPageToInt();
		int adItemCount = result.getAdvertisingCount();
		int adSearchItemCount = parameter.getAdSearchItemCount();
		int searchCount = Integer.parseInt(StringUtils.defaultIfEmpty(parameter.getCount(), "40"));
		String aplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "10"); 
		//int adItemDispIndex =  StringUtils.equals(aplTgtMediaCd, "10") ? 4 : 3;
		int adItemDispIndex =  1;
		
		int startInx = 0; 
		
		if (page > 1) {
			startInx = adSearchItemCount - adItemCount;
		}
		
        for(int i=startInx;i<count;i++){
        	if (page == 1 && adItemCount > 0 && i == adItemDispIndex) {
        		for (int j=0;j<adItemCount;j++) {
        			item = getAdvertisingItemSetting(result.getAdvertisingList(), j);
		        	
		        	StringBuilder ids = new StringBuilder().append(item.getSiteNo()).append(":").append(item.getItemId()).append(":").append(item.getSalestrNo()).append(",");
		        	if(srchItemIds.indexOf(ids.toString()) < 0){
		        	    srchItemIds.append(ids);
		        	}
		        	
		        	itemList.add(item);
        		}
        	}
        	
        	item = getItemSetting(search, name, i, parameter);

        	StringBuilder ids = new StringBuilder().append(item.getSiteNo()).append(":").append(item.getItemId()).append(":").append(item.getSalestrNo()).append(",");
        	if(srchItemIds.indexOf(ids.toString()) < 0){
        	    srchItemIds.append(ids);
        	}
        	itemList.add(item);
        	
        	if (itemList.size() == searchCount) {
        		break;
        	}
        }
        
        
        if (page == 1 && adItemCount > 0 && count <= adItemDispIndex) {
    		for (int j=0;j<adItemCount;j++) {
    			item = getAdvertisingItemSetting(result.getAdvertisingList(), j);
	        	
	        	StringBuilder ids = new StringBuilder().append(item.getSiteNo()).append(":").append(item.getItemId()).append(":").append(item.getSalestrNo()).append(",");
	        	if(srchItemIds.indexOf(ids.toString()) < 0){
	        	    srchItemIds.append(ids);
	        	}
	        	
	        	itemList.add(item);
    		}
    	}
        
        // 실패검색어 추천상품을 위해
        if(name.equals("mall")){
        	result.setNoResultItemRecomList(itemList);
        	result.setNoResultItemRecomIds(srchItemIds.toString());
        	result.setNoResultItemCount(search.w3GetResultTotalCount(name));
        }
        // BOOK LIST
        else if(name.indexOf("book")>-1){
        	result.setBookItemIds(srchItemIds.toString());
    		result.setBookCount(search.w3GetResultTotalCount(name));
    		result.setBookItemList(itemList);
        }
        // ITEM LIST
        else{
        	result.setItemList(itemList);
        	result.setItemCount(search.w3GetResultTotalCount(name) + adItemCount);			//광고상품의 카운트를 더해준다 
        	result.setSrchItemIds(srchItemIds.toString());
        }
		return result;
	}
	public static List<Map<String,List<Category>>> getMobileBrandCategoryGroup(String siteNo, String selectedCtgId, String ctgLst){
		List<Map<String,List<Category>>> ctgList = new ArrayList<Map<String,List<Category>>>();
        List<Category> ctgList1 = new ArrayList<Category>();
        List<Category> ctgList2 = new ArrayList<Category>();
        for(Iterator<String> iter = Splitter.on("@").trimResults().split(ctgLst).iterator();iter.hasNext();){
        	String ctgElement = iter.next();
			if(ctgElement!=null && !ctgElement.equals("")){
				int i=0;
				Category c = new Category();
				String ctgType = "";
				String ctgCls  = "";
				String ctgLv   = "";
				for(Iterator<String> ctgToken = Splitter.on("^").trimResults().split(ctgElement).iterator();ctgToken.hasNext();){
					String ctg = ctgToken.next();
					if(ctg!=null && !ctg.equals("")){
						// CTG_ID
						if(i == 0){
							c.setCtgId(ctg);
						}
						// CTGNM__CTGLV__SITE__CLS__TYPE__PRIOR_DISP_CTG_ID
						else if(i == 1){
							String[] ctgNms = ctg.split("\\__");
							if(ctgNms.length>=6){
								ctgLv = ctgNms[1];
								c.setCtgNm(ctgNms[0].replaceAll("\\++", " "));
								c.setCtgLevel(Integer.parseInt(ctgLv));
								c.setSiteNo(ctgNms[2]);
								c.setPriorCtgId(ctgNms[5]);
								ctgCls  = ctgNms[3];
								ctgType = ctgNms[4];
							}
						}
						// ITEM_COUNT
						else if(i == 2){
							c.setCtgItemCount(Integer.parseInt(ctg));
						}
					}
					if(i>0 && i%2 == 0){
						i = 0;
					}else i++;
				}
				// 현재의 사이트와 동일할 경우에만 add
				if(siteNo.equals(c.getSiteNo())){
					if(ctgCls.equals("10") && ctgType.equals("10") && ctgLv.equals("1")){
						ctgList1.add(c);
					}else if(ctgCls.equals("10") && ctgType.equals("10") && ctgLv.equals("2")){
						ctgList2.add(c);
					}
				}
			}
        }
        if(ctgList1!=null && ctgList1.size()>0 && ctgList2!=null && ctgList2.size()>0){
        	// CATEGORY LEVEL1 SORT
	        Collections.sort(ctgList1,new Comparator<Category>() {
	        	public int compare(Category c1, Category c2){
	        		String d1 = c1.getCtgId();
	        		String d2 = c2.getCtgId();
	        		return (d1).compareTo(d2);
	        	}
			});

	        // CATEGORY LEVEL2 SORT
	        Collections.sort(ctgList2,new Comparator<Category>() {
	        	public int compare(Category c1, Category c2){
	        		String p1 = c1.getPriorCtgId();
	        		String p2 = c2.getPriorCtgId();
	        		return (p1).compareTo(p2);
	        	}
	        });

	        String selectedPriorCtgId = "";		// 선택된 1 LV 카테고리를 찾는다.
	        Map<String,List<Category>> ctgMap1 = Maps.newHashMap();
	        List<Category> t1 = new ArrayList<Category>();
        	List<Category> t2 = new ArrayList<Category>();

	        for (Category c1 : ctgList1) {
	        	if(selectedCtgId!=null && !selectedCtgId.equals("") && c1.getCtgId().equals(selectedCtgId)){
	        		c1.setSelectedArea("selected");
	        	}
	        	t1.add(c1);
				for (Category c2 : ctgList2) {
					if(c1.getCtgId().equals(c2.getPriorCtgId())){
						if(selectedCtgId!=null && !selectedCtgId.equals("") && c2.getCtgId().equals(selectedCtgId)){
							selectedPriorCtgId = c2.getPriorCtgId();
						}
						t2.add(c2);
					}
				}
			}
	        ctgMap1.put("ctg1", t1);
	        ctgMap1.put("ctg2", t2);

	        Map<String,List<Category>> ctgM = new HashMap<String, List<Category>>();
	        // 선택된 카테고리 찾기
	        if(selectedPriorCtgId!=null && !selectedPriorCtgId.equals("")){
	        	List<Category> ctg1= ctgMap1.get("ctg1");
	        	for (int i=0;i<ctg1.size();i++) {
	        		Category c1 = ctg1.get(i);
	        		if(c1.getCtgId().equals(selectedPriorCtgId)){
	        			c1.setSelectedArea("selected");
	        			t1.set(i,c1);
					}
	        	}
	        	List<Category> ctg2= ctgMap1.get("ctg2");
	        	for (int i=0;i<ctg2.size();i++) {
	        		Category c2 = ctg2.get(i);
	        		if(c2.getCtgId().equals(selectedPriorCtgId)){
	        			c2.setSelectedArea("selected");
	        			t2.set(i,c2);
					}
	        	}
	        }
	        //카테고리 전체 리스트로 묶기
	        for(int i=0; i< ctgList1.size(); i++){
	        	Category c1 = ctgList1.get(i);
	        	List<Category> c1List = new ArrayList<Category>();
	        	c1List.add(c1);
	        	for (Category c2 : ctgList2) {
	        		if(c1.getCtgId().equals(c2.getPriorCtgId())){
	        			c1List.add(c2);
	        		}
	        	}
	        	ctgM.put("ctg"+i,c1List);
	        	ctgList.add(ctgM);
	        }
        }
        return ctgList;
	}

	public static List<Bshop> getBshopGroup(String bshopInfo){
		List<Bshop> bshopList  = new ArrayList<Bshop>();
        for(Iterator<String> iter = Splitter.on("@").trimResults().split(bshopInfo).iterator();iter.hasNext();){
        	String bshopElement = iter.next();
			if(bshopElement!=null && !bshopElement.equals("") && bshopElement.indexOf("__") > -1){
				int i=0;
				Bshop c = new Bshop();
				for(Iterator<String> bshopToken = Splitter.on("^").trimResults().split(bshopElement).iterator();bshopToken.hasNext();){
					String bshop = bshopToken.next();
					if(bshop!=null && !bshop.equals("")){
						// BSHOPID
						if(i == 0){
							c.setBshopId(bshop);
						}
						// BSHOPTITLE__BSHOPENGTITLE1__BSHOPENGTITLE2__IMGURL__REPBRANDID
						else if(i == 1){
							String[] bshopNms = bshop.split("\\__");
							if(bshopNms.length>=5){
								c.setBshopTitleNm(bshopNms[0].replaceAll("\\++", " "));
								c.setBshopEngTitleNm1(bshopNms[1].replaceAll("\\++", " "));
								c.setBshopEngTitleNm2(bshopNms[2].replaceAll("\\++", " "));
								c.setRepBrandId(bshopNms[4]);
								
								String imgFileNm = StringUtils.defaultIfEmpty(bshopNms[3], "");
					            if(!imgFileNm.equals("")) imgFileNm = "http://static.ssgcdn.com"+ imgFileNm;
					            c.setImgFileNm(imgFileNm);
							}
						}
						// ITEM_COUNT
						else if(i == 2){
							c.setBshopItemCount(Integer.parseInt(bshop));
						}
					}
					if(i>0 && i%2 == 0){
						i = 0;
					}else i++;
				}
				bshopList.add(c);
			}
        }
        return bshopList;
	}
	public static List<MobileCategory> getMobileCategoryGroup(Search search, String name, String idx, int maxLevel){
		List<MobileCategory> cList = new ArrayList<MobileCategory>();
		Map<String, List<MobileCategory>> categoryMap = Maps.newHashMap();
		for(int level=1;level<=maxLevel;level++){
			List<MobileCategory> categoryList = Lists.newArrayList();
			
			//1레벨부터 전체 카테고리 돌린다.(부모/자식 상관없이 일단 모든 카테고리 다 넣는다)
			for(int i=0;i<search.w3GetCategoryCount(name, idx, level);i++){
				
				//1레벨은 1레벨만   ( 6001^0006510000^생활용품/세제/제지 )
				//2레벨부터는 자기자신 포함 전 레벨을 다 가져옴 ( 6001^0006510000^생활용품/세제/제지:6001^0006510003^세탁/주방/생활 세제 )
				String s = search.w3GetCategoryName(name, idx, level, i);
				String[] t = s.split(":");
				String[] categoryToken = t[level-1].split("\\^");
				
				MobileCategory category = new MobileCategory();
				category.setSiteNo(categoryToken[0]);
				category.setDispCtgId(categoryToken[1]);
				category.setDispCtgNm(categoryToken[2]);
				category.setItemCount(String.valueOf(search.w3GetDocumentCountInCategory(name, idx, level, i)));
				category.setDispCtgLvl(String.valueOf(level));
				category.setHasChild("false");
				if(level>1)category.setPriorDispCtgId(t[level-2].split("\\^")[1]);
				else category.setPriorDispCtgId("");
				categoryList.add(category);
			}
			
			// CATEGORY SORT(상품 많은순)
			if(categoryList!=null && categoryList.size()>0){
				Collections.sort(categoryList, new Comparator <MobileCategory>(){
					public int compare(MobileCategory c1,MobileCategory c2){
		                if(Integer.parseInt(c1.getItemCount())> Integer.parseInt(c2.getItemCount())){
		                    return -1;
		                } else if(Integer.parseInt(c1.getItemCount())<Integer.parseInt(c2.getItemCount())){
		                    return 1;
		                } else{
		                    return 0;
		                }
		            }
		        });
			}
			categoryMap.put(String.valueOf(level), categoryList);
		}

		// Set 한 데이터의 참조를 가지고 부모/자식관계를 생성한다.
		for(int level=maxLevel;level>1;level--){
			for(MobileCategory prior : categoryMap.get(String.valueOf(level-1))){
				for(MobileCategory target : categoryMap.get(String.valueOf(level))){
					if(target.getPriorDispCtgId().equals(prior.getDispCtgId())){
						prior.setHasChild("true");
						prior.add(target);
					}
				}
			}
		}
		cList = categoryMap.get("1");
		return cList;
	}	
	
	public static Item getAdvertisingItemSetting (List<Advertising> advertisingList, int i) {
		Item item = new Item();
		
		Advertising advertising = advertisingList.get(i);
		
		String strAdvertAcctId 	= advertising.getAdvertAcctId();
    	String strAdvertBidId 	= advertising.getAdvertBidId();
    	String strSiteNo  		= advertising.getSiteNo();
    	String strItemId  		= advertising.getItemId();
    	String strItemNm  		= advertising.getItemNm();
    	String strSellprc 		= advertising.getSellprc();
    	String strItemRegDivCd 	= advertising.getItemRegDivCd();
    	String strShppTypeDtlCd = advertising.getShppTypeDtlCd();
    	String strExusItemDivCd = advertising.getExusItemDivCd();
    	String strExusItemDtlCd = advertising.getExusItemDtlCd();
    	String strShppMainCd 	= advertising.getShppMainCd();
    	String strShppMthdCd	= advertising.getShppMthdCd();
    	String advertBilngTypeCd = advertising.getAdvertBilngTypeCd();		// 10 CPT / 20 CPC
    	String advertKindCd 	= advertising.getAdvertKindCd();			// 10 딜광고 / 20 검색광고 / 30 외부광고
    	String advertExtensTeryDivCd = advertising.getAdvertExtensTeryDivCd();
    	
    	String strSalestrNo = "6005";
    	
    	item.setSalestrNo(strSalestrNo);
    	item.setSiteNo(strSiteNo);
    	item.setItemId(strItemId);
    	item.setItemNm(strItemNm);
    	item.setAdvertAcctId(strAdvertAcctId);
    	item.setAdvertBidId(strAdvertBidId);
    	item.setSellprc(strSellprc);
    	item.setItemRegDivCd(strItemRegDivCd);
    	item.setShppTypeDtlCd(strShppTypeDtlCd);
    	item.setExusItemDivCd(strExusItemDivCd);
    	item.setExusItemDtlCd(strExusItemDtlCd);
    	item.setShppMainCd(strShppMainCd);
    	item.setShppMthdCd(strShppMthdCd);
    	item.setAdvertBilngTypeCd(advertBilngTypeCd);
    	item.setAdvertKindCd(advertKindCd);
    	item.setAdvertExtensTeryDivCd(advertExtensTeryDivCd);
		
		return item;
	}
	
	public static Item getItemSetting (Search search, String name, int i, Parameter parameter) {
		Item item = new Item();
		
		String strItemId = search.w3GetField(name,"ITEM_ID",i);
    	String strSiteNo = search.w3GetField(name,"SITE_NO",i);
    	String strItemNm = search.w3GetField(name,"ITEM_NM",i);
    	String sellprc     = search.w3GetField(name, "SELLPRC", i);
    	String shppTypeCd = search.w3GetField(name, "SHPP_TYPE_CD", i);
    	String shppTypeDtlCd = search.w3GetField(name, "SHPP_TYPE_DTL_CD", i);
    	String itemRegDivCd  = search.w3GetField(name, "ITEM_REG_DIV_CD", i);
    	String strSalestrNo = "";
    	
    	String salestrLst = search.w3GetField(name, "SALESTR_LST", i);
    	String exusItemDivCd = search.w3GetField(name,"EXUS_ITEM_DIV_CD", i);
    	String exusItemDtlCd = search.w3GetField(name,"EXUS_ITEM_DTL_CD", i);
    	String shppMainCd = search.w3GetField(name,"SHPP_MAIN_CD", i);
    	String shppMthdCd = search.w3GetField(name,"SHPP_MTHD_CD", i);
    	// 상품의 이빨이 빠졌을 때 점포값이 필수인 것들이 있기 때문에 강제로  점포 매핑작업을 한다.
    	FrontUserInfo userInfo = parameter.getUserInfo();
    	if(strSiteNo.equals("6009") && itemRegDivCd.equals("30")){
    		if(salestrLst.indexOf("0001")>-1){
    			int idx = 0;
    			salestrLst = salestrLst.replace("0001,", "").trim();
    			salestrLst = salestrLst.replace("0001", "").trim();
    			for(StringTokenizer st = new StringTokenizer(salestrLst," ");st.hasMoreTokens();){
    				if(idx>1)break;
    				strSalestrNo = st.nextToken().replace("D", "");
    	            idx++;
    			}
    			item.setSellSalestrCnt(1);
    		}
    		else{
    			strSalestrNo = salestrLst.replaceAll("\\p{Space}", "").replace("D", "");
    			strSalestrNo = salestrLst.replaceAll("\\p{Space}", "").replace("Y", "");	//백화점쓱배송점포처리
    			item.setSellSalestrCnt(1);
    		}
    	}
    	// 그외 이/트/분 상품의 경우에는 거점점포의 데이터를 넣어준다.
        else if(strSiteNo.equals("6001") && itemRegDivCd.equals("20")){
        	strSalestrNo = userInfo.getEmSaleStrNo();
        	item.setSellSalestrCnt(1);
        }else if(strSiteNo.equals("6002") && itemRegDivCd.equals("20")){
        	strSalestrNo = userInfo.getTrSaleStrNo();
        	item.setSellSalestrCnt(1);
        }else if(strSiteNo.equals("6003") && itemRegDivCd.equals("20")){
        	strSalestrNo = userInfo.getBnSaleStrNo();
        	item.setSellSalestrCnt(1);
        }else{
        	strSalestrNo = "6005";
			item.setSellSalestrCnt(1);
        }
    	item.setSalestrNo(strSalestrNo);
    	item.setSiteNo(strSiteNo);
    	item.setItemId(strItemId);
    	item.setItemNm(strItemNm);
    	
    	item.setSellprc(sellprc);
    	item.setShppTypeCd(shppTypeCd);
    	item.setShppTypeDtlCd(shppTypeDtlCd);
    	item.setItemRegDivCd(itemRegDivCd);
    	item.setExusItemDivCd(exusItemDivCd);
    	item.setExusItemDtlCd(exusItemDtlCd);
    	item.setShppMainCd(shppMainCd);
    	item.setShppMthdCd(shppMthdCd);

    	if(name.indexOf("book")>-1){
    		item.setAuthorNm(search.w3GetField(name, "AUTHOR_NM", i));
    		item.setTrltpeNm(search.w3GetField(name, "TRLTPE_NM", i));
    		item.setPubscoNm(search.w3GetField(name, "PUBSCO_NM", i));
    	}
		
		return item;
	}

	public static List<Prc> getSellprcGroupping(String sellprcLst) {
		try {
			List<Prc> prcGroupList = Lists.newArrayList();
			List<Sellprc> sellprcAList = Lists.newArrayList();
			List<Sellprc> sellprcBList = Lists.newArrayList();
			List<Sellprc> sellprcResultList = Lists.newArrayList();

			Sellprc sellprc = null;
			int sellprcBCount = 0;

			for(Iterator<String> iter = Splitter.on("@").trimResults().split(sellprcLst).iterator();iter.hasNext();){
				sellprc = new Sellprc();
				String[] sellprcs = iter.next().split("\\^");
				if(sellprcs.length>2){
					sellprc.setPrcCd(sellprcs[0]);
					sellprc.setSizeCount(Integer.parseInt(sellprcs[2]));

					if (sellprcs[0] != null && sellprcs[0].endsWith("A")) {
						sellprc.setPrc(Integer.parseInt(sellprcs[0].replace("A", "")) * 1000);
						sellprcAList.add(sellprc);
					} else if (sellprcs[0] != null && sellprcs[0].endsWith("B")) {
						sellprc.setPrc(Integer.parseInt(sellprcs[0].replace("B", "")) * 10000);
						sellprcBList.add(sellprc);
						sellprcBCount += sellprc.getSizeCount();
					}
				}
			}

			// 만원대가 하나이고 그 구간이 만원인경우 천원대 사용
			if (sellprcBList.size() == 1 && sellprcBList.get(0).getPrc() == 10000) {
				Collections.sort(sellprcAList, new Comparator<Sellprc>(){
					public int compare(Sellprc c1,Sellprc c2){
						try{
							int k1 = c1.getPrc();
							int k2 = c2.getPrc();
							if(k1<k2){
								return -1;
							} else if(k1>k2){
								return 1;
							} else{
								return 0;
							}
						}catch(NumberFormatException ne){
							return 0;
						}
					}
				});

				sellprcResultList = sellprcAList;

			} else {
				Collections.sort(sellprcBList, new Comparator<Sellprc>(){
					public int compare(Sellprc c1,Sellprc c2){
						try{
							int k1 = c1.getPrc();
							int k2 = c2.getPrc();
							if(k1<k2){
								return -1;
							} else if(k1>k2){
								return 1;
							} else{
								return 0;
							}
						}catch(NumberFormatException ne){
							return 0;
						}
					}
				});

				sellprcResultList = sellprcBList;
			}


			int avgCount = 0;
			int groupSumCout = 0;
			int minprc = 0;
			int maxprc = 0;

			if (sellprcResultList.size() > 5) {
				avgCount = sellprcBCount / 5;
			}

			for (int i = 0; i < sellprcResultList.size(); i++) {
				Sellprc sellprcResult = sellprcResultList.get(i);

				if (groupSumCout == 0) {
					minprc = maxprc;
				}

				groupSumCout += sellprcResult.getSizeCount();

				if (groupSumCout >= avgCount) {
					maxprc = sellprcResult.getPrc();

					Prc prc = new Prc();
					prc.setMinPrc(minprc);
					prc.setMaxPrc(maxprc);
					prcGroupList.add(prc);

					groupSumCout = 0;
				} else if (sellprcResultList.size() == i + 1) {	//마지막일경우
					maxprc = sellprcResult.getPrc();

					Prc prc = new Prc();
					prc.setMinPrc(minprc);
					prc.setMaxPrc(maxprc);
					prcGroupList.add(prc);
				}
			}

			return prcGroupList;
		} catch (Exception e) {
			return Lists.newArrayList();
		}
	}
	
	public static List<Banr> getAdvertisingBanrSetting (List<BanrAdvertising> advertisingList) {
		List<Banr> adBanrList = Lists.newArrayList();
		
		if (advertisingList != null && advertisingList.size() > 0) {
			for (BanrAdvertising banrAdvertising : advertisingList) {
				Banr banr = new Banr();
				
				banr.setAdvertAcctId(banrAdvertising.getAdvertAcctId());
				banr.setAdvertBidId(banrAdvertising.getAdvertBidId());
				banr.setAdvertBilngTypeCd(banrAdvertising.getAdvertBilngTypeCd());
				banr.setAdvertKindCd(banrAdvertising.getAdvertKindCd());
				banr.setLinkUrl(banrAdvertising.getLinkUrl());
				banr.setImgFileNm(banrAdvertising.getImgFileNm());
				banr.setBanrRplcTextNm(banrAdvertising.getBanrRplcTextNm());
				banr.setPopYn(banrAdvertising.getPopYn());
				banr.setAdvertExtensTeryDivCd(banrAdvertising.getAdvertExtensTeryDivCd());

				adBanrList.add(banr);
			}
		}
		
		return adBanrList;
	}
}
