if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}

function RealGamePanel (argument) {
	var panel = new BasePanel();
	var gameBox = $('<div>').addClass('fb-rgame-box').appendTo(panel);

	panel.data('fb').load = function(){
		$.ajax({url:"/game/game/realGame/enable",
			success:function(response){

				var g_rgames = response.list;
				for (var i = 0; g_rgames && i < g_rgames.length; i++) {
					var rgame = g_rgames[i];
					$('<img>').data('fb',rgame).attr({"src":"../" + rgame.image}).appendTo(gameBox).click(function(){
						openGame($(this).data('fb').id);
					});
				}
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
GLOBAL.Panels["RealGamePanel"] = RealGamePanel;