$(function(){
	/*获取数据*/
	$.ajax({
		type:'post',
		dataType:'json',
		url:'../servlet/ResultServlet',
		success:function(res){
			var resArry = res.model,//数组
			    data = [
				    ['序号','运行算法与数据','运行开始时间','运行结束时间','运行状态','运行平台','结果数据']
			    ];

			for(var i = 0,len = resArry.length;i < len;i ++){    //遍历每个结果
				data.push([i + 1,[resArry[i].json_detail.algorithm_name,resArry[i].json_detail.dataset_name],resArry[i].process_start,resArry[i].process_end,resArry[i].run_state,resArry[i].json_detail.platform,resArry[i].process_id]);
			}
			createTable(data);   //二维数组

            topSlide();
		}
	});
});

/*创建table*/
function createTable(data){
	var fragment = document.createDocumentFragment();
    var body = document.createElement('tbody');
    for(var i = 0,len = data.length;i < len;i ++){
    	if(i === 0){
    		var head = document.createElement('thead');
    		var row = head.insertRow(0);
    		for(var j = 0,leg = data[i].length;j < leg;j ++){
    			var cell = row.insertCell(j);         //每增加一列插入相对应数据
                cell.innerHTML = data[i][j];  
    		}
    		fragment.appendChild(head);
    	}else{
    		var row = body.insertRow(i-1);           //首行插入一行
    		for(var j = 0,leg = data[i].length;j < leg;j ++){
    			var cell = row.insertCell(j);   //根据结果行的列数创建列
    			if(j === 1){
    				cell.innerHTML = '<span>' + data[i][j][0] + '</span>' + '<span class=\"glyphicon glyphicon-plus\" aria-label=\"加上\"></span>' + '<span>' + data[i][j][1] + '</span>';
                    cell.className ='process-prent';
                }else if(j === 4){
                    cell.innerHTML = data[i][j];
                    /*if(data[i][j] === '-1') cell.innerHTML = '运行失败';
                    if(data[i][j] === '0') cell.innerHTML = '运行结束';
                    if(data[i][j] === '2') cell.innerHTML = '运行未开始';*/
                }else if(j === 6){
                    cell.innerHTML = '<a href="resultDetails.html" ' + 'data-id=' + data[i][j]+ '>详情</a>|<a data-id= '+data[i][j] +' href="javascript:">选择</a>|<a data-id='+data[i][j] +' href="javascript:">删除</a>';
                }
                else {
    				if(data[i][j])cell.innerHTML = data[i][j];   //j=2、3 第2、3列插入的内容
                    else cell.innerHTML = '------';
    			}
    		}
    	}
    } 
    fragment.appendChild(body);
    document.getElementById('table-run').appendChild(fragment);
}

/*事件委托 给每一个详情|选择*/
$('.container').on('click','a',function(){

    /*点击详情*/
    if($(this).html() === '详情'){
        /*存储结果id至本地*/
        localStorage.setItem('process_id',$(this).data("id"));

    }else if($(this).html()==='选择'){
        var obj = {
            process_id:$(this).data("id")
        };

        $.ajax({
            type:'post',
            url:'../servlet/StoreServlet',
            dataType:'json',
            data:obj
        });
    }else if($(this).html()==='删除'){
    	if(confirm('确定要删除本条数据？')){
    		var obj = {
            process_id:$(this).data("id")
        };
        $.ajax({
            type:'post',
            url:'../servlet/DeleteProcessRecordServlet',
            dataType:'json',
            data:obj
        });
        var deleteTr= $(this).parent().parent();
        deleteTr.remove();
        var firstTds= $("#table-run tbody td:first-child");
        for(var i=0;i<firstTds.length;i++){ //重新更新结果计数
        	console.log($(firstTds[i]));
        	$(firstTds[i]).html(i+1);
        }
    	}else return false;
    	
    }
    
    
});


/*滑动至顶部*/
function topSlide(){
    
    $('#top-control').on('click',function(){
        $('body').animate({scrollTop:'0'},'normal');
    });
    $(window).scroll(function(){
        if($('body').scrollTop() === 0){
            $('#top-control').hide();
        }else{
            $('#top-control').show();
        }
    });
}