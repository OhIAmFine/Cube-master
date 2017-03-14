$.ajax({
	type:'post',
	url:'../servlet/ExecutePageServlet',
	dataType:'json'
}).success(function(res){
    pickSelectDom(res);
    algoDataSelect(res);
}).then(algoDataAll).then(pickClick).then(drag('car'));

function pickSelectDom(res){
	var selectAlgo = res.algorithm, 
		selectDataset = res.dataset,
		algoStr = '',
		datasetStr = '';

	/*组装算法*/
	for (var i = 0,len = selectAlgo.length;i < len;i ++){
		algoStr += daTpl(selectAlgo[i],'algorithm');
	}
	/*组装数据*/
	for (var i = 0,len = selectDataset.length;i < len;i ++){
		datasetStr += daTpl(selectDataset[i],'dataset');
	}

	$('.select-algorithm ul').html(algoStr);
	$('.select-dataset ul').html(datasetStr);

}

function daTpl(data,type){
	var option = {
		name:data[type + '_name'],
		id:data[type + '_id']
	};
	return tplEngine('<li><span>{name}</span><a href="javascript:" data-id={id}>删除</a></li>',option);
}


/*飞入购物车动画*/
function animation(source,target,type){
    /*滚动条距离*/
    var scrollLeft = document.documentElement.scrollLeft || document.body.scrollLeft,
        scrollTop = document.documentElement.scrollTop || document.body.scrollTop,
        /*四角距离*/
        rectSource = source.getBoundingClientRect(),
        rectTarget = target.getBoundingClientRect(),
        /*移动元素中心位置*/
        encterSource = {
            x:rectSource.left + (rectSource.right - rectSource.left) / 2  + scrollLeft,
            y:rectSource.top + (rectSource.bottom -rectSource.top) / 2 + scrollTop
        },
        /*目标元素中心位置*/
        encterTarget = {
            x:rectTarget.left + (rectTarget.right - rectTarget.left) / 2  + scrollLeft,
            y:rectTarget.top + (rectTarget.bottom -rectTarget.top) / 2 + scrollTop
        };

    move(encterSource,encterTarget);

    /*抛物线运动*/
    function move(encterSource,encterTarget){
        /*飞入购物车的圆*/
        $('.ion').css({
            'display':'block',
            'left':encterSource.x,
            'top':encterSource.y
        })

        /*获取抛物线 y = a * x * x + b * x + c */
        var x1 = encterSource.x, 
            y1 = encterSource.y,
            x2 = encterTarget.x,
            y2 = encterTarget.y,
            a = 0.002, /*抛物线弯曲度*/
            b = (y2 - a * x2 * x2 - y1 + a * x1 * x1) / (x2 - x1),
            c = y1 - a * x1 * x1 -[(y2 - a * x2 * x2 - y1 + a * x1 * x1) / (x2 - x1)] * x1,
            startX = encterSource.x, /*开始点的横坐标*/
            speed = x2 - x1 > 0 ? 15 :-15; /*速度*/

        function step(){
            startX = startX + speed;
            var x = startX,
                y = a * x * x + b * x + c;
            $('.ion').css({
                'left':x,
                'top':y
            });
            if(x1 < x2 &&  x < x2){
                requestAnimationFrame(step);
            }else if(x1 > x2 &&  x > x2){
                requestAnimationFrame(step);
            }else{
                /*动画结束后*/
                $('.pick-car ul').show();
                $('.ion').hide();
                /*添加数据*/
                var data = {};
                data[type + '_name'] = $(source).data('name'),
                data[type + '_id'] = $(source).data('id'),
                $('.select-' + type + ' ul').append(daTpl(data,type));
                isScroll();
                algoDataAll();
            }
        }

        requestAnimationFrame(step);
    }

}


/*点击pick图标*/
function pickClick(){
    /*点击pick图标*/
    $('.pick-picture').on('click',function(){

        $('.pick-car ul').toggle();

        isScroll();


    });



    /*删除pick的数据、算法*/
    $('.pick-car').on('click','a:not(.btn)',function(){

    	var oParent = this,
            $oParent = $(this);
    	while(oParent.tagName.toUpperCase() !== 'DIV'){
    		oParent = oParent.parentNode;
    	}

    	var reData = {},
            parentName = oParent.className.substring(7);

    	reData[parentName + '_id'] = $(this).data('id');


        $.ajax({
            type:'post',
            url:'../servlet/RemoveSessionServlet',
            dataType:'json',
            data:reData,
            success:function(res){
                if(res.dalete_status === "true"){

                    $oParent.parent().remove(); 
                    isScroll();

                    /*删除了的数据算法可以再选*/
                    $('.' + parentName).find('button').each(function(){
                        if($(this).data('id') === reData[parentName + '_id']){
                            $(this).removeClass('active');
                        }
                    });

                    algoDataAll();

                }else{
                    alert('删除不成功！');
                }
            }
        });
    });

}

/*选择的数据算法总数*/
function algoDataAll(){
    var algoAll = $('.select-algorithm li').length, /*算法个数*/
        dataAll = $('.select-dataset li').length; /*数据个数*/

    $('.algorithm-dataset-select span').eq(0).html(algoAll).end().eq(1).html(dataAll);
}

/*判断是否出现滚动条*/
function isScroll(){
    if($('.select-dataset').height() + $('.select-algorithm').height() < 540){
        $('.pick-car > div').removeClass('pick-body');
    }else{
        $('.pick-car > div').addClass('pick-body');
    }
}


/*购物车拖拽*/
function drag(id)
{
    var element = document.getElementById(id);
    
    element.onmousedown = function (event)
    {
        event = event || window.event;
        var disX = event.clientX - element.offsetLeft;
        
        element.onmousemove = function (event)
        {
            event = event || window.event;
            
            element.style.left=event.clientX - disX + 'px';

            if(event.clientX - disX < 0){
                element.style.left = 0;
            }
            if(element.offsetLeft + element.offsetWidth > window.innerWidth ){
                element.style.left = 'auto';
                element.style.right = 0;
            }

        };

        
        document.onmouseup = function ()
        { 
            element.onmousemove = null;
            document.onmouseup = null;
        };
    };
}

/*如果数据算法已经选择就active*/
function algoDataSelect(resSelect){
    var type = $('header').next('div').attr('class').split(' ')[1];
    var res = resSelect[type],/*已选择的算法|数据*/
        resAll = $('.' + type).find('button'); /*列表的算法|数据*/

    for(var i = 0,len = res.length;i < len;i ++){
        for(var j = 0,numLen = resAll.length;j < numLen;j ++){
            if(res[i][type + '_id'] === resAll.eq(j).data('id').toString()){
                resAll.eq(j).addClass('active');
            }
        }
    }

}

/*获得浏览器大小*/
function page(){
    var pageWidth = window.innerWidth,
        pageHeight = window.innerHeight;
    if(typeof pageWidth === 'number'){
        page = function(){
            return {
                width:pageWidth,
                height:pageHeight
            };
        }
    }else{
        if(document.compatMode === 'CSS1Compat'){
            page = function(){
                return {
                    width:document.documentElement.clientWidth,
                    height:document.documentElement.clientHeight
                };
            } 
        }else{
            page = function(){
                return {
                    width:document.body.clientWidth,
                    height:document.body.clientHeight
                };
            }
        }
    }
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