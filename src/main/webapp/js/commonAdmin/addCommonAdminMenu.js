/**
 * 描述：增加普通管理员权限的菜单显示
 *
 * @author xiongyanan
 * @date 2014-8-26
 *
 */

$(function() {

});


function addCommonAdminMenu() {
	$("#accountManage > ul").find("li:last").after('<li><a href="#" id="btnAddAdmin" class="menuBtnItem"><span class="language" data-word="addAdmin">新增管理员</span></a></li>');
	$("#accountManage > ul").find("li:last").after('<li><a href="#" id="btnCSAdminManage" class="menuBtnItem"><span class="language" data-word="customerServiceAdminManage">客服管理员管理</span></a></li>');
}