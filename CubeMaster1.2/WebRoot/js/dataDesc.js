$(function(){
    /*vue set title*/
    /*var descName = decodeURIComponent(window.location.search.split('=')[1]);
    var headerVue = new Vue({
        el:'header',
        data:{
            name:descName
        }
    })*/
   /*加载页面后第一次请求*/
    getDataAjax();                 
   
    var dataPanel = $('#data-panel'),
        dataHssMore = $('#data_hasmore');

    var turn = true,
        slideTurn = false,
        timer = 1,
        data_page = 1,
        dataObj = {
            "protocol": "A-1-3-request",
            "dataset_id": data_id,
            "data_page": data_page,
            "dataset_viewmore": "true"
        };
//  dataPanel.hide();
   /* 给展示按钮添加点击事件*/
    $('#show-datas').on('click',function(){     
        var $this = $(this);
        /*点击后请求ajax*/
        if(turn){                           
            $.ajax({
                type:'get',
                dataType:'json',
                data:dataObj,
                url: urlConfig.dataDesc.DataSetDetailsServlet,
                success:function(data){
                    if (data.data_source.length === 0) {
                        alert('没有数据');
                        return false;
                    } else {
                        dataObj.data_page++;
                        slideTurn = true;
                        dataPanel.slideDown();
                        if (data.data_hasmore == 'false') {
                            dataHssMore.hide();
                        }
                        $this.html("点击收起");
                        formatdata(data);   
                     }
                }
            });         
        }
        turn = false;
        if (slideTurn === true) {
            timer++;
            if (timer % 2 == 0) {
                dataPanel.slideUp();
                $(this).html("点击展开");
            } else {
                dataPanel.slideDown();
            }
        }
        
    });
    /*点击更多按钮请求ajax*/
    dataHssMore.unbind('click').on('click',function(){
        $.ajax({
            type:'get',
            dataType:'json',
            data:dataObj,
            url: urlConfig.dataDesc.DataSetDetailsServlet,
            success:function(data){
                /*如果后台没有更多数据，隐藏按钮*/
                if (data.data_hasmore == 'false') {   
                    dataHssMore.hide();
                } else {
                    dataObj.data_page++;
                    formatdata(data);
                }
            }
        });
    });
})

/*获取数据id*/
var data_id = localStorage.getItem('dataset_id');
var obj = {
    "protocol":"A-1-3-request",
    "dataset_id":data_id,
    "dataset_viewmore":"false"
};

