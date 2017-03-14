/*获取数据api_id*/
var dataApi = {
	"api_id":localStorage.getItem('api_id'),
	"protocol_id":"A-6-2-request"
}

$(function(){
	$.ajax({
		type:'get',
		data:dataApi,
		url:'../list/api',
		dataType:'json'
	}).success(function(data){
		createDom(data);
	})
})

function createDom(data){
	createDetails(data.api_details);
	createTab(data.api_attributes);
	createAside(data.reco_api);
}

function createDetails(res){

	/*面包屑导航*/

	$('.breadcrumb').append('<li><a href="runEnvironment.html">' + res.api_type + '</a></li><li class="active"><h2>' + res.api_name + '<h2></li>');

	var $detailList = $('.col-md-8  .row').eq(0);
	$detailList.html('<div class="col-md-4 col-sm-4">类别：' + res.api_type + '</div>' + 
					'<div class="col-md-4 col-sm-4">更新频率：实时</div>' +
                    '<div class="col-md-4 col-sm-4">版本号：' + res.version + '<span class="label label-success">最新</span></div>' + 
                    '<div class="col-md-12"><b>概述：</b>' + res.api_description + '</div>');
}

function createTab(res){

	var strTypeName = '',
		strTab = '',
		dataArry = [],
		interfaceArry = [];
	for (var i = 0,len = res.length;i < len;i ++){
		strTypeName += tplType(res[i].type,i);

		if(res[i].type.indexOf('数据') > -1){
			strTab += '<div class="tab-pane data" id=' + i + '><p>数据描述：' + res[i].data_description + '</p></div>';
			dataArry = res[i].attribute_values;
		}else if(res[i].type.indexOf('接口') > -1){
			strTab +=  '<div class="tab-pane interface" id=' + i + '>' + ' <p>请求方式：' + res[i].type +'</p>' +
                            '<p>接口地址：' + res[i].interface_add + '</p>' +
                            '<p>样例地址：' +  res[i].example_add +'</p><p>返回格式：' + res[i].format + '</p></div>';
            interfaceArry = res[i].attribute_values;
		}/*else if(res[i].type.indexOf('返回样例') > -1){*/
			// strTab += '<div class="tab-pane example" id=' + i +'><div><h4>接口样例：</h4><div>' + res[1].example_add + '</div></div>' + 
			// 	'<div><h4>返回JSON样例</h4><div><pre>' + res[1].api_example + '</pre></div></div></div>';
		// }
	}

	strTab += '<div class="tab-pane example" id=2><div><h4>接口样例：</h4><div>' + res[1].example_add + '</div></div>' + 
				'<div><h4>返回JSON样例</h4><div><pre>' + res[1].api_example + '</pre></div></div></div>';

	strTypeName += '<li><a href="#2" data-toggle="tab">返回样例</a></li>'

	$('.nav-tabs').html(strTypeName);
	$('.tab-content').html(strTab);
	$('.nav-tabs li').eq(0).addClass('active');
	$('.tab-pane').eq(0).addClass('active');
	createTable(dataArry,'数据信息');
	createTable(interfaceArry,'接口信息');


}

/*表格*/
function createTable(resArr,type){
	/*构造数据*/
	var data = [];
	if(type === '数据信息'){
		data = [['返回项目','项目名称','项目样例']];
		for(var i = 0,len = resArr.length;i < len;i ++){
			data.push([resArr[i].attribute_item,resArr[i].attribute_name,resArr[i].attribute_example]);
		}
	}else{
		data = [['参数项目','参数名称','参数类型','参数样例']];
		for(var i = 0,len = resArr.length;i < len;i ++){
			data.push([resArr[i].attribute_item,resArr[i].attribute_name,resArr[i].attribute_type,resArr[i].attribute_example]);
		}
	}

	var table = document.createElement('table'),
		thead = document.createElement('thead'),
		tbody = document.createElement('tbody'),
		row = '',
		cell = '';
	for (var i = 0,len = data.length;i < len;i ++){
		if(i === 0) {
			row = thead.insertRow(0);
		}else{
			row = tbody.insertRow(i - 1);
		}
		for(var j = 0,numLen = data[i].length;j < numLen;j ++){
			cell = row.insertCell(j);
			cell.innerHTML = data[i][j];
		}
	}
	table.appendChild(thead);
	table.appendChild(tbody);
	table.className = 'table table-bordered table-hover table-striped';
	if(type === '数据信息'){
		$('.data').get(0).appendChild(table);
	}else{
		$('.interface').get(0).appendChild(table);
	}
}

/*侧边栏*/
function createAside(res){
	var strAside = '';
	for (var i = 0,len = res.length;i < len;i ++){
		strAside += tplAside(res[i]);
	}
	$('.reco-api').append(strAside);
}

function tplType(resType,number){
	var option = {
		type:resType,
		number:number
	}
	return tplEngine('<li><a href="#{number}" data-toggle="tab">{type}</a></li>',option)
}

function tplAside(res){
	var option = {
		id:res.model_api_id,
		description:res.api_description,
		name:res.api_name
	}
	return tplEngine('<div><h4 data-id="{id}">{name}</h4><p>{description}</p></div>',option)
}

/*点击选择推荐api*/
$('.reco-api').on('click','div + div',function(){
	localStorage.setItem('api_id',$(this).find('h4').data('id'));
	location.href = 'apiDetail.html';
})



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
    return string;
}