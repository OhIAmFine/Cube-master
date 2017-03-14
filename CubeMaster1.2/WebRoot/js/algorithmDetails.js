
$(function(){
    /*初始化SyntaxHighlighter*/
// prettyPrint();
//   SyntaxHighlighter.highlight();
    /*从本地获取算法id*/
  
    var algId = localStorage.getItem('algorithm_id');
    var obj = {
        "protocol":"A-2-3-request",
        "algorithm_id":algId
    };
    $.ajax({
        type:'get',
        url: urlConfig.algorithmDetails.AlgorithmDetailsServlet,
        data:obj,
        dataType:'json',
        success:function(data){ 
            createPage(data);            
            /*获取源码模块*/
            var showdatas=$("#data-show");
            var timer=0;
            var turn=true;
            var dataPanel=$("#data-panel");
            var algId = localStorage.getItem('algorithm_id');
            dataPanel.hide();
            $("#content").on('click','button',function(event){  
                if($(this).attr("name")=="open"){
                var dataPanel=$("#data-panel");
                var dataObj = {             
                    "algorithm_id":algId, 
                    "data_page": 1
                };
                dataPanel.show();
                $(event.target).html("点击收起");
                timer++;
                if(turn){                                           //第一次点击后请求ajax
                        $.ajax({
                            type:'post',    
                            data:dataObj,
                            url: urlConfig.algorithmDetails.AlgorithmCodeServlet,
                            success:function(data){ 
                                formatdata(data);                   //接收数据并展示
                            },
                            error: function(){                      //后台返回内容解析不成功提示
                               alert(arguments[1]);
                            }
                        });
                    }
                turn=false;
                if(timer%2==0){
                        dataPanel.slideUp();
                        $(event.target).html("点击展开");
                    }else{
                        dataPanel.slideDown();
                    }
                }else {
                    return false;
                }           
            });      
        }
    });
    /*创建dom节点*/
    function createPage(data){

        /*面包屑导航*/
        var bread=document.getElementsByTagName('ol')[0];

        bread.innerHTML = tplEngine('<li><a href="./algorithmList.html">算法模块</a></li>' + 
                    '<li><a href="javascript:">{type}</a></li><li class="acitve">{name}算法</li>',{
                        type:data.algorithm.associated_tasks,
                        name:data.algorithm.algorithm_name
                    });

        /*主体内容*/
        // var parent = document.getElementById('content');
        var parent = document.getElementById('content');
//      parent.style.display = 'block';

        var option = {
            name:data.algorithm.algorithm_name,
            user:data.user_name,
            time:data.algorithm.submit_datetime,
            description:data.algorithm.description,
            type:data.algorithm.data_test,
            out_pattern:data.algorithm.out_pattern,
            in_pattern:data.algorithm.in_pattern,
            platform:data.algorithm.platform
        }, 
        arry = [];
        for(var i = 0,len = data.key_word.length;i < len;i ++){
            arry.push(data.key_word[i].keyword);
        }
        option.key_word = arry.join(',');

        parent.innerHTML = tplEngine(parent.innerHTML,option);

        /*参数列表*/
        var wrapList = document.getElementById('parameter-list'),
        param = data.parameters,
        parameterListTpl = '';
        console.log(param);
        if(param.length){
            for(var i = 0,len = param.length;i < len;i ++){
                parameterListTpl += paramTpl(param[i]);
            }
            // console.log(param);
        }else{
            document.getElementById('no-param').innerHTML = '无';
        }

        wrapList.innerHTML = parameterListTpl;

    }

function paramTpl(param){
    var option = {
        name:param.parameter_name,
        type:param.parameter_type,
        value:param.parameter_value
    };
    return tplEngine('<div class="col-md-2">名称：{name}</div><div class="col-md-2">类型：{type}</div><div class="col-md-2">值：{value}</div>',option);
}


/*通过calssname获得元素 返回所有元素的数组*/
function getElementsByClass(className){
    var oele = document.getElementsByTagName('*');
    var re = new RegExp('\\b' + className + '\\b', 'i');
    var arr = [];
    for(var i = 0;i < oele.length ;i ++){
        if(re.test(oele[i].className)){
        arr.push(oele[i]);
        }
    }
    return arr;
}

function formatdata(data){
    var data= JSON.parse(data);
    var dataPanel=$("#data-panel");
    if (data.source) {

    } else if (data.java_files) {
        var preStrings = '';
        data.java_files.forEach(function (item, index) {
            if (!item.file_name) return;
            preStrings+='<div class="row code-title"><div class="col-md-12"><div class="alert alert-info" role="alert"><span class="label label-info">'+index+'</span>&nbsp;&nbsp;<strong>类名：</strong>'+index+'</div></div></div>'+
                '<pre class="prettyprint Lang-java" dataKey="'+index+'">'+'<code>'+item.file_name+'</code>'+
                '</pre>'+
                '<button id="data_hasmore" class="data_hasmore btn btn-primary col-md-1"  dataName="'+item.file_name+'" data-page="1">显示更多</button>'+'</br></br>';
        });
    }


    // var data_page = 2;
    // var size = 0;
    // var preStrings="";
    // $.each(codeObj,function(key,value){
    //     if (codeObj.hasOwnProperty(key)) size++;
    //     var dataPanel=$("#data-panel");
                        
    //     // if((value.indexOf("该文件代码读取完了")!==-1)||(value.indexOf("作者没有公开的源代码")!==-1)){
    //     //     preStrings += 
    //     //     '<pre class="prettyprint Lang-java" dataKey="'+key+'">'+'<code>'+value+'</code>'+'</pre>';
    //     // }else{
            
    //     // }
                        
    // })
    dataPanel.append(preStrings);
    prettyPrint();
    dataPanel.find("button").on('click',function(){
        var $this = $(this);
        var algId = localStorage.getItem('algorithm_id');
        var data_page = $this.attr('data-page');
        dataObj2={
            "algorithm_id": algId,
            "page": data_page,
            "file_name": $this.attr("dataName")
        };
        $.ajax({
            type:"post",
            data:dataObj2,
            dataType:'json',
            url: urlConfig.algorithmDetails.AlgorithmCodeServlet,      
            success:function(data){       
                var dataSource = data.java_files;
                var subStrings = '';
                dataSource.forEach(function (item, index) {
                    item.file_content.forEach(function (item, index) {
                        if (!item[0]) return false;
                        subStrings+='<pre class="prettyprint Lang-java" >' + item[0] + '</pre>'
                    });
                    if (!item.has_more) {
                        $this.hide();
                        return false;
                    }
                });
                $this.before(subStrings);
                prettyPrint();
                $this.attr('data-page',  parseInt(data_page) + 1);
            }
        });
    });
    
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




    