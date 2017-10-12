/**
 * 描述：增加客服管理员权限的菜单显示
 *
 * @author xiongyanan
 * @date 2014-8-26
 *
 */

$(function() {

});


function addCountInOutMenu() {
	$("#parentMenu").append('<li class="submenu" id="countInOut"></li>');
	$("#countInOut").append('<a href="#"><i class="icon icon-th-list"></i><span class="language" data-word="countInOut">点数存取</span><span class="label">2</span></a>');
	$("#countInOut").append('<ul><li><a href="#" id="btnCountIn" class="menuBtnItem"><span class="language" data-word="countIn">存入</span></a></li><li><a href="#" id="btnCountOut" class="menuBtnItem"><span class="language" data-word="countOut">提出</span></a></li></ul>');
}


function addLogManageMenu() {
	$("#parentMenu").append('<li class="submenu" id="logManage"></li>');
	$("#logManage").append('<a href="#"><i class="icon icon-th-list"></i><span class="language" data-word="logManage">日志管理</span><span class="label">1</span></a>');
	$("#logManage").append('<ul><li><a href="#" id="btnSearchLog" class="menuBtnItem"><span class="language" data-word="logSearch">日志查询</span></a></li></ul>');
}

function addConfigManagerMenu() {
	$("#parentMenu").append('<li class="submenu" id="configManager"></li>');
	$("#configManager").append('<a href="#"><i class="icon icon-th-list"></i><span class="language" data-word="configManage">配置管理</span><span class="label">3</span></a><ul><li><a href="#" id="btnRealGameConfig" class="menuBtnItem"><span class="language" data-word="realPersonGame">真人游戏</span></a></li></ul><ul><li><a href="#" id="btnVideoGameConfig" class="menuBtnItem"><span class="language" data-word="electronicGame">电子游戏</span></a></li></ul><ul><li><a href="#" id="btnLotteryGameConfig" class="menuBtnItem"><span class="language" data-word="lotteryGame">彩票游戏</span></a></li></ul>');
}