$(function(){
   $.ajax({
        type:'post',
        url:'../servlet/VisualResultDisplayServlet',
        dataType:'json',
        success:function(res){
           
        console.log(res);
            var type = res.visuals[0].visual_type;
            var dataArry=res.data;                                              
            /*展示方式类型*/
            var graphType = {
                '表格':createTable,
                '饼状图':createPie,
                '柱状图':createLineBar,
                '折线图':createLineBar,
                '散点图':creatScatter
            }; 
            console.log(type);
             graphType[type](res.visuals,dataArry,type); 
             
        }
    });

    topSlide();
     /*创建表格 表格类型只有一种*/
    function createTable(dataset,dataArry){
        /*dataset = dataset[0];*/
        var table = document.createElement('table'),
            thead = document.createElement('thead'),
            tbody = document.createElement('tbody'),
            name = thead.insertRow(0);

        /*创建每一列的列名*/
        for(var i = 0,len = dataArry.length;i < len;i ++){  
            var itemName = name.insertCell(i);
            itemName.innerHTML = dataArry[i].label;
        }
        table.appendChild(thead);

        /*创建每一列的具体值*/
        for(var i = 0,len = dataArry[0].value.length;i < len;i ++){
            var itemData = tbody.insertRow(i);
            for(var j = 0,itemLen = dataArry.length;j < itemLen;j ++){
                itemData.insertCell(j).innerHTML = dataArry[j].value[i];
            }
        }
        table.appendChild(tbody);

        table.className = ' table table-hover table-bordered';

        document.getElementById('graph-wrap').appendChild(table);

        /*大致调整表格大小*/
        if(dataArry.length === 1){
            table.style.width = '50%';
        }else if(dataArry.length === 2){
            table.style.width = '75%';
        }else{
            table.style.width = '100%';
        }

    }

    /*创建条形图*/
    function createLineBar(dataset,dataArry,type){
        var len = dataset.length;
        /*当前只有一列数据*/
        dataset = dataset[0];
        domInstall(len,'条形图');
        var option = {
            backgroundColor:'rgb(245,245,245)',
            tooltip : {
                trigger: 'axis',
                formatter:'<br/> 数据{b}：{c}个'
            },
            legend:{
                x:'left',
                orient:'horizontal',
                data:[]//dataset.attribute_name[0]
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataZoom : {show: true},
                    dataView : {show: true},
                    magicType : {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            dataZoom : {
                show : true,
                realtime : true,
                height: 20
            },
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : function (){
                        var list = [];
                        for(var i = 0;i < dataArry.length;i ++){
                            list.push(dataArry[i].label);
                        }
                        return list;
                    }()
                }
            ],
            yAxis : [
                {   
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'',
                    type:'line',
                    data:function (){
                        var list = [];
                        for(var i = 0,len = dataArry.length;i < len;i ++){
                            list.push(dataArry[i].value[0]);
                        }
                        return list;
                    }()
                }
            ],
            calculable:false
        };

        var barType = {
            '折线图':{
                '标准折线图':function(){
                    return option;
                }
            },
            '柱状图':{
                '标准柱状图':function(){
                    var barOption = cloneObject(option);
                    barOption.series[0].type = 'bar';
                    barOption.xAxis[0].boundaryGap = 'true';
                    return barOption;
                }
            }
        };
        	
        	console.log(dataset.visual_name);
        	console.log(barType[type][dataset.visual_name]());
        for(var i = 0;i < len;i ++){
            echarts.init($('.bar').get(i)).setOption(barType[type][dataset.visual_name]());
        }

    }

    /*创建pie*/
    function createPie(dataset,dataArry){
        var  len = dataset.length; /*pie数量*/
        domInstall(len,'饼图');

        /*基本option*/
        var option = {
                backgroundColor:'rgb(245,245,245)',
                title:{
                    text:'正确率分布图',
                    x:'center'
                },
                tooltip:{
                    trigger:'item',
                    formatter:'{a}： <br/> {b}：{c}（{d}%）'
                },
                legend:{
                    x:'left',
                    orient:'vertical',
                    data:function(){
                        var list = [];
                        for(var i = 0;i < dataArry.length;i ++){
                            list.push(dataArry[i].label);
                        }
                        return list;
                    }()
                },
                toolbox:{
                    show:true,
                    feature:{
                        dataView:{show: true, readOnly: false},
                        magicType:{
                            show:true,
                            type:['bar','funnel'],
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
                        name:dataset[0].dataset_name,
                        type:'pie',
                        data:function(){
                            var list = [];
                            for(var i = 0;i < dataArry.length;i ++){
                                list.push({value:dataArry[i].value[0],name:dataArry[i].label});
                            }
                            return list;
                        }()
                    }
                ]
            };


        /*饼图具体类型*/
        var itemType = {
            '标准饼图':function(){
                return option;
            },
            '环形标准图':function(){
                var ringOption = cloneObject(option);
                ringOption.series[0].radius = ['50%','70%'];
                return ringOption;
            },
            '南丁格尔玫瑰图':function(){
                var roseOption = cloneObject(option);
                roseOption.series[0].radius = '70%';
                roseOption.series[0].roseType = 'radius';
                return roseOption;
            }
        };

        for(var i = 0;i < len;i ++){
            echarts.init($('.pie').get(i)).setOption(itemType[dataset[i].visual_name]());
        }


    }

    /*创建散点图*/    
    function creatScatter(dataset,dataArry){
        var len = dataset.length;/*散点图个数*/
        console.log(len);
        domInstall(len,'散点图');/*dom组装*/
        /*组装series*/
        function seriesCreate(dataset,dataArry){
            var data = dataArry,
                seriesScatter = [];
            for(var i = 0,len = data.length;i < len;i ++){
                seriesScatter[i] = {};
                seriesScatter[i].name = data[i].label;
                seriesScatter[i].type = 'scatter';
                seriesScatter[i].data = [];
                /*构造series里data值*/
                for(var j = 0, coordinateLen = data[i].value.length;j < coordinateLen;j ++){            
                    
                    seriesScatter[i].data[j] = data[i].value[j];
                }
            }
            console.log(seriesScatter);
            console.log(JSON.stringify(seriesScatter));
            return seriesScatter;
            
        }

        function minMax(){
            var data = seriesCreate(dataset,dataArry),
                list = [];
            for(var i = 0,len = data.length;i < len;i ++){
                for(var j = 0,numLen = data[i].data.length;j < numLen;j ++){
                    list.push(data[i].data[j][0]);
                }
            }
            return {
                min:Math.floor(Math.min.apply(null,list)),
                max:Math.ceil(Math.max.apply(null,list))
            };
        }


        var option = {
            backgroundColor:'rgb(245,245,245)',
            tooltip:{
                trigger:'item',
                formatter:'{a}： <br/>{c}'
            },
            toolbox:{
                show:true,
                feature:{
                    dataView:{show: true, readOnly: false},
                    dataZoom:{show:true},
                    mark:{show:true},
                    magicType:{
                        show:true,
                        type:['stack'],
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
            dataZoom : {
                show : true,
                realtime : true,
                height: 20
            },
            xAxis : [
                {
                    type : 'value',
                    boundaryGap : false,
                    min:minMax().min,
                    max:minMax().max
                }
            ],
            yAxis : [
                {  
                    type : 'value'
                }
            ],
            series:seriesCreate(dataset,dataArry)
        };
		var itemType = {
            	'标准散点图':function(){
                return option;
            }
           
        };
        for(var i = 0;i < len;i ++){
            echarts.init($('.bar').get(i)).setOption(itemType[dataset[i].visual_name]());
        }
        console.log(option.xAxis[0].min);

    }



    /*非table类型的dom组装*/ 
    function domInstall(len,type){
        var wrap = document.getElementById('graph-wrap'),
            str = '';

        if(type === '饼图'){
            for(i = 0;i < len - 1;i ++){
                str += '<div  class="col-md-6"><div class="graph pie"></div></div>';
            }
            if(len % 2){
                str += '<div  class="col-md-6 col-md-offset-3"><div class="graph pie"></div></div>';
            }else{
                str += '<div  class="col-md-6"><div class="graph pie"></div></div>';
            }
        }else/* if(type === '条形图')*/{
            for(i = 0;i < len;i ++){
                str += '<div class="col-md-10 col-md-offset-1"><div class="graph bar"></div></div>';
            }
        }

       wrap.innerHTML = str;
    }

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

    /*克隆对象*/
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