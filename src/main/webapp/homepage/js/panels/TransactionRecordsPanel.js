if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}


function TransactionRecordsPanel(){
	var BillStates = ["审核中","已通过","未通过"];
	var BillStatesColor = ["#00F","#0A0","#F00"];
	var panel = new BasePanel();
	var table = $('<table>').addClass('fb-transation-table').appendTo(panel);
	var head = $('<thead>').addClass('fb-transation-head').appendTo(table);
	var trHread = $('<tr>').appendTo(head);
	$('<th colspan="1">').width(97).text('日期时间').appendTo(trHread);
	$('<th colspan="1">').text('存款/取款').appendTo(trHread);
	$('<th colspan="1">').text('额度').appendTo(trHread);
	$('<th colspan="1">').text('状态').appendTo(trHread);

	var body = $('<tbody>').addClass('fb-transation-body').appendTo(table);
	panel.data('fb').load = function(){
		$.ajax({
			type:"get",
			url:"/game/bill/" + GLOBAL.User.uuid + "/all",
			beforesend:function(){
				panel.find('.fb-panel-mask').show();
			},
			success:function(response){
				window.console.log('debug');
				var records = response.list;
				for (var i = 0; records && i < records.length; i++) {
					var r = records[i];
					r.createdTime = new Date(r.createdTime);
					r.state = r.state == null? 0 : r.state;
					var line = $('<tr>').appendTo(body);
					if(i%2 == 1){
						line.css({"background-color":"rgb(245, 245, 245)"});
					}
					$('<td>').append(r.createdTime.toLocaleDateString() + "<br/>" + r.createdTime.getWeekday()).appendTo(line);
					$('<td>').text(r.type == 0 ? "存款" : "取款").appendTo(line);
					$('<td>').text(r.point).appendTo(line);
					$('<td>').css({"color":BillStatesColor[r.state]}).text(BillStates[r.state]).appendTo(line);
				}
				panel.find('.fb-panel-mask').hide();
			}
		});
	};
	return panel;
}

GLOBAL.Panels["TransactionRecordsPanel"] = TransactionRecordsPanel;