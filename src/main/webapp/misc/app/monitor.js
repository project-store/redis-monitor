$(function() {
	Highcharts.setOptions({
		global : {
			useUTC : false
		}
	});
	var charts_tps = drawChartsTPS();
	var charts_clients = drawChartsClients();

	setInterval(function() {
		$.get("/monitor/" + node, function(data) {
			var now = (new Date()).getTime();
			charts_clients.series[0].addPoint([ now, parseInt(data.connected_clients) ], true, true);
			charts_tps.series[0].addPoint([ now, parseInt(data.instantaneous_ops_per_sec) ], true, true);
			$("#dbsize").text(data.dbSize);
			if (data.dbSize > 0) {
				eval("var " + data.db0);
				$("#dbsize_expired").text("(临时:" + expires + ")");
			}
			$("#used_memory_human").text(data.used_memory_human);
			$("#blocked_clients").text(data.blocked_clients);
			var hit_rate = data.keyspace_hits / (parseInt(data.keyspace_hits) + parseInt(data.keyspace_misses));
			$("#hit_rate").text((hit_rate * 100).toFixed(1));
		});
	}, 1500);
});

function drawChartsClients() {
	return new Highcharts.Chart({
		chart : {
			renderTo : 'charts_clients',
			borderColor : '#ccc',
			borderWidth : 1,
			type : 'spline',
			animation : Highcharts.svg,
			marginRight : 10,
			credits : false
		},
		credits : {
			enabled : false
		},
		title : {
			text : 'Clients'
		},
		xAxis : {
			type : 'datetime'
		},
		yAxis : {
			title : {
				text : '连接数'
			},
			min : 0,
			allowDecimals : false
		},
		tooltip : {
			formatter : function() {
				return "时间: " + Highcharts.dateFormat('%H:%M:%S', this.x) + "<br/>连接数: " + this.y;
			},
			crosshairs : true
		},
		legend : {
			enabled : false
		},
		exporting : {
			enabled : false
		},
		series : [ {
			data : (function() {
				var data = [], time = (new Date()).getTime(), i;
				for (i = -180; i <= 0; i++) {
					data.push({
						x : time + i * 1000,
						y : 0
					});
				}
				return data;
			})()
		} ]
	});
}
function drawChartsTPS() {
	return new Highcharts.Chart({
		chart : {
			renderTo : 'charts_tps',
			borderColor : '#ccc',
			borderWidth : 1,
			type : 'spline',
			animation : Highcharts.svg,
			marginRight : 10,
			credits : false
		},
		credits : {
			enabled : false
		},
		title : {
			text : 'TPS'
		},
		xAxis : {
			type : 'datetime'
		},
		yAxis : {
			title : {
				text : '执行命令数'
			},
			min : 0,
			allowDecimals : false
		},
		tooltip : {
			formatter : function() {
				return "时间: " + Highcharts.dateFormat('%H:%M:%S', this.x) + "<br/> TPS: " + this.y;
			},
			crosshairs : true
		},
		legend : {
			enabled : false
		},
		exporting : {
			enabled : false
		},
		series : [ {
			data : (function() {
				var data = [], time = (new Date()).getTime(), i;
				for (i = -180; i <= 0; i++) {
					data.push({
						x : time + i * 1000,
						y : 0
					});
				}
				return data;
			})()
		} ]
	});
}
