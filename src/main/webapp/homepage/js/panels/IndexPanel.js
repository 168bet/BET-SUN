if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}

function IndexPanel(){
	var panel = new Panel();
	panel.height(663);
	
	var ads = $('<div>').addClass('fb-index-ad').appendTo(panel);
	//$('<img>').attr({"src":"./img/index/fb-ad.png"}).appendTo(ads);

	ads.append('<object type="application/x-shockwave-flash" data="./img/banner.swf" width="830" height="345"> \
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

	var bigGameBox = $('<div>').addClass('fb-index-bgame-box').appendTo(panel);
	var g_bgames = GLOBAL.BigGame;
	for(var i = 0; i < g_bgames.length; ++i){
		var bgame = g_bgames[i];
		var box = $('<div>').addClass('fb-index-bgame').appendTo(bigGameBox);
		box.data('fb',bgame);
		box.click(BigGameClick);
		$('<img>').attr({"src":bgame.img}).appendTo(box);
	}

	var newsBox = $('<div>').addClass('fb-news-box').appendTo(panel);
	var newsContent = $('<div>').addClass('fb-news-content').appendTo(newsBox);
	$('<marquee scrolldelay="5" scrollamount="2" onmouseover="this.stop()" onmouseout="this.start()">').appendTo(newsContent);
	$.ajax({
		type: "get",
		contentType: "application/json",
		url: "/game/publicMsg/news/latest",
		success: function(data) {
			if(data!=null && data.content != null){
				$('.fb-news-content').find('marquee').text(data.content);
			}else{
				$('.fb-news-content').find('marquee').text('暂时没有消息');
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrow) {
			$('.fb-news-content').find('marquee').text('暂时 没有 消息');
		}
	});
	newsContent.click(function(){
		GLOBAL.Panels.showPanel('PublicMsgPanel','news');
	});
	return panel;
}

function BigGameClick(e){
	GLOBAL.Panels.showPanel($(e.currentTarget).data('fb').panel);
}

GLOBAL.Panels["IndexPanel"] = IndexPanel;

GLOBAL.BigGame = [];

(function initGigGame() {
	var bigGame = [];
	bigGame.push({
		id: 0,
		name:"真人娱乐",
		panel:"RealGamePanel",
		img:"./img/index/fb-bgame-1.png",
		hover:"./img/index/fb-bgame-1-hover.png"
	});
	bigGame.push({
		id: 1,
		name:"彩票游戏",
		panel:"LotteryGamePanel",
		img:"./img/index/fb-bgame-2.png",
		hover:"./img/index/fb-bgame-2-hover.png"
	});
	bigGame.push({
		id: 2,
		name:"足　　球",
		panel:"video",
		img:"./img/index/fb-bgame-3.png",
		hover:"./img/index/fb-bgame-3-hover.png"
	});
	bigGame.push({
		id: 3,
		name:"电子游戏",
		panel:"VideoGamePanel",
		img:"./img/index/fb-bgame-4.png",
		hover:"./img/index/fb-bgame-4-hover.png"
	});
	GLOBAL.BigGame = bigGame;
})();
