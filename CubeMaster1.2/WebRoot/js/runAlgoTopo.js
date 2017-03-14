$(document).ready(function(){
	/*获取数据*/
	$.ajax({
		type:"post",
		url:"../servlet/ExecutePageServlet",
		dataType:'json',
		async: true,
		success:function(res){
			var A31ResponseJson;
			A31ResponseJson = res;
			packRunAlgoSelections(A31ResponseJson);
			jsPlumbFunction();
		}
	});
	/*组装数据*/
	function packRunAlgoSelections(A31ResponseJson){
		var algos = A31ResponseJson["algorithm"],  //resp中的算法数组
			datasets = A31ResponseJson["dataset"],
			dataDiv=$('.leftContent').find('.data'),
			algoDiv=$('.leftContent').find('.algorithm'),
			count=0;//resp中的数据数组
		/*数据插入*/
		for( var i = 0, len = datasets.length ; i<len ; i++ ){
			var dataName= datasets[i]["dataset_name"];
			var dataId= datasets[i]["dataset_id"];
			var dataPlatform = datasets[i]["platform"];
			dataDiv.append('<div class="window hoverwindow" data-platform='+dataPlatform+' data-id='+dataId+'><div class="indow_top"><div class="contentp"><p>'+dataName+'</p></div><i class="iconfont closediv closeposition">&#xe600;</i></div><i class="iconfont"  id="icondata">&#xe602;</i></div>');
		}
		/*算法插入*/
		for(var i=0,len=algos.length;i<len;i++){
			console.log(len);
			var algoName= algos[i]["algorithm_name"];
			var algoId= algos[i]["algorithm_id"];
			var params=algos[i]['parameters'];
			var algoPaltform = algos[i]['platform'];

			console.log(params);
			if(params.length!==0){
				count++;
				algoDiv.append('<div class="algoWindow hoverlogo" data-platform='+algoPaltform+' data-id='+algoId+' flag="'+count+'"><div class="algo_top"><div class="contentp2"><p class="arithtext">'+algoName+'</p></div><i class="iconfont closediv2 closeposition">&#xe600;</i></div><i class="iconfont" id="icondata">&#xe601;</i></div>');
				var $parent= $('.parameterPanel .form-inline');
				$parent.append('<div flag="'+count+'" class="form-group parameters" id="algoInputDiv"></div>');
				$parent.find('.form-group').eq(($parent.find('.form-group').length)-1).append('<h4>'+algoName+'</h4>');
				for(var j=0,leng=params.length;j<leng;j++){
					$parent.find('.form-group').eq(($parent.find('.form-group').length)-1).append('<label>'+params[j].parameter_name+'('+params[j].parameter_type+')'+'</label><input type="text" data-type="'+params[j].parameter_type+'"  value="'+params[j].parameter_value+'" class="form-control" data-id="'+params[j].parameter_id+'"/>');
				}
			}else{
				algoDiv.append('<div class="algoWindow hoverlogo" flag="undefined" data-platform='+algoPaltform+' data-id='+algoId+' ><div class="algo_top"><div class="contentp2"><p class="arithtext">'+algoName+'</p></div><i class="iconfont closediv2 closeposition">&#xe600;</i></div><i class="iconfont" id="icondata">&#xe601;</i></div>');
			}
		}


	}

	function jsPlumbFunction(){
		jsPlumb.ready(function(){
			/*声明jsplumb实例*/
			var instance = jsPlumb.getInstance({
				DragOptions: { cursor: 'pointer', zIndex: 2000 },
				PaintStyle: { strokeStyle: '#666' },
				EndpointHoverStyle: { fillStyle: "orange" },
				HoverPaintStyle: { strokeStyle: "orange" },
				EndpointStyle: { width: 20, height: 16, strokeStyle: '#666' },
				Endpoint: "Rectangle",
				Anchors: ["TopCenter", "TopCenter"],
				Container:"rightcontent"
			});
			/*拖拽样式*/
			var exampleDropOptions = {
				tolerance: "touch",
				hoverClass: "dropHover",
				activeClass: "dragActive"
			};
			/*连线样式*/
			var connectorPaintStyle = {
				lineWidth: 2,
				strokeStyle: "#000",
				joinstyle:"round",
				outlineColor: "#ffffff",
				outlineWidth: 2
			};
			/*连线hover样式*/
			var connectorHoverStyle = {
				lineWidth: 2,
				strokeStyle: "red",
				outlineWidth: 2,
				outlineColor:"white"
			};
			/*点hover颜色*/
			var endpointHoverStyle = {
				fillStyle:"#000"
			};
			/*点的样式*/
			var exampleEndpoint = {
				endpoint:"Dot",
				paintStyle:{
					strokeStyle:"#000",
					fillStyle:"transparent",
					radius: 3,
					lineWidth:3
				},
				scope:"scopeexample",
				isSource:true,
				maxConnections:1,
				isTarget: true,
				connector:[ "Flowchart", { stub:[40, 60], gap:10, cornerRadius:5 } ],
				connectorStyle: connectorPaintStyle,
				hoverPaintStyle: endpointHoverStyle,
				connectorHoverStyle: connectorHoverStyle,
				dragOptions: {},
				connectorOverlays:[
					[ "Arrow", { location: 1, width: 12, length: 12 }],
					[ "Arrow", { location: 0.3, width: 12, length: 12 }]
				]
			};

			/*点坐标集，判断是否达到最大连线数*/
			var anchors = [
					[1, 0.2, 1, 0],
					[0.8, 1, 0, 1],
					[0, 0.8, -1, 0],
					[0.2, 0, 0, -1]
				],
				maxConnectionsCallback = function (info) {
					alert("Cannot drop connection " + info.connection.id + " : maxConnections has been reached on Endpoint " + info.endpoint.id);
				};

			/*可拖动和释放*/
			$('.leftContent .data').find('.window').draggable({
				helper:"clone",
				revert:"invalid"
			});
			$('.leftContent .algorithm').find('.algoWindow').draggable({
				helper:"clone",
				revert:"invalid"
			});

			var idnum = 1;
			/*drop事件*/
			$('.rightContent').droppable({
				accept:'.window,.algoWindow',
				drop:function(event,ui) {
					if (!exist('"' + "id" + idnum + '"')) {
						var id = "id" + idnum;
						var left = parseInt(ui.offset.left) - $(".rightContent").offset().left - 6;

						var top = parseInt(ui.offset.top) - $(".rightContent").offset().top - 6;

						var patten1 = /window/;
						var patten2 = /algoWindow/;
						var classname = ui.draggable.attr('class');
						var par1 = patten1.test(classname);
						var par2 = patten2.test(classname);
						/*拖入数据*/
						if (par1) {
							ui.draggable.attr('hasDragged', 'true');
							var dataId = ui.draggable.attr('data-id');
							$(this).append('<div class="window position" data-platform=' + $(ui.helper).attr("data-platform") + ' data-id=' + $(ui.helper).attr("data-id") + ' id="'+id+'">' + $(ui.helper).html() + '</div>');
							$("#"+id).css('left', left).css('top', top);
							instance.addEndpoint($('.rightContent .window[data-id=' + $(ui.helper).attr("data-id") + ']'), {
								anchor: "RightMiddle",
								isTarget: false
							}, exampleEndpoint);
							instance.draggable($('.rightContent .window[data-id=' + $(ui.helper).attr("data-id") + ']'), {
								containment: "parent"
							});
							var flag = ui.draggable.attr('data-id');
							var $parameter = $('.rightContent').find('.window[data-id=' + flag + ']');
							var $thisclose = $parameter.find('.closediv');
							$thisclose.css('display', 'block');


							/*拖入算法*/
						} else if (par2) {
							ui.draggable.attr('hasDragged', 'true');
							var dataId = ui.draggable.attr('data-id');
							console.log(dataId);
							$(this).append('<div class="algoWindow position algostyle" data-platform=' + $(ui.helper).attr("data-platform") + ' flag=' + $(ui.helper).attr("flag") + ' data-id=' + $(ui.helper).attr("data-id") + ' id="'+id+'">' + $(ui.helper).html() + '</div>');
							$("#"+id).css('left', left).css('top', top);

							instance.addEndpoint($('.rightContent .algoWindow[data-id=' + $(ui.helper).attr("data-id") + ']'), {
								anchor: "LeftMiddle",
								isSource: false
							}, exampleEndpoint);
							instance.addEndpoint($('.rightContent .algoWindow[data-id=' + $(ui.helper).attr("data-id") + ']'), {
								anchor: "RightMiddle",
								isTarget: false
							}, exampleEndpoint);

							instance.draggable($('.rightContent .algoWindow[data-id=' + $(ui.helper).attr("data-id") + ']'), {
								containment: "parent"
							});
							var flag = ui.draggable.attr('flag');
							var $parameter = $('.parameterPanel .form-inline').find('.form-group[flag=' + flag + ']');
							var $algoDiv = $('.rightContent').find('.algoWindow[flag=' + flag + ']');
							var $thisclose = $algoDiv.find('.closediv2');
							$parameter.css('display', 'block');
							$thisclose.css('display', 'block');

						}
					idnum++;
					}
				}
			});
			/*result连点框*/
			instance.addEndpoint($("#resultcontent"),{anchor:"LeftMiddle",isSource:false,paintStyle:{strokeStyle:"#000",fillStyle:"transparent",radius:4,lineWidth:4}},exampleEndpoint);


			/*监听*/
			instance.bind("click", function (conn) {
				instance.detach(conn);
			});
			instance.bind("beforeDetach", function (conn) {
				return confirm("删除链接?");
			});
			var connectionArrys = [];
			var dataIdArry = [];
			var algoIdArry = [];
			var dataPlatform = [];
			var algoPlatform = [];

			var inputParams=[];
			var strParams=undefined;
			/*储存连线关系*/
			function updateConnections(conn,remove){
				if(!remove) connectionArrys.push(conn);
				else{
					var idx=-1;
					for(var i=0;i<connectionArrys.length;i++){
						if(connectionArrys[i]==conn){
							idx=i;
							break;
						}
					}
					if(idx!=-1) connectionArrys.splice(idx,1);
				}
				return;
			};
			/*连线事件*/
			instance.bind("connection", function(info) {
				if(info.connection.target == info.connection.source){
					alert('算法与算法不能相连');
					instance.detach(info.connection);
					return;
				}
				if(info.targetId == 'resultcontent' && $(info.connection.source).attr('class').indexOf('window')>=0){
					alert('数据不能直接连到res');
					instance.detach(info.connection);
					return;
				}
				updateConnections(info.connection);
				/*数据、算法data-id存入数组*/
				if(info.targetId =="resultcontent"){
					return;
				}else {
					dataIdArry.push($(info.source).attr('data-id'));
					algoIdArry.push($(info.target).attr('data-id'));
					dataPlatform.push($(info.source).attr('data-platform'));
					algoPlatform.push($(info.target).attr('data-platform'));

					console.log('data:' + dataIdArry);
					console.log('algo:' + algoIdArry);
				}
			});
			/*连线删除事件*/
			instance.bind("connectionDetached",function(info){
				updateConnections(info.connection,true);
				if(info.targetId =="resultcontent" || info.connection.target == info.connection.source){
					return;
				}else{
					/*数据、算法data-id data-platform从数组删除*/
					var existDataId = $(info.source).attr('data-id');
					var existAlgoId = $(info.target).attr('data-id');
					var existDataPlatform = $(info.source).attr('data-platform');
					var existAlgoPlatform = $(info.target).attr('data-platform');

					for(var i=0,len=dataIdArry.length;i<len;i++){
						if(dataIdArry[i]===existDataId){
							dataIdArry.splice(i,1);
						}
					}
					for(var j=0,len=algoIdArry.length;j<len;j++){
						if(algoIdArry[j]===existAlgoId){
							algoIdArry.splice(j,1);
						}
					}
					for(var i=0,len=existDataPlatform.length;i<len;i++){
						if(dataPlatform[i]===existDataPlatform){
							dataPlatform.splice(i,1);
						}
					}
					for(var j=0,len=existAlgoPlatform.length;j<len;j++){
						if(algoPlatform[j]===existAlgoPlatform){
							algoPlatform.splice(j,1);
						}
					}
				}

			});
			/*运行按钮监听*/
			$('#run-btn').unbind('click').on('click',function(){
				/*判断连线是否连接*/
				var nowAlgo=$('.rightContent .algoWindow').attr('flag');
				var len = connectionArrys.length;
				var countfunc=0;
				var countArry=[];
				for(var i = 0; i < len; i++){
					console.log(connectionArrys[i].target);
					console.log(connectionArrys[i].targetId);
					if(connectionArrys[i].targetId == "resultcontent"){
						for(var j = 0;j<len; j++){
							console.log(connectionArrys[j].target);
							if(connectionArrys[j].target) {
								if ($(connectionArrys[j].target).attr("class").indexOf("algoWindow") >= 0) {
									console.log("可以传送");
									/*组装数据，发送到后台*/
									postData(nowAlgo,countArry,dataIdArry,algoIdArry,dataPlatform,algoPlatform);
									return;
								}
							}
						}
						alert("请将数据与算法相连");
						return;
					}
				}
				alert("请将算法连到res点上");

			});

			/*监听*/
			$('.rightContent').on('click','.algoWindow',function(){
				var flag=$(this).attr('flag');
				var $allparameter=$('.parameterPanel .form-inline').find('.form-group');
				var $parameter= $('.parameterPanel .form-inline').find('.form-group[flag='+flag+']');
				$allparameter.css('display','none');
				$parameter.css('display','block');
			});
			/*监听关闭数据按钮 事件委托删除对应div*/
			$(".rightContent").on("click",'.closediv',function(){
				var oParent = this;
				while(oParent.className.indexOf('window') < 0){
					oParent = oParent.parentNode;
				}
				instance.remove($(oParent));
				var id = $(oParent).attr("data-id");
				var section = $(".data .window[data-id="+id+"]");
				section.attr('hasDragged','false');

			});
			/*监听关闭算法按钮 事件委托删除对应div并重设重复拖拽flag*/
			$(".rightContent").on("click",'.closediv2',function(event){
				var oParent = this;
				while(oParent.className.indexOf('algoWindow') < 0){
					oParent = oParent.parentNode;
				}
				var flag = $(oParent).attr("flag");
				var section = $(".algorithm .algoWindow[flag="+flag+"]");
				section.attr('hasDragged','false');

				var $parameter= $('.parameterPanel .form-inline').find('.form-group[flag='+flag+']');
				console.log($parameter);
				$parameter.css('display','none');
				instance.remove($(oParent));
				event.stopPropagation();

			});


		});
		//
		///*动画监听*/
		Listener.algoTitleListener();
		Listener.closeDivListener();
		Listener.closeDivListener2();
		Listener.dataTitleListener();


	}
	function postData(nowAlgo,countArry,dataIdArry,algoIdArry,dataPlatform,algoplatform){
		if(dataPlatform[dataPlatform.length-1] != algoplatform[algoplatform.length-1]){
			alert("算法数据平台不匹配");
			return;
		}
		if(nowAlgo==='undefined'){//针对于没有参数的算法组装数据
			var A32 = {
				"protocol_id" : "A-3-2",
				"platform" : '"'+dataPlatform[dataPlatform.length-1]+'"',
				"data" : '[{"id" : '+dataIdArry[dataIdArry.length-1]+',"type" : "real"}]',
				"model" : '[{"number" : "0","id" : '+algoIdArry[algoIdArry.length-1]+',"parameters" :[],"input" : '+dataIdArry[dataIdArry.length-1]+',"output" : "o1"}]'
			};
			console.log(A32);
			$.ajax({
				type:'post',
				url:'../servlet/RunServlet',
				data:A32,
				dataType:'json',
				success:function(data){
					if(data["if_success"]===1){
						alert('运算成功!');
					}else{
						alert('运算失败!\n'+ data['reason']);
					}
				}
			});
		}else{//针对于有参数的算法组装参数
			inputParams=[];
			$('.parameterPanel .parameters').each(function(){
				if($(this).css('display')=='block'){
					countArry.push($(this));
					var $input= $(this).find('input');
					for (var i = 0,len=$input.length; i < len; i++) {
						console.log('push:' + (i + 1));
						var inputValue = $($input[i]).val();
						var inputId = $($input[i]).attr('data-id');
						var inputType=$($input[i]).attr('data-type');
						inputParams.push({
							"id": Number(inputId),
							"value": inputValue,
							"type":inputType
						});
					}
					strParams = JSON.stringify(inputParams);
					var A32 = {
						"protocol_id" : "A-3-2",
						"platform" : '"'+dataPlatform[dataPlatform.length-1]+'"',
						"data" : '[{"id" : '+dataIdArry[dataIdArry.length-1]+',"type" : "real"}]',
						"model" : '[{"number" : "0","id" : '+algoIdArry[algoIdArry.length-1]+',"parameters" :' + strParams + ',"input" : '+dataIdArry[dataIdArry.length-1]+',"output" : "o1"}]'
					};
					console.log(A32);
					$.ajax({
						type:'post',
						url:'../servlet/RunServlet',
						data:A32,
						dataType:'json',
						success:function(data){
							if(data["if_success"]===1){
								alert('运算成功!');
							}else{
								alert('运算失败!\n'+ data['reason']);
							}
						}
					});
				}
			});
		}
		if(countArry.length===0&&nowAlgo!=='undefined'){
			alert('请检查算法参数');
		}
	}



	function cloneObject(src) {
		/*对非引用类型*/
		if (src == null || typeof src != 'object') {
			return src;
		}
		/*对于日期类型*/
		if (src instanceof Date) {
			var clone = new Date(src.getDate());
			return clone;
		}
		/*对于Array*/
		if (src instanceof Array) {
			var clone = [];
			for (var i = 0, len = src.length; i < len; i++) {
				clone[i] = src[i];
			}
			return clone;
		}
		/*对于Object  ！！！递归*/
		if (src instanceof Object) {
			var clone = {};
			for (var key in src) {
				clone[key] = cloneObject(src[key]);
			}
			return clone;
		}
	}

});
function exist(id){  //判断是否已存在id
	var s = document.getElementById(id);
	if(s){
		return true;
	}
	else{
		return false;
	}
};


