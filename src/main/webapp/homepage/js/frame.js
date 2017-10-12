if(!GLOBAL){
	var GLOBAL = {};
}

if(!GLOBAL.ReqParam){
	GLOBAL.ReqParam = {};
}
(function getReqParam() {
	var reg = new RegExp("(?:\\?|&)(\\w+)\=(\\w+)", "ig");
	var result;
	var params = {};
	while ((result = reg.exec(window.location.search)) != null) {
		params[result[1]] = result[2];
	}
	GLOBAL.ReqParam = params;
})();

GLOBAL.Lang.init(GLOBAL.ReqParam.lang);

if(!GLOBAL.User){
	GLOBAL.User = {};	
}

function Header(){
	var header = $('<div>').addClass('fb-header');
	var content = $('<div>').addClass('fb-content').appendTo(header);
	var logo = $('<div>').addClass('fb-logo').appendTo(content);
	logo.append('<object id="FlashID" width="330" height="100">\
		<param name="movie" value="./img/logo.swf">\
		<param name="quality" value="high">\
		<param name="wmode" value="transparent">\
		<param name="swfversion" value="8.0.35.0">\
		<!-- 此 param 標籤會提示使用 Flash Player 6.0 r65 和更新版本的使用者下載最新版本的 Flash Player。如果您不想讓使用者看到這項提示，請將其刪除。 -->\
		<param name="expressinstall" value="swf/expressInstall.swf">\
		<!-- 下一個物件標籤僅供非 IE 瀏覽器使用。因此，請使用 IECC 將其自 IE 隱藏。 -->\
		<!--[if !IE]>-->\
		<object type="application/x-shockwave-flash" data="./img/logo.swf" width="330" height="100">\
		<!--<![endif]-->\
		<param name="quality" value="high">\
		<param name="wmode" value="transparent">\
		<param name="swfversion" value="8.0.35.0">\
		<param name="expressinstall" value="swf/expressInstall.swf">\
		<!-- 瀏覽器會為使用 Flash Player 6.0 和更早版本的使用者顯示下列替代內容。 -->\
		<div>\
		<h4>这个页面上的内容需要较新版本的 Adobe Flash Player。</h4>\
		<p><a href="www.adobe.com/go/getflashplayer"><img src="./img/get_flash_player.gif" alt="取得 Adobe Flash Player" width="112" height="33"></a></p>\
		</div>\
		<!--[if !IE]>-->\
		</object>\
		<!--<![endif]-->\
		</object>');
	$('<div>').addClass('fb-header-someone').appendTo(content);

	var login = new Login().appendTo(content);
	return header;
}

