package ssg.search.collection.search;

import ssg.search.base.Collection;
import ssg.search.function.*;
import ssg.search.parameter.Parameter;

public class ItemTestCollection extends ItemCollection implements Collection, Prefixable, Sortable, Filterable, Pageable, Morphable, Rankable, Boostable{

	@Override
	public String getCollectionName(Parameter parameter){
		return "item_test";
	}

	@Override
	public String[] getDocumentField(Parameter parameter){
		return new String[]{
				// NORMAL META DATA
				"SITE_NO",
				"ITEM_ID",
				"ITEM_NM",
				"SELLPRC",
				"ITEM_REG_DIV_CD",
				"SHPP_TYPE_CD",
				"SHPP_TYPE_DTL_CD",
				"SALESTR_LST",
				"EXUS_ITEM_DIV_CD",
				"EXUS_ITEM_DTL_CD",
				"SHPP_MAIN_CD",
				"SHPP_MTHD_CD"
		};
	}
}