var Listener = {
	/*数据文字过长显示事件*/
	dataTitleListener : function(){
		$('.rightContent').on("mouseover",'.contentp',function(){
			var now = this;
			var oP = $(this).find('p');
			var width = oP.width();
			if(width > 90) {
				var movewidth = -(width - 90) + "px";
				$(this).find("p").animate({left: movewidth}, 3000);
			};
		});
		$('.rightContent').on("mouseout",'.contentp',function(){
			var now = this;
			var oP =$(this).find("p");
			oP.stop();
			oP.css({"left":"0"});
		});
	},
	/*算法文字显示过长事件*/
	algoTitleListener : function(){
		$('.rightContent').on("mouseover",".contentp2",function(){
			var now = this;
			var oP = $(this).find('p');
			var width = oP.width();
			if(width > 80) {
				var movewidth = -(width - 80) + "px";
				$(this).find("p").animate({left: movewidth}, 3000);
			};
		});
		$('.rightContent').on("mouseout",".contentp2",function(){
			var now = this;
			var oP =$(this).find("p");
			oP.stop();
			oP.css({"left":"0"});
		});
	},
	/*数据关闭按钮动画*/
	closeDivListener : function(){
		$(".rightContent").on("mouseover",'.closediv',function(){
			$(this).removeClass("animate2");
			$(this).addClass("animate1");
		});
		$(".rightContent").on("mouseout",'.closediv',function(){
			$(this).removeClass("animate1");
			$(this).addClass("animate2");
		});
	},
	/*算法关闭按钮动画*/
	closeDivListener2 : function(){
		$(".rightContent").on("mouseover",'.closediv2',function(){
			$(this).removeClass("animate2");
			$(this).addClass("animate1");
		});
		$(".rightContent").on("mouseout",'.closediv2',function(){
			$(this).removeClass("animate1");
			$(this).addClass("animate2");
		});
	}
};

