$(function(){
	$.ajax({
		type:'get',
		url:'../list/apilist',
		dataType:'json'
	}).success(function(data){
		createDom(data.api);
		tip();
		asideChosse();
		apiChosse();
	})
})

/*组装dom元素*/
function createDom(data){
	createAside(data);
	createList(data);
}

/*侧边栏*/
function createAside(data){
	var aside = $('.api-type'),
		option = {},
		str = '';
	for(var i = 0,len = data.length;i < len;i ++){
		option = {
			type:data[i].api_type,
			number:data[i].api_value.length
		}
		str += tplEngine('<li>{type}<span class="label-success label">{number}</span></li>',option);
	}

	aside.append(str);
}

/*具体api*/
function createList(data){
	var contentApi = $('.data-api'),
		option = {},
		str = '',
		listStr = '';
	for(var i = 0,len = data.length;i < len;i ++){
		listStr = '';
		for(var j = 0,numLen = data[i].api_value.length;j < numLen;j ++){
			option = {
				name:data[i].api_value[j].api_name,
				description:data[i].api_value[j].api_description,
				id:data[i].api_value[j].model_api_id
			}
			listStr += tplEngine('<div><h5 data-id={id}>{name}</h5><p>{description}</p><div class="detail">{description}</div></div>',option);
		}
		str += '<div>' + listStr + '</div>';
	}
	contentApi.html(str);
}

function tip(){
	$('.detail').each(function(){
		if($(this).height() < 78){
			$(this).height(78);
		}
	})
}
/*选择侧边栏*/
function asideChosse(){
	$('.data-api > div').hide().eq(0).show();
	$('.api-type li').eq(1).addClass('active');
	var sideList = document.getElementsByClassName('api-type')[0].getElementsByTagName('li');
	
	for(var i = 1,len = sideList.length;i < len;i ++){
		sideList[i].onclick = (function(num){
			return function(){
				if ($(this).hasClass('active')) return;
				$('.active').removeClass('active');
				$('.data-api > div').hide().eq(num - 1).show();
				$(this).addClass('active');
			}
		}(i))
	}
}
/*具体api选择*/
function apiChosse(){
	$('.data-api').on('click','div > div',function(){
		localStorage.setItem('api_id',$(this).find('h5').data('id'));
		location.href = 'apiDetail.html';
	})
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
    return string;
}