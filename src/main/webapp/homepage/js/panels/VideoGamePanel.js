if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}

function VideoGamePanel (argument) {
	var panel = new BasePanel();
	var gameBox = $('<div>').addClass('fb-vgame-box').appendTo(panel);
	panel.data('fb').load = function(){
		$.ajax({url:"/game/game/vedioGame/enable",
			success:function(response){
				var g_vgames = response.list;
				for (var i = 0; g_vgames && i < g_vgames.length; i++) {
					var vgame = g_vgames[i];
					$('<img>').data('fb',vgame).attr({"src":"../" + vgame.image}).appendTo(gameBox).click(function(){
						openGame($(this).data('fb').id);
					});
				};
				panel.find('.fb-panel-mask').remove();
			}
		});
	};
	return panel;
}

function openGame(id){
	$.ajax({
		headers: {
			"uuid": GLOBAL.User.uuid,
			"role": GLOBAL.User.role,
		},
		type: "get",
		contentType: "application/json",
		url: "/game/game/" + id + "/open",
		success: function(data) {
			window.open(data.link,'game');
		},
		error:function(){
			alert("游戏打开失败，请重新登陆再使用。");
		}
	});
}

GLOBAL.Panels["VideoGamePanel"] = VideoGamePanel;