function Login(){

	function LoginFunctions(){
		var loginFunc = $('<div>').addClass('fb-login-func');
		$('<div>').addClass("clickable").text("未派彩").appendTo(loginFunc).click(function(){
			if(GLOBAL.User.uuid == undefined){
				new Warning("userNotLogin");
				return;
			}
			GLOBAL.Panels.showPanel('WeiPaiCaiPanel');
		});
		$('<div>').addClass("clickable").text("交易记录").appendTo(loginFunc).click(function(){
			if(GLOBAL.User.uuid == undefined){
				new Warning("userNotLogin");
				return;
			}
			GLOBAL.Panels.showPanel('TransactionRecordsPanel');
		});
		$('<div>').addClass("clickable").text("会员存款").appendTo(loginFunc).click(function(){
			if(GLOBAL.User.uuid == undefined){
				new Warning("userNotLogin");
				return;
			}
			GLOBAL.Panels.showPanel('ChargePanel');
		});
		$('<div>').addClass("clickable").text("会员取款").appendTo(loginFunc).click(function(){
			if(GLOBAL.User.uuid == undefined){
				new Warning("userNotLogin");
				return;
			}
			GLOBAL.Panels.showPanel('WithdrawPanel');
		});	
		$('<div>').addClass("clickable").text("修改资料").appendTo(loginFunc).click(function(){
			if(GLOBAL.User.uuid == undefined){
				new Warning("userNotLogin");
				return;
			}
			GLOBAL.Panels.showPanel('EditProfilePanel');
		});
		$('<div>').addClass("clickable").text("代理申请").appendTo(loginFunc).click(function(){
			if(GLOBAL.User.uuid == undefined){
				new Warning("userNotLogin");
				return;
			}
			GLOBAL.Panels.showPanel('ApplyForAgencyPanel');
		});
		return loginFunc;
	}

	var loginBox =  $('<div>').addClass('fb-login-box');
	loginBox.data('fb',{
		reload:function(user){
			var _this = this;
			if(typeof(user) == 'undefined'){
				user = GLOBAL.User;
			}else{
				GLOBAL.User = user;
			}

			if(typeof(user.username) == "undefined" || typeof(user.point) == "undefined" || typeof(user.username) == "undefined"){
				this.userLogout();
				return;
			}

			$.ajaxSetup({
		    headers: {
			    "uuid": GLOBAL.User.uuid,
			    "role": GLOBAL.User.role,
		    }});
		    
			var infoBox = $('<div>').addClass('fb-login-info-box');
			$('<span>').i18ntext('accountName').append("：").appendTo(infoBox);
			$('<span>').text(user.username).css("margin-right","5px").appendTo(infoBox);

			$('<span>').i18ntext('userBalance').append("：").appendTo(infoBox);
			$('<span>').text(user.point.toFixed(2)).css("color","#FF0").appendTo(infoBox);
			var refresh = $('<a>').i18ntext('refresh').appendTo(infoBox);
			refresh.text("[" + refresh.text() + "]");

			var newLine = $('<div>');
			$('<span>').i18ntext('internalMessage').append("：").appendTo(newLine);
			var newMsgs = $.ajax({url: "/game/internalMsg/" + user.uuid + "/new",async:false}).responseText;
			if(newMsgs == null || newMsgs == "null") newMsgs = 0;
			$('<span style="color:#9F6">').addClass('clickable').i18ntext('classifierLetters').prepend(newMsgs).appendTo(newLine).click(function(){
				GLOBAL.Panels.showPanel("MsgBoxPanel");
			});
			$('<span>').addClass('fb-login-info-inandout clickable').i18ntext('inAndOut').appendTo(newLine);
			$('<a>').addClass('fb-login-info-logout clickable').i18ntext('logout').appendTo(newLine).click(function(){
				_this.userLogout();
				if(GLOBAL.Panels.Current == "MsgBoxPanel"){
					GLOBAL.Panels.showPanel("IndexPanel");
				}
			});
			newLine.appendTo(infoBox);
			loginBox.empty();
			infoBox.appendTo(loginBox);
			new LoginFunctions().appendTo(loginBox);
		},
		userLogout:function(){
			loginBox.empty();
			$('<input id="loginAcc" type="text">').val("账号").appendTo(loginBox);
			$('<input id="loginPswd" type="password">').val("******").appendTo(loginBox);
			var loginBtn = $('<button>').addClass('login').appendTo(loginBox);
			loginBtn.click(function(){
				var account = $('#loginAcc').val();
				if(account == ""){
					new Warning('loginAccEmpty','green');
					return;
				}
				var pswd = $('#loginPswd').val();
				if( pswd == ""){
					new Warning('loginAccEmpty','green');
					return;
				}
				var data = {};
				data.username = account;
				data.password = pswd;
				$.ajax({
					type: 'post',
					url: "/game/member/login",
					contentType: "application/json",
					data: JSON.stringify(data),
					dataType: "json",
					success: function(response){
						$('.fb-login-box').data('fb').reload(response);
					}
				});
			});
			var registerBtn = $('<button>').addClass('register').appendTo(loginBox);
			registerBtn.click(function(){
				GLOBAL.Panels.showPanel('RegisterPanel');
			});

			new LoginFunctions().appendTo(loginBox);


			$.ajaxSetup({headers:null});
			GLOBAL.User = {};
		}
	});
	loginBox.data('fb').reload();
	return loginBox;
}

function Navigation(){
	var navigation = $('<div>').addClass('fb-navigation');
	var content = $('<div>').addClass('fb-content').appendTo(navigation);
	$('<div>').addClass('fb-navigation-clock').appendTo(content);
	var timer = $('<div>').addClass('fb-navigation-timer').appendTo(content);
	timer.text(new Date().toChineseTimeString());
	setInterval(function(){
		timer.text(new Date().toChineseTimeString());
	}, 1000);

	var nav = [{name:"index",panel:"IndexPanel"},
	{name:"sb",panel:"ErrorPanel"},
	{name:"rgame",panel:"RealGamePanel"},
	{name:"lgame",panel:"LotteryGamePanel"},
	{name:"vgame",panel:"VideoGamePanel"},
	{name:"phone",panel:"PhoneBetPanel"},
	{name:"discount",panel:"DiscountPanel"}];
	for(var i = 0; i < nav.length; ++ i){
		var item = $('<div>').addClass('fb-navigation-item');
		item.data('fb',nav[i]);
		item.css("background-image","url(./img/navigation/fb-navigation-" + nav[i].name+".png)");
		item.mouseover(function(){
			var name = $(this).data('fb').name
			$(this).css("background-image","url(./img/navigation/fb-navigation-" + name+"-h.png)");
		});
		item.mouseout(function(){
			var name = $(this).data('fb').name
			$(this).css("background-image","url(./img/navigation/fb-navigation-" + name+".png)");
		});
		item.click(function(){
			GLOBAL.Panels.showPanel($(this).data('fb').panel);
		});
		item.appendTo(content);
	}
	return navigation;
}

