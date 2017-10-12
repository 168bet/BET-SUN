/**
 * 描述：在代理管理页面动态加载删除代理按钮
 *
 * @author xiongyanan
 * @date 2014-8-23
 *
 */

$(function() {

});

function addDeleteHead(rowHead) {
	$("#" + rowHead).find("th:last").after("<th><span class='language' data-word='delete'>删除</span></th>");
}

function addDeleteBody(rowID, uuid, currentPage, numberPerPage, bodyID) {
	$("#" + bodyID).find("tr:last").append("<td><button type='button' class='btn btn-danger' onclick='deleteInfo(" + rowID + "," + currentPage + "," + numberPerPage + ",\"" + uuid + "\");'><span class='language' data-word='delete'>删除</span></button></td>");
}