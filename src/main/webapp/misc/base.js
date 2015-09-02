$(function() {
	// ajax默认选项
	$.ajaxSetup({
		global : false,
		type : "POST",
		cache : false,
		dataType : "json",
		dataFilter : function(data, type) {
			if (type == "json" && data == "") {
				data = "{}";
			}
			return data;
		},
		error : function(data, status) {
			bootbox.alert({
				message : data.responseJSON.message,
				// size : "small",
				title : "<i class='fa fa-info-circle text-yellow'></i> 提示"
			});
		}
	});
	// bootbox默认选项
	bootbox.setDefaults({
		"animate" : false,
		"locale" : "zh_CN"
	});
});