function Container(){
	var container = $('<div>').addClass("fb-container");
	var content = $('<div>').addClass("fb-content").appendTo(container);

	var lList = $('<div>').addClass('fb-container-left').appendTo(container);
	var lotteries = [{name:"重庆时时彩",panel:"LotteryPanel",image:"./img/container/cqLottery.png"},
	{name:"广州快乐彩",panel:"LotteryPanel",image:"./img/container/gdLottery.png"},
	{name:"北京赛车PK10",panel:"LotteryPanel",image:"./img/container/bjLottery.png"},
	{name:"上海时时彩",panel:"LotteryPanel",image:"./img/container/shLottery.png"},
	{name:"福彩3D",panel:"LotteryPanel",image:"./img/container/fcLottery.png"},
	{name:"体彩系列",panel:"LotteryPanel",image:"./img/container/tcLottery.png"}];
	for(var i = 0; i < lotteries.length; ++i){
		var lottery = lotteries[i];
		var item = $('<div>').css("background-image","url(" + lottery.image + ")");
		item.data('fb',lottery).click(function(){
			GLOBAL.Panels.showPanel($(this).data('fb').panel);
		});
		item.appendTo(lList); 
	}
	lList.appendTo(content);
	var rPanel = $('<div>').addClass('fb-container-right').appendTo(content);
	var noticeBox = $('<div>').addClass('fb-notice-box').appendTo(rPanel);
	var noticeContent = $('<div>').addClass('fb-notice-content').appendTo(noticeBox);
	$('<marquee scrolldelay="5" scrollamount="2" onmouseover="this.stop()" onmouseout="this.start()">').appendTo(noticeContent);
	$.ajax({
		type: "get",
		contentType: "application/json",
		url: "/game/publicMsg/notice/latest",
		success: function(data) {
			if(data!=null && data.content != null){
				$('.fb-notice-content').find('marquee').text(data.content);
			}else{
				$('.fb-notice-content').find('marquee').text('暂时没有公告');
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrow) {
			$('.fb-notice-content').find('marquee').text('暂时 没有 公告');
		}
	});
	noticeContent.click(function(){
		GLOBAL.Panels.showPanel('PublicMsgPanel','notice');
	});
	$('<div>').addClass('fb-panel-loc').appendTo(rPanel);

	$('<div>').css("clear","both").appendTo(container);
	return container;
}

function Footer(){
	var footer = $('<div>').addClass('fb-footer');
	var content = $('<div>').addClass('fb-footer-content').appendTo(footer);

	var links = $('<div>').addClass('fb-footer-link').appendTo(content);
	var list = [{dataWord:"aboutUs",panel:"AboutUsPanel"},
	{dataWord:"agentCooperation",panel:"AgentCooperationPanel"},
	{dataWord:"howtoDeposit",panel:"HowtoDepositPanel"},
	{dataWord:"howtoDrawing",panel:"HowtoDrawingPanel"},
	{dataWord:"FAQ",panel:"FAQPanel"},
	{dataWord:"rules",panel:"RulesPanel"}];
	links.append("|");
	for(var i=0;i<list.length;++i){
		var item=$('<a>').i18ntext(list[i].dataWord);
		item.data('fb',list[i]);
		item.click(function(){
			GLOBAL.Panels.showPanel($(this).data('fb').panel);
		});
		item.appendTo(links);
		links.append("|");
	}
	
	var info = $('<p>').addClass('fb-footer-info').appendTo(content);
	info.i18ntext('copyrightStatement');

	var ads = $('<div>').addClass('fb-footer-ads').appendTo(content);
	$('<img>').attr({"src":"./img/ads/fb-footerad1.png"}).appendTo(ads);
	$('<img>').attr({"src":"./img/ads/fb-footerad2.png"}).appendTo(ads);
	$('<img>').attr({"src":"./img/ads/fb-footerad3.png"}).appendTo(ads);

	return footer;
}

function LeftFloat(){
	var floating = $('<div>').addClass('fb-floating left');
	$('<div>').css({"width":"100%","height":"300px"}).appendTo(floating).click(function(){
		GLOBAL.Panels.showPanel('LotteryGamePanel');
	});
	$('<div>').css({"width":"100%","height":"38px"}).appendTo(floating).click(function(){
		floating.remove();
	});
	return floating;
}

function RightFloat(){
	var floating = $('<div>').addClass('fb-floating right');
	$('<div>').css({"width":"100%","height":"252px"}).appendTo(floating).click(function(){
		GLOBAL.Panels.showPanel('CustomerServicePanel');
	});
	$('<div style="width:100%;height:33px;background-image:url(./img/fb-float-right-kahu.png)">').appendTo(floating).click(function(){
		GLOBAL.Panels.showPanel('VideoGamePanel');
	});
	$('<div style="width:100%;height:22px;background-image:url(./img/fb-float-right-close.png)">').appendTo(floating).click(function(){
		floating.remove();
	});
	return floating;
}

$(function onload(){
	var body = $('body');
	new Header().appendTo(body);
	new Navigation().appendTo(body);
	new Container().appendTo(body);
	new Footer().appendTo(body);
	new LeftFloat().appendTo(body);
	new RightFloat().appendTo(body);

	GLOBAL.Panels.showPanel("IndexPanel");

	//#Debug配置
	// $.ajax({
	// 	type: 'post',
	// 	url: "/game/member/login",
	// 	contentType: "application/json",
	// 	data: JSON.stringify({username:"TestMember",password:"11111111"}),
	// 	dataType: "json",
	// 	success: function(response){
	// 		$('.fb-login-box').data('fb').reload(response);
	// 	}
	// });
});
