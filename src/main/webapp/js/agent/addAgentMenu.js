/**
 * 描述：增加代理的取款菜单显示
 *
 * @author xiongyanan
 * @date 2014-8-26
 *
 */

$(function() {

});


function addAgentMenu() {
	$("#btnChangePassword").parent().after('<li><a href="#" id="btnDrawMoney" class="menuBtnItem"><span class="language" data-word="drawMoney">点数提取</span></a></li>');
}