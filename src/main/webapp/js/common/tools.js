/**
 * 描述：公共js，用于翻页，获取属性等
 * 
 * @author xiongyanan
 * @date 2014-8-24
 * 
 */

var url = "";
var allCountParam = "";

/**
* 获取url中传递过来的参数
*/
 function getUrlParam(name) {
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		return unescape(r[2]); 
	} else {
		return null;
	}
}

/**
* 显示翻页控件及实现翻页功能
*/
//listLength请求到的数据， linesPerPage每页显示多少行，currentPage当前页, allPages所有页
function showPages(listLength, linesPerPage, currentPage, allPages) {
	listLength = parseInt(listLength);
	linesPerPage = parseInt(linesPerPage);
	currentPage = parseInt(currentPage);
	allPages = parseInt(allPages);

	$("#DataTables_Table_0_paginate").parent().attr("id", "pageformat");
 	$("#DataTables_Table_0_paginate").remove();

 	var pageInfoHtml = '<div id="pageinfo" class="dataTables_paginate fg-buttonset ui-buttonset fg-buttonset-multi ui-buttonset-multi paging_full_numbers"></div>';
 	$("#pageformat").html(pageInfoHtml);
 	$("#pageinfo").append('<a id="first" class="first ui-corner-tl ui-corner-bl fg-button ui-button ui-state-default" onclick="jumpSelectedPage(' + linesPerPage + ',' + 1 +  ',' + allPages + ')">首页</a>')
 				.append('<a id="previous" class="previous fg-button ui-button ui-state-default" onclick="jumpSelectedPage(' + linesPerPage + ',' + (currentPage - 1) +  ',' + allPages + ')">上一页</a>')
 				.append('<a id="next" class="next fg-button ui-button ui-state-default" onclick="jumpSelectedPage(' + linesPerPage + ','  + (currentPage + 1) +  ',' + allPages + ')">下一页</a>')
 				.append('<a id="last" class="last ui-corner-tr ui-corner-br fg-button ui-button ui-state-default" onclick="jumpSelectedPage(' + linesPerPage + ',' + allPages +  ',' + allPages + ')">尾页</a>');

 	if (1 == allPages || 0 == allPages) {
 		$("#previous").after('<span><a class="fg-button ui-button ui-state-default ui-state-disabled">' + currentPage + '</a></span>');
	 	$("#first").addClass("ui-state-disabled");
	 	$("#previous").addClass("ui-state-disabled");
	 	$("#next").addClass("ui-state-disabled");
	 	$("#last").addClass("ui-state-disabled");
 	} else {
 		if (1 ==  currentPage) {
 			$("#first").addClass("ui-state-disabled");
 			$("#previous").addClass("ui-state-disabled");
	 		$("#next").removeClass("ui-state-disabled");
	 		$("#last").removeClass("ui-state-disabled");
 		} else if (allPages == currentPage) {
	 		$("#first").removeClass("ui-state-disabled");
	 		$("#previous").removeClass("ui-state-disabled");
	 		$("#next").addClass("ui-state-disabled");
	 		$("#last").addClass("ui-state-disabled");
 		} else {
 			$("#first").removeClass("ui-state-disabled");
	 		$("#previous").removeClass("ui-state-disabled");
	 		$("#next").removeClass("ui-state-disabled");
	 		$("#last").removeClass("ui-state-disabled");	
 		}

 		for (var index = allPages; index > 0; index--) {
 			$("#previous").after('<a class="fg-button ui-button ui-state-default"  onclick="jumpSelectedPage(' + linesPerPage + ',' + index + ',' + allPages + ')" id=page_' + index + '>' + index + '</a>')
 		}

 		$("#page_" + currentPage).wrap("<span></span>");
 		$("#page_" + currentPage).addClass("ui-state-disabled");
 	}
}