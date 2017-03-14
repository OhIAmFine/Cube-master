$(function(){
	$.ajax({
		type:'post',
		url:'../list/upload',
		dataType:'json'
	}).success(function(data){
		var dataArr = ['参数名称','参数类型','返回项目','参数描述','项目样例','参数样例'],
			attribute = data.attribute;
		for (var i = 0,len = attribute.length;i < len;i ++){
			arr.push([attribute[i].attribute_name,'attribute_type','attribute_item','attribute_type','type','attribute_example']);
		}
		createTable(attribute);

		$('container row row')[0].html('<b>Model名称：</b>' + data.api.model_name).data('id',data.api.model_id);
		$('container row row')[1].html('<b>Model描述：</b>' + data.api.model_description);
	})
})

/*构造参数table*/
createTable(data){
	var fragment= document.createDocumentFragment(),
		thead = document.createElement('thead'),
		tbody = document.createElement('tbody'),
		row = '',
		cell = '';
	for (var i = 0,len = data.length;i < len;i ++){
		if(i === 0){
			row = thead.insertRow(0);
		}else{
			row = tbody.insertRow(i);
		}

		for( var j = 0,numLen = data[i].length;j < numLen;j ++){
			cell = row.insertCell(j);
			if(j === 0 || j === 1){
				cell.innerHTML = data[i][j];
			}else{
				cell.innerHTML = '<input type="text" class="form-control" name=' + data[i][j] + '>';
			}
		}
	}
	fragment.appendChild(thead);
	fragment.appendChild(tbody);
	document.getElementsByClassName('table')[0].appendChild(fragment);
}

/*验证表单是否填完*/
function testAll(){
	var input = document.getElementsByClassName('form-control');
	for(var i = 0,len = input.length;i < len;i ++){
		if(input[i].value === ''){
			alert('请完整填完信息！');
			return false;
		}
	}
	return true;
}

/*点击上传*/
document.getElementsByClassName('btn')[0].onclick = function(){
	/*验证通过*/
	if(testAll){
		/*构造json*/
		var apiJson = {},
			apiJson.api = {},
			apiJson.attributes = [],
			content = document,
			apiDetail = content.getElementsByClassName('form-group').getElementsByClassName('form-control'),
			apiAttributes = content.getElementsByTagName('tbody').getElementsByTagName('tr');

			for (var i = 0,len = apiDetail.length;i < len;i ++){
				apiJson.api[apiDetail[i].name] = apiDetail[i].value;
			}
			apiJson.api.model_id = $('container row row')[0].data(id);
			apiJson.protocol_id =  "A-6-3-request";

			for (var i = 0,len = apiAttributes.length;i < len;i ++){
				apiJson.attributes[i] = {};
				var td = apiAttributes[i].getElementsByTagName('td');
				for(var j = 0,numLen = td.length;j < numLen;j ++){
					if(j === 0 || j === 1){
						apiJson.attributes[i][td[j].name] = td[j].innerHTML;
					}else{
						var input = td[j].getElementsByTagName('input')[0];
						apiJson.attributes[i][input.name] = input.value;
					}
				}
			}
			apiJson.attributes = JSON.stringfiy(apiJson.attributes);
		}

		/*向后台发送数据*/
		$.ajax({
			type:'post',
			url:'',
			data:apiJson,
			async:false
		}).success(function(data){
			if(data.status === 0){
				alert('上传成功！');
			}else{
				alert('上传失败!');
				return false;
			}
		})
	}
}