package ssg.search.constant;

import com.google.common.collect.Lists;
import ssg.search.parameter.Parameter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public enum CollectionOrder {
	MOBILE_OMNI_All {
		public Map<String, String> getCollectionOrder(Parameter parameter) {
			Map<String, String> collectionMap = new LinkedHashMap <String, String>();
			collectionMap.put("myTasteList", "0");
			collectionMap.put("tasteList", "6");
			collectionMap.put("pnshopList", "15");
			collectionMap.put("recipeList", "20");
			collectionMap.put("lifeMagazineList", "30");
			collectionMap.put("starfieldList", "50");

			return collectionMap;
		}
/*
		public List<String> getCollectionOrder2(Parameter parameter) {
			Map<String, String> collectionMap =  this.getCollectionOrder(parameter);
			List<String> collectionList = Lists.newLinkedList();
			
			for (Map.Entry<String, String> entry: collectionMap.entrySet()) {
				collectionList.add(entry.getKey());
			}
			return collectionList;
		}
*/		
	}
	;
	
	public abstract Map<String, String> getCollectionOrder(Parameter parameter);
	
	public List<String> getCollectionOrderList(Parameter parameter) {
		Map<String, String> collectionMap =  this.getCollectionOrder(parameter);
		List<String> collectionList = Lists.newLinkedList();
		
		for (Map.Entry<String, String> entry: collectionMap.entrySet()) {
			collectionList.add(entry.getKey());
		}
		
		return collectionList;
	}
	
	
}