$(function(){
    getDataAjax();
   var data_id = localStorage.getItem('dataset_id');
   var process_id=localStorage.getItem('process_id');
   var dataPanel=$('#data-panel'),
        turn=true,
        timer=1,
        data_page=1,
        slideTurn=false,
        dataObj={
            "protocol":"A-5-2-request",
            "dataset_id":data_id,
            "process_id":process_id,
            "data_page": data_page,
            "resultdataset_viewmore":"true"
        };
    
    dataPanel.hide();
    
    $('#send-id').on('click',function(){    //点击选择按钮
        var objId = {
            process_id:process_id
        };
        var $this=$(this);
         $.ajax({
            type:'post',
            url:'../servlet/StoreServlet',
            dataType:'json',
            data:objId
        });
    })
     
    $('#show-datas').on('click',function(){
        var $this=$(this);                        //给展示按钮添加点击事件
        var dataHasmore=$('#data_hasmore');
        if(turn){                            //点击后请求ajax
            $.ajax({
            type:'post',
            dataType:'json',
            data:dataObj,
            url: urlConfig.resultDetails.ResultDetailsServlet,
            error:function(){
                alert('运行失败，没有数据');
            },
            success:function(data){
                if(data.data_source.length === 0){
                    alert('没有数据');
                    return false;
                }else{
                    if (data.data_hasmore === 'false') {
                        dataHasmore.hide();
                    }
                    dataObj.data_page++;
                    slideTurn=true;
                    dataPanel.slideDown();
                    $this.html("点击收起");
                    console.log(data);
                    formatdata(data);
                    }
                }
            });
        }
        turn=false;
        if(slideTurn===true){
            timer++;
            if(timer%2==0){
                dataPanel.slideUp();
                $(this).html("点击展开");
            }else{
                dataPanel.slideDown();
            }
        }
        
        
        dataHasmore.unbind('click').on('click',function(){    //点击更多按钮请求ajax
            $.ajax({
                type:'post',
                dataType:'json',
                data:dataObj,
                url: urlConfig.resultDetails.ResultDetailsServlet,
                success:function(data){
                    dataObj.data_page++;
                    if(data.data_hasmore==="false"){   //如果后台没有更多数据，隐藏按钮
                        dataHasmore.hide();
                    }
                    formatdata(data);               
                }
            });
        });
        
        
    });
    
    function getDataAjax(){
        
        var data_id = localStorage.getItem('dataset_id');
        var process_id=localStorage.getItem('process_id');
        var obj = {
            "protocol":"A-5-2-request",
//          "dataset_id":data_id,
            "process_id":process_id  
        };
        
        $.ajax({
            type:"post",
            dataType:"json",
            url:"../servlet/ResultDetailsServlet",
            data:obj,
            success:function(data){
                console.log(data);
                var zeroRow=getElementsByClass('row')[0];
                var optionZeroRow={
                        algorithmName:data.algorithm_name,
                        dataName:data.dataset_name,
                        startTime:data.process_start,
                        endTime:data.process_end,
                        status:data.run_state,
                        platform:data.platform
                };
                if(data.run_state==0){
                    optionZeroRow.status='运行结束';
                }else if(data.run_state==-1){
                    optionZeroRow.status='运行失败';
                }else if(data.run_state==1){
                    optionZeroRow.status='运行中';
                }else if(data.run_state==2){
                    optionZeroRow.status='运行未开始';
                }
                
                
                
                zeroRow.innerHTML=tplEngine(
                '<div class="col-md-4"><b>算法与数据:</b>&nbsp;<span class="algorithm-left">{algorithmName}</span>&nbsp;<span class="glyphicon glyphicon-plus"></span>&nbsp;<span class="dataset-right">{dataName}</span></div>'+
                '<div class="col-md-4"><b>运行开始时间:</b>{startTime}</div>'+
                '<div class="col-md-4"><b>运行结束时间:</b>{endTime}</div>'+
                '<div class="col-md-4"><b>运行状态:</b>{status}</div>'+
                '<div class="col-md-4"><b>运行平台:</b>{platform}</div>',optionZeroRow);
                    
               
                
                 /*组装单个字段列表*/
                function listTpl(attributes){
                    var option = {
                        name:attributes.attribute_name,
                        type:attributes.attribute_type,
                        range:attributes.attribute_range
                    };
        
                    return tplEngine(
                        '<div class="col-md-11 col-md-offset-1 list">' + 
                            '<div class="col-md-2">名称：{name}</div>' + 
                            '<div class="col-md-2">类型：{type}</div>' + 
                            '<div class="col-md-2">范围：{range}</div>' + 
                        '</div>',option);
                }
                
                
                function getElementsByClass(className){
                    var oele = document.getElementsByTagName('*');
                    var re=new RegExp('\\b'+className+'\\b', 'i');
                    var arr=[];
                    for(var i=0;i<oele.length;i++){
                        if(re.test(oele[i].className)){
                        arr.push(oele[i]);
                        }
                    }
                    return arr;
                }
                
            }
            
        });
        
    }
    
    
function formatdata(data){
    var dataCode=$('#data-code');
    var content= data.data_source; //content是一个二维数组 
    var arryVals="";
    $.each(content, function(i,arry) {
        var arryLength= arry.length;//arry是一维数组
        $.each(arry, function(j,val) {//遍历数组中的每个值并把它赋予到arryVals
            arryVals+=val+' ';
            if(j==arryLength-1){
                arryVals+='<br/>';  //按列数做换行处理
            }
        });
        
    });
    dataCode.append(arryVals);
    
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
})