/*get data using ajax*/
function getDataAjax(){
    var data = [];
    $.ajax({
        type:'get',
        dataType:'json',
        data:obj,
        url:'../servlet/DataSetDetailsServlet'
    }).success(function(res){
        console.log(res);

        /*面包屑数据名称*/
        var descName = res.dataset.dataset_name;
        var headerVue = new Vue({
            el:'header',
            data:{
                name:descName
            }
        })

        var oneRow = getElementsByClass('row')[0],
            twoRow = getElementsByClass('row')[1],
            threeRow = getElementsByClass('row')[2],
            FourRow = getElementsByClass('row')[3],
            optionOneRow = {
                name:res.dataset.dataset_name,
                user:res.user_name,
                time:res.dataset.submit_datetime
            },
            optionTwoRow = {
                description:res.dataset.description
            },
            optionThreeRow = {
                tasks:res.dataset.associated_tasks,
                number:res.dataset.download_count,
                area:res.dataset.area
            };

        oneRow.innerHTML = tplEngine(
            '<div class="col-md-4"><h3 class="text-primary">{name}</h3></div>' +
            '<div class="col-md-4"><b>作者：</b>{user}</div>' + 
            '<div class="col-md-4"><b>最近更新时间：</b>{time}</div>',optionOneRow);

        twoRow.innerHTML = tplEngine(
            '<div class="col-md-12">' + 
                '<p><b>简介：</b><span>{description}</span></p>' + 
            '</div>',optionTwoRow);

        threeRow.innerHTML = tplEngine(
            '<div class="col-md-4"><b>类型：</b>{tasks}</div>' +
            '<div class="col-md-4"><b>下载次数：</b>{number}</div>' +
            '<div class="col-md-4"><b>来源领域：</b>{area}</div>',optionThreeRow);

        var attributes = res.attributes, /*字段数组*/
            len = attributes.length,
            attributeFirstTpl = '',
            attributeListTpl = '';

        if(len){
            attributeFirstTpl = '<div class="col-md-12"><b>字段列表：</b></div>';
            for(var i = 0;i < len; i ++){
                attributeListTpl += listTpl(attributes[i]);
            }
        }

        FourRow.innerHTML = attributeFirstTpl + attributeListTpl;
        
        
        
        /*组装单个字段列表*/
        function listTpl(attribute){
            var option = {
                name:attribute.attribute_name,
                type:attribute.attribute_type,
                range:attribute.attribute_range
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

        /*学长代码*/
        /*res = res.split(',').join('\',\'').split('][').join('\'],[\'');
        res = '[[\''+res.substring(2);
        res = res.split(']]').join('\']]');
        console.log(res);
        data = eval(res);

        /* redner table, graphs */
        /*data = [
            ['length','value','width','height','label']
        ]
        for(var i = 0 ; i<20 ; i++){
            data.push([ (Math.random()+2)*2+1,Math.random()*4,Math.random()*4,Math.random()*4,'123']);
        }
        drawTable(data);
        renderStem(data);
        renderCorrect(data);
        renderDistribute(data);*/
    });

    /*data = [
            ['length','value','width','height','label']
        ]
        for(var i = 0 ; i<20 ; i++){
            data.push([ (Math.random()+2)*2+1,Math.random()*4,Math.random()*4,Math.random()*4,'123']);
        }
        drawTable(data);
        renderStem(data);
        renderCorrect(data);
        renderDistribute(data);*/
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

/*draw the data table*/
/*function drawTable(data){
    var fragment = document.createDocumentFragment();
    var tbody = document.createElement('tbody');
    for(var i = 0,len = data.length;i<len;i++){
        if(i === 0){
            var head = document.createElement('thead');
            var tr = document.createElement('tr');
            for(var j = 0;j<data[0].length;j++){
                var td = document .createElement('td');
                td.innerHTML = data[0][j];
                tr.appendChild(td);
            }
            head.appendChild(tr);
            fragment.appendChild(head);
        }else{
            var tr = document.createElement('tr');
            for(var j = 0;j<data[i].length;j++){
                var td = document .createElement('td');
                td.innerHTML = data[i][j];
                tr.appendChild(td);
            }
            tbody.appendChild(tr);
        }
    }
    fragment.appendChild(tbody);
    document.getElementById('table-info').appendChild(fragment);
}*/


/*function renderStem(data){
    var highCount = 0,lowCount = 0;
    for(var i = 1,len = data.length;i<len;i++){
        parseInt(data[i][0])>5 ? highCount++ : lowCount++;
    }
    var pie = echarts.init(document.getElementById('render-stem'),e_macarons);
    var option = {
        backgroundColor:'rgb(245,245,245)',
        title:{
            text:'花茎高矮的比例',
            x:'center'
        },
        tooltip:{
            trigger:'item',
            formatter:'{a}： <br/> {b}茎数量：{c}（{d}%）'
        },
        legend:{
            x:'left',
            data:['高','矮']
        },
        toolbox:{
            show:true,
            feature:{
                dataView:{show: true, readOnly: false},
                magicType:{
                    show:true,
                    type:['pie','funnel'],
                    funnel: {
                        x: '30%',
                        width: '40%',
                        funnelAlign: 'center'
                    }
                },
                restore:{show:true},
                saveAsImage:{show:true}
            }
        },
        calculable:true,
        series:[
            {
                name:'花',
                type:'pie',
                radius:'60%',
                center:['50%','60%'],
                data:[
                    {value:highCount,name:'高'},
                    {value:lowCount,name:'矮'}
                ]
            }
        ]
    };
    pie.setOption(option);
}

function renderCorrect(data){
    var pie = echarts.init(document.getElementById('render-correct'),e_macarons);
    var option = {
        backgroundColor:'rgb(245,245,245)',
        title:{
            text:'花数据正确率',
            x:'center'
        },
        tooltip:{
            trigger:'item',
            formatter:'{a}： <br/> {b}数量：{c}（{d}%）'
        },
        legend:{
            x:'left',
            data:['正确','错误']
        },
        toolbox:{
            show:true,
            feature:{
                dataView:{show: true, readOnly: false},
                magicType:{
                    show:true,
                    type:['pie','funnel'],
                    option: {
                        funnel: {
                            x: '30%',
                            width: '40%',
                            funnelAlign: 'center'
                        }
                    }
                },
                restore:{show:true},
                saveAsImage:{show:true}
            }
        },
        calculable:true,
        series:[
            {
                name:'花数据正确率',
                type:'pie',
                radius:'60%',
                center:['50%','60%'],
                data:[
                    {value:100,name:'正确'},
                    {value:50,name:'错误'}
                ]
            }
        ]
    };
    pie.setOption(option);
}

function renderDistribute(data){
    var data = [];
    for(var i = 0;i<200;i++){
        var plus = 0;
        plus=(100-Math.abs(i-100))/5;
        data.push( Math.ceil(Math.random()*8+plus) );
    }
    var line = echarts.init(document.getElementById('render-distribute'),e_macarons);
    var option = {
        backgroundColor:'rgb(245,245,245)',
        title:{
            text:'花茎数据分布',
            x:'center'
        },
        tooltip:{
            trigger:'axis',
            formatter:'{a}<br/>花茎长为{b}的花有：{c}'
        },
        legend:{
            show:false,
            x:'left',
            data:['花数据统计']
        },
        toolbox:{
            show:true,
            feature:{
                dataView:{show: true, readOnly: false},
                magicType:{
                    show:false
                },
                restore:{show:true},
                saveAsImage:{show:true}
            }
        },
        calculable:true,
        dataZoom:{
            show:true,
            orient:'horizontal'
        },
        xAxis:[{
            type:'category',
            boundaryGap:true,
            name:'花茎长度',
            data:function(){
                var list = [];
                for(var i = 4;i<6;i+=0.01){
                    list.push(Number(i).toFixed(2));
                }
                return list;
            }()
        }],
        yAxis:[{
            type:'value',
            name:'数量',
        }],
        series:[
            {
                name:'花数据统计',
                type:'bar',
                data:data
            },
            {
                name:'花数据统计',
                type:'line',
                data:data,
                itemStyle:{
                    normal:{
                        color:e_macarons.color[3],
                        lineStyle:{
                            color:e_macarons.color[3]
                        }
                    }
                }
            }
        ]
    };
    line.setOption(option);

}*/ 