/**
 * 描述：增加超级管理员权限的菜单显示
 * 
 * @author xiongyanan
 * @date 2014-8-26
 * 
 */

 $(function() {

 });


function addSuperAdminMenu() {
	$("#accountManage > ul").find("li:last").after('<li><a href="#" id="btnCommonAdminManage" class="menuBtnItem"><span class="language" data-word="adminManage">普通管理员管理</span></a></li>');
}
