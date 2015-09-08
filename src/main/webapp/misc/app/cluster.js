$(function() {

	drawClusterMap();

});

function drawClusterMap() {
	var graph = new joint.dia.Graph;

	var paper = new joint.dia.Paper({
		el : $('#myholder'),
		width : "100%",
		model : graph,
		gridSize : 1
	});

	var root_template = new joint.shapes.basic.Rect({
		position : {
			x : 400,
			y : 30
		},
		size : {
			width : 100,
			height : 30
		},
		attrs : {
			rect : {
				fill : '#ddd',
				stroke : '#999',
				'stroke-width' : 1,
			},
			text : {
				text : 'root',
				'font-size' : 15,
				'font-weight' : 'normal',
				'font-variant' : 'small-caps',
				'text-transform' : 'capitalize'
			}
		}
	});

	var node_template = new joint.shapes.basic.Rect({
		position : {
			x : 400,
			y : 130
		},
		size : {
			width : 150,
			height : 30
		},
		attrs : {
			rect : {
				fill : '#2C3E50',
				rx : 5,
				ry : 5,
				'stroke-width' : 1,
				'stroke' : 'black'
			},
			text : {
				text : 'node',
				fill : '#ccc'
			}
		}
	});

	for ( var i in cluster_map) {
		var cluster = cluster_map[i];
		var root = root_template.clone();
		root.attr({
			text : {
				text : cluster.name
			}
		});
		graph.addCell(root);

		for ( var j in cluster.nodes) {
			var redis_node = cluster.nodes[j];
			var node = node_template.clone();
			node.attr({
				text : {
					text : redis_node.host + ":" + redis_node.port
				}
			});
			node.translate((j - 1) * 200);

			var link = new joint.dia.Link({
				source : {
					id : root.id
				},
				target : {
					id : node.id
				},
				attr : {
					'.connection' : {
						stroke : 'red',
						'stroke-width' : 2
					}
				}
			});
			graph.addCells([ node, link ]);
		}
	}

	// var rect2 = rect.clone();
	// rect2.translate(300);
	//
	// var link = new joint.dia.Link({
	// source : {
	// id : rect.id
	// },
	// target : {
	// id : rect2.id
	// }
	// });

	// graph.addCells([ rect, rect2, link ]);
}