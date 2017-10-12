if (!GLOBAL) {
	var GLOBAL = {};
}
if (!GLOBAL.Panels) {
	GLOBAL.Panels = {};
}

function PhoneBetPanel(argument) {
	var panel = new BasePanel();
	var phoneBox = $('<div>').addClass('fb-pgame-box').appendTo(panel);

	panel.data('fb').load = function() {
		var innerBox = $('<div>').append('<object type="application/x-shockwave-flash" data="./img/moblieing_zh-cn.swf" width="687" height="444"> \
			<!--<![endif]-->\
			<param name="quality" value="high">\
			<param name="wmode" value="transparent">\
			<param name="swfversion" value="8.0.35.0">\
			<param name="expressinstall" value="swf/expressInstall.swf">\
			<!-- 瀏覽器會為使用 Flash Player 6.0 和更早版本的使用者顯示下列替代內容。 -->\
			<div> <h4>这个页面上的内容需要较新版本的 Adobe Flash Player。</h4> \
					<p>\
						<a href="http://www.adobe.com/go/getflashplayer">\
							<img src="img/get_flash_player.gif" alt="取得 Adobe Flash Player" width="112" height="33">\
						</a>\
					</p> \
				</div> <!--[if !IE]>--> \
			</object>');
		innerBox.appendTo(phoneBox);
		$('<span>').addClass('fb-span-left').html('建议手机下载安装兼容性较高的<a href="http://www.uc.cn/" target="_bank">UC浏览器</a>进行投注。').appendTo(innerBox);
		phoneBox.append(phoneBox);
		panel.find('.fb-panel-mask').remove();
	};
	return panel;
}

GLOBAL.Panels["PhoneBetPanel"] = PhoneBetPanel;