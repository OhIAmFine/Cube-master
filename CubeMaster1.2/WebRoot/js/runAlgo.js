/*保存两个选项的用户选择*/
var selectedMsg = {
    "algorithm" : undefined,
    "dataSet" : undefined
}

$(function(){

    /*通过id获取算法跟数据的详细信息，协议A-3-1*/
    /*var A31RequestJson = getA31RequestJson(),*/
    var A31ResponseJson = {},
    $runAlgoSelections = $('.run-algo-selections');

    $.ajax({
        type:"post",
        url:"../servlet/ExecutePageServlet",
        /* data:A31RequestJson,*/
        dataType:'JSON',
        success:function(res){
            /*alert(res.algorithm.parameters[0].parameter_id);*/
            A31ResponseJson = res;
            packRunAlgoSelections(A31ResponseJson);

        }
    });
    
    var mes=$('.run-algo-message');
	/*queryMessage();*/

    /*事件委托，两个选项里面每个li对应的btn*/
    $runAlgoSelections.on('click','button',function(){



        /*如果是已经选中了的*/
        if( $(this).hasClass('active') ) return;

        var oParent = this,
        $this = $(this),
        optionName = '';

        /*拿到这个btn所属的选项(算法/数据)*/
        while( oParent.tagName.toUpperCase() !== 'DIV' ){
            oParent = oParent.parentNode;   
        }

        /*选项对应的单词*/
        optionName = oParent.id.substring(7);
        if(this.innerHTML=="选择"){
            /*如果之前已经在这个选项选过一个了*/
            if( selectedMsg[optionName] !== undefined ){
                var $oldSelect = $('#' + oParent.id + ' button.active'),
                $oParamUl = $oldSelect.parent().siblings('ul');
                /*清除选中效果*/
                $oldSelect.removeClass('active');
                $oldSelect.html('选择');
                /*如果之前选项有参数要收起*/
                if( $oParamUl.length ){
                    $oParamUl.eq(0).slideUp('fast');
                }
            }
            /*应用选中的样式*/
            this.innerHTML = '已选择';
            $this.addClass('active');
            $('.' + optionName + '-selected').css('visibility','visible');
            /*如果有参数的话展示参数列表*/
            if( $this.parent().siblings('ul').length ){
                $this.parent().siblings('ul').eq(0).slideDown('fast');
            }    
            /*判断用户是否选完了*/
            selectedMsg[optionName] = parseInt(this.dataset.id);
            judSelectedAll();

        }

        if(this.innerHTML=="删除"){
            var dataId=this.getAttribute("data-id"),
            currentTarget=this;
            reqParam={
                protocol:"A-5-3-request"
            };
            if(optionName=="algorithm"){
                reqParam.algorithm_id=dataId;

                delBtn(currentTarget,reqParam);
            }else if(optionName=="dataSet"){
                reqParam.dataset_id=dataId;

                delBtn(currentTarget,reqParam);
            }

            /*如果删除已选中的（数据|算法）*/
            if($(this).prev('button').hasClass('active')){
                $('.' + optionName + '-selected').css('visibility','hidden');
            }

        }
    })

/*运行按钮的监听*/
$('#run-btn').on('click',function(){
    if( $(this).hasClass('disabled') ) return;

    /*判断参数输入*/
    var $algoParams = $('#select-algorithm button.active').parent().siblings('ul');
    if( $algoParams.length ){
        $algoParams = $algoParams.eq(0);
        /*每一个参数对应的li*/
        var $aLi = $algoParams.children('li'),
        $oLi = undefined,
        $oInput = undefined,
        type = '',
        judType = true;

        for(var i = 0, len = $aLi.length ; i<len-1 ; i++){
            $oLi = $aLi.eq(i+1);
            $oInput = $oLi.find('input').eq(0);
            type = $oInput.data('type').toUpperCase();
            /*验证输入类型*/
            if( type !== "STRING" && !Number($oInput.val()) ){
                $oLi.addClass('has-error');
                judType = false;
            }else{
                $oLi.addClass('has-success'); 
            }
        }

        if(!judType) return;
    }

    /*ajax把组合发送到后台*/
    ajaxSendA32();

	/*queryMessageBtn();*/
})


/*判断用户是不是满足运行的要求了*/
function judSelectedAll(){
    for( var msg in selectedMsg ){
        if( selectedMsg[msg] === undefined ) return;
    }
    /*满足的话按钮就可以点了*/
    $('#run-btn').removeClass('disabled');
}

function ajaxSendA32(){

    var dataId = $('#select-dataSet button.active').data('id'),
    $algo = $('#select-algorithm button.active'),
    algoId = $algo.data('id'),
    algoPlatform = $algo.data('platform'),
    $algoParams = $algo.parent().siblings('ul'),
    $aLi = $algoParams.children('li'),
    $oLi = undefined,
    $oInput = undefined,
    params = [],
    A32 = {};
    for(var i = 0, len = $aLi.length ; i<len-1 ; i++){
        $oLi = $aLi.eq(i+1);
        $oInput = $oLi.find('input').eq(0);
        params.push({
            "id" : $oInput.data('id'),
            "value" : $oInput.val(),
            "type" : $oInput.data('type')
        })
    }
    
    var strParams=JSON.stringify(params);
    
    A32 = {
        "protocol_id" : "A-3-2",
        "platform" : algoPlatform,
        "data" : '[{"id" : '+dataId+',"type" : "real"}]',
        "model" : '[{"number" : "0","id" : '+algoId+',"parameters" :' + strParams + ',"input" : '+dataId+',"output" : "o1"}]'
    }


    console.log(JSON.stringify(A32));
    
    ////
    A32T = {
        protocol_id : "A-3-2",
        platform : "java",
        data : [{id : dataId,type : "real"}],
        model : [{number : "0",id : algoId,parameters : params,input :dataId,output: "o1"}]
    }
    
    console.log(JSON.stringify(A32T));


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

/*删除算法源与数据源*/
function delBtn(targ,reqParam){
    $.ajax({
        type:"post",
        url:"../servlet/RemoveSessionServlet",
        /* data:A31RequestJson,*/
        dataType:'JSON',
        data:reqParam,
        success:function(res){
            if(res.dalete_status=="true"){
                targ.parentNode.parentNode.style.display="none";
                alert("删除成功");
            }
        }
    });
}

/*跟踪运算进度*/
/*function queryMessage(){	
		$.ajax({                                               
	    	type:"get",
	    	url:"../servlet/RunServlet?operate=getCallBack",
	    	async:true,
	    	dataType:'json',
	    	success:function(data){
	    		if(data.endFlag===1){	
	    			return false;
	    		}else if(data.endFlag===0){
	    			mes.html(data.callback);	
	    	  		var querys=setTimeout('queryMessage()',400);
	    	  		if(data.endFlag===1){
	    	  			clearTimeout(querys);
	    	  		}
	    		}
	    	}
    	});
    	
	}
function queryMessageBtn(){
	$.ajax({
		type:"get",
		url:"../servlet/RunServlet?operate=getCallBack",
		async:true,
		dataType:'json',
		success:function(data){
			if(data.endFlag===0){
				mes.html(data.callback);	
	    	  	var querys= setTimeout('queryMessageBtn()',400);
	    	  	if(data.endFlag===1){
	    	  		clearTimeout(querys);
	    	  	}
			}else if(data.endFlag===1){
				return false;
			}
		}
	});
}*/
/**
 * 在本地存储获得用户已选择的算法和数据的id数组
 * @return {JSONObject} 协议A-3-1 request标准的json对象
 */

 /*function getA31RequestJson(){*/
    /*获取存在localStorage的用户选择*/
   /* var selectedAlgos = localStorage.getItem('selectedAlgos')? localStorage.getItem('selectedAlgos').split(',') : [],
   selectedDatasets = localStorage.getItem('selectedDatasets')? localStorage.getItem('selectedDatasets').split(',') : [],*/
        /*postAlgos = [],
        postDatasets = [];

        for( var i = 0, len = selectedAlgos.length ; i < len ; i++ ){
            postAlgos.push( {"algorithm_id" : selectedAlgos[i]} );
        }

        for( var i = 0, len = selectedDatasets.length ; i < len ; i++ ){
            postDatasets.push( {"dataset_id" : selectedDatasets[i]} );
        }*/

        /*构造成字符串*/
        /*postAlgos = '[',
        postDatasets = '[';
        for( var i = 0, len = selectedAlgos.length ; i < len ; i++ ){
            postAlgos += '{"algorithm_id" : '+ selectedAlgos[i] + '}' ;
            if( i !== (len-1) ){
                postAlgos += ',';
            }
        }
        postAlgos += ']';

        for( var i = 0, len = selectedDatasets.length ; i < len ; i++ ){
            postDatasets += '{"dataset_id" : ' + selectedDatasets[i] + '}' ;
            if( i !== (len-1) ){
                postDatasets += ',';
            }
        }
        postDatasets += ']';

        A31RequestJson = {
            "protocol" : "A-3-1request",
            "algorithm" : postAlgos,
            "dataset" : postDatasets
            /*"algorithm" : [{"algorithm_id":25}],
            "dataset" : [{"dataset_id":25}]*/
        /*}

    return A31RequestJson;
}*/
/*通过A-3-1得到的用户选择信息组装dom元素*/
function packRunAlgoSelections(A31ResponseJson){
    var algos = A31ResponseJson["algorithm"],  //resp中的算法数组
        datasets = A31ResponseJson["dataset"],  //resp中的数据数组
        oAlgosUl = document.getElementById('select-algorithm').getElementsByTagName('ul')[0],
        oDatasUl = document.getElementById('select-dataSet').getElementsByTagName('ul')[0],
        algoTpl = '', 
        dataTpl = '';
        /*组装每一个算法*/
        for( var i = 0, len = algos.length ; i<len ; i++ ){
            algoTpl += algoSelectionsTpl(algos[i]);
        }

        /*组装每一个数据*/
        for( var i = 0, len = datasets.length ; i<len ; i++ ){
            dataTpl += dataSelectionsTpl(datasets[i]);
        }

        oAlgosUl.innerHTML = algoTpl;
        oDatasUl.innerHTML = dataTpl;

    }

    /*组装每一个数据选项*/
    function dataSelectionsTpl(data){
        var option = {
            name : data["dataset_name"],
            id : data["dataset_id"]
           
        }
        return tplEngine(
            '<li class="select-item">'+
                '<a href="javascript:">{name}</a>\
                <span class="dataBtn-wrap">\
                    <button role="button" class="btn btn-primary" data-id="{id}" >选择</button>\
                    <button role="button" class="btn btn-primary" data-id="{id}">删除</button>\
                </span>\
            </li>'
            ,option);
    }

    /*组装每一个算法选项*/
    function algoSelectionsTpl(algo){
        var option = {
            name : algo["algorithm_name"],
            id : algo["algorithm_id"],
            platform : algo["platform"]
        }
        // alert(123+" "+algoParamTpl(algo["parameters"]));
        return tplEngine(
            '<li class="select-item">'+
                '<a href="javascript:">{name}</a>\
                <span class="algoBtn-wrap">\
                    <button role="button" class="btn btn-primary" data-id="{id}" data-platform="{platform}">选择</button>\
                    <button role="button" class="btn btn-primary" data-id="{id}">删除</button>\
                </span>'
            ,option) + algoParamTpl(algo["parameters"]) + '</li>';
    }

    /*组装单个算法的所有参数选项。如无，返回空字符串*/
    function algoParamTpl(params){
        var paramsTpl = '';
        if(!params.length) return '';
        for( var i = 0, len = params.length ; i<len ; i++ ){
            paramsTpl += _oneParamTpl(params[i]);
        }
        
        return _totalTpl(paramsTpl);
    }

    /*组装包裹所有参数的ul*/
    function _totalTpl(paramsTpl){
        // alert("_totalTpl");
        // alert(paramsTpl);
        return tplEngine(
            '<ul class="item-param"><li class="item-param-intro">请填写参数</li>{paramsTpl}</ul>' 
            ,{
                paramsTpl:paramsTpl
            });
    }

    /*组装单个参数填写的模板*/
    function _oneParamTpl(param){


        var option = {
            name : param["parameter_name"],
            type : param["parameter_type"],
            value : param["parameter_value"],
            id : param["parameter_id"]
        }

        return tplEngine(
            '<li class="item-param-input input-group-sm form-group">'+

            '<label class="col-md-3 col-sm-10 col-xs-10">{name} ({type})</label>'+
            '<div class="col-md-7 col-sm-10 col-xs-10">'+
            '<input class="form-control" type="text" value="{value}" data-type="{type}" data-id="{id}">'+
            '</div>'+

            '</li>'
            ,option);
    }

/**
 * 模板引擎
 * @param  {string} string 模板字符串
 * @param  {Object} option 参数对象
 * @return {string}        构造好的字符串
 */
 function tplEngine(string,option){
    for (var single in option){
        string = string.replace(new RegExp('{' + single + '}', 'g'), option[single]);
    }
    // console.log(string);
    return string;
}